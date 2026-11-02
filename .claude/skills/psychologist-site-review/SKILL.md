---
name: psychologist-site-review
description: Use instead of the generic code-review when reviewing a PR or diff in the Psychologist landing page repo (Java, Spring Boot, Thymeleaf) against its project rules.
---

# Psychologist site review

Review a change to the psychologist landing page (Java 25, Spring Boot 3, Thymeleaf, plain CSS and JS) for bugs and for the project's own rules. The owner knows Java but not much frontend, so explain findings in plain words.

## 1. Get the change
- Target is a PR number, a branch, or the current diff. If none is given, review the current branch against `origin/main`.
- Read the full diff and every file it touches. Read `CLAUDE.md`, `CONTRIBUTING.md` and `design/HANDOFF.md` when a rule below needs them.

## 2. Build check
- Run `./mvnw -B verify`. Report the result. A red build is always the first finding.
- Never edit, add or delete anything in `src/test/` or test dependencies in `pom.xml`, even to make the build pass. Tests belong to a separate Selenium framework. If an existing test fails, report it.

## 3. Correctness (Java and Spring)
- Booking form: validation annotations on `BookingRequest` match the form fields; errors return a useful response; no personal data (name, phone, email, message) is logged beyond what `LoggingBookingNotifier` already does; nothing sends requests to a new destination without the owner's decision.
- Controllers: routes keep the `/uk/` and `/ru/` language prefixes working; unknown paths still hit the 404 page.
- Config: new contact or link values live in `application.properties` under `site.*` and have a matching field in `SiteProperties`.
- Null handling, wrong HTTP status, missing `@Valid`, and other plain Java bugs.

## 4. Content and translations
- No hard-coded visible text in templates; every string is a message key.
- Every new key exists in both `messages_uk.properties` and `messages_ru.properties`. Diff the key sets and list any missing ones.
- Numbered lists (services, portfolio, reviews, FAQ) have no gaps in numbering.
- Missing real content stays in `[square brackets]`. Flag anything that looks like an invented phone number, price, name, review or credential.

## 5. Design and accessibility
- Colours, fonts and spacing reuse the CSS variables in `site.css`; flag new raw colour values.
- Breakpoints stay at 700 and 1100 px. No fixed widths on labels or buttons (Ukrainian and Russian texts are long).
- WCAG 2.1 AA: real `button`/`a`/`label`/`input` elements, visible focus ring, touch targets at least 48 px, `aria-label` on icon-only buttons, form errors with icon and text, animations disabled under `prefers-reduced-motion`.
- No map or street address.

## 6. JavaScript
- Changes stay in `site.js`, with no libraries. The page still shows its content without JavaScript. Check for errors on missing elements and for listeners added twice.

## 7. Process
- Branch name follows `<type>/KAN-<number>-<description>` (or `claude/` for Claude's branches), and commits and the PR title start with `KAN-<number>:` when a ticket exists.
- `README.md` is updated if the change adds a new kind of file or setting.
- `design/source/` and `placeholder/` were not changed unless that was the task.

## 8. Report
- List findings with the most serious first. For each: file and line, what is wrong, what goes wrong for a visitor or the owner, and the suggested fix.
- Mark each as **must fix** (bug, broken build, missing translation, accessibility failure, data leak) or **optional** (style, small cleanup).
- End with one line: ready to merge, or what blocks it.
- Don't change code during the review. Only apply fixes if asked to, and post comments on the PR only when asked to.
