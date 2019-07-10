package com.hy.wf.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = {"com.hy.wf.admin.modules.*.dao","com.hy.wf.admin.modules.dao"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class WfAdminApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WfAdminApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WfAdminApplication.class);
    }
}
