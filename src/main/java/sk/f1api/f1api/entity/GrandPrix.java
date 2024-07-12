package sk.f1api.f1api.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import sk.f1api.f1api.util.CriteriaUtil;
import sk.f1api.f1api.util.CriteriaUtil.CriteriaComponents;

@Getter
@Setter
@Entity
@Table(name = "grand_prix")
public class GrandPrix {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "version_id")
	private Version version;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "circuit_id")
	private Circuit circuit;

	@OneToMany(mappedBy = "grandPrix", cascade = CascadeType.ALL)
	private List<Event> events;

	@Column(nullable = false)
	private Byte round;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false)
	private boolean cancelled;

	public GrandPrix() {

	}

	public GrandPrix(Version version, Byte round) {
		this.version = version;
		this.round = round;
		this.circuit = new Circuit();
	}

	private static GrandPrix load(Session session, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<GrandPrix> criteriaQuery, Root<GrandPrix> root, Predicate predicate, Integer currentVersionId,
			Integer year) {

		Join<GrandPrix, Version> versionJoin = root.join("version");

		Join<Version, Season> seasonJoin = versionJoin.join("season");

		List<Order> order = new ArrayList<Order>();

		order.add(criteriaBuilder.desc(seasonJoin.get("year")));
		order.add(criteriaBuilder.asc(root.get("round")));

		if (year != null) {
			Predicate predicateYear = criteriaBuilder.equal(seasonJoin.get("year"), year);
			if (predicate == null) {
				predicate = predicateYear;
			} else {
				predicate = criteriaBuilder.and(predicate, predicateYear);
			}
		}

		if (currentVersionId == null) {
			criteriaQuery = criteriaQuery.select(root).orderBy(order);

			if (predicate != null) {
				criteriaQuery = criteriaQuery.where(predicate);
			}

			return session.createQuery(criteriaQuery).getResultList().get(0);
		}

		Predicate predicateVersion = criteriaBuilder.greaterThan(versionJoin.get("id"), currentVersionId);

		if (predicate != null) {
			predicate = criteriaBuilder.and(predicate, predicateVersion);
		} else {
			predicate = predicateVersion;
		}

		criteriaQuery.select(root).where(criteriaBuilder.and(predicate)).orderBy(order);

		return session.createQuery(criteriaQuery).getResultList().get(0);
	}

	public static GrandPrix loadByYearAndRound(Session session, Integer currentVersionId, int year, int round) {
		CriteriaComponents<GrandPrix> criteriaComponents = CriteriaUtil.getCriteriaComponents(session, GrandPrix.class);

		return load(
				session,
				criteriaComponents.getCriteriaBuilder(),
				criteriaComponents.getCriteriaQuery(),
				criteriaComponents.getRoot(),
				criteriaComponents.getCriteriaBuilder().equal(criteriaComponents.getRoot().get("round"), round),
				currentVersionId,
				year);
	}

	@Override
	public String toString() {
		String eventsConcated = "";
		if (events == null || events.isEmpty()) {
			eventsConcated = "null";
		} else {
			for (Event event : events) {
				eventsConcated += event + ", ";
			}
			eventsConcated = eventsConcated.substring(0, eventsConcated.length() - 3);
		}

		return String.format("GrandPrix(id='%s', round='%s', name='%s', cancelled='%s', circuit=%s, events=[%s])", id,
				round, name, cancelled, circuit, eventsConcated);
	}

	public void save(Session session) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(this);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}