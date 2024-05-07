package sk.f1api.f1api.entity;

import org.hibernate.Session;

public interface Identifiable {
    public boolean isDuplicate(Session session);
}
