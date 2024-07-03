package sk.f1api.f1api.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.util.HibernateUtil;

@Getter
@Setter
public class CalendarModel {

	private int year;

	private List<GrandPrixModel> grandPrixes;

	public CalendarModel(Season season) {
		this.year = season.getYear();
		this.grandPrixes = new ArrayList<GrandPrixModel>();
		
		if (season.getGrandPrixes() != null) {
			season.getGrandPrixes().forEach(grandPrix -> this.grandPrixes.add(new GrandPrixModel(grandPrix)));
		}
	}

	public static List<CalendarModel> loadAll() {
		List<Season> seasons = Season.loadAll(HibernateUtil.getSessionFactory().openSession());

		List<CalendarModel> calendars = new ArrayList<CalendarModel>();

		seasons.forEach(season -> calendars.add(new CalendarModel(season)));

		return calendars;
	}

}
