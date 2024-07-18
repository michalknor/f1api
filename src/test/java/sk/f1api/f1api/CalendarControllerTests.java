package sk.f1api.f1api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import sk.f1api.f1api.controller.CalendarController;
import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.service.CalendarService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;


@WebMvcTest(CalendarController.class)
@ExtendWith(SpringExtension.class)
class CalendarControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CalendarService calendarService;

	@Test
	void existingYear() throws Exception {
		Integer year = 2024;
		ArrayList<CalendarModel> calendars = new ArrayList<>();
		var a = new Season();
		a.setYear(year.shortValue());
		calendars.add(new CalendarModel(a));
		when(calendarService.find(null, year)).thenReturn(calendars);

		mockMvc.perform(get("/api/calendar?year={year}", year)).andExpect(status().isOk());
	}

	@Test
	void nonExistingYear() throws Exception {
		Integer year = 2024;
		when(calendarService.find(null, year)).thenReturn(new ArrayList<CalendarModel>());

		mockMvc.perform(get("/api/calendar?year={year}", year)).andExpect(status().isNoContent());
	}

}
