package sk.f1api.f1api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Country {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 2, columnDefinition="CHAR")
    private String abbreviation;

    @Column(nullable = false, unique = true, length = 30)
    private String name;
}