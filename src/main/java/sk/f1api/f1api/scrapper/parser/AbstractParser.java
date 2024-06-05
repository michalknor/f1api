package sk.f1api.f1api.scrapper.parser;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.nodes.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractParser {

    private Document document;

    public AbstractParser(Document document) {
        this.document = document;
    }

    public void saveToFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            String html = document.outerHtml();

            fileWriter.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
