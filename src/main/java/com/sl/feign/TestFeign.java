package com.sl.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="sl-feign")
public interface TestFeign {

    @PostMapping("/feign/testFeign")
    String testFeign();

}
