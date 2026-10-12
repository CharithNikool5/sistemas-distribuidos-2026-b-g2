<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       10-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 10

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Pms_Property
- SPRINT_GOAL: Finish aligning the documentation (API contracts, UML and UX) and start the coding phase with the frontend — the Angular container, the Catalog and Booking portals and their Docker composition — promoting each story from `develop` to `qa` and into the 2.0.0 release.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| DOC-API-008 | Rewrite the API Gateway contract and translate the Notification contract | done | https://github.com/code-corhuila/property-docs/pull/97 |
| DOC-API-009 | Write the Saga rulebook (workflow fiches) and the Payment fiches | done | https://github.com/code-corhuila/property-docs/pull/99 |
| DOC-UML-001 | Redraw the login, Saga and expiry sequence diagrams | done | https://github.com/code-corhuila/property-docs/pull/112 |
| DOC-UML-002 | Redraw the ER diagrams, one per schema | done | https://github.com/code-corhuila/property-docs/pull/114 |
| DOC-UML-003 | Add the state and event diagrams and update the UML index | done | https://github.com/code-corhuila/property-docs/pull/135 |
| DOC-UX-001 | Align the navigation map with the contracts and the portals | done | https://github.com/code-corhuila/property-docs/pull/116 |
| DOC-UX-002 | Fix the design tokens and record the portal contract | done | https://github.com/code-corhuila/property-docs/pull/140 |
| DOC-API-010 | Close GAP-019 (account lock) and correct stale references | done | https://github.com/code-corhuila/property-docs/pull/142 |
| HU-PLAT-002 | Frontend container: shared HTTP client, session and development sign-in | done | https://github.com/code-corhuila/property-front/pull/2 |
| HU-CAT-001 | Catalog portal: scaffold the Angular portal | done | https://github.com/code-corhuila/property-catalog-portal/pull/1 |
| HU-CAT-002 | Catalog portal: show the detail of a property | done | https://github.com/code-corhuila/property-catalog-portal/pull/5 |
| HU-SAGA-001 | Booking portal: scaffold the Angular portal | done | https://github.com/code-corhuila/property-booking-portal/pull/1 |
| HU-PLAT-003 | Composition: add the Booking portal and a development gateway | done | https://github.com/code-corhuila/property-infra-postgres/pull/2 |
| HU-PLAT-003 | Promote the demo composition to `qa` — `hu-plat-003-qa` → `qa` | done | https://github.com/code-corhuila/property-infra-postgres/pull/3 |
| HU-BOOK-006 | Promote "My bookings" to `qa` — `hu-book-006-qa` → `qa` | done | https://github.com/code-corhuila/property-booking-portal/pull/7 |
| HU-PLAT-002 | Carry the web container into the release, keeping the development sign-in out — `hu-plat-002-release` → `release/2.0.0` | done | https://github.com/code-corhuila/property-front/pull/12 |
| HU-PLAT-003 | Carry the `.dockerignore` of the container into the release — `hu-plat-003-release` → `release/2.0.0` | done | https://github.com/code-corhuila/property-front/pull/13 |
| — | Release 2.0.0 of `property-front` into `main`, tagged `v2.0.0` | done | https://github.com/code-corhuila/property-front/pull/14 |

## 2. My individual contribution
- **API contracts.** Rewrote the API Gateway contract (routing table, credential filter, its own errors) and translated the Notification contract. Wrote the Saga rulebook in the workflow fiches — every answer of every step and what the orchestrator does with it — and the Payment fiches for charge and refund.
- **UML.** Redrew the login, Saga and expiry sequence diagrams, the ER diagrams per schema, and added the state and event diagrams, updating the UML index.
- **UX.** Aligned the navigation map with the contracts and the portals, fixed the design tokens (font syntax and the contrast of the primary colour) and recorded how the container mounts each portal. Closed GAP-019 (account lock after five failed logins) and corrected stale references.
- **Frontend (HU-PLAT-002, HU-CAT-001, HU-CAT-002, HU-SAGA-001).** Added the shared HTTP client and the session to the container; scaffolded the Catalog and Booking portals; built the property detail of the Catalog portal.
- **Docker (HU-PLAT-003).** Added the Booking portal and a development gateway to the composition, so the whole flow runs locally against the mocks, and promoted the composition to `qa`.
- **Promotion and release.** Promoted "My bookings" to `qa`, carried HU-PLAT-002 and HU-PLAT-003 of `property-front` into `release/2.0.0` with `cherry-pick -x` — with the controlled commit that keeps the development sign-in out of `main` — and opened the 2.0.0 release of `property-front` into `main`, approved by the teacher and tagged `v2.0.0`.

## 3. Blockers and risks
- There is no backend yet: the portals run against mock APIs. The teacher said backend and database are not required for this cut.
- The automated review raised findings on the release (hard-coded `localhost` addresses, the controlled commit without a cherry-pick trail, branch names); each one was answered in the pull request with the decision taken.

## 4. Plan for next week
- Delivery on 2026-10-13: presentation, the release on `main` with tags, the `.zip` of the repositories and `release.md`.
- Document the services in `09-microservices/`: the API Gateway, the Payment service and the workflow.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary` — *(e.g. `feat(infra): add the booking portal and a development gateway`)*
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) — *(`hu-plat-003-qa`, `hu-book-006-qa`, `hu-plat-002-release`, `hu-plat-003-release`, with `cherry-pick -x`)*
- [x] Testable acceptance criteria — *(the stories carry Given/When/Then scenarios)*
- [x] Tests added/updated (unit / integration) — *(frontend PRs passed CI before merging)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — *(no backend code yet)*
- [x] No secrets; config via environment variables — *(the development sign-in is kept out of the release; no keys in the repositories)*

## 6. Evidence links
- property-docs: https://github.com/code-corhuila/property-docs
- property-front: https://github.com/code-corhuila/property-front
- property-catalog-portal: https://github.com/code-corhuila/property-catalog-portal
- property-booking-portal: https://github.com/code-corhuila/property-booking-portal
- property-infra-postgres: https://github.com/code-corhuila/property-infra-postgres
