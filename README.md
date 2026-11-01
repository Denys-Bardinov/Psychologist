# Psychologist website

Landing page for a psychologist: general info, services and prices, portfolio, reviews, FAQ, booking form, social links. Ukrainian first, Russian second, English can be added.

Design: see design/HANDOFF.md and design/source/.
Branching and pull request rules: see [CONTRIBUTING.md](CONTRIBUTING.md). CI/CD: see [docs/ci-cd.md](docs/ci-cd.md).

## Tech
Java 21, Spring Boot 3, Thymeleaf templates, Maven. Plain CSS and a small plain JavaScript file for the interactive parts.

## Run it locally
Needs Java 21 and Maven.

```
mvn spring-boot:run
```
Open http://localhost:8080 (redirects to `/uk/`; Russian is `/ru/`). Run the tests with `mvn verify`.

## Where things are
| What | Where |
| --- | --- |
| Texts (Ukrainian / Russian) | `src/main/resources/messages_uk.properties`, `messages_ru.properties` |
| Phone, email, social links | `src/main/resources/application.properties` (`site.*`) |
| Page sections (HTML) | `src/main/resources/templates/sections/` (one file per section) |
| Header, footer, chat button, cookie banner | `src/main/resources/templates/fragments/layout.html` |
| Styles (colours, fonts, breakpoints 700 / 1100 px) | `src/main/resources/static/css/site.css` |
| Menu, slider, modal, form logic in the browser | `src/main/resources/static/js/site.js` |
| Page routes, languages | `src/main/java/com/psychologist/site/web/` |
| Booking form on the server | `src/main/java/com/psychologist/site/booking/` |

To add a service, portfolio work, review or FAQ item, add the next numbered keys (for example `services.4.title`, `services.4.text`, ...) to both messages files.

## Open items
- Real content: texts, photos, prices, contacts (everything in `[brackets]` is a placeholder).
- Where booking requests go (email, Telegram bot, Google Sheet). For now `LoggingBookingNotifier` only writes them to the log.
- A host for the Java app (GitHub Pages cannot run it).
- Russian texts are a draft and need a native speaker's review.
