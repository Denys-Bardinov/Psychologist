package com.psychologist.site.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Reads the repeating blocks (services, portfolio, reviews, FAQ) from the messages files.
 * Items are numbered from 1: services.1.title, services.2.title, ...
 * The list ends at the first number whose first field is missing.
 */
@Service
public class PageContent {

    public record Offer(String id, String title, String text, String price, String duration) { }
    public record Work(String title, String category, String text, String image) { }
    public record Review(String text, String author) { }
    public record Question(String question, String answer) { }

    private final MessageSource messages;

    public PageContent(MessageSource messages) {
        this.messages = messages;
    }

    public List<Offer> services(Locale locale) {
        return read("services", "title", locale, item -> new Offer(
                "service-" + item.number(), item.get("title"), item.get("text"), item.get("price"), item.get("duration")));
    }

    public List<Work> portfolio(Locale locale) {
        return read("portfolio", "title", locale, item -> new Work(
                item.get("title"), item.get("category"), item.get("text"), item.get("image")));
    }

    public List<Review> reviews(Locale locale) {
        return read("reviews", "text", locale, item -> new Review(item.get("text"), item.get("author")));
    }

    public List<Question> faq(Locale locale) {
        return read("faq", "q", locale, item -> new Question(item.get("q"), item.get("a")));
    }

    private <T> List<T> read(String block, String firstField, Locale locale, Function<Item, T> toItem) {
        List<T> result = new ArrayList<>();
        for (int number = 1; ; number++) {
            Item item = new Item(block, number, locale);
            if (item.get(firstField).isEmpty()) {
                return result;
            }
            result.add(toItem.apply(item));
        }
    }

    /** One numbered item of a block, e.g. services.2.* */
    private final class Item {
        private final String block;
        private final int number;
        private final Locale locale;

        Item(String block, int number, Locale locale) {
            this.block = block;
            this.number = number;
            this.locale = locale;
        }

        int number() {
            return number;
        }

        /** The text of block.number.field, or "" when it does not exist. */
        String get(String field) {
            return messages.getMessage(block + "." + number + "." + field, null, "", locale);
        }
    }
}
