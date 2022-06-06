package com.bytedance.hego;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bytedance.hego.dao") //mapper扫描。针对持久层的扫描配置。
public class HegoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HegoApplication.class, args);
    }

}