# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working in this repository.

## What this is
A one-page landing site for a psychologist: hero, about, services and prices, portfolio, reviews, FAQ, booking form, social links, contacts. Ukrainian is the default language, Russian second, English may be added later.

Stack: Java 25, Spring Boot 3, Thymeleaf (server-rendered), Maven via the wrapper. Plain CSS and one small plain JavaScript file. No frontend framework, no Node build.

The owner (Bardinov) knows Java well and nothing else in depth. Keep code simple and readable, prefer plain Spring and Thymeleaf over clever abstractions, and explain any JavaScript you add.

## Commands
- Run locally: `./mvnw spring-boot:run` (Windows: `mvnw spring-boot:run`), then open http://localhost:8080 (redirects to `/uk/`; Russian is `/ru/`).
- Build and check: `./mvnw -B verify` (this is what CI runs; it must pass before a PR is merged).
- The app reads the port from `PORT` (default 8080).
- Needs JDK 25 with `JAVA_HOME` set; otherwise the wrapper stops with "JAVA_HOME is not defined correctly".
- Run one test class (running tests is allowed, editing them is not): `./mvnw -B test -Dtest=SiteTests`.
- CI also runs `.github/scripts/build-site.sh`, which builds the static placeholder for GitHub Pages.

## Architecture (needs several files to see)
- **The language comes from the URL, not the browser.** `web/PathLocaleResolver` (registered in `web/WebConfig`) takes the locale from the first path segment (`/uk/`, `/ru/`) and ignores `Accept-Language`. `web/PageController` sends `/` to the language in the `lang` cookie (set by `site.js`), or Ukrainian by default. An unknown language gives a 404. `spring.messages.fallback-to-system-locale=false` and the empty `messages.properties` are what make Ukrainian the default.
- **Repeating blocks are read from the messages files.** `content/PageContent` reads `services.N.*`, `portfolio.N.*`, `reviews.N.*` and `faq.N.*`, counting from 1. A block ends at the first number whose first field is missing (`title` for services and portfolio, `text` for reviews, `q` for FAQ), so a gap in the numbering silently hides every item after it.
- **The booking form is a JSON API, not a Thymeleaf form post.** The form in `sections/booking.html` has `th:action="@{/api/booking}"`, and `site.js` validates it on the page, then sends the fields as JSON with `fetch`. `booking/BookingController` validates again with `@Valid`. A bad request gives 400 with `{"ok": false, "errors": {"name" | "contact" | "consent": <code>}}` (codes, not texts), but `site.js` does not read them: any non-OK response just shows the generic "fail" state with a retry button. Field values like `phone` arrive in lower case, which is why `spring.jackson.mapper.accept-case-insensitive-enums=true` is set.

## Tests: do not touch
Do not create, edit, move or delete anything under `src/test/`, and do not add test dependencies to `pom.xml`. Regression testing will be a separate framework (Java + Selenium, Page Object Model), built and owned separately. If a change breaks an existing test, stop and report it instead of changing the test.

## Where things are
| What | Where |
| --- | --- |
| Texts (Ukrainian / Russian) | `src/main/resources/messages_uk.properties`, `messages_ru.properties` (`messages.properties` is an empty base file Spring needs) |
| Phone, email, social links | `src/main/resources/application.properties` (`site.*`, bound to `content/SiteProperties`) |
| Page sections | `src/main/resources/templates/sections/` (one file per section) |
| Header, footer, chat button, cookie banner | `src/main/resources/templates/fragments/layout.html` |
| Icons | `src/main/resources/templates/fragments/icons.html` |
| Styles, design tokens, breakpoints | `src/main/resources/static/css/site.css` |
| Menu, slider, modal, form logic in the browser | `src/main/resources/static/js/site.js` |
| Routes and languages | `src/main/java/com/psychologist/site/web/` |
| Booking form on the server | `src/main/java/com/psychologist/site/booking/` |
| Design reference | `design/HANDOFF.md`, `design/source/` (exported canvas, read-only) |
| CI/CD | `.github/workflows/`, `docs/ci-cd.md` |
| Claude skills for this repo | `.claude/skills/<skill-name>/SKILL.md` (e.g. `psychologist-site-review` for PR reviews) |
| Claude hooks | `.claude/settings.json` and `.claude/hooks/` (checks that uk and ru message keys match after an edit) |

## Rules for changes
- **No hard-coded text in templates.** Every visible string is a message key, added to both `messages_uk.properties` and `messages_ru.properties` in the same change. Lists (services, portfolio, reviews, FAQ) use numbered keys like `services.4.title`; add the next number in both files.
- **Placeholders stay marked.** Content we do not have yet is written in `[square brackets]`. Never invent real-looking phone numbers, prices, names, reviews or credentials.
- **Contacts and links** go in `application.properties` under `site.*`, never directly in templates. A new key also needs a field in `SiteProperties`.
- **Follow the design handoff.** Colours, fonts, spacing and breakpoints (700 / 1100 px) come from `design/HANDOFF.md` and the tokens in `site.css`. Reuse existing CSS variables instead of new colour values. No map or street address (client decision).
- **Accessibility is required (WCAG 2.1 AA):** real `button`/`a`/`label`/`input` elements, visible focus ring, touch targets of at least 48 px, `aria-label` on icon-only buttons, errors shown with icon and text (not colour only), animations off under `prefers-reduced-motion`.
- **Texts wrap.** Ukrainian and Russian run 20-30% longer than English; no fixed-width labels or buttons.
- **JavaScript stays small** and dependency-free, in `site.js`. Pages must still be readable without it.
- **Booking requests** go through the `BookingNotifier` interface. `LoggingBookingNotifier` is a stand-in until the destination (email, Telegram bot or Google Sheet) is decided; do not pick one without the owner.
- Do not edit `design/source/` files; they are exports of the design canvas.

## Workflow
- Wait for an explicit go-ahead from Bardinov before starting new development or changing CI/CD. Preparing, reading and planning are fine.
- Follow `CONTRIBUTING.md`: branch from the latest `main`, never push to `main` directly, one task per branch, PR into `main`, squash and merge, branches are kept after merging.
- Branch names: `<type>/KAN-<number>-<short-description>` (`feature/`, `fix/`, `design/`, `chore/`). Branches made by Claude start with `claude/`.
- Commit messages and PR titles start with the Jira ticket when there is one: `KAN-12: Add booking form validation`. Tasks are tracked in Jira (project KAN).
- Keep `README.md` in sync when you add a new kind of file or setting someone would need to find.

## Hosting gotchas
- GitHub Pages cannot run the Java app. `deploy.yml` publishes only the "coming soon" page from `placeholder/` to https://denys-bardinov.github.io/Psychologist/ (capital P, the URL is case-sensitive).
- A real server host (Render, Railway, Fly.io, a VPS) is still to be chosen; until then do not remove `placeholder/` or the Pages workflow.

## Line endings gotcha
`.gitattributes` forces `*.cmd` files to CRLF. Because of that, `mvnw.cmd` often shows as modified on Windows, and an IDE "commit all" rewrites all its lines. Do not stage `mvnw.cmd` unless you really edited it; stage files by name.
