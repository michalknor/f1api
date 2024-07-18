package sk.f1api.f1api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@PropertySource("classpath:scrapper.properties")
@Getter
public class ScrapperConfig {

	@Value("${url.calendar}")
	private String urlCalendar;

	@Value("${url.wiki}")
	private String urlWiki;
}
