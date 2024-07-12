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
public class GrandPrixService {

  private List<CalendarModel> calendars;

  public GrandPrixService() {

  }

  public List<CalendarModel> find(Integer currentVersion, Integer year) {
    calendars = new ArrayList<>();

    if (currentVersion == null) {
      currentVersion = 0;
    }

    if (year == null) {
      loadAll(currentVersion).forEach(season -> calendars.add(new CalendarModel(season)));
      return calendars;
    }

    loadByYear(currentVersion, year).forEach(season -> calendars.add(new CalendarModel(season)));
    return calendars;
  }

	private List<Season> loadAll(int currentVersion) {
		List<Season> seasons = Season.loadAll(HibernateUtil.getSessionFactory().openSession(), currentVersion);

		return seasons;
	}

	private List<Season> loadByYear(int currentVersion, int year) {
		List<Season> seasons = Season.loadByYear(HibernateUtil.getSessionFactory().openSession(), currentVersion, year);

		return seasons;
	}

}
