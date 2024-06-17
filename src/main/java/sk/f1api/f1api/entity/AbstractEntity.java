package sk.f1api.f1api.entity;

import java.lang.reflect.Field;

public abstract class AbstractEntity {

    public <T> void copy(T from) {
        Field[] fields = from.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(from);
                field.set(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
