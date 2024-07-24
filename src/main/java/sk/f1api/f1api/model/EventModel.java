package sk.f1api.f1api.model;

import java.time.LocalDateTime;
import java.util.HashMap;

import lombok.Getter;
import sk.f1api.f1api.entity.Event;
import sk.f1api.f1api.entity.GrandPrix;

@Getter
public class EventModel {

	private LocalDateTime timeFrom;

	private LocalDateTime timeTo;

	private String abbreviation;

	public EventModel() {
		
	}

	public EventModel(Event event) {
		this.timeFrom = event.getTimeFrom();
		this.timeTo = event.getTimeTo();

		this.abbreviation = event.getEventType().getAbbreviation();
	}
}
