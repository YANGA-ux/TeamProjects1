package com.txm.teamprojects;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.txm.teamprojects.mapper")
@SpringBootApplication
public class TeamProjectsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamProjectsApplication.class, args);
    }

}
