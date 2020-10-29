package com.sl.common.elasticsearch.config;

import lombok.Data;
import org.elasticsearch.common.xcontent.XContentType;

@Data
public class ESIndex {

    public ESIndex(){}
    public ESIndex(String indexName){
        this.indexName = indexName;
    }
    // 索引名
    private String indexName;
    // 索引类型
    private XContentType indexType = XContentType.JSON;
    // 分片数
    private Integer indexNumberShards = 3;
    // 副本数
    private Integer indexNumberReplicas = 1;
    // 索引mapping  JSON数据。
    private String mappingSource;
}
