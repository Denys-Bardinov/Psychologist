# CI/CD

Two GitHub Actions workflows live in `.github/workflows/`.

## CI (`ci.yml`)
Runs on every pull request into `main`, on every push to `main`, and by hand from the Actions tab.

- Sets up Java 25 and runs `./mvnw -B verify`: compiles the Spring Boot app, runs the tests and packages a runnable jar.
- Builds the static placeholder page for GitHub Pages (see Deploy).
- On pull requests the jar is uploaded as an artifact (`site-jar-pr-<number>`, kept 7 days). Download it from the run in the Actions tab and start it with `java -jar <file>.jar`, then open http://localhost:8080.

## Deploy (`deploy.yml`)
Runs after every push (merge) to `main`, and by hand from the Actions tab. It builds the site into `dist/` and publishes it to GitHub Pages:
https://denys-bardinov.github.io/Psychologist/

The site is a Java (Spring Boot) server app, and GitHub Pages can only host static files. So Pages keeps showing the "coming soon" page from `placeholder/` until a server host is chosen (for example Render, Railway or a VPS); then this workflow is replaced by a deploy to that host.

Both workflows use the same build script, `.github/scripts/build-site.sh`.

## One-time setup (repo owner)
1. **Turn on Pages:** Settings → Pages → Build and deployment → Source: **GitHub Actions**.
2. **Require CI before merging (recommended):** Settings → Branches (or Rules → Rulesets) → add a rule for `main`:
   - Require a pull request before merging (required approvals: 0).
   - Require status checks to pass: **Check and build**.
   - Allow only squash merging (Settings → General → Pull Requests).

## Hosting the Java app (to do)
Pick a host that runs Java 25 (Render, Railway, Fly.io, a VPS). The app reads the port from the `PORT` environment variable (default 8080). Then replace `deploy.yml` with a deploy to that host and delete `placeholder/`.
