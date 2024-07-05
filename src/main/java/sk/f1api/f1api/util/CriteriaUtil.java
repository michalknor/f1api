package sk.f1api.f1api.util;

import org.hibernate.Session;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

public class CriteriaUtil {

	@Getter
	public static class CriteriaComponents<T> {

		private CriteriaBuilder criteriaBuilder;

		private CriteriaQuery<T> criteriaQuery;

		private Root<T> root;

		public CriteriaComponents(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<T> root) {
			this.criteriaBuilder = criteriaBuilder;
			this.criteriaQuery = criteriaQuery;
			this.root = root;
		}
	}

	public static <T> CriteriaComponents<T> getCriteriaComponents(Session session, Class<T> c) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);

        Root<T> root = criteriaQuery.from(c);

		return new CriteriaComponents<T>(criteriaBuilder, criteriaQuery, root);
	}

}
