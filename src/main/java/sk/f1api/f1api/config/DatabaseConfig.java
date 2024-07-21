package sk.f1api.f1api.config;

import java.util.Properties;

import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@PropertySource("classpath:database.properties")
@Getter
public class DatabaseConfig {

	@Value("${hibernate.connection.url}")
	private String url;

	@Value("${hibernate.connection.username}")
	private String username;

	@Value("${hibernate.connection.password}")
	private String password;

	@Bean
	public SessionFactory sessionFactory() {
		Properties dbProperties = new Properties();
		dbProperties.setProperty("hibernate.connection.url", url);
		dbProperties.setProperty("hibernate.connection.username", username);
		dbProperties.setProperty("hibernate.connection.password", password);

		org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
		configuration.setProperties(dbProperties);
		configuration.configure("hibernate.cfg.xml");

		return configuration.buildSessionFactory();
	}
}
