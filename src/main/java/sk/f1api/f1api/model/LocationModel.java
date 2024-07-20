package sk.f1api.f1api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import sk.f1api.f1api.entity.Circuit;

@Getter
public class LocationModel {

	private String circuit;

	private String city;

	private CountryModel country;

	public LocationModel(Circuit circuit) {
		this.circuit = circuit.getName();
		this.city = circuit.getCity().getName();

		this.country = new CountryModel(circuit.getCity());
	}
	
	public LocationModel() {

	}

	@JsonCreator
    public LocationModel(@JsonProperty("circuit") String circuit, @JsonProperty("city") String city, @JsonProperty("country") CountryModel country) {
        this.circuit = circuit;
        this.city = city;
        this.country = country;
    }
	
}
