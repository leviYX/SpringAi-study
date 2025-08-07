package com.levi.springai.toolsfunctioncall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.levi.springai.toolsfunctioncall")
public class ToolsFunctioncallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolsFunctioncallApplication.class, args);
    }

}
