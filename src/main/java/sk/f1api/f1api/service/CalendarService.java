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

  public List<CalendarModel> find(Integer version, Integer year) {
    calendars = new ArrayList<>();

    if (version == null) {
      version = 1;
    }

    if (year == null) {
      loadAll(version).forEach(season -> calendars.add(new CalendarModel(season)));
      return calendars;
    }

    loadByYear(version, year).forEach(season -> calendars.add(new CalendarModel(season)));
    return calendars;
  }

	private List<Season> loadAll(int version) {
		List<Season> seasons = Season.loadAll(HibernateUtil.getSessionFactory().openSession(), version);

		return seasons;
	}

	private List<Season> loadByYear(int version, int year) {
		List<Season> seasons = Season.loadByYear(HibernateUtil.getSessionFactory().openSession(), version, year);

		return seasons;
	}

}
