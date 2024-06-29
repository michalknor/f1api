package sk.f1api.f1api.entity;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.Session;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "event_type")
public class EventType  extends AbstractEntity implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, columnDefinition="VARCHAR(2)")
    private String abbreviation;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    public EventType() {
    }

    public EventType(String abbreviation, String name) {
        this.abbreviation = abbreviation;
        this.name = name;
    }

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EventType> criteria = cb.createQuery(EventType.class);
        Root<EventType> root = criteria.from(EventType.class);

        criteria.select(root).where(cb.equal(root.get("abbreviation"), abbreviation));

        List<EventType> sessionTypes = session.createQuery(criteria).getResultList();

        return sessionTypes.size() == 1;
    }

    public static void fillTable(Session session) {
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

			List<EventType> sessionTypes = new ArrayList<EventType>() {{
                add(new EventType("P1", "Practice 1"));
                add(new EventType("P2", "Practice 2"));
                add(new EventType("P3", "Practice 3"));
                add(new EventType("SQ", "Sprint Qualifying"));
                add(new EventType("S", "Sprint"));
                add(new EventType("Q", "Qualifying"));
                add(new EventType("R", "Race"));
            }};

            for (EventType sessionType : sessionTypes) {
                if (sessionType.isDuplicate(session)) {
                    continue;
                }
				session.persist(sessionType);
            }

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

    public void load(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EventType> criteria = cb.createQuery(EventType.class);
        Root<EventType> root = criteria.from(EventType.class);

        criteria.select(root).where(cb.equal(root.get("abbreviation"), abbreviation));

        EventType eventType = session.createQuery(criteria).setMaxResults(1).uniqueResult();

        if (eventType != null) {
            this.copy(eventType);
        }
    }

    @Override
    public String toString() {
        return String.format("EventType(id='%s', abbreviation='%s', name='%s')", id, abbreviation, name);
    }
}