package sk.f1api.f1api.entity;

import java.util.List;

import org.hibernate.Session;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Country implements Identifiable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 2, columnDefinition="CHAR")
    private String abbreviation;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Override
    public boolean isDuplicate(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Country> criteria = cb.createQuery(Country.class);
        Root<Country> root = criteria.from(Country.class);

        criteria.select(root).where(cb.equal(root.get("abbreviation"), abbreviation));

        List<Country> countries = session.createQuery(criteria).getResultList();

        return countries.size() == 1;
    }
}