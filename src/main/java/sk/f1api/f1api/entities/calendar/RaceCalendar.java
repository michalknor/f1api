package sk.f1api.f1api.entities.calendar;

import jakarta.persistence.*;

@Entity
public class RaceCalendar {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String countryAbbreviation;

    private int index;

    public void setCountryAbbreviation(String countryAbbreviation) {
        this.countryAbbreviation = countryAbbreviation;
    }
    
    public String getCountryAbbreviation() {
        return this.countryAbbreviation;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getName() {
        return this.index;
    }

	@Override
	public String toString() {
		return "countryAbbreviation: " + this.countryAbbreviation + ", index: " + this.index;
	}
}