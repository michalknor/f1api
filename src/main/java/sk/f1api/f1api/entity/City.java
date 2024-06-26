package sk.f1api.f1api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class City {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false, length = 50)
    private String name;

    public City() {
        country = new Country();
    }

    @Override
    public String toString() {
        return "City(id='" + id + "', name='" + name + "', country=" + country + ")";
    }
}