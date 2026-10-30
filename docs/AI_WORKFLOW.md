# AI workflow — Cursor and Claude Code

Honest record of how AI tools support this Appium automation repo. Do not treat this file as proof of runs that did not happen.

## Tools

| Tool | Role |
|------|------|
| **Cursor Agent** | Primary day-to-day coding, suite wiring, bug docs, git safety, dashboard updates |
| **Claude Code** (`claude` CLI) | Optional deep repo analysis, refactors, and debugging when authenticated |
| **Human** | Reviews locators, approves pushes, validates device runs, owns bug severity |

Installed Claude Code version checked during professionalization (2026-10-30): **2.1.314**.  
A non-interactive `claude -p` probe failed with **OAuth session expired** — so Claude Code was **not** used to generate code or reports in that session. Re-auth (`claude` login) is required before claiming Claude Code executions.

## How work usually flows

1. **Discover** — dump UI / read edge-case catalogs under `docs/*-edge-cases.md`.
2. **Automate** — page objects + Dump/Landing/Interact/Edge tests under `modules/<feature>/presentation`.
3. **Run** — smallest TestNG XML on emulator + Appium 3; Allure via `./report.sh`.
4. **Report** — update `docs/bugs.html` / `BUGS_FOUND.docx` only with observed defects; refresh coverage when screens land.
5. **Ship** — logical commits to `origin` (`Rajcharchil/L2B-Vendor-Appium-Automation`) after compile and review.

## Representative tasks (patterns, not a fabricated transcript)

- Scaffold Material Orders / Inventory / registration suites from existing KYC/Home patterns
- Tighten empty-state assertions to avoid false passes
- Reconcile diverged `main` with `git pull --rebase` and backup refs
- Document report locations and CI compile checks for a public GitHub portfolio

## Human review practices

- Agents must not invent pass counts, device logs, or Allure totals.
- OTP and tokens stay out of git; QA phones in `config.properties` are non-production personas only.
- Public repo: avoid customer PII, proprietary internal-only docs, and credentialed screenshots in commits.
- Every push should be explainable from the diff alone.

## Re-running Claude Code later

```bash
claude --version   # expect 2.x
# after login:
claude -p "Summarize module layout under src/test/java/com/l2b/vendor/modules without inventing metrics."
```

Record real prompts and outcomes here when Claude Code is used for a meaningful change.
