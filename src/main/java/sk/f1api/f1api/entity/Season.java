package sk.f1api.f1api.entity;

import java.util.List;

import org.hibernate.Session;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

import sk.f1api.f1api.util.CriteriaUtil;
import sk.f1api.f1api.util.CriteriaUtil.CriteriaComponents;

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
                        cb.equal(root.get("year"), year)));

        return session.createQuery(criteria).getMaxResults() > 0;
    }

    private static List<Season> load(Session session, CriteriaBuilder criteriaBuilder,
            CriteriaQuery<Season> criteriaQuery, Root<Season> root, Predicate predicate, Integer currentVersionId) {
        if (currentVersionId == null) {
            if (predicate == null) {
                criteriaQuery.select(root);
                return session.createQuery(criteriaQuery).getResultList();
            }

            criteriaQuery.select(root).where(predicate);

            return session.createQuery(criteriaQuery).getResultList();
        }

        root.fetch("versions", JoinType.LEFT);

        Join<Season, Version> versionJoin = root.join("versions");

        Predicate versionPredicate = criteriaBuilder.greaterThan(versionJoin.get("id"), currentVersionId);

        if (predicate != null) {
            versionPredicate = criteriaBuilder.and(predicate, versionPredicate);
        }

        criteriaQuery.select(root).where(criteriaBuilder.and(versionPredicate));

        return session.createQuery(criteriaQuery).getResultList();
    }

    private static List<Season> load(Session session, CriteriaBuilder criteriaBuilder,
            CriteriaQuery<Season> criteriaQuery, Root<Season> root, Integer currentVersionId) {
        return load(session, criteriaBuilder, criteriaQuery, root, null, currentVersionId);
    }

    public static List<Season> loadAll(Session session, int currentVersionId) {
        CriteriaComponents<Season> criteriaComponents = CriteriaUtil.getCriteriaComponents(session, Season.class);

        return load(session, criteriaComponents.getCriteriaBuilder(), criteriaComponents.getCriteriaQuery(),
                criteriaComponents.getRoot(), currentVersionId);
    }

    public static List<Season> loadByYear(Session session, int currentVersionId, int year) {
        CriteriaComponents<Season> criteriaComponents = CriteriaUtil.getCriteriaComponents(session, Season.class);

        return load(
                session,
                criteriaComponents.getCriteriaBuilder(),
                criteriaComponents.getCriteriaQuery(),
                criteriaComponents.getRoot(),
                criteriaComponents.getCriteriaBuilder().equal(criteriaComponents.getRoot().get("year"), year),
                currentVersionId);
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
