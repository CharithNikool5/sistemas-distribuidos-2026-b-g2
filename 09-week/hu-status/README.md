<!-- Your weekly grade is read AUTOMATICALLY from this file:
   09-week/hu-status/README.md  (inside YOUR fork). English. -->
# Weekly Status - Week 09

FULL_NAME: <WRITE-YOUR-FULL-NAME-HERE>
GITHUB_USER: CharithNikool5
TEAM: Pms_Property
SPRINT_GOAL: Bring architecture and data documentation in line with the project's
decisions and the course norm — endpoint fiches, closed gaps, ADRs for workflow scope,
languages and frontend, deployment view, and the data model (payments, migrations).

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-API-009 | Add endpoint fiches for `identity-service` and `notification-service` | done | https://github.com/code-corhuila/property-docs/pull/36 (merged 2026-09-28) — commit `2bb9125` |
| DOC-CTX-002 | Close GAP-001/002/003/012 (Stripe, RabbitMQ, MongoDB, gateway routing) in `scope.md`, `overview.md`, `environments.md` and a base `docker-compose.yml` | done | https://github.com/code-corhuila/property-docs/pull/40 (merged 2026-09-30) — commit `3a9a0f4` |
| DOC-ARCH-011 | ADR-007 — scope `-workflow` to reactive observability only, preserving ADR-004 (choreographed Saga) | done | https://github.com/code-corhuila/property-docs/pull/43 (opened 2026-09-30, merged 2026-10-02) — commit `8d0114d` |
| DOC-ARCH-012 | ADR-011 (service languages) and ADR-012 (frontend frameworks) | done | https://github.com/code-corhuila/property-docs/pull/51 (merged 2026-10-02) — commit `40bba6d` |
| DOC-ARCH-013 | Update `pattern-guide.md` for the orchestrated Saga and the per-domain schemas | done | https://github.com/code-corhuila/property-docs/pull/65 (opened 2026-10-03, merged 2026-10-04) — commit `de32d25` |
| DOC-ARCH-014 | Add the deployment view of repositories and environments (`05-architecture/deployment.md`) | done | https://github.com/code-corhuila/property-docs/pull/67 (opened 2026-10-03, merged 2026-10-04) — commit `73c517f` |
| DOC-DATA-001 | Move the migration strategy to Liquibase, one changelog per schema | done | https://github.com/code-corhuila/property-docs/pull/77 (opened 2026-10-04, merged 2026-10-05) — commit `a1c033b` |
| DOC-DATA-002 | Rewrite the payment data model with refunds and idempotency keys (`models.md`, `data-dictionary.md`, `entities-and-rules.md`) | done | https://github.com/code-corhuila/property-docs/pull/79 (opened 2026-10-04, merged 2026-10-05) — commit `3c44714` |

> **Evidence note:** PRs #77 and #79 were opened inside Week 9 and merged on the first
> day of Week 10; their merge dates are shown instead of hidden.

## 2. My individual contribution

- **Endpoint fiches.** Wrote the fiches of `identity-service` (+167 lines) and
  `notification-service` (+71 lines), the two services that had none.
- **Closed gaps.** Resolved GAP-001/002/003/012 by recording the payment gateway
  (Stripe), the broker (RabbitMQ), the Catalog store (MongoDB) and the gateway routing
  in the context and architecture documents, with a base `docker-compose.yml` and the
  environments document updated to match.
- **ADR-007 (workflow scope).** Recorded that `-workflow` is reactive observability
  only, so the choreographed Saga of ADR-004 is preserved; ADR-004 got a cross-reference
  and `07-api/gaps.md` was updated.
- **ADR-011 and ADR-012.** Documented the language choice per service and the frontend
  framework choice, each with options, dominant criterion and accepted cost.
- **Pattern guide and deployment view.** Updated `pattern-guide.md` for the
  orchestrated Saga and per-domain schemas, and added `deployment.md` (+185 lines)
  describing repositories and environments.
- **Data model.** Rewrote the payment model (refunds, idempotency keys) and moved the
  migration strategy to Liquibase per schema.
- Merged PRs #63 and #65 into `main`.

## 3. Blockers and risks

- The documents touched this week must stay consistent with the other ADRs accepted
  in the same period (single database per engine, broker and worker decisions) and with
  `07-api/guidelines.md`.
- Some wording ("orchestrated Saga") needs a second pass to make sure it matches the
  final team decision on the Saga style.
- No service code exists yet, so none of this can be verified by tests.

## 4. Plan for next week

- Redraw the sequence, state and ER diagrams so they match the schemas and contracts.
- Align the UX navigation map with the contracts and the portals.
- Update the gateway contract and the notification contract.

## 5. Compliance self-check

- [x] Conventional Commits - type(scope): summary — *(e.g. `docs(architecture): add adr-011 and adr-012 for languages and frontend`)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; every change went through its own branch + PR to `main`)*
- [x] Testable acceptance criteria — *(each ADR lists context, options, dominant criterion, accepted cost and consequences; each closed gap has an explicit exit condition)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- `07-api/contracts/identity-endpoint-fiches.md`, `07-api/contracts/notification-endpoint-fiches.md`
- `05-architecture/decisions/records/ADR-007-workflow-scope.md`
- `05-architecture/decisions/records/ADR-011-service-languages.md`
- `05-architecture/decisions/records/ADR-012-frontend-frameworks.md`
- `05-architecture/deployment.md`, `05-architecture/pattern-guide.md`
- `06-data/migration-strategy.md`, `06-data/models.md`
- https://github.com/code-corhuila/property-docs/pull/36, https://github.com/code-corhuila/property-docs/pull/40, https://github.com/code-corhuila/property-docs/pull/43, https://github.com/code-corhuila/property-docs/pull/51, https://github.com/code-corhuila/property-docs/pull/65, https://github.com/code-corhuila/property-docs/pull/67, https://github.com/code-corhuila/property-docs/pull/77, https://github.com/code-corhuila/property-docs/pull/79
- Repository: https://github.com/code-corhuila/property-docs
