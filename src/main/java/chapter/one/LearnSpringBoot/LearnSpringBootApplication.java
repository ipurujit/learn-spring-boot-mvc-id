package chapter.one.LearnSpringBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearnSpringBootApplication {
	private static final Logger logger = LoggerFactory.getLogger(LearnSpringBootApplication.class);

	public static void main(String[] args) {
		// add comment
		SpringApplication.run(LearnSpringBootApplication.class, args);
		logger.info("Hello world!");
	}

}
