package sk.f1api.f1api.entity;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "season_id")
    private Season season;

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

    public GrandPrix(Version version, Season season, Byte round) {
        this.season = season;
        this.version = version;
        this.round = round;
        this.circuit = new Circuit();
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
        
        return String.format("GrandPrix(id='%s', round='%s', name='%s', cancelled='%s', version=%s, season=%s, circuit=%s, events=[%s])", id, round, name, cancelled, version, season, circuit, eventsConcated);
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