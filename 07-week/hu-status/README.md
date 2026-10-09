<!-- Your weekly grade is read AUTOMATICALLY from this file:
   07-week/hu-status/README.md  (inside YOUR fork). English. -->
# Weekly Status - Week 07

FULL_NAME: <WRITE-YOUR-FULL-NAME-HERE>
GITHUB_USER: CharithNikool5
TEAM: Pms_Property
SPRINT_GOAL: Formalize the contracts between services — adapt the shared OpenAPI
components to the real project and practice the pull-request flow of the team.

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-API-006 | Update `07-api/contracts/openapi/_shared.yaml` with project-specific components (replace the course template's generic schemas) and fix a follow-up typo | done | https://github.com/code-corhuila/property-docs/pull/7 (opened 2026-09-20, merged after the week closed) — commits `13cc885`, `0a669e1` |
| DOC-GOV-003 | Practice PR: open a pull request with a small change to `04-requirements/README.md` to exercise the review and merge flow | done | https://github.com/code-corhuila/property-docs/pull/5 (opened 2026-09-17, merged 2026-09-22) — commits `7db6628`, `cbf583d` |

> **Evidence note:** both PRs were opened inside Week 7 and merged after it closed
> (PR #5 on 2026-09-22, PR #7 shortly after), so this is disclosed instead of hidden.

## 2. My individual contribution

- Rewrote the shared OpenAPI components (`_shared.yaml`, +122/-35 lines) so every
  service contract references schemas that belong to this project instead of the
  course template's generic ones; a second small commit fixed a mistake in the first.
- Opened a practice pull request (#5) that adds a point to the requirements README, to
  learn the review/merge flow before sending the real contract changes. It is a test
  change by design and is listed as such.

## 3. Blockers and risks

- Both PRs were merged after the week closed.
- Two commit messages do not follow Conventional Commits (`feat(docs): addpoint ...`
  and `fead(Docs): update _shared`, a typo in the type); fixed in later commits.

## 4. Plan for next week

- Update the API Gateway contract so it uses the new shared components.
- Document the cross-cutting API decisions that apply to more than one service.

## 5. Compliance self-check

- [ ] Conventional Commits - type(scope): summary — *(two messages have a malformed type; see Blockers)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; each change went through its own branch + PR to `main`)*
- [x] Testable acceptance criteria — *(every schema in `_shared.yaml` can be checked against the OpenAPI files that reference it)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- `07-api/contracts/openapi/_shared.yaml`
- https://github.com/code-corhuila/property-docs/pull/7
- https://github.com/code-corhuila/property-docs/pull/5
- Repository: https://github.com/code-corhuila/property-docs
