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
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.scrapper.parser.Calendar;
import sk.f1api.f1api.scrapper.parser.Wiki;

public class Scrapper {
	public static SessionFactory sessionFactory;
	public static void main(String[] args) {
		initSessionFactory();

		Wiki f1Wiki = new Wiki();
		
		Calendar f1Calendar = new Calendar();

		List<Country> countries = f1Wiki.fillCountry();

		for (Country country : countries) {
			System.out.println(country);
		}

		// Document f1CalendarRoot = Jsoup.parse(html);
		
		// Element f1CalendarCalendar = f1CalendarRoot.select("body > div > main > div > div").first();

		// int sectionIndex = 0;
		// Element f1CalendarSection = f1CalendarCalendar.select("div:nth-child(" + (sectionIndex + 2) + ") > section").first();


		// while (f1CalendarSection != null) {
		// 	parseRace(f1CalendarSection);

		// 	sectionIndex++;
		// 	f1CalendarSection = f1CalendarCalendar.select("div:nth-child(" + (sectionIndex + 2) + ") > section").first();
		// }
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

	public static void parseRace(Element section) {
		String countryAbbreviation = getCountryAbbreviation(section.select("div > div > img").first());
			
		Country country = new Country();

		country.setAbbreviation(countryAbbreviation);

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Country> criteria = cb.createQuery(Country.class);
			Root<Country> root = criteria.from(Country.class);

			criteria.select(root).where(cb.equal(root.get("abbreviation"), countryAbbreviation));

			Query<Country> query = session.createQuery(criteria);
			List<Country> countries = query.getResultList();

			if (countries.size() == 0) {
				country = new Country();
				country.setAbbreviation(getCountryAbbreviation(section.select("div > div > img").first()));
				session.persist(country);
			}

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

		// for (int i = 1; i < 6; i++) {
		// 	Element sectionSession = section.select("table > tbody > tr:nth-child(" + i + ")").first();

			// System.out.println(sectionSession);

			// System.out.println("");

			// System.out.println(getAbbreviationForEventName(sectionSession.select("td").first().text()));
		// }


		// race = {}
		// race["title"] = get_country_abbreviation(section.find("div/div/img").values())
		// sessions = []
	
		// for i in range(1, 6):
		// 	sectionSession = section.find(f"table/tbody/tr[{i}]")
		// 	session = {}
		// 	session["name"] = get_abbreviation_name(sectionSession.find("td").text.strip())
		// 	session["day"], session["month"] = sectionSession.find("td[2]").text.strip().replace("  ", " ").replace(".", "").split(" ")
		// 	session["time_from"] = sectionSession.find("td[3]/span").text.strip()
	
		// 	if sectionSession.find("td[3]/span[2]") is not None:
		// 		session["time_to"] = sectionSession.find("td[3]/span[2]").text.strip().replace("- ", "")
		// 	sessions.append(session)
	
		// race["sessions"] = sessions
	
		// return race
	}

	public static String getCountryAbbreviation(Element imgValues) {
		String src = imgValues.attributes().get("src");

		int lastIndexOfSlash = src.lastIndexOf("/");

		return src.substring(lastIndexOfSlash + 1, lastIndexOfSlash + 3);
	}

	public static String getAbbreviationForEventName(String eventName) {
		return switch (eventName) {
			case "1. tréning" -> "P1";
			case "2. tréning" -> "P2";
			case "3. tréning" -> "P3";
			case "Šprint. rozstrel" -> "SQ";
			case "Šprint" -> "SR";
			case "Kvalifikácia" -> "Q";
			case "Preteky" -> "R";
			default -> {
				System.out.println("invalid name!");
				yield "?";
			}
		};
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
