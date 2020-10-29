package com.sl.common.elasticsearch.util;

import com.sl.common.elasticsearch.config.ESIndex;
import com.sl.common.elasticsearch.config.ESQueryModel;
import com.sl.common.elasticsearch.config.ESQueryResult;
import com.sl.common.elasticsearch.constant.ElasticsearchConstant;
import net.sf.ehcache.search.aggregator.Count;
import net.sf.saxon.functions.Minimax;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Component
@DependsOn("elasticSearchConfig")
public class ElasticsearchUtil {
    @Resource
    private RestHighLevelClient client;


    private static RestHighLevelClient restHighLevelClient;


    @PostConstruct
    public void init() {
        restHighLevelClient = client;
    }

    // 创建索引
    public static boolean createIndex(String indexName) throws IOException {
        if (isExistsIndex(indexName)) {
            return false;
        }

        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(indexName); //hcode_index为索引名
        //执行请求，获得响应
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }


    /**
     * 创建复杂索引(类型/mapping)
     *
     * @param esIndex 索引
     */
    public static boolean createIndex(ESIndex esIndex) throws IOException {

        if (isExistsIndex(esIndex.getIndexName())) {
            return false;
        }
        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(esIndex.getIndexName());
        //设置分片和副本数
        request.settings(Settings.builder()
                .put("index.number_of_shards", esIndex.getIndexNumberShards())
                .put("index.number_of_replicas", esIndex.getIndexNumberReplicas())
        );
        //创建映射，中间填写需要存到ES上的JavaDTO对象对应的JSON数据。
        request.mapping(esIndex.getMappingSource(), esIndex.getIndexType());
        //创建索引
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 判断指定的索引名是否存在
     *
     * @param indexName 索引名
     * @return 存在：true; 不存在：false;
     */
    public static boolean isExistsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }


    /**
     * 索引删除
     *
     * @param indexName 索引名称
     */
    public static boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);//指定要删除的索引名称
        //索引删除
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }


    /**
     * 单条数据添加
     *
     * @param indexName
     * @param dataId
     * @param dataSource
     * @throws IOException
     */
    public static void addData(String indexName, String dataId, String dataSource) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(dataId).source(dataSource, XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 单条数据添加
     *
     * @param indexName
     * @param dataId
     * @param dataMap
     * @throws IOException
     */
    public static void addData(String indexName, String dataId, Map<String, Object> dataMap) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(dataId).source(dataMap);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 批量插入数据
     *
     * @param indexName 索引名
     * @param dataList  其中数据map要是有id字段则作为该条数据唯一id否则随机生成id
     * @throws IOException
     */
    public static void batchAddData(String indexName, List<Map<String, Object>> dataList) throws IOException {
        if (StringUtils.isEmpty(indexName) || CollectionUtils.isEmpty(dataList)) {
            return;
        }
        IndexRequest indexRequest;
        String id;
        List<IndexRequest> requests = new ArrayList<>();
        for (Map<String, Object> dataMap : dataList) {
            if (dataMap.containsKey(ElasticsearchConstant.ES_ID)) {
                id = (String) dataMap.get(ElasticsearchConstant.ES_ID);
            } else {
                id = UUID.randomUUID().toString();
            }
            indexRequest = new IndexRequest(indexName);
            indexRequest.id(id).source(dataMap);
            requests.add(indexRequest);
        }
        batchAddData(requests);

    }

    /**
     * 批量插入数据
     *
     * @param indexRequestList 批量数据请求request
     * @throws IOException
     */
    public static void batchAddData(List<IndexRequest> indexRequestList) throws IOException {
        if (CollectionUtils.isEmpty(indexRequestList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (IndexRequest indexRequest : indexRequestList) {
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    }

    /**
     * 根据id删除索引下数据
     *
     * @param indexName 索引名
     * @param id        删除数据id
     * @throws IOException
     */
    public static void deleteData(String indexName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        deleteRequest.id(id);
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * 根据id批量删除
     *
     * @param indexName
     * @param ids
     * @throws IOException
     */
    public static void batchDeleteData(String indexName, List<String> ids) throws IOException {
        if (StringUtils.isEmpty(indexName) || CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<DeleteRequest> deleteRequestList = new ArrayList<>();
        DeleteRequest deleteRequest;
        for (String id : ids) {
            deleteRequest = new DeleteRequest(indexName).id(id);
            deleteRequestList.add(deleteRequest);
        }
        batchDeleteData(deleteRequestList);
    }

    /**
     * 批量删除
     *
     * @param deleteRequestList 删除请求request
     * @throws IOException
     */
    public static void batchDeleteData(List<DeleteRequest> deleteRequestList) throws IOException {
        if (CollectionUtils.isEmpty(deleteRequestList)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (DeleteRequest indexRequest : deleteRequestList) {
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * 根据查询条件删除
     *
     * @param deleteByQueryRequest
     * @throws IOException
     */
    public static long deleteByQuery(DeleteByQueryRequest deleteByQueryRequest) throws IOException {
        BulkByScrollResponse response = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        return response.getDeleted();
    }

    /**
     * es查询
     *
     * @param esQueryModel 查询实体类
     * @return
     * @throws IOException
     */
    public static ESQueryResult queryData(ESQueryModel esQueryModel) throws IOException {
        if (StringUtils.isEmpty(esQueryModel.getIndexName())) {
            return null;
        }
        SearchRequest searchRequest = generateSearchRequest(esQueryModel);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        if (response.status() != RestStatus.OK) {
            return null;
        }
        return getESQueryResult(response, esQueryModel);

    }


    private static SearchRequest generateSearchRequest(ESQueryModel esQueryModel) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(esQueryModel.getIndexName());
        // 组装查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();

        /** 拼装 单个匹配MustTermQuery参数*/
        List<ESQueryModel.MustTermQuery> mustTermQueries = esQueryModel.getMustTermQueries();
        if (CollectionUtils.isNotEmpty(mustTermQueries)) {
            for (ESQueryModel.MustTermQuery mustTermQuery : mustTermQueries) {
                if (mustTermQuery.isMust()) {
                    queryBuilder.must(QueryBuilders.termQuery(mustTermQuery.getName(), mustTermQuery.getValue()));
                } else {
                    queryBuilder.mustNot(QueryBuilders.termQuery(mustTermQuery.getName(), mustTermQuery.getValue()));
                }
            }
        }

        /** 多项匹配查询（in） MustTermsQuery参数*/
        List<ESQueryModel.MustTermsQuery> mustTermsQueries = esQueryModel.getMustTermsQueries();
        if (CollectionUtils.isNotEmpty(mustTermsQueries)) {
            for (ESQueryModel.MustTermsQuery mustTermsQuery : mustTermsQueries) {
                if (mustTermsQuery.isMust()) {
                    queryBuilder.must(QueryBuilders.termsQuery(mustTermsQuery.getName(), mustTermsQuery.getValues()));
                } else {
                    queryBuilder.mustNot(QueryBuilders.termsQuery(mustTermsQuery.getName(), mustTermsQuery.getValues()));
                }
            }
        }


        /** 区间匹配查询 拼装 MustRangeQuery*/
        List<ESQueryModel.MustRangeQuery> mustRangeQueries = esQueryModel.getMustRangeQueries();
        if (CollectionUtils.isNotEmpty(mustRangeQueries)) {
            for (ESQueryModel.MustRangeQuery mustRangeQuery : mustRangeQueries) {
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(mustRangeQuery.getName());
                if (mustRangeQuery.getGte() != null) {
                    rangeQuery.gte(mustRangeQuery.getGte());
                }
                if (mustRangeQuery.getGt() != null) {
                    rangeQuery.gt(mustRangeQuery.getGt());
                }
                if (mustRangeQuery.getLte() != null) {
                    rangeQuery.lte(mustRangeQuery.getLte());
                }
                if (mustRangeQuery.getLt() != null) {
                    rangeQuery.lt(mustRangeQuery.getLt());
                }
                if (mustRangeQuery.isMust()) {
                    queryBuilder.must(rangeQuery);
                } else {
                    queryBuilder.mustNot(rangeQuery);
                }
            }
        }

        /** 通配符匹配查询（模糊） 拼装 MustWildcardQuery*/
        List<ESQueryModel.MustWildcardQuery> mustWildcardQueries = esQueryModel.getMustWildcardQueries();
        if (CollectionUtils.isNotEmpty(mustWildcardQueries)) {
            for (ESQueryModel.MustWildcardQuery mustWildcardQuery : mustWildcardQueries) {
                if (mustWildcardQuery.isMust()) {
                    queryBuilder.must(QueryBuilders.wildcardQuery(mustWildcardQuery.getName(), mustWildcardQuery.getQuery()));
                } else {
                    queryBuilder.mustNot(QueryBuilders.wildcardQuery(mustWildcardQuery.getName(), mustWildcardQuery.getQuery()));
                }
            }

        }

        /** 条件查询 **/
        sourceBuilder.query(queryBuilder);

        /** 拼装 AggregationQuery*/
        List<ESQueryModel.AggregationQuery> aggregationQueries = esQueryModel.getAggregationQueries();
        if (CollectionUtils.isNotEmpty(aggregationQueries)) {
            for (ESQueryModel.AggregationQuery aggregationQuery : aggregationQueries) {
                if (!aggregationQuery.isAggTermEmpty()) {
                    //先取出最内层分组
                    ESQueryModel.AggregationQuery.AggBean aggBean = aggregationQuery.getAggTerms().get(aggregationQuery.getAggTerms().size()-1);
                    AggregationBuilder aggregationBuilder = AggregationBuilders.terms(aggBean.getName()).field(aggBean.getField());

                    //拼装求和参数
                    for (ESQueryModel.AggregationQuery.AggBean sumAggBean : aggregationQuery.getAggSum()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.sum(sumAggBean.getName()).field(sumAggBean.getField()));
                    }

                    //拼装平均参数
                    for (ESQueryModel.AggregationQuery.AggBean avgAggBean : aggregationQuery.getAggAvg()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.avg(avgAggBean.getName()).field(avgAggBean.getField()));
                    }

                    //拼装计数参数
                    for (ESQueryModel.AggregationQuery.AggBean countAggBean : aggregationQuery.getAggCount()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.count(countAggBean.getName()).field(countAggBean.getField()));
                    }
                    // 拼装去重计数参数
                    for (ESQueryModel.AggregationQuery.AggBean cardinalityCount : aggregationQuery.getAggCardinalityCount()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.cardinality(cardinalityCount.getName()).field(cardinalityCount.getField()));
                    }
                    // 拼装最大值参数
                    for (ESQueryModel.AggregationQuery.AggBean maxAggBean : aggregationQuery.getAggMax()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.max(maxAggBean.getName()).field(maxAggBean.getField()));
                    }
                    // 拼装最小值参数
                    for (ESQueryModel.AggregationQuery.AggBean minAggBean : aggregationQuery.getAggMin()) {
                        aggregationBuilder.subAggregation(AggregationBuilders.min(minAggBean.getName()).field(minAggBean.getField()));
                    }
                    //最后拼装外层分组
                    AggregationBuilder finalBuilder = aggregationBuilder;
                    List<ESQueryModel.AggregationQuery.AggBean> aggBeans = aggregationQuery.getAggTerms();
                    if(CollectionUtils.isNotEmpty(aggBeans)) {
                        // 先加入的分组放在最外层
                        for (int i=aggBeans.size()-2;i>=0;i-- ) {
                            AggregationBuilder tempBuilder = AggregationBuilders.terms(aggBeans.get(i).getName()).field(aggBeans.get(i).getField());
                            finalBuilder = tempBuilder.subAggregation(finalBuilder);
                        }
                    }
                    /** 分组汇总 **/
                    sourceBuilder.aggregation(finalBuilder);
                }
            }

        }

        /** 拼装排序参数*/
        List<ESQueryModel.SortQuery> sortQueries = esQueryModel.getSortQueries();
        if (CollectionUtils.isNotEmpty(sortQueries)) {
            for (ESQueryModel.SortQuery sortQuery : sortQueries) {
                sourceBuilder.sort(new FieldSortBuilder(sortQuery.getField()).order(sortQuery.getSortOrder()));
            }

        }

        /**分页 **/
        if (esQueryModel.getFrom() != null && esQueryModel.getSize() != null) {
            sourceBuilder.from(esQueryModel.getFrom());
            sourceBuilder.size(esQueryModel.getSize());
        }
        /** 查询结果字段控制**/
        if ((esQueryModel.getIncludeFields() != null && esQueryModel.getIncludeFields().length > 0) || (esQueryModel.getExcludeFields() != null && esQueryModel.getExcludeFields().length > 0)) {
            sourceBuilder.fetchSource(esQueryModel.getIncludeFields(), esQueryModel.getExcludeFields());
        }
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     * 解析查询结果返回
     *
     * @param response     es结果
     * @param esQueryModel es查询条件
     * @return
     */
    private static ESQueryResult getESQueryResult(SearchResponse response, ESQueryModel esQueryModel) {
        ESQueryResult esQueryResult = new ESQueryResult();

        //获取source
        List<Map<String, Object>> dataList = Arrays.stream(response.getHits().getHits()).map(b -> {
            return b.getSourceAsMap();
        }).collect(Collectors.toList());
        esQueryResult.setHitsList(dataList);
        //得到这个分组的数据集合
        List<ESQueryModel.AggregationQuery> aggregationQueryList = esQueryModel.getAggregationQueries();
        if(CollectionUtils.isNotEmpty(aggregationQueryList)){
            // 存储分组的名称
            List<List<Map<String,Object>>> listDataList = new ArrayList<>();
            for (ESQueryModel.AggregationQuery aggregationQuery : aggregationQueryList) {
                // 每个整体分组结果
                if (!aggregationQuery.isAggTermEmpty()) {
                    List<ESQueryModel.AggregationQuery.AggBean> aggBeanList = aggregationQuery.getAggTerms();
                    if(CollectionUtils.isNotEmpty(aggBeanList)) {
                        // 获取最外层分组递归处理结果集
                        int index = 0;
                        ParsedLongTerms terms = response.getAggregations().get(aggBeanList.get(index).getName());
                        List bucketList =  terms.getBuckets();
                        // 从最外层分组开始组装数据
                        List<Map<String,Object>> mapList = processBuckets(bucketList,index,aggBeanList,null,aggregationQuery);
                        listDataList.add(mapList);
                    }

                }
            }
            esQueryResult.setAggregationsList(listDataList);
        }

        return esQueryResult;
    }

    private  static List<Map<String,Object>>  processBuckets(List<Terms.Bucket> bucketList,int index,List<ESQueryModel.AggregationQuery.AggBean> aggBeanList,Map<String,Object> dataMap,ESQueryModel.AggregationQuery aggregationQuery){
        if(CollectionUtils.isEmpty(bucketList)){
            return null;
        }
        if(dataMap == null){
            dataMap = new HashMap<>();
        }
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(index < aggBeanList.size()-1){    // 该分组下还有其他分组
            for(Terms.Bucket bucket : bucketList){
                  Map<String,Object> subMap = new HashMap<>();
                  subMap.putAll(dataMap);
                  subMap.put(aggBeanList.get(index).getField(),bucket.getKey());
                  Terms terms =  bucket.getAggregations().get(aggBeanList.get(index+1).getName());
                  List<Map<String,Object>> dataList = processBuckets((List<Terms.Bucket>)terms.getBuckets(),index+1,aggBeanList,subMap,aggregationQuery);
                  mapList.addAll(dataList);
              }
        }else{
            // 最后一层组装结果数据
            Map<String,Object> finaMap;
            for (Terms.Bucket bucket : bucketList) {
                finaMap = new HashMap<>();
                finaMap.put(aggBeanList.get(index).getField(),bucket.getKey());
                finaMap.putAll(dataMap);
                    //拼装求和参数
                    for (ESQueryModel.AggregationQuery.AggBean sumAggBean : aggregationQuery.getAggSum()) {
                        Sum sum = bucket.getAggregations().get(sumAggBean.getName());
                        finaMap.put(sumAggBean.getName(),sum.getValue());
                    }

                    //拼装平均参数
                    for (ESQueryModel.AggregationQuery.AggBean avgAggBean : aggregationQuery.getAggAvg()) {
                        Avg avg = bucket.getAggregations().get(avgAggBean.getName());
                        finaMap.put(avgAggBean.getName(),avg.getValue());
                    }
                    for (ESQueryModel.AggregationQuery.AggBean count : aggregationQuery.getAggCount()){
                        ValueCount valueCount = bucket.getAggregations().get(count.getName());
                        finaMap.put(count.getName(),valueCount.getValue());
                    }
                    // 拼装去重计数参数
                    for (ESQueryModel.AggregationQuery.AggBean cardinalityCount : aggregationQuery.getAggCardinalityCount() ){
                        Cardinality cardinality = bucket.getAggregations().get(cardinalityCount.getName());
                        finaMap.put(cardinalityCount.getName(),cardinality.getValue());
                    }

                    //拼装最大值参数
                    for (ESQueryModel.AggregationQuery.AggBean maxAggBean : aggregationQuery.getAggMax() ){
                        Max max = bucket.getAggregations().get(maxAggBean.getName());
                        finaMap.put(maxAggBean.getName(),max.getValue());
                    }
                    //拼装最小值参数
                    for (ESQueryModel.AggregationQuery.AggBean minAggBean : aggregationQuery.getAggMin() ){
                        Min max = bucket.getAggregations().get(minAggBean.getName());
                        finaMap.put(minAggBean.getName(),max.getValue());
                    }
                 mapList.add(finaMap);
              }
        }

        return mapList;
    }

}

