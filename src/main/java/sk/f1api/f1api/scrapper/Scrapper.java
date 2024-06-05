package sk.f1api.f1api.scrapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import sk.f1api.f1api.entity.EventType;
import sk.f1api.f1api.entity.GrandPrix;
import sk.f1api.f1api.entity.Season;
import sk.f1api.f1api.entity.Version;
import sk.f1api.f1api.scrapper.parser.Calendar;
import sk.f1api.f1api.scrapper.parser.Wiki;

public class Scrapper {
	public static SessionFactory sessionFactory;
	public static void main(String[] args) {
		initSessionFactory();

		EventType.fillTable(sessionFactory.openSession());

		short year = 2024;

		Wiki f1Wiki = new Wiki();
		f1Wiki.saveToFile("wiki.html");
		
		Calendar f1Calendar = new Calendar();

		Version version = new Version();
		Season season = new Season();
		season.setVersion(version);
		season.setYear(year);
		
		GrandPrix[] grandPrixes = new GrandPrix[f1Wiki.getNumberOfRaces()];

		for (int i = 1; i < f1Wiki.getNumberOfRaces() + 1; i++) {
			GrandPrix grandPrix = new GrandPrix(version, season, (byte) i);
			
			f1Calendar.fillEvents(grandPrix, i);
			f1Wiki.fillGrandPrix(grandPrix, i);
			f1Wiki.fillCircuit(grandPrix.getCircuit(), i);
			f1Wiki.fillCity(grandPrix.getCircuit().getCity(), i);
			f1Wiki.fillCountry(grandPrix.getCircuit().getCity().getCountry(), i);
			f1Calendar.fillCountry(grandPrix.getCircuit().getCity().getCountry(), i);

			grandPrixes[i-1] = grandPrix;
		}
		
		for (GrandPrix grandPrix : grandPrixes) {
			System.out.println(grandPrix);
		}
	}

	public static Document getDocument(String url) {
		Map.Entry<Integer, String> result = getHTMLFromPage(url);

		if (result.getKey() != 200) {
			return null;
		}

		String html = result.getValue();

		return Jsoup.parse(html);
	}

	public static Map.Entry<Integer, String> getHTMLFromPage(String page) {
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(
					HttpRequest.newBuilder()
							.uri(URI.create(page))
							.build(),
					HttpResponse.BodyHandlers.ofString());

			return new AbstractMap.SimpleEntry<>(response.statusCode(), response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return new AbstractMap.SimpleEntry<>(-1, null);
	}

	public static String getValueOfKeyFromProperties(String node) {
		try (FileInputStream input = new FileInputStream(
                Paths.get("src", "main", "resources", "scrapper.properties").toString())) {
			Properties dbProperties = new Properties();
            dbProperties.load(input);

            return dbProperties.getProperty(node);
        } catch (IOException e) {
            e.printStackTrace();
        }

		return "";
	}

	public static void initSessionFactory() {
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
        Scrapper.sessionFactory = configuration
                // .addAnnotatedClass(Test.class)
                .buildSessionFactory();
	}
}
