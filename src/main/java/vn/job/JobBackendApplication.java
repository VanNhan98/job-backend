package vn.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JobBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobBackendApplication.class, args);
	}

}
