package sk.f1api.f1api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(@SuppressWarnings("null") PathMatchConfigurer configurer) {
		configurer.addPathPrefix("/api", cls -> cls.getPackage().getName().equals("sk.f1api.f1api.controller"));
	}
}
