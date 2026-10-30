# L2B Vendor Appium Automation

Black-box UI automation for the **L2B Vendor Partner** QA Android app (`com.l2b.app.qa`).

This repository demonstrates industry-style **SDET / mobile QA automation**: Appium page objects, TestNG suites, Allure reporting, API-assisted setup, and a living coverage/bugs dashboard — without inventing device results.

**Code host:** [Rajcharchil/L2B-Vendor-Appium-Automation](https://github.com/Rajcharchil/L2B-Vendor-Appium-Automation) (public)

Related but separate: `L2B-Vendor-app-test`, `L2B-Vendor-backend-api-test`, `L2B-Customer-Testing-App`.

---

## Objectives

- Automate critical Vendor flows (onboarding, home, bookings, fleet, settings, Material orders/inventory, KYC, and more) against the QA APK.
- Produce reproducible Allure + Surefire evidence and a human dashboard under `docs/`.
- Keep configuration environment-driven; never commit OTPs or secrets.

## Technology stack

| Area | Tool |
|------|------|
| Language / build | Java 17, Maven |
| Mobile | Appium 3, UiAutomator2, Appium Java Client 10.x |
| Assertions | TestNG 7.x, AssertJ |
| HTTP | Rest Assured |
| Reporting | Allure 2.x, Surefire reports |
| CI (this repo) | GitHub Actions **compile-only** (`mvn -DskipTests test-compile`) |

## Architecture

```
src/test/java/com/l2b/vendor/
  environment/          Config, device checks, Allure metadata, TestListener
  core/                 Driver factory/manager, waits, HTTP, BaseTest
  modules/<feature>/
    data/               Swagger-oriented API + DTOs (where used)
    domain/             *Environment (session, noReset, prepareSuite)
    presentation/       Page objects + Dump / Landing / Interact / Edge tests
src/test/resources/     config.properties + TestNG suite XML
docs/                   Dashboard, bugs, coverage, edge-case catalogs, report index
scripts/                Allure publish / live deploy helpers
apk/                    Drop QA APK locally (binaries gitignored)
```

Compose screens often lack resource-ids; locators prefer text / content-desc / structured XPath via shared helpers.

## Prerequisites

- JDK **17**
- Maven 3.9+
- Android SDK + AVD (default: `Medium_Phone_API_36.1`)
- Appium **3.x** + `uiautomator2` driver
- QA APK at `apk/app-qa-release.apk` (install with `adb install -r …`)

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"
```

## Environment configuration

Committed defaults: [`src/test/resources/config.properties`](src/test/resources/config.properties)  
Template: [`src/test/resources/config.properties.example`](src/test/resources/config.properties.example)

Precedence: **`-Dkey=value` > `L2B_*` env > properties file**.

- OTP: `-Dqa.otp=......` or `L2B_QA_OTP` — leave `qa.otp=` empty in git.
- QA phones are non-production personas for the QA environment (`api.base.url`).

**Public-repo note:** this project is public. Do not add customer PII, production credentials, or internal-only assets.

## Device / emulator

```bash
$ANDROID_HOME/emulator/emulator -avd Medium_Phone_API_36.1
adb devices   # expect emulator-5554   device
```

## Appium server

```bash
appium --address 127.0.0.1 --port 4723
```

## Execution

Full helper (boots checks + tests + publish hooks as scripted):

```bash
./run-tests.sh
```

Or manually:

```bash
mvn test && ./report.sh
# isolated suite:
mvn -DsuiteXmlFile=src/test/resources/orders/material-orders-all.xml test
```

Default `src/test/resources/testng.xml` is the committed smoke path (Splash → Language → onboarding Smoke → Carousel → SignUp).  
**SmokeTest uses `noReset=false`** and clears app data — do not run it if you need the current logged-in session.

## Reports and screenshots

See **[docs/REPORTS.md](docs/REPORTS.md)** for the full index.

| Kind | Location |
|------|----------|
| Allure HTML | `target/allure-report/` via `./report.sh` |
| Surefire | `target/surefire-reports/` |
| Screenshots on failure | `screenshots/` (gitignored) |
| Company pack | `./scripts/publish-allure.sh` → `reports/company-allure/` |
| Dashboard | [`docs/index.html`](docs/index.html), [`docs/bugs.html`](docs/bugs.html) |

## CI/CD

[`.github/workflows/compile-check.yml`](.github/workflows/compile-check.yml) runs **`mvn -DskipTests test-compile`** on `main` and PRs. It does **not** run UI tests (no emulator in Actions). Local/device farm runs remain the source of pass/fail evidence.

## Live dashboard / Pages

GitHub Pages may need to be enabled on this fork/owner. Deploy helpers accept overrides:

```bash
export ALLURE_GH_REPO=Rajcharchil/L2B-Vendor-Appium-Automation
export ALLURE_PAGES_BASE=https://rajcharchil.github.io/L2B-Vendor-Appium-Automation
./scripts/deploy-allure-live.sh
```

If Pages returns 404, treat live Allure as **not published yet** — use local `target/allure-report` or company packs instead.

## AI-assisted workflow

- [`CLAUDE.md`](CLAUDE.md) — conventions for Claude Code / agents  
- [`docs/AI_WORKFLOW.md`](docs/AI_WORKFLOW.md) — honest Cursor + Claude Code usage notes  

## Troubleshooting

| Symptom | Check |
|---------|--------|
| Wrong Java | `java -version` must be 17; set `JAVA_HOME` |
| No device | `adb devices`; cold boot AVD if unauthorized |
| Session / empty Home | Persona phone, login state, `noReset` |
| OTP failures | Runtime `-Dqa.otp`; known QA OTP issues are tracked in bugs docs |
| Blank Allure single-file | Serve folder with `python3 -m http.server` |

## Current limitations

- Full UI regression needs a local emulator + Appium; CI is compile-only.
- Some Material/Orders/registration suites may be WIP relative to published Allure totals — see `docs/last-run.json` notes.
- Live GitHub Pages on the new owner may not be configured yet.
- Compose locators remain inherently brittle; dumps before Interact/Edge are intentional.

## Roadmap (planned, not claimed done)

- [ ] Enable Pages + republish Allure under `Rajcharchil`
- [ ] Expand Material Orders empty-state hardening and inventory coverage
- [ ] Optional device-farm CI when credentials exist
- [ ] Keep edge-case catalogs in sync as screens ship

## License / visibility

Public educational / portfolio automation against the **QA** build. Do not publish production secrets or customer data.
