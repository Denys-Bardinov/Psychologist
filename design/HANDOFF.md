# Design handoff: psychologist landing page (KAN-6)

Source of truth: the design canvas (private link, owner can share). The files in `design/source/` are exported copies. The mockup `Site.dc.html` is real HTML and CSS and can be used as a starting point; `Mockup-Mobile` (375) and `Mockup-Tablet` (768) render the same file at other widths.

## Direction
Approved style "Lavender and lemon": light, airy pastels for backgrounds and buttons, dark violet for text.

## Languages
- Ukrainian is the main language and the default. Russian second. English or others can be added later.
- Separate addresses per language (`/uk`, `/ru`, `/en`), `lang` attribute set on the page, language choice remembered.
- Language switcher in the header: native names (Українська, Русский, English). Texts in Ukrainian and Russian are 20-30% longer than English, so no fixed-width labels; everything must wrap.
- All fonts must support Cyrillic (Lora and Nunito do).

## Tokens
Colours: ink #2E2542, violet #4B3A7A, lavender #D9CFF2, lemon #FBF1B8, apricot #FFDCC7, mist (page) #FCFAFF, light lavender (sections) #EFE9FB, secondary text #50466A, hints #6E6486, field border #8C82A6, error #B3261E.
Fonts: Lora 500/600 (headings, italic accent word), Nunito 400/500/600/700 (body).
Type scale desktop / tablet / mobile: H1 56 / 46 / 36, H2 40 / 36 / 30, H3 21, body 18 / 17 / 17, small 14.
Spacing grid 4 px. Section padding 104 / 80 / 56 px; side padding 120 / 40 / 16 px; max content width 1200 px.
Radii: buttons 999, cards 24-28, fields 16, images 16.
Breakpoints: mobile under 700, tablet 700 and up, desktop 1100 and up (the mockup uses container queries at these widths).

## Sections (order)
Header (sticky) - Hero - About me - Services and prices - Portfolio - Testimonials - FAQ - Booking form - Message me (Instagram, Messenger) - Contacts - Footer.
Floating chat button on all screens; cookie banner on first visit.
Not wanted: map and street address (client decision). Contacts show phone, email, working hours only.

## Booking form
Fields: name; how to contact (Phone, Email, Instagram, Messenger, one selected, the field label, hint and keyboard type change with the choice); session type; preferred day and time; optional message; consent checkbox with link to the privacy policy.
States are on the `Form-States` board: empty, focus, validation errors, sending, success, failure. Errors always have an icon and text, never colour only; focus moves to the first error; sending locks the fields; status is announced to screen readers; failure keeps entered data and offers retry plus Instagram and Messenger links.
The `Prototype` board is a working mobile example of this logic.
Open decision: where requests are sent (email, Telegram bot, Google Sheet).

## Interactive elements (board `Interactive`)
Mobile menu (full screen), language list, chat button and bottom sheet, portfolio modal (centred on desktop, full screen on mobile, keyboard arrows and Esc), cookie banner, hover states, testimonial slider (no autoplay, swipe on mobile), FAQ accordion (first item open).
Animations 150-250 ms, disabled with "reduce motion".

## Accessibility
WCAG 2.1 AA: contrast table in the `UI-Kit` board (hand-calculated, re-check with a tool), visible focus ring on every control, touch targets at least 48 px, aria-label on icon-only buttons, real `button`, `a`, `label` and `input` elements.

## Assets still needed
- Logo (SVG), favicon, Open Graph image (1200x630)
- Portrait photo, practice photos, portfolio images (crops: portrait arch, 4:3 cards)
- Brand SVG icons for Instagram, Telegram, Facebook, LinkedIn (the mockup uses letters as placeholders)
- Real texts in Ukrainian and Russian, prices, FAQ answers, testimonials
- Phone, email, working hours, Instagram handle, Messenger page name, domain

## Open questions
- Hosting and stack (recommended: Astro static site on Cloudflare Pages or Netlify)
- Form destination
- Who writes the Russian translation
- Real analytics and cookie categories
