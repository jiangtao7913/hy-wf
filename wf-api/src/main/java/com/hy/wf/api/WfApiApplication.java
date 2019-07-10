package com.hy.wf.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.hy"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class WfApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WfApiApplication.class, args);
    }

}
