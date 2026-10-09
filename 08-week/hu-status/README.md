<!-- Your weekly grade is read AUTOMATICALLY from this file:
   08-week/hu-status/README.md  (inside YOUR fork). English. -->
# Weekly Status - Week 08

FULL_NAME: <WRITE-YOUR-FULL-NAME-HERE>
GITHUB_USER: CharithNikool5
TEAM: Pms_Property
SPRINT_GOAL: Make the core aggregate and the entry point explicit — update the API
Gateway contract, publish the `Reserva` lifecycle state diagram and close the API
decisions that apply to several services.

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-API-007 | Update `07-api/contracts/openapi/api-gateway.yaml` to the real project (5 microservices, `GUEST`/`HOST`/`ADMIN` roles, `POST /v1/identity/login`, correlation id propagation, shared `Timestamp`) | done | https://github.com/code-corhuila/property-docs/pull/17 (opened 2026-09-21, merged the same week) — commit `5f70d18` |
| DOC-UML-003 | Publish the state diagram of the `Reserva` lifecycle (`PENDIENTE` → `CONFIRMADA` / `CANCELADA` / `EXPIRADA`) | done | https://github.com/code-corhuila/property-docs/pull/23 (opened 2026-09-23, merged 2026-09-24) — commit `b9d1464` |
| DOC-API-008 | Write `07-api/decisions.md` — cross-cutting API decisions D-C1..D-C9 and declared gaps with an owner | done | https://github.com/code-corhuila/property-docs/pull/25 (opened 2026-09-24, merged 2026-09-25) — commit `4c4f3b3` |

## 2. My individual contribution

- **API Gateway contract.** Replaced the template text with the project's 5 real
  microservices; documented the login route (`POST /v1/identity/login`), RS256 token
  validation via JWKS at the gateway, permission checks in each service's Use Case
  layer, the three real roles, and the propagation of `X-Correlation-Id` as
  `metadata.correlationId` in the reservation Saga events.
- **State diagram.** Published the lifecycle of the `Reserva` aggregate in Mermaid:
  creation validates INV-001 (no date overlap), only `PENDIENTE` transitions
  (INV-002), the other three states are terminal, and each transition names the event
  that triggers it (`PagoAprobado`, `PagoRechazado`, 15-minute expiry).
- **Cross-cutting API decisions.** Wrote `07-api/decisions.md` (+257 lines): the three
  error-body forms and nine closed decisions D-C1..D-C9 (URL versioning, offset
  pagination, date-range semantics, error taxonomy, `CorrelationId`, idempotency on
  Saga events, `Money`, JWT RS256 and roles). Each decision is marked "decided —
  pending verification at implementation" because no service runs yet.

## 3. Blockers and risks

- All three PRs were opened and merged inside Week 8.
- `decisions.md` must stay consistent with the course norm and with
  `07-api/guidelines.md` (single error envelope and fixed error codes).
- The state diagram was committed over a template file name
  (`c4-container-example.md`); the file should be renamed to match its content.
- Two commit messages are not clean Conventional Commits (`Docs(api): ...` with a
  capital letter and `dosc(api): ...` with a typo).

## 4. Plan for next week

- Add the endpoint fiches of `identity-service` and `notification-service`.
- Rename the state diagram file and update the diagram index.

## 5. Compliance self-check

- [ ] Conventional Commits - type(scope): summary — *(see Blockers: two messages need a fix)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(not applicable to the `-docs` repo: single `main` branch; each change went through its own branch + PR to `main`)*
- [x] Testable acceptance criteria — *(each transition can be checked against `entities-and-rules.md`; each decision lists its rationale and what verifies it at implementation)*
- [ ] Tests added/updated (unit / integration) — *(no service code yet)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no code yet)*
- [x] No secrets; config via environment variables — *(documentation only; no credentials or key material committed)*

## 6. Evidence links

- `07-api/contracts/openapi/api-gateway.yaml`
- `08-uml/diagrams/source/` — `Reserva` lifecycle state diagram
- `07-api/decisions.md`
- https://github.com/code-corhuila/property-docs/pull/17
- https://github.com/code-corhuila/property-docs/pull/23
- https://github.com/code-corhuila/property-docs/pull/25
- Repository: https://github.com/code-corhuila/property-docs
