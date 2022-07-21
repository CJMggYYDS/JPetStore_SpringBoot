package com.csuse.jpetstoressm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com/csuse/jpetstoressm/persistence")
public class JPetStoreSsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(JPetStoreSsmApplication.class, args);
    }

}
