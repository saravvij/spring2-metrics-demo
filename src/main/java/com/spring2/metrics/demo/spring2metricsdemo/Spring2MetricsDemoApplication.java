package com.spring2.metrics.demo.spring2metricsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Spring2MetricsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring2MetricsDemoApplication.class, args);
	}

}
