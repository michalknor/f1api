package sk.f1api.f1api.entity;

import java.util.List;

import org.hibernate.Session;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Season implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Version> versions;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    private Short year;

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EventType> criteria = cb.createQuery(EventType.class);
        Root<EventType> root = criteria.from(EventType.class);

        criteria.select(root).where(
            cb.and(
                cb.equal(root.get("year"), year)
            )
        );

        return session.createQuery(criteria).getMaxResults() > 0;
    }

    public static List<Season> loadAll(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Season> criteriaQuery = criteriaBuilder.createQuery(Season.class);
        Root<Season> root = criteriaQuery.from(Season.class);

        root.fetch("versions", JoinType.LEFT);

        criteriaQuery.select(root);

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public String toString() {
        String versionsConcated = "";
        if (versions == null || versions.isEmpty()) {
            versionsConcated = "null";
        } else {
            for (Version version : versions) {
                versionsConcated += version + ", ";
            }
            versionsConcated = versionsConcated.substring(0, versionsConcated.length() - 3);
        }

        return String.format("Season(id='%s', year='%d', versions=[%s])", id, year, versionsConcated);
    }
}
