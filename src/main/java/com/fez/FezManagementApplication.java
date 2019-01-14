package com.fez;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FezManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FezManagementApplication.class, args);
    }

}

