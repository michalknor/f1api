package sk.f1api.f1api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.util.HibernateUtil;

@Service
@Setter
@Getter
public class CalendarService {

	private List<CalendarModel> calendars;

	public CalendarService() {

	}

	public List<CalendarModel> find(Integer currentVersion, Integer year) {
		calendars = new ArrayList<>();

		if (year == null) {
			loadAll(currentVersion).forEach(season -> calendars.add(new CalendarModel(season)));
			return calendars;
		}

		loadByYear(currentVersion, year).forEach(season -> calendars.add(new CalendarModel(season)));
		return calendars;
	}

	private List<Season> loadAll(Integer currentVersion) {
		return Season.loadAll(HibernateUtil.getSessionFactory().openSession(), currentVersion);
	}

	private List<Season> loadByYear(Integer currentVersion, int year) {
		return Season.loadByYear(HibernateUtil.getSessionFactory().openSession(), currentVersion, year);
	}

}
