package com.psychologist.site.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import org.springframework.web.servlet.LocaleResolver;

/** Takes the page language from the first part of the address: /uk/... or /ru/... */
public class PathLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        String first = uri.replaceFirst("^/", "").split("/", 2)[0];
        return SiteLanguage.fromPath(first).orElse(SiteLanguage.DEFAULT).toLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("The language is chosen by the page address");
    }
}
