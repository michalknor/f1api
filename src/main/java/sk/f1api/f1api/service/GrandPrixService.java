package sk.f1api.f1api.service;

import org.springframework.stereotype.Service;

import lombok.Getter;
import sk.f1api.f1api.entity.GrandPrix;
import sk.f1api.f1api.model.GrandPrixModel;
import sk.f1api.f1api.util.HibernateUtil;

@Service
@Getter
public class GrandPrixService {

	private GrandPrixModel grandPrixModel;

	public GrandPrixService() {

	}

	public GrandPrixModel find(Integer currentVersion, Short year, Byte round) {
		GrandPrix grandPrix = loadAll(currentVersion, year, round);

		if (grandPrix == null) {
			grandPrixModel = null == null ? null : new GrandPrixModel(grandPrix);
			return grandPrixModel;
		}
		
		grandPrixModel = new GrandPrixModel(grandPrix);
		return grandPrixModel;
	}

	private GrandPrix loadAll(Integer currentVersion, Short year, Byte round) {
		return GrandPrix.loadByYearAndRound(HibernateUtil.getSessionFactory().openSession(), currentVersion, year, round);
	}

}
