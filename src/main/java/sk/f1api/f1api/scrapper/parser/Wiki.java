package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.scrapper.Scrapper;

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
                numberOfRaces = Integer.parseInt(data.select("tr:nth-of-type(" + (numberOfRaces + 2) + ") > th").html());
            } catch (Exception e) {
                break;
            }
        }
    }

    public void fillCountry(Country country, int round) {
		Element f1Races = data.select("tr:nth-of-type(" + (round + 1) + ")").first();
        Element imgElements = f1Races.select("img").first();

        if (imgElements == null) {
            return;
        }

        country.setName(imgElements.attr("alt"));
        return;
    }
}
