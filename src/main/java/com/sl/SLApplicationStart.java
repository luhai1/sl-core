package com.sl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sl.dao")
public class SLApplicationStart {
    public static void main(String[] args){
        SpringApplication.run(SLApplicationStart.class);
    }
}
