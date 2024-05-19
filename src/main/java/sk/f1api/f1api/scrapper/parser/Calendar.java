package sk.f1api.f1api.scrapper.parser;

import sk.f1api.f1api.scrapper.Scrapper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Calendar {

    private String url;

    public Calendar() {
        url = Scrapper.getValueOfKeyFromProperties("url.calendar");
    }
}
