package com.psychologist.site;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SiteTests {

    @Autowired
    MockMvc mvc;

    @Test
    void rootRedirectsToUkrainianByDefault() throws Exception {
        mvc.perform(get("/")).andExpect(redirectedUrl("/uk/"));
    }

    @Test
    void rootRedirectsToRememberedLanguage() throws Exception {
        mvc.perform(get("/").cookie(new Cookie("lang", "ru"))).andExpect(redirectedUrl("/ru/"));
    }

    @Test
    void ukrainianPageHasAllSections() throws Exception {
        mvc.perform(get("/uk/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<html lang=\"uk\"")))
                .andExpect(content().string(containsString("Зробіть вдих.")))
                .andExpect(content().string(containsString("id=\"about\"")))
                .andExpect(content().string(containsString("id=\"services\"")))
                .andExpect(content().string(containsString("id=\"portfolio\"")))
                .andExpect(content().string(containsString("id=\"reviews\"")))
                .andExpect(content().string(containsString("id=\"faq\"")))
                .andExpect(content().string(containsString("id=\"booking\"")))
                .andExpect(content().string(containsString("id=\"contacts\"")))
                .andExpect(content().string(containsString("Індивідуальна сесія")))
                .andExpect(content().string(not(containsString("??"))));
    }

    @Test
    void russianPageIsTranslated() throws Exception {
        mvc.perform(get("/ru/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<html lang=\"ru\"")))
                .andExpect(content().string(containsString("Сделайте вдох.")))
                .andExpect(content().string(not(containsString("??"))));
    }

    @Test
    void privacyPageWorks() throws Exception {
        mvc.perform(get("/uk/privacy/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Політика конфіденційності")));
    }

    @Test
    void unknownLanguageIsNotFound() throws Exception {
        mvc.perform(get("/de/")).andExpect(status().isNotFound());
    }

    @Test
    void validBookingIsAccepted() throws Exception {
        mvc.perform(post("/api/booking").contentType(MediaType.APPLICATION_JSON).content("""
                        {"name":"Анна","method":"phone","contact":"+380 50 123 45 67","consent":true,"lang":"uk"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    void invalidBookingReturnsFieldErrors() throws Exception {
        mvc.perform(post("/api/booking").contentType(MediaType.APPLICATION_JSON).content("""
                        {"name":"","method":"email","contact":"not-an-email","consent":false}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.contact").exists())
                .andExpect(jsonPath("$.errors.consent").exists());
    }
}
