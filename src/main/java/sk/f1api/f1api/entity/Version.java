package sk.f1api.f1api.entity;

import java.util.List;
import org.hibernate.Session;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Version implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDateTime created;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EventType> criteria = cb.createQuery(EventType.class);
        Root<EventType> root = criteria.from(EventType.class);

        criteria.select(root).where(cb.equal(root.get("id"), id));

        List<EventType> sessionTypes = session.createQuery(criteria).getResultList();

        return sessionTypes.size() == 1;
    }
}