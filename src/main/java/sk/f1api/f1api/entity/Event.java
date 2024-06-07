package sk.f1api.f1api.entity;

import org.hibernate.Session;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Event implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grand_prix_id")
    private GrandPrix grandPrix;

    @OneToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @Column(nullable = false)
    private Byte round;

    @Column(nullable = false, name = "time_from")
    private LocalDateTime timeFrom;

    @Column(name = "time_to")
    private LocalDateTime timeTo;

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> root = criteria.from(Event.class);

        // criteria.select(root).where(
        //     cb.and(
        //         cb.equal(root.get("year"), year), 
        //         cb.equal(root.get("version"), version)
        //     )
        // );

        CriteriaQuery<Event> searchQuery = cb.createQuery(Event.class);
        Root<Event> aRoot = searchQuery.from(Event.class);
        Join<Event, Season> bJoin= aRoot.join("Round", JoinType.LEFT);
        bJoin.on(cb.equal(bJoin.get("idLanguage"), 22));

        return session.createQuery(criteria).getMaxResults() > 0;
    }

    @Override
    public String toString() {
        return String.format("Event(id='%s', round='%s', timeFrom='%s', timeTo='%s', sessionType=%s)", id, round, timeFrom, timeTo, eventType);
    }
}