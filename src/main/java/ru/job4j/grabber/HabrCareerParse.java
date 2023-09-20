package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) {
        String result = "";
           try {
               result = Jsoup.connect(link).get().select(".vacancy-description__text").text();
           } catch (IOException e) {
               e.printStackTrace();
           }
        return result;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        String pageLink = String.format("%s/vacancies/java_developer?page=", link);
        for (int i = 1; i <= 5; i++) {
            try {
            Elements rows = Jsoup.connect(String.format("%s%d", pageLink, i)).get()
                    .select(".vacancy-card__inner");
            rows.forEach(row -> {
                posts.add(this.parsePost(row));
            });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    public Post parsePost(Element row) {
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String aLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        String description = this.retrieveDescription(aLink);
        Element dateElement = row.select(".vacancy-card__date").first().child(0);
        String date = dateElement.attr("datetime");
        return new Post(vacancyName, aLink, description, dateTimeParser.parse(date));
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse habrParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> l = habrParse.list(SOURCE_LINK);
        for (Post post : l) {
            System.out.println(post);
        }
    }
}