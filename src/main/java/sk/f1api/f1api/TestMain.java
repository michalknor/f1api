package sk.f1api.f1api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.nio.file.Paths;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;

public class TestMain {
    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

        Properties dbProperties = new Properties();
        try (FileInputStream input = new FileInputStream(
                Paths.get("src", "main", "resources", "database.properties").toString())) {
            dbProperties.load(input);
            Enumeration<?> propertyNames = dbProperties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                String propertyValue = dbProperties.getProperty(propertyName);
                configuration.setProperty(propertyName, propertyValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create Hibernate SessionFactory
        SessionFactory sessionFactory = configuration
                // .addAnnotatedClass(Test.class)
                .buildSessionFactory();

        // Create session
        try (Session session = sessionFactory.getCurrentSession()) {
            // Begin transaction
            session.beginTransaction();

            System.out.println(Test.class);

            // Perform query
            Test test = session.get(Test.class, 1); // Assuming employee with ID 1 exists

            // Print result
            System.out.println(test.getName());

            // Commit transaction
            session.getTransaction().commit();
            System.out.println(test);
        } finally {
            // Close SessionFactory
            sessionFactory.close();
        }

        // Session session = sessionFactory.openSession();
        // Transaction tx = null;
        // try {
        //     tx = session.beginTransaction();
        //     Test test = new Test();
        //     test.setName("John");
        //     session.persist(test);
        //     tx.commit();
        // } catch (HibernateException e) {
        //     if (tx != null) {
        //         tx.rollback();
        //     }
        //     e.printStackTrace();
        // } finally {
        //     session.close();
        // }
    }
}
