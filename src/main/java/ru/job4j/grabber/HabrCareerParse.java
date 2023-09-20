package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements descr = document.select(".vacancy-description__text");
        return descr.text();
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse habrParse = new HabrCareerParse();
        Connection connection;
        for (int i = 1; i <= 5; i++) {
            connection = Jsoup.connect(String.format("%s%d", PAGE_LINK, i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String description = habrParse.retrieveDescription(link);
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                String date = dateElement.attr("datetime");
                System.out.printf("%s %s %s%n%s%n", vacancyName, link, date, description);
            });
        }
    }
}