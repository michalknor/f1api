package sk.f1api.f1api.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
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

			sessionFactory = configuration.buildSessionFactory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
