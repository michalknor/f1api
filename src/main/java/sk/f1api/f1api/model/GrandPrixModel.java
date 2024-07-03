package sk.f1api.f1api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrandPrixModel {
	

    private Byte round;

    private String name;

    private boolean cancelled;

	public GrandPrixModel(sk.f1api.f1api.entity.GrandPrix grandPrix) {
		this.round = grandPrix.getRound();
		this.name = grandPrix.getName();
		this.cancelled = grandPrix.isCancelled();

		System.out.println(grandPrix.getEvents());
	}

}
