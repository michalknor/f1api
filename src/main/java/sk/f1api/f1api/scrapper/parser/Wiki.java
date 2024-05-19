package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.scrapper.Scrapper;

import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Wiki {

    private Elements table;

    public Wiki() {
        table = Scrapper.getDocument(Scrapper.getValueOfKeyFromProperties("url.wiki")).select(
                """
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
                        """);
    }
}
