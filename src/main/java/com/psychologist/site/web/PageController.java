package com.psychologist.site.web;

import com.psychologist.site.content.PageContent;
import com.psychologist.site.content.SiteProperties;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PageController {

    /** Cookie set by site.js when the visitor picks a language; used when they open the bare address "/". */
    static final String LANG_COOKIE = "lang";

    private final PageContent content;
    private final SiteProperties site;

    public PageController(PageContent content, SiteProperties site) {
        this.content = content;
        this.site = site;
    }

    /** "/" sends the visitor to their remembered language, or Ukrainian by default. */
    @GetMapping("/")
    public String root(@CookieValue(name = LANG_COOKIE, required = false) String remembered) {
        SiteLanguage lang = SiteLanguage.fromPath(remembered).orElse(SiteLanguage.DEFAULT);
        return "redirect:/" + lang.getPath() + "/";
    }

    @GetMapping({"/{lang}", "/{lang}/"})
    public String home(@PathVariable String lang, Model model) {
        SiteLanguage language = language(lang);
        Locale locale = language.toLocale();
        addCommon(model, language, "");
        model.addAttribute("services", content.services(locale));
        model.addAttribute("portfolio", content.portfolio(locale));
        model.addAttribute("reviews", content.reviews(locale));
        model.addAttribute("faq", content.faq(locale));
        model.addAttribute("contactMethods", List.of("phone", "email", "instagram", "messenger"));
        return "index";
    }

    @GetMapping({"/{lang}/privacy", "/{lang}/privacy/"})
    public String privacy(@PathVariable String lang, Model model) {
        addCommon(model, language(lang), "privacy/");
        model.addAttribute("paragraphs", List.of("privacy.1", "privacy.2", "privacy.3"));
        return "privacy";
    }

    private static SiteLanguage language(String path) {
        return SiteLanguage.fromPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /** Data used by the header, footer and language switcher on every page. */
    private void addCommon(Model model, SiteLanguage language, String subPath) {
        model.addAttribute("lang", language);
        model.addAttribute("languages", Arrays.asList(SiteLanguage.values()));
        model.addAttribute("subPath", subPath);
        model.addAttribute("site", site);
        model.addAttribute("year", Year.now().getValue());
    }
}
