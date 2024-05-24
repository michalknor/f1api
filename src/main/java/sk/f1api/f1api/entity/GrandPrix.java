package sk.f1api.f1api.entity;

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

    @ManyToOne
    @JoinColumn(name = "version_id")
    Version version;

    @ManyToOne
    @JoinColumn(name = "season_id")
    Season season;

    @ManyToOne
    @JoinColumn(name = "circuit_id")
    Circuit circuit;

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
        return String.format("GrandPrix(id='%s', round='%s', name='%s', cancelled='%s', version=%s, season=%s, circuit=%s)", id, round, name, cancelled, version, season, circuit);
    }
}