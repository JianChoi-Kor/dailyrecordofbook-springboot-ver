package com.community.dailyrecordofbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DailyrecordofbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyrecordofbookApplication.class, args);
	}

}
