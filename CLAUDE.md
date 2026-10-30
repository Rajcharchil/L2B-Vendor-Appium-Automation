# CLAUDE.md — L2B Vendor Appium Automation

Guidance for Claude Code (and other agents) working in this repository.

## Project

Black-box **Appium 3** UI automation for the L2B Vendor Partner QA APK (`com.l2b.app.qa`).

| Layer | Choice |
|-------|--------|
| Language | Java 17 |
| Build | Maven (`pom.xml`) |
| Tests | TestNG + AssertJ |
| Mobile | Appium Java Client 10.x / UiAutomator2 |
| API helpers | Rest Assured (module `data/` clients) |
| Reporting | Allure TestNG + `./report.sh` / `./scripts/publish-allure.sh` |
| Dashboard | `docs/index.html`, `docs/bugs.html`, `docs/coverage.json`, `docs/last-run.json` |

Compose UI often has **no resource-ids**. Prefer stable text / content-desc / hierarchy XPath via shared locator helpers — do not sprinkle brittle absolute XPaths in tests.

## Architecture (do not flatten)

```
src/test/java/com/l2b/vendor/
  environment/     Config, EnvironmentCheck, Allure metadata, TestListener
  core/            DriverFactory, DriverManager, Waits, HTTP, BaseTest
  modules/<feature>/
    data/          API clients + DTOs (when needed)
    domain/        *Environment (noReset, session, prepareSuite)
    presentation/  pages + Dump / Landing / Interact / Edge tests
src/test/resources/
  config.properties
  <feature>/*.xml  TestNG suites
docs/              Dashboard, edge-case catalogs, reports index
reports/           Local company Allure packs (gitignored)
```

Do **not** invent parallel empty trees (`tests/`, `pages/` at repo root) that fight this layout.

## Setup and execution

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_HOME=$HOME/Library/Android/sdk

# Emulator + Appium 3 on 127.0.0.1:4723, then:
mvn -DsuiteXmlFile=src/test/resources/<suite>.xml test
./report.sh
```

- Default `mvn test` uses `src/test/resources/testng.xml` (onboarding-focused smoke path).
- OTP: `-Dqa.otp=...` or `L2B_QA_OTP` — never commit OTP values.
- Config precedence: `-Dkey=value` > `L2B_*` env > `config.properties`.

## Coding and locator conventions

- Page objects in `presentation/pages`; tests call pages, not raw drivers.
- Explicit waits via shared `Waits` / page helpers — no fixed `Thread.sleep` unless diagnosing.
- Suite pattern: Dump → Landing → Interact → Edge; isolate with TestNG XML.
- `noReset=false` clears app data — never enable casually on a logged-in Material/Rental session.
- Skip honestly (`SkipException`) when preconditions are missing; do not false-pass empty states.

## Testing and reporting

- Allure results: `target/allure-results` → HTML via `./report.sh` → `target/allure-report`.
- Company pack: `./scripts/publish-allure.sh` → `reports/company-allure/`.
- Dashboard Allure total in `docs/last-run.json` must match the **published** Allure summary when that is the source of truth — do not overwrite with a single-module suite count.
- Failures: screenshots under `screenshots/` (gitignored); attach via TestListener / Allure.

## Git safety

- Remote: `origin` → `https://github.com/Rajcharchil/L2B-Vendor-Appium-Automation.git`
- Prefer `git pull --rebase`. No `reset --hard`, `clean -fd`, or force-push as a shortcut.
- Backup branch/tag before reconciling diverge.
- Never commit secrets, `.env`, real OTPs, tokens, or customer PII.
- Group logical commits. Push only after compile (and device runs when available).

## Debugging

1. Confirm JDK 17, `adb devices`, Appium `/status`, correct `app.package`.
2. Capture page source / screenshot on failure before changing locators.
3. Re-run the smallest suite XML that reproduces the issue.
4. If Home/Orders empty, skip or assert empty chrome — do not treat missing rows as pass for row actions.

## Definition of done

- [ ] Change matches existing module patterns
- [ ] Suite XML updated if new tests are runnable
- [ ] No secrets in the diff
- [ ] `mvn -DskipTests test-compile` succeeds
- [ ] Device evidence only claimed when a run actually happened
- [ ] Dashboard / bugs docs updated only when the run or bug list truly changed
