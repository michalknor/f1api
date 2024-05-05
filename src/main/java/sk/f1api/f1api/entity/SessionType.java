package sk.f1api.f1api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SessionType {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 2, columnDefinition="CHAR")
    private String abbreviation;

    @Column(nullable = false, unique = true, length = 20)
    private String name;
}