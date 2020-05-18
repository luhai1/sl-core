package com.sl;

import com.sl.common.dataSource.multids.DruidProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan(basePackages = "com.sl.dao")
public class SLApplicationStart {
    public static void main(String[] args){
        SpringApplication.run(SLApplicationStart.class);
    }
}
