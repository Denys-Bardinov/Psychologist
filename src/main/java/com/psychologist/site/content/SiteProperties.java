package com.psychologist.site.content;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Contact details and links from application.properties (keys starting with "site."). */
@ConfigurationProperties(prefix = "site")
public record SiteProperties(
        String phone,
        String phoneHref,
        String email,
        String emailHref,
        String instagram,
        String messenger,
        String telegram,
        String facebook,
        String linkedin) {
}
