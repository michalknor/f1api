package sk.f1api.f1api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sk.f1api.f1api.util.HibernateUtil;

@SpringBootApplication
public class F1ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(F1ApiApplication.class, args);
		HibernateUtil.getSessionFactory();
	}

}
