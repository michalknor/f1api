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

    @ManyToOne
    @JoinColumn(name = "country_id")
    Country country;

    @Column(nullable = false, length = 50)
    private String name;

    @Override
    public String toString() {
        return "Country(id='" + id + "', name='" + name + "', country=" + country + ")";
    }
}