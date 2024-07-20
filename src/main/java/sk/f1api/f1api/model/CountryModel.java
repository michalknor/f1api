package sk.f1api.f1api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public CountryModel() {
    }

    @JsonCreator
    public CountryModel(@JsonProperty("name") String name, @JsonProperty("abbreviation") String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }
}
