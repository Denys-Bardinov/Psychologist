---
name: kan-10-header
description: Works on Jira KAN-10 "Update header text and fonts in the desktop version" (1100 px and wider) in the Psychologist landing site. Use only after Bardinov gives an explicit go-ahead.
tools: Read, Grep, Glob, Edit, Write, Bash, mcp__claude_ai_Atlassian_MCP__getJiraIssue, mcp__claude_ai_Atlassian_MCP__listJiraIssueComments
---

You work on one task: KAN-10, https://petprojectpsy.atlassian.net/browse/KAN-10 (parent design task KAN-6).

## Task summary
Rework the site header in the desktop version (1100 px and wider):
- Update header text: logo name, navigation labels, the "Записатися" button (Ukrainian and Russian).
- Update header fonts (family, weight, size), keeping Cyrillic support.
- Keep the header sticky, with visible focus states and touch targets of at least 48 px. Labels must wrap or fit in both languages (uk/ru run 20-30% longer than English).
- Open: final texts and fonts come from the owner. If they are not in the ticket or its comments, STOP and ask. Use `[square bracket]` placeholders, never invent texts.

## First steps
1. Re-read the ticket and its comments (Atlassian tools) in case the texts or fonts were added.
2. Read `CLAUDE.md`, `design/HANDOFF.md`, `fragments/layout.html`, `site.css`, `messages_uk.properties`, `messages_ru.properties`.
3. Do not start changing files until the parent session confirms Bardinov's go-ahead. Planning is fine.

## Where the work happens
- Header markup: `src/main/resources/templates/fragments/layout.html`
- Styles and tokens: `src/main/resources/static/css/site.css` (reuse existing CSS variables, breakpoints 700 / 1100)
- Texts: `messages_uk.properties` and `messages_ru.properties`, both in the same change
- Design reference: `design/HANDOFF.md`

## Hard rules (from CLAUDE.md)
- No hard-coded text in templates; every string is a message key in both languages.
- Never touch `src/test/` or add test dependencies. If a test breaks, stop and report.
- `design/source/` is read-only. The ticket asks to update `design/source/Site.dc.html`, which conflicts with this rule: do NOT edit it, report it to Bardinov instead.
- No invented phone numbers, prices, names or credentials.
- WCAG 2.1 AA: visible focus ring, 48 px targets, text wraps, animations off under `prefers-reduced-motion`. Pages must work without JavaScript.
- Never delete files; never overwrite existing files without a suffix.
- Do not stage `mvnw.cmd`; stage files by name.

## Git workflow
- Branch from latest `main`, name like `claude/feature/KAN-10-header-text-fonts` (one task per branch). Never push to `main`.
- Commit messages and PR title start with `KAN-10: `.
- Do not push, open a PR, or comment/transition in Jira without asking first.

## Check before reporting done
- `./mvnw -B verify` passes (needs JDK 25 and `JAVA_HOME`).
- uk and ru keys match.
- Look at the header at 1100 px and wider in both `/uk/` and `/ru/`.

## Report format
Short: what changed (files), what was verified, what was not verified, open questions.
