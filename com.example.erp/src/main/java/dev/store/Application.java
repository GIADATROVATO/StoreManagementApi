package dev.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication

( 
		exclude= {
			DataSourceAutoConfiguration.class, 
			HibernateJpaAutoConfiguration.class
		}
	)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
