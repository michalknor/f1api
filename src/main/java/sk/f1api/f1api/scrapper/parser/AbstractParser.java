package sk.f1api.f1api.scrapper.parser;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.Setter;
import sk.f1api.f1api.scrapper.Scrapper;

@Setter
@Getter
public abstract class AbstractParser {

	protected Document document;

	protected Element mainContent;

	protected int numberOfRaces;

	protected String url;

	protected AbstractParser(String url) {
		this.url = url;
		this.document = Scrapper.getDocument(url);
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
