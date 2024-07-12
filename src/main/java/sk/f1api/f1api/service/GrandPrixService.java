package sk.f1api.f1api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import sk.f1api.f1api.entity.GrandPrix;
import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.model.GrandPrixModel;
import sk.f1api.f1api.util.HibernateUtil;

@Service
@Setter
@Getter
public class GrandPrixService {

	private GrandPrixModel grandPrix;

	public GrandPrixService() {

	}

	public GrandPrixModel find(Integer currentVersion, int year, int round) {
		grandPrix = new GrandPrixModel(loadAll(currentVersion, year, round));
		return grandPrix;
	}

	private GrandPrix loadAll(Integer currentVersion, int year, int round) {
		return GrandPrix.loadByYearAndRound(HibernateUtil.getSessionFactory().openSession(), currentVersion, year, round);
	}

}
