package sk.f1api.f1api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.AbstractMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Scrapper {
	public static void main(String[] args) {
		String page = "";
		Map.Entry<Integer, String> result = getHTMLFromPage(page);

		if (result.getKey() != 200) {
			return;
		}

		String html = result.getValue();
		// System.out.println(html);

		Document f1calendar_root = Jsoup.parse(html);
		
		Element f1calendar_calendar = f1calendar_root.select("body > div > main > div > div").first();
        
        System.out.println(f1calendar_calendar);
	}

	public static Map.Entry<Integer, String> getHTMLFromPage(String page) {
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(
					HttpRequest.newBuilder()
							.uri(URI.create(page))
							.build(),
					HttpResponse.BodyHandlers.ofString());

			return new AbstractMap.SimpleEntry<>(response.statusCode(), response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return new AbstractMap.SimpleEntry<>(-1, null);
	}
}
