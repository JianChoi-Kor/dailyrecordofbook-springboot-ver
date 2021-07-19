package com.community.dailyrecordofbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@SpringBootApplication
public class DailyrecordofbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyrecordofbookApplication.class, args);
	}
}
