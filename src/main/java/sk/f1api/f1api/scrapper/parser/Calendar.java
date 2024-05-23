package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.entity.City;
import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.scrapper.Scrapper;

import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Calendar {

    private Element data;

    private int numberOfRaces;

    public Calendar() {
        data = Scrapper.getDocument(Scrapper.getValueOfKeyFromProperties("url.calendar"))
                .select("""
                        body >
                        div >
                        main >
                        div >
                        div
                        """).first();
		
		String lastRace = data.lastElementChild().select("div > section > div > div > h4").first().html();
		numberOfRaces = Integer.parseInt(lastRace.substring(0, lastRace.indexOf("."))) - 1;
    }

	public void fillCountry(Country country, int round) {
		Element f1Races = data.select("div:nth-of-type(" + (round + 1) + ") > section > div > div > img").first();

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
