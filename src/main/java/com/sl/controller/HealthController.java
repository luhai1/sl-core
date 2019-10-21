package com.sl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class HealthController {
    @RequestMapping("/health")
    public String health(){
        log.info("HealthController-Start");
        return "ok";
    }
}
