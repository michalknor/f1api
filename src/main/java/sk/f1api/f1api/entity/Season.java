package sk.f1api.f1api.entity;

import java.util.List;

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
public class Season implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<GrandPrix> grandPrixes;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    private Short year;

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EventType> criteria = cb.createQuery(EventType.class);
        Root<EventType> root = criteria.from(EventType.class);

        criteria.select(root).where(
            cb.and(
                cb.equal(root.get("year"), year), 
                cb.equal(root.get("version"), version)
            )
        );

        return session.createQuery(criteria).getMaxResults() > 0;
    }
}
