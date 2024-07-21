package sk.f1api.f1api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Getter;

import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.util.HibernateUtil;

@Service
@Getter
public class CalendarService {

	public CalendarService() {

	}
	
	public HashMap<Short, CalendarModel> find(Integer currentVersion) {
		HashMap<Short, CalendarModel> calendars = new HashMap<>();

		loadAll(currentVersion).forEach(season -> calendars.put(season.getYear(), new CalendarModel(season)));
		return calendars;
	}

	public CalendarModel find(Integer currentVersion, Short year) {
		List<Season> season = loadByYear(currentVersion, year);
		
		return season.isEmpty() ? null : new CalendarModel(season.get(0));
	}

	private List<Season> loadAll(Integer currentVersion) {
		return Season.loadAll(HibernateUtil.getSessionFactory().openSession(), currentVersion);
	}

	private List<Season> loadByYear(Integer currentVersion, Short year) {
		return Season.loadByYear(HibernateUtil.getSessionFactory().openSession(), currentVersion, year);
	}

}
