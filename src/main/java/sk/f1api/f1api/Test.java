package sk.f1api.f1api;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "\"Test\"")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}