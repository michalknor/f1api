package sk.f1api.f1api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

	@Value("${f1.openapi.prod-url}")
	private String prodUrl;

	@Bean
	public OpenAPI myOpenAPI() {
		Server prodServer = new Server();
		prodServer.setUrl(prodUrl);
		prodServer.setDescription("Server URL in Production environment");

		Contact contact = new Contact();
		contact.setEmail("mikeknor2503@gmail.com");
		contact.setName("Michal Knor");

		Info info = new Info()
				.title("Calendars Management API")
				.version("1.0")
				.contact(contact)
				.description("This API exposes endpoints to show calendars.");

		return new OpenAPI().info(info);// .servers(List.of(prodServer));
	}
}
