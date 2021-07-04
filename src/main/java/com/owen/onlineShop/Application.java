package com.owen.onlineShop;


import org.springframework.context.ApplicationContext; // 就是一个Inversion of Control Container
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }
}
