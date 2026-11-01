package com.psychologist.site.web;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/** Languages of the site. To add English: add EN here and create messages_en.properties. */
public enum SiteLanguage {
    UK("uk", "UA", "Українська"),
    RU("ru", "RU", "Русский");

    public static final SiteLanguage DEFAULT = UK;

    private final String path;
    private final String code;
    private final String nativeName;

    SiteLanguage(String path, String code, String nativeName) {
        this.path = path;
        this.code = code;
        this.nativeName = nativeName;
    }

    /** URL segment, e.g. "uk" in /uk/. Also the value of the html lang attribute. */
    public String getPath() {
        return path;
    }

    /** Short label shown on the language button, e.g. "UA". */
    public String getCode() {
        return code;
    }

    public String getNativeName() {
        return nativeName;
    }

    public Locale toLocale() {
        return Locale.forLanguageTag(path);
    }

    public static Optional<SiteLanguage> fromPath(String path) {
        return Arrays.stream(values()).filter(l -> l.path.equalsIgnoreCase(path)).findFirst();
    }
}
