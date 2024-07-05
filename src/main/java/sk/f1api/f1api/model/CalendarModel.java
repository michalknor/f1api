package sk.f1api.f1api.model;

import java.util.HashMap;

import lombok.Getter;
import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.entity.Version;

@Getter
public class CalendarModel {

	private int year;

	private HashMap<Byte, GrandPrixModel> grandPrixes;

	public CalendarModel(Season season) {
		this.year = season.getYear();
		this.grandPrixes = new HashMap<>();
		
		if (season.getVersions() != null) {
			for (Version version : season.getVersions()) {
				if (version.getGrandPrixes() != null) {
					version.getGrandPrixes().forEach(grandPrix -> this.grandPrixes.put(grandPrix.getRound(), new GrandPrixModel(grandPrix)));
				}
			}
		}
	}

}
