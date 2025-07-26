package com.txm.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.txm.hospital.mapper")
@SpringBootApplication
public class TeamProjectsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamProjectsApplication.class, args);
    }

}
