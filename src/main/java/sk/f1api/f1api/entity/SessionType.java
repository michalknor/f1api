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
@Table(name = "session_type")
public class SessionType implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, columnDefinition="CHAR(2)")
    private String abbreviation;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    public SessionType() {
    }

    public SessionType(String abbreviation, String name) {
        this.abbreviation = abbreviation;
        this.name = name;
    }

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SessionType> criteria = cb.createQuery(SessionType.class);
        Root<SessionType> root = criteria.from(SessionType.class);

        criteria.select(root).where(cb.equal(root.get("abbreviation"), abbreviation));

        List<SessionType> sessionTypes = session.createQuery(criteria).getResultList();

        return sessionTypes.size() == 1;
    }

    public static void fillTable(Session session) {
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

			List<SessionType> sessionTypes = new ArrayList<SessionType>() {{
                add(new SessionType("P1", "Practice 1"));
                add(new SessionType("P2", "Practice 2"));
                add(new SessionType("P3", "Practice 3"));
                add(new SessionType("SQ", "Sprint Qualifying"));
                add(new SessionType("S", "Sprint"));
                add(new SessionType("Q", "Qualifying"));
                add(new SessionType("R", "Race"));
            }};

            for (SessionType sessionType : sessionTypes) {
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
}