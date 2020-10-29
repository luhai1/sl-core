package com.sl.common.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
@Slf4j
public class ElasticSearchConfig {
  @Resource
  private ElasticsearchEntity elasticsearchEntity;
    @Bean
    public RestHighLevelClient getTransportClient(){
        List<ElasticsearchProperties> elasticsearchPropertiesList = elasticsearchEntity.getHosts();
        HttpHost[] hosts = new HttpHost[elasticsearchPropertiesList.size()];
        HttpHost httpHost;
        for(int i=0;i<elasticsearchPropertiesList.size();i++){
            httpHost= new HttpHost(elasticsearchPropertiesList.get(i).getIp(),elasticsearchPropertiesList.get(i).getPort());
            hosts[i] = httpHost;
        }

        RestClientBuilder restClientBuilder = RestClient.builder(hosts);
        // 异步httpclient连接延时配置
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder ->  {
                requestConfigBuilder.setConnectTimeout(elasticsearchEntity.getConnectTimeOut());
                requestConfigBuilder.setSocketTimeout(elasticsearchEntity.getSocketTimeOut());
                requestConfigBuilder.setConnectionRequestTimeout(elasticsearchEntity.getConnectRequestTimeOut());
                return requestConfigBuilder;
            }
        );
        // 异步httpclient连接数配置
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder ->  {
                                                              httpClientBuilder.setMaxConnTotal(elasticsearchEntity.getMaxConnectNum());
                                                              httpClientBuilder.setMaxConnPerRoute(elasticsearchEntity.getMaxConnectPerRoute());
                                                              return httpClientBuilder;
                                                      });
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        return restHighLevelClient;
    }


}
