package sk.f1api.f1api.model;

import lombok.Getter;
import sk.f1api.f1api.entity.City;

@Getter
public class CountryModel {

	private String name;

	private String abbreviation;

	public CountryModel(City city) {
		this.name = city.getCountry().getName();
		this.abbreviation = city.getCountry().getAbbreviation();
	}
	
}
