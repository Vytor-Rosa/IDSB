package net.weg.gedesti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class GedestiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GedestiApplication.class, args);
	}

}
