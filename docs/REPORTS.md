# Reports and test evidence

Index of where results live. Distinguish **historical** packs from **newly executed** runs.

## Generated locally (usually gitignored)

| Artifact | Path | How to produce |
|----------|------|----------------|
| Allure raw results | `target/allure-results/` | `mvn test` (or suite XML) |
| Allure HTML | `target/allure-report/` | `./report.sh` after tests |
| Surefire / TestNG | `target/surefire-reports/` | Maven Surefire (JUnit-compatible XML + text) |
| Failure screenshots | `screenshots/` | `screenshot.on.failure=true` in config |
| Logs | `logs/`, `*.log` | runtime / Appium |

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
# suiteXmlFile is wired in pom.xml (not ignored by Surefire)
mvn -DsuiteXmlFile=src/test/resources/<suite>.xml test
./report.sh
allure open target/allure-report
```

Live Pages (dashboard; large single-file Allure packs are skipped on purpose):

```bash
./scripts/deploy-pages-git.sh
```

`mvn allure:serve` skips history — prefer `./report.sh` when you need the TREND widget.

## Company / durable packs (gitignored under `reports/`)

| Artifact | Path |
|----------|------|
| Latest single-file pack | `reports/company-allure/latest/index.html` |
| Timestamped copies | `reports/company-allure/<timestamp>/` |
| Zip for sharing | `reports/company-allure/L2B-Vendor-Allure-*.zip` |
| Older deep regression | `reports/deep-regression-2026-09-11/` (historical) |
| Onboarding dumps | `reports/onboarding-dumps-2026-09-15/` (historical) |

Produce packs with:

```bash
./scripts/publish-allure.sh
# optional live Pages deploy when configured:
./scripts/deploy-allure-live.sh
```

## Dashboard (committed, human-readable)

| File | Purpose |
|------|---------|
| [`index.html`](index.html) | Coverage / flow status page |
| [`bugs.html`](bugs.html) | Bug list for stakeholders |
| [`coverage.json`](coverage.json) | Machine-readable screen coverage |
| [`last-run.json`](last-run.json) | Stamp for last published or noted run |
| [`BUGS_FOUND.docx`](BUGS_FOUND.docx) | Word export of bugs |

**Rule:** when `last-run.json` cites the published Allure total (e.g. 700), do not overwrite it with a single-module suite count.

## Historical vs new

- **Historical:** dated folders under `reports/`, committed screenshots under `APPscreenshots/`, older Allure on GitHub Pages (if enabled).
- **New:** only claim pass/fail after a run in *this* environment; attach Allure/Surefire paths from that run.

## Live URLs

Canonical code host: https://github.com/Rajcharchil/L2B-Vendor-Appium-Automation  

GitHub Pages / live Allure may be unset on the new owner until `gh-pages` is published. Override deploy targets:

```bash
export ALLURE_GH_REPO=Rajcharchil/L2B-Vendor-Appium-Automation
export ALLURE_PAGES_BASE=https://rajcharchil.github.io/L2B-Vendor-Appium-Automation
./scripts/deploy-allure-live.sh
```

Do not fabricate a green live badge if Pages returns 404.
