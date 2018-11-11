package com.asiainfo.projectmg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectMgApplication {

	public static void main(String[] args) {
		System.out.println("Jenkins" +
				"自动构建测试----------------------------");
		SpringApplication.run(ProjectMgApplication.class, args);
	}
}
