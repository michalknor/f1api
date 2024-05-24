package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.entity.Circuit;
import sk.f1api.f1api.entity.City;
import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.scrapper.Scrapper;

import java.util.List;
import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Wiki {

    private Element data;

    private int numberOfRaces;

    public Wiki() {
        data = Scrapper.getDocument(Scrapper.getValueOfKeyFromProperties("url.wiki"))
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
                        .parseInt(data.select("tr:nth-of-type(" + (numberOfRaces + 2) + ") > th").html());
            } catch (Exception e) {
                break;
            }
        }
    }

    public void fillCircuit(Circuit circuit, int race) {
        if (race <= 0 || race > numberOfRaces) {
            return;
        }

        Element td = data.select("tr:nth-of-type(" + (race + 1) + ") > td:nth-of-type(2)").first();
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

        Element td = data.select("tr:nth-of-type(" + (race + 1) + ") > td:nth-of-type(2)").first();
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

        Element f1Races = data.select("tr:nth-of-type(" + (race + 1) + ")").first();
        Element imgElements = f1Races.select("img").first();

        country.setName(imgElements.attr("alt"));

        return;
    }
}
