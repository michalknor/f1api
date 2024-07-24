package sk.f1api.f1api.model;

import java.util.HashMap;

import lombok.Getter;
import sk.f1api.f1api.entity.Event;
import sk.f1api.f1api.entity.GrandPrix;
import sk.f1api.f1api.entity.Version;

@Getter
public class GrandPrixModel {

	private String name;

	private boolean cancelled;

	private LocationModel location;

	private HashMap<Byte, EventModel> events;

	public GrandPrixModel() {
		
	}

	public GrandPrixModel(GrandPrix grandPrix) {
		this.name = grandPrix.getName();
		this.cancelled = grandPrix.isCancelled();

		this.location = new LocationModel(grandPrix.getCircuit());

		this.events = new HashMap<>();
		for (Event event : grandPrix.getEvents()) {
			this.events.put(event.getRound(), new EventModel(event));
		}
	}
}
