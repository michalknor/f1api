package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.entity.Country;
import sk.f1api.f1api.scrapper.Scrapper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Wiki {

    private Element data;

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
    }

    public List<Country> fillCountry() {
        int trIndex = 2;

		Element f1Races = data.select("tr:nth-of-type(" + trIndex + ")").first();

        List<Country> countries = new ArrayList<>();

		while (f1Races != null) {
			Element imgElements = f1Races.select("img").first();
			if (imgElements == null) {
				break;
			}
            Country country = new Country();
            country.setName(imgElements.attr("alt"));
			countries.add(country);
			trIndex++;
			f1Races = data.select("tr:nth-of-type(" + trIndex + ")").first();
		}

        return countries;
    }
}
