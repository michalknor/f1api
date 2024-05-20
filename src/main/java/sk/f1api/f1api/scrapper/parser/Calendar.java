package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.scrapper.Scrapper;

import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Calendar {

    private Element data;

    public Calendar() {
        data = Scrapper.getDocument(Scrapper.getValueOfKeyFromProperties("url.calendar"))
                .select("""
                        body >
                        div >
                        main >
                        div >
                        div
                        """).first();
    }
}
