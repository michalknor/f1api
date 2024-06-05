package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.entity.Circuit;
import sk.f1api.f1api.entity.City;
import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.entity.GrandPrix;
import sk.f1api.f1api.scrapper.Scrapper;

import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Wiki extends AbstractParser {

    private Element mainContent;

    private int numberOfRaces;

    public Wiki() {
		super(Scrapper.getDocument(Scrapper.getValueOfKeyFromProperties("url.wiki")));
        
        mainContent = getDocument()
                .select("""
                        body >
                        div:nth-of-type(2) >
                        div >
                        div:nth-of-type(3) >
                        main >
                        div:nth-of-type(3) >
                        div:nth-of-type(3) >
                        div >
                        table:nth-of-type(3) >
                        tbody
                        """).first();

        numberOfRaces = 0;
        while (true) {
            try {
                numberOfRaces = Integer
                        .parseInt(mainContent.select("tr:nth-of-type(" + (numberOfRaces + 2) + ") > th").html());
            } catch (Exception e) {
                break;
            }
        }
    }

    public void fillGrandPrix(GrandPrix grandPrix, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

        Element td = mainContent.select("tr:nth-of-type(" + (race + 1) + ") > td").first();
        Element grandPrixNameElement = td.select(":root > a").first();
        String GrandPrixName;

        if (grandPrixNameElement != null) {
            GrandPrixName = grandPrixNameElement.text();
        } else {
            GrandPrixName = td.select(":root > span > a").first().text();
        }

        grandPrix.setName(GrandPrixName.replace(" Grand Prix", ""));

        return;
    }

    public void fillCircuit(Circuit circuit, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

        Element td = mainContent.select("tr:nth-of-type(" + (race + 1) + ") > td:nth-of-type(2)").first();
        Element circuitName = td.select(":root > a").first();

        if (circuitName != null) {
            circuit.setName(circuitName.text());

            return;
        }

        circuit.setName(td.select(":root > span > a:nth-of-type(2)").first().text());

        return;
    }

    public void fillCity(City city, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

        Element td = mainContent.select("tr:nth-of-type(" + (race + 1) + ") > td:nth-of-type(2)").first();
        Element location = td.select(":root > a:nth-of-type(2)").first();

        if (location != null) {
            city.setName(location.text());

            return;
        }

        String locationHtml = td.select(":root > span > a").html();
        city.setName(locationHtml.substring(locationHtml.indexOf(",") + 2, locationHtml.length()));

        return;
    }

    public void fillCountry(Country country, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

        Element f1Races = mainContent.select("tr:nth-of-type(" + (race + 1) + ")").first();
        Element imgElements = f1Races.select("img").first();

        country.setName(imgElements.attr("alt"));

        return;
    }
}
