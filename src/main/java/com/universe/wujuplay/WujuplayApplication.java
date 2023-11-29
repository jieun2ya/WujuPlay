package com.universe.wujuplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WujuplayApplication {

    // 리뷰 이미지 등록-aws사용하려고 com.amazonaws.sdk.disableEc2Metadata 설정해줌
    static {
        System.setProperty("com.amazoneaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(WujuplayApplication.class, args);
    }


}
