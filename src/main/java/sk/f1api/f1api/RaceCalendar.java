package sk.f1api.f1api;

public class RaceCalendar {
	String countryAbbreviation;

	int index;

	@Override
	public String toString() {
		return "countryAbbreviation: " + this.countryAbbreviation + ", index: " + this.index;
	}
}
