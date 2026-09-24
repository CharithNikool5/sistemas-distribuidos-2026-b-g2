<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       07-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 07

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Charith Nikool Chavarro, Juan Esteban Oliveros Duran, Daniel Stiven Poveda, Angie Valentina Flores
- SPRINT_GOAL: Consolidate the reusable API contract layer — enrich `_shared.yaml` with the schemas actually reused across domains (UserRole, DateRange, Money) and align `api-gateway.yaml` with the real upstream services and roles, replacing the generic placeholders both files started with.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | No HU from the backlog was implemented this week — work was on the cross-cutting API contract layer (`_shared.yaml`, `api-gateway.yaml`) that every domain service depends on | done (documented) | [COMPLETAR] |

> Note: `_shared.yaml` and `api-gateway.yaml` are not tied to a single HU — they're
> foundational contracts referenced by Catalog, Booking, Payment, Auth, and Notification.

## 2. My individual contribution
- Enriched `_shared.yaml` (v1.0.0 → v1.1.0) with the schemas that are actually reused by
  2+ domains instead of leaving it fully generic:
  - `UserRole` (GUEST/HOST/ADMIN) — used by Auth (issues it), API Gateway (propagates it via
    `X-User-Role`), and any service enforcing RBAC.
  - `DateRange` (checkInDate/checkOutDate) — shared between Catalog's availability search
    and Booking's reservation creation, so both use the exact same shape.
  - `Money` (amount + currency) — shared between Catalog's `pricePerNight` and Payment's
    charge/ledger amounts.
  - A reusable `CorrelationId` header and a generic `Conflict` (409) response shape.
- Updated `api-gateway.yaml`: replaced the generic `ADMIN/USER/VIEWER` role reference and
  placeholder `nombre-servicio` upstream with the project's real services (auth-service,
  catalog-service, booking-service, payment-service, notification-service) and documented
  the actual routing table (`/api/v1/bookings/*` → booking-service, etc.).
- [COMPLETAR: agrega aquí cualquier detalle adicional propio — p. ej. si tú mismo escribiste
  los ejemplos/descripciones o si fue una decisión conjunta con el equipo.]

## 3. Blockers and risks
- **PR review finding (professor):** a previous PR touching `_shared.yaml` (`feat/update-shared`
  branch) had an effectively empty diff — only the file's trailing newline was dropped, so the
  enriched version above was never actually merged. The branch name and PR title also didn't
  follow the course's Conventional Commits standard for this docs repo (`feat/...` used
  instead of `docs/...`, and no PR description).
  - **Action taken:** re-doing this as a new, correctly-named branch (`docs/update-shared-schemas`)
    with a Conventional Commit (`docs(api): add UserRole, DateRange and Money to _shared.yaml`)
    and a full PR description, so the diff this time actually reflects the schema additions.
- A second, unrelated finding from the same review — placeholder text (`......` / `..`) left in
  `04-requirements/README.md` — is still pending and not part of my scope this week, but is
  flagged here so it isn't lost before the checkpoint defense.
- [COMPLETAR: cualquier bloqueador técnico propio, p. ej. si necesitas que el equipo confirme
  el broker/gateway de pago antes de que Booking/Payment puedan consumir `Money`/`DateRange`.]

## 4. Plan for next week
- Open the corrected PR (`docs/update-shared-schemas`) with the real `_shared.yaml` v1.1.0
  content and get it merged, closing out the professor's point 4.
- Update `booking-service.yaml`, `catalog-service.yaml`, and `payment-service.yaml` to
  reference the new shared `DateRange` and `Money` schemas via `$ref` instead of redefining
  those fields locally, now that they exist in `_shared.yaml`.
- [COMPLETAR: tu tarea puntual, p. ej. limpiar el placeholder en `04-requirements/README.md`
  si te toca a ti, o avanzar con el `_template-service.yaml` de tu dominio asignado.]

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary` — not followed in the prior PR (`feat/update-shared`); being corrected this week with `docs(api): ...`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — not applicable yet, this work isn't tied to an HU branch
- [x] Testable acceptance criteria — not applicable to this contract-layer work (no HU/Gherkin attached)
- [ ] Tests added/updated (unit / integration) — no contract tests (Pact/Spectral) written yet for `_shared.yaml` or `api-gateway.yaml`
- [x] DDD / hexagonal boundaries respected (domain has no I/O) — `_shared.yaml`/`api-gateway.yaml` are pure contract definitions, no implementation code
- [x] No secrets; config via environment variables — the gateway's `servers.variables` (domain, port) stay parameterized, no hardcoded values

## 6. Evidence links
- Shared schemas contract: `07-api/contracts/openapi/_shared.yaml`
- API Gateway contract: `07-api/contracts/openapi/api-gateway.yaml`
- Professor's PR review (branch/commit/content findings on `_shared.yaml`): [COMPLETAR: link al PR/comentario]
- [COMPLETAR: link al nuevo PR `docs/update-shared-schemas` una vez lo abras]