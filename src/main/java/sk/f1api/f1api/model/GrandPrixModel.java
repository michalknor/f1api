package sk.f1api.f1api.model;

import lombok.Getter;
import sk.f1api.f1api.entity.GrandPrix;

@Getter
public class GrandPrixModel {

	private String name;

	private boolean cancelled;

	private LocationModel location;

	public GrandPrixModel() {
		
	}

	public GrandPrixModel(GrandPrix grandPrix) {
		this.name = grandPrix.getName();
		this.cancelled = grandPrix.isCancelled();

		this.location = new LocationModel(grandPrix.getCircuit());
	}
}
