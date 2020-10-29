package com.sl.common.elasticsearch.config;

import lombok.Data;
import lombok.Getter;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ES查询参数实体类
 */
public class ESQueryModel {
    /** 索引名称*/
    private String indexName;
    public ESQueryModel setIndexName(String indexName){
        this.indexName = indexName;
        return this;
    }
    
    /** 分页参数*/
    private Integer from;
    public ESQueryModel setFrom(Integer from){
        this.from = from;
        return this;
    }
    private Integer size;
    public ESQueryModel setSize(Integer size){
        this.size = size;
        return this;
    }

    /** 需要查询字段**/
    String[] includeFields ;
    /** 过滤查询字段**/
    String[] excludeFields ;

    public String[] getIncludeFields() {
        return includeFields;
    }

    public ESQueryModel setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
        return this;
    }

    public String[] getExcludeFields() {
        return excludeFields;
    }

    public ESQueryModel setExcludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
        return this;
    }

    /** MustTermQuery 类型的参数*/
    private List<MustTermQuery> mustTermQueries = new ArrayList<>();
    public ESQueryModel addMustTermQuery(MustTermQuery mustTermQuery){
        mustTermQueries.add(mustTermQuery);
        return this;
    }

    /** MustInTermQuery 类型的参数*/
    private List<MustTermsQuery> mustTermsQueries = new ArrayList<>();
    public ESQueryModel addMustTermsQuery(MustTermsQuery mustTermsQuery){
        mustTermsQueries.add(mustTermsQuery);
        return this;
    }

    /** MustRangeQuery 类型的参数*/
    private List<MustRangeQuery> mustRangeQueries = new ArrayList<>();
    public ESQueryModel addMustRangeQuery(MustRangeQuery mustRangeQuery){
        mustRangeQueries.add(mustRangeQuery);
        return this;
    }

    /** MustWildcardQuery 类型的参数*/
    private List<MustWildcardQuery> mustWildcardQueries = new ArrayList<>();
    public ESQueryModel addMustWildcardQuery(MustWildcardQuery mustWildcardQuery){
        mustWildcardQueries.add(mustWildcardQuery);
        return this;
    }

    /** AggregationQuery 类型的参数*/
    private List<AggregationQuery> aggregationQueries = new ArrayList<>();
    public ESQueryModel addAggregationQuery(AggregationQuery aggregationQuery){
        aggregationQueries.add(aggregationQuery);
        return this;
    }

    /** SortQuery 类型的参数*/
    private List<SortQuery> sortQueries = new ArrayList<>();
    public ESQueryModel addSortQuery(SortQuery sortQuery){
        sortQueries.add(sortQuery);
        return this;
    }


    /**
     * 单项匹配查询
     */
    @Getter
    public class MustTermQuery{
        /** must or must not*/
        private boolean isMust;
        /** 参数名*/
        private String name;
        /** 参数值*/
        private Object value;


        public MustTermQuery(String name, Object value){
            this.isMust = true;
            this.name = name;
            this.value = value;
        }

        public MustTermQuery(boolean isMust, String name, Object value){
            this.isMust = isMust;
            this.name = name;
            this.value = value;
        }
    }

    /**
     * 多项匹配查询（in）
     */
    @Getter
    public class MustTermsQuery{
        /** must or must not*/
        private boolean isMust;
        /** 参数名*/
        private String name;
        /** 参数值*/
        private Object[] values;

        public MustTermsQuery(String name, Object... values){
            this.isMust = true;
            this.name = name;
            this.values = values;
        }

        public MustTermsQuery(boolean isMust, String name, Object... values){
            this.isMust = isMust;
            this.name = name;
            this.values = values;
        }
    }

    /**
     * 区间匹配查询
     */
    @Getter
    public class MustRangeQuery{
        /** must or must not*/
        private boolean isMust;
        /** 参数名*/
        private String name;
        /** 大于等于*/
        private Object gte;
        /** 大于*/
        private Object gt;
        /** 小于等于*/
        private Object lte;
        /** 小于*/
        private Object lt;

        public MustRangeQuery(String name, Object gte, Object gt, Object lte, Object lt) {
            this.isMust = true;
            this.name = name;
            this.gte = gte;
            this.gt = gt;
            this.lte = lte;
            this.lt = lt;
        }

        public MustRangeQuery(boolean isMust, String name, Object gte, Object gt, Object lte, Object lt) {
            this.isMust = isMust;
            this.name = name;
            this.gte = gte;
            this.gt = gt;
            this.lte = lte;
            this.lt = lt;
        }
    }

    /**
     * 通配符匹配查询（模糊）
     */
    @Getter
    public class MustWildcardQuery{
        /** must or must not*/
        private boolean isMust;
        /** 参数名*/
        private String name;
        /** 参数值*/
        private String query;

        public MustWildcardQuery(String name, String query){
            this.isMust = true;
            this.name = name;
            this.query = query;
        }

        public MustWildcardQuery(boolean isMust, String name, String query){
            this.isMust = isMust;
            this.name = name;
            this.query = query;
        }
    }

    /**
     * 聚合函数查询
     */
    public class AggregationQuery{
        @Data
        public
        class AggBean{
            private String name;
            private String field;

            public AggBean(String name, String field){
                this.name = name;
                this.field = field;
            }
        }

        /**
         *  分组参数
         *  因分组存在嵌套，使用栈存储，最先加入的分组嵌套在最外层
         **/
        private List<AggBean> aggTerms = new ArrayList<AggBean>();
        public AggregationQuery pushAggTerm(AggBean aggBean){
            aggTerms.add(aggBean);
            return this;
        }

        public List<AggBean> getAggTerms(){
            return aggTerms;
        }
        public AggregationQuery setAggTerms(List<AggBean> aggTerms){
             this.aggTerms = aggTerms;
            return this;
        }



        public boolean isAggTermEmpty(){
            return aggTerms.isEmpty();
        }


        /** 求和参数*/
        @Getter
        private List<AggBean> aggSum = new ArrayList<AggBean>();
        public AggregationQuery addAggSum(AggBean aggBean){
            aggSum.add(aggBean);
            return this;
        }

        /** 平均值参数*/
        @Getter
        private List<AggBean> aggAvg = new ArrayList<AggBean>();
        public AggregationQuery addAggAvg(AggBean aggBean){
            aggAvg.add(aggBean);
            return this;
        }
        /** 计数参数*/
        @Getter
        private List<AggBean> aggCount = new ArrayList<AggBean>();
        public AggregationQuery addAggCount(AggBean aggBean){
            aggCount.add(aggBean);
            return this;
        }

        /** 去重计数参数*/
        @Getter
        private List<AggBean> aggCardinalityCount = new ArrayList<AggBean>();
        public AggregationQuery addCardinalityCount(AggBean aggBean){
            aggCardinalityCount.add(aggBean);
            return this;
        }

        /** 最大值参数 **/
        @Getter
        private List<AggBean> aggMax = new ArrayList<AggBean>();
        public AggregationQuery addAggMax(AggBean aggBean){
            aggMax.add(aggBean);
            return this;
        }
        /** 最小值参数 **/
        @Getter
        private List<AggBean> aggMin = new ArrayList<AggBean>();
        public AggregationQuery addAggMin(AggBean aggBean){
            aggMin.add(aggBean);
            return this;
        }

    }

    /**
     *  排序参数
     */
    @Getter
    public class SortQuery{
        /** 排序字段*/
        private String field;
        /** 排序方式*/
        private SortOrder sortOrder = SortOrder.ASC;

        public SortQuery(String field, SortOrder sortOrder){
            this.field = field;
            this.sortOrder = sortOrder;
        }
    }

    public String getIndexName() {
        return indexName;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getSize() {
        return size;
    }

    public List<MustTermQuery> getMustTermQueries() {
        return mustTermQueries;
    }

    public void setMustTermQueries(List<MustTermQuery> mustTermQueries) {
        this.mustTermQueries = mustTermQueries;
    }

    public List<MustTermsQuery> getMustTermsQueries() {
        return mustTermsQueries;
    }

    public void setMustTermsQueries(List<MustTermsQuery> mustTermsQueries) {
        this.mustTermsQueries = mustTermsQueries;
    }

    public List<MustRangeQuery> getMustRangeQueries() {
        return mustRangeQueries;
    }

    public void setMustRangeQueries(List<MustRangeQuery> mustRangeQueries) {
        this.mustRangeQueries = mustRangeQueries;
    }

    public List<MustWildcardQuery> getMustWildcardQueries() {
        return mustWildcardQueries;
    }

    public void setMustWildcardQueries(List<MustWildcardQuery> mustWildcardQueries) {
        this.mustWildcardQueries = mustWildcardQueries;
    }

    public List<AggregationQuery> getAggregationQueries() {
        return aggregationQueries;
    }

    public void setAggregationQueries(List<AggregationQuery> aggregationQueries) {
        this.aggregationQueries = aggregationQueries;
    }

    public List<SortQuery> getSortQueries() {
        return sortQueries;
    }

    public void setSortQueries(List<SortQuery> sortQueries) {
        this.sortQueries = sortQueries;
    }
}
