package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.entity.Event;
import sk.f1api.f1api.entity.EventType;
import sk.f1api.f1api.entity.GrandPrix;
import sk.f1api.f1api.scrapper.Scrapper;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Calendar {

	private Document document;

    private Element mainContent;

    private int numberOfRaces;

    public Calendar() {
		document = Scrapper.getDocument(Scrapper.getValueOfKeyFromProperties("url.calendar"));
        mainContent = document
                .select("""
                        body >
                        div >
                        main >
                        div >
                        div
                        """).first();
		
		String lastRace = mainContent.lastElementChild().select("div > section > div > div > h4").first().html();
		numberOfRaces = Integer.parseInt(lastRace.substring(0, lastRace.indexOf(".")));
    }

	public void fillEvents(GrandPrix grandPrix, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

		List<Event> events = new ArrayList<>();
		
		Elements scheduleTable = mainContent.select("div:nth-of-type(" + (race + 1) + ") > section > table > tbody > tr");
		
		DateTimeFormatter dateTimeFormatterFrom = DateTimeFormatter.ofPattern("yyyy d. M. HH:mm");
		DateTimeFormatter dateTimeFormatterTo = DateTimeFormatter.ofPattern("yyyy d. M. - HH:mm");

		for (int i = 0; i < scheduleTable.size(); i++) {
			Elements eventInfo = scheduleTable.get(i).select("td");

			Event event = new Event();

			Elements times = eventInfo.get(2).select("span");

			event.setGrandPrix(grandPrix);
			event.setRound((byte) (i + 1));
			event.setTimeFrom(LocalDateTime.parse("2024 " + eventInfo.get(1).text() + " " + times.first().text(), dateTimeFormatterFrom));

			System.out.println(event.getTimeFrom());
			if (times.size() == 2) {
				event.setTimeTo(LocalDateTime.parse("2024 " + eventInfo.get(1).text() + " " + times.get(1).text(), dateTimeFormatterTo));
			}

			EventType eventType = new EventType();
			eventType.setAbbreviation(getAbbreviationForEventName(eventInfo.get(0).text()));
			eventType.load(Scrapper.sessionFactory.openSession());

			event.setEventType(eventType);

			events.add(event);
		}

		grandPrix.setEvents(events);
    }

	public void fillCountry(Country country, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

		Element f1Races = mainContent.select("div:nth-of-type(" + (race + 1) + ") > section > div > div > img").first();

		String abbreviation = f1Races.attr("src");
		int indexFrom = abbreviation.lastIndexOf("/") + 1;
		abbreviation = abbreviation.substring(indexFrom, indexFrom + 2);

		country.setAbbreviation(abbreviation);
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
				yield "?";
			}
		};
	}
}
