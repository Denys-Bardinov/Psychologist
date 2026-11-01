# CI/CD

Two GitHub Actions workflows live in `.github/workflows/`.

## CI (`ci.yml`)
Runs on every pull request into `main`, on every push to `main`, and by hand from the Actions tab.

- If the app exists (`package.json` in the repo root): install with `npm ci`, then run the `lint`, `check` and `test` npm scripts when they exist, then build.
- Before the app exists: it only builds the placeholder page, so design and docs pull requests are never blocked.
- On pull requests the built site is uploaded as an artifact (`site-preview-pr-<number>`, kept 7 days). Open the run in the Actions tab and download it to look at the site before merging.

## Deploy (`deploy.yml`)
Runs after every push (merge) to `main`, and by hand from the Actions tab. It builds the site into `dist/` and publishes it to GitHub Pages:
https://denys-bardinov.github.io/Psychologist/

Until the Astro app is added, the published site is the "coming soon" page from `placeholder/`.

Both workflows use the same build script, `.github/scripts/build-site.sh`.

## One-time setup (repo owner)
1. **Turn on Pages:** Settings → Pages → Build and deployment → Source: **GitHub Actions**.
2. **Require CI before merging (recommended):** Settings → Branches (or Rules → Rulesets) → add a rule for `main`:
   - Require a pull request before merging (required approvals: 0).
   - Require status checks to pass: **Check and build**.
   - Allow only squash merging (Settings → General → Pull Requests).

## When the Astro app is added
- Put `lint`, `check` (`astro check`) and `test` scripts in `package.json` if wanted; CI picks them up automatically.
- The site is served from the `/Psychologist/` sub-path. The deploy workflow passes `SITE_URL` and `BASE_PATH` as environment variables, so `astro.config.mjs` can use:
  ```js
  export default defineConfig({
    site: process.env.SITE_URL,
    base: process.env.BASE_PATH || '/',
  });
  ```
- Delete the `placeholder/` folder once the real site is live.
- With a custom domain later, the base path becomes `/` automatically.
