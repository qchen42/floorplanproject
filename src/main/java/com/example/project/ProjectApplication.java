package com.example.project;

import controller.FileUploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan({"com.example.project", "controller"})
public class ProjectApplication {

	public static void main(String[] args) {
		new File(FileUploadController.uploadDirectory).mkdirs();
		SpringApplication.run(ProjectApplication.class, args);
	}

}
