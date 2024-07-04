package sk.f1api.f1api.model;

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
	
}
