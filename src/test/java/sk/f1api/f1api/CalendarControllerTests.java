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

import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.model.CountryModel;
import sk.f1api.f1api.model.GrandPrixModel;
import sk.f1api.f1api.model.LocationModel;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CalendarControllerTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@SuppressWarnings("null")
	@Test
	public void existingRecords() {
		ResponseEntity<HashMap<Short, CalendarModel>> response = getResponse();
		assertEquals(HttpStatus.OK, response.getStatusCode());

		CalendarModel calendarModel = response.getBody().get((short) 2024);

		HashMap<Byte, GrandPrixModel> hashMapOfGrandPrixModel = calendarModel.getGrandPrixes();
		assertEquals(24, hashMapOfGrandPrixModel.size());

		GrandPrixModel grandPrixModel = hashMapOfGrandPrixModel.get((byte) 11);
		assertEquals("Austrian", grandPrixModel.getName());
		assertEquals(false, grandPrixModel.isCancelled());

		LocationModel locationModel = grandPrixModel.getLocation();
		assertEquals("Red Bull Ring", locationModel.getCircuit());
		assertEquals("Spielberg", locationModel.getCity());

		CountryModel countryModel = locationModel.getCountry();
		assertEquals("Austria", countryModel.getName());
		assertEquals("at", countryModel.getAbbreviation());
	}

	@SuppressWarnings("null")
	@Test
	public void existingRecord() {
		ResponseEntity<CalendarModel> response = getResponse(2024);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		CalendarModel calendarModel = response.getBody();

		HashMap<Byte, GrandPrixModel> hashMapOfGrandPrixModel = calendarModel.getGrandPrixes();

		GrandPrixModel grandPrixModel = hashMapOfGrandPrixModel.get((byte) 11);
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
		ResponseEntity<CalendarModel> response = getResponse(2023);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void overflowYear() {
		ResponseEntity<CalendarModel> response = getResponse(327682132);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	private ResponseEntity<HashMap<Short, CalendarModel>> getResponse() {
		return restTemplate.exchange(
				"/api/calendars",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<HashMap<Short, CalendarModel>>() {
				});
	}

	private ResponseEntity<CalendarModel> getResponse(Integer year) {
		return restTemplate.exchange(
				String.format("/api/calendar?year=%d", year),
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<CalendarModel>() {
				});
	}
}
