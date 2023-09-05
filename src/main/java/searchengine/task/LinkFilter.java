package searchengine.task;

import java.util.Arrays;
import java.util.List;

public class LinkFilter {
    public static final List<String> keyWords = Arrays.asList(
            "://vk.com", "tel:", "://t.me", "://twitter", "://ok.ru",
            "://www.youtube.com", "/extlink", "mailto:", "://rambler",
            "://motor", "://www.behance", ".pdf", "tg://", "#", "://my.advcake",
            "https://drive", "http://lokation.ru", "https://coworking.tmn-it.ru",
            "https://www.google", "https://yandex");

    public static boolean shouldBeFilteredOut(String link) {
        for (String keyWord : keyWords) {
            if (link.contains(keyWord) || link.length() <= 1) {
                return true;
            }
        }
        return false;
    }
}
