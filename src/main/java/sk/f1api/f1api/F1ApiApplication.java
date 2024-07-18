package sk.f1api.f1api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import sk.f1api.f1api.config.ScrapperConfig;
import sk.f1api.f1api.scrapper.Scrapper;

@SpringBootApplication
public class F1ApiApplication {

	static ScrapperConfig scrapperConfig;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(F1ApiApplication.class, args);

		scrapperConfig = context.getBean(ScrapperConfig.class);

		if (false) {
			Scrapper.scrape(scrapperConfig.getUrlCalendar(), scrapperConfig.getUrlWiki());
		}
	}

}
