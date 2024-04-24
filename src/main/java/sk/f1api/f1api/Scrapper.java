package sk.f1api.f1api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.AbstractMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Scrapper {
	public static void main(String[] args) {
		String page = "";
		Map.Entry<Integer, String> result = getHTMLFromPage(page);

		if (result.getKey() != 200) {
			return;
		}

		String html = result.getValue();

		Document f1CalendarRoot = Jsoup.parse(html);
		
		Element f1CalendarCalendar = f1CalendarRoot.select("body > div > main > div > div").first();

		int sectionIndex = 0;
        Element f1CalendarSection = f1CalendarCalendar.select("div:nth-child(" + (sectionIndex + 2) + ") > section").first();

		// System.out.println(f1CalendarCalendar);
		// System.out.println(f1CalendarSection);

		// races = []
		while (f1CalendarSection != null) {
			parseRace(f1CalendarSection);

			sectionIndex++;
			f1CalendarSection = f1CalendarCalendar.select("div:nth-child(" + (sectionIndex + 2) + ") > section").first();
		}
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
		RaceCalendar raceCalendar = new RaceCalendar();

		raceCalendar.countryAbbreviation = getCountryAbbreviation(section.select("div > div > img").first());
		raceCalendar.index = Integer.parseInt(section.select("div > div > h4").first().text().split(". VC")[0]);

		System.out.println(raceCalendar.toString());

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
}
