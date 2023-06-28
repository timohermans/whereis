package nl.thermans.whereis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@SpringBootApplication
public class Where_is_application {
	public static void main(String[] args) {
		SpringApplication.run(Where_is_application.class, args);
	}
}
