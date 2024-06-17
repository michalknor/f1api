package sk.f1api.f1api.entity;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    @OneToOne(mappedBy = "version", cascade = CascadeType.ALL)
    private Season season;

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

    public void removeDuplicity() {
        List<GrandPrix> grandPrixes = season.getGrandPrixes();
        
        for (int i = 0; i < grandPrixes.size(); i++) {
            GrandPrix grandPrixI = grandPrixes.get(i);

            for (int j = i + 1; j < grandPrixes.size(); j++) {
                GrandPrix grandPrixJ = grandPrixes.get(j);

                if (grandPrixJ.getCircuit().getCity().getCountry().getAbbreviation()
                        .equals(grandPrixI.getCircuit().getCity().getCountry().getAbbreviation())) {
                            
                    grandPrixJ.getCircuit().getCity().setCountry(grandPrixI.getCircuit().getCity().getCountry());

                    if (grandPrixJ.getCircuit().getCity().getName()
                            .equals(grandPrixI.getCircuit().getCity().getName())) {

                        grandPrixJ.getCircuit().setCity(grandPrixI.getCircuit().getCity());

                        if (grandPrixJ.getCircuit().getName().equals(grandPrixI.getCircuit().getName())) {
                            grandPrixJ.setCircuit(grandPrixI.getCircuit());
                        }
                    }
                }
            }
        }
    }
}