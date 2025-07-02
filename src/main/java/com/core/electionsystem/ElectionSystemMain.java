package com.core.electionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ElectionSystemMain {

  public static void main(String[] args) {
    SpringApplication.run(ElectionSystemMain.class, args);
  }
}
