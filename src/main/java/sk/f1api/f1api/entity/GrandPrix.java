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
}