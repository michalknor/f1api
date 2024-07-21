package sk.f1api.f1api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import sk.f1api.f1api.model.CountryModel;
import sk.f1api.f1api.model.GrandPrixModel;
import sk.f1api.f1api.model.LocationModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GrandPrixControllerTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@SuppressWarnings("null")
	@Test
	public void existingRecord() {
		ResponseEntity<GrandPrixModel> response = getResponse(2024, 11);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		GrandPrixModel grandPrixModel = response.getBody();
		assertEquals("Austrian", grandPrixModel.getName());
		assertEquals(false, grandPrixModel.isCancelled());

		LocationModel locationModel = grandPrixModel.getLocation();
		assertEquals("Red Bull Ring", locationModel.getCircuit());
		assertEquals("Spielberg", locationModel.getCity());

		CountryModel countryModel = locationModel.getCountry();
		assertEquals("Austria", countryModel.getName());
		assertEquals("at", countryModel.getAbbreviation());
	}

	@Test
	public void nonExistingYear() {
		ResponseEntity<GrandPrixModel> response = getResponse(2022, 11);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void nonExistingRound() {
		ResponseEntity<GrandPrixModel> response = getResponse(2024, 25);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void overflowYear() {
		ResponseEntity<GrandPrixModel> response = getResponse(327682132, 25);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	private ResponseEntity<GrandPrixModel> getResponse(Integer year, Integer round) {
		return restTemplate.exchange(
				String.format("/api/grandprix?year=%d&round=%d", year, round),
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<GrandPrixModel>() {
				});
	}
}
