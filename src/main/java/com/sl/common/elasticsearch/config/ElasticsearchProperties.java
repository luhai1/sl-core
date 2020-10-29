package com.sl.common.elasticsearch.config;


public class ElasticsearchProperties {
    // 集群名称

    private String clusterName;

    // ip地址
    private String ip;

    // port 端口号
    private Integer port;

    // 协议
    private String scheme;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
