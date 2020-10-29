package com.sl.common.elasticsearch.config;

import java.util.List;
import java.util.Map;

public class ESQueryResult {
    /**基本查询结果集 **/
    List<Map<String,Object>> hitsList;
    /**分组聚合函数结果集 **/
    List<List<Map<String,Object>>> aggregationsList;

    public List<Map<String, Object>> getHitsList() {
        return hitsList;
    }

    public void setHitsList(List<Map<String, Object>> hitsList) {
        this.hitsList = hitsList;
    }

    public List<List<Map<String,Object>>> getAggregationsList() {
        return aggregationsList;
    }

    public void setAggregationsList(List<List<Map<String,Object>>> aggregationsList) {
        this.aggregationsList = aggregationsList;
    }
}
