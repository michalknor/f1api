package sk.f1api.f1api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Circuit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    City city;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public Circuit() {
        city = new City();
    }

    @Override
    public String toString() {
        return "Country(id='" + id + "', name='" + name + "', city=" + city + ")";
    }
}