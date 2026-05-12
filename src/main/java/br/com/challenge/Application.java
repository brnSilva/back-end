package br.com.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.challenge.config.properties.JwtProperties;
import br.com.challenge.config.properties.SecurityProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    SecurityProperties.class,
    JwtProperties.class
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}