package himedia.oneshot;

import himedia.oneshot.config.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebMvcConfig.class)
public class OneshotApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneshotApplication.class, args);
	}
}
