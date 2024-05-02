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
    private Long id;

    @Column(name = "country_abbreviation", nullable = false, unique = true)
	private String countryAbbreviation;
}