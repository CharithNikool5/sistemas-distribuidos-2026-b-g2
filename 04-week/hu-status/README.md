<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       04-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 04

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Charith Nikool Chavarro, Juan Esteban Oliveros Duran, Daniel Stiven Poveda, Angie Valentina Flores
- SPRINT_GOAL: Catch up on project documentation — formalize the requirements baseline (functional requirements, user stories, non-functional requirements, and traceability matrix) so Sprint 1 implementation starts from a complete, consistent spec derived from the PDR and the domain model.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| HU-CAT-001 | Search properties | done (documented) | [COMPLETAR] |
| HU-CAT-002 | View property detail | done (documented) | [COMPLETAR] |
| HU-CAT-003 | Filter search by availability | done (documented) | [COMPLETAR] |
| HU-CAT-004 | Sync availability read-model from Booking events | done (documented) | [COMPLETAR] |
| HU-BOOK-001 | Request a booking (create Reserva + lock dates) | done (documented) | [COMPLETAR] |
| HU-BOOK-002 | Prevent double-booking under concurrency | done (documented) | [COMPLETAR] |
| HU-BOOK-003 | Confirm booking on payment approval | done (documented) | [COMPLETAR] |
| HU-BOOK-004 | Cancel booking and release dates on payment rejection | done (documented) | [COMPLETAR] |
| HU-BOOK-005 | Auto-expire stale pending bookings | done (documented) | [COMPLETAR] |
| HU-BOOK-006 | Query booking status | done (documented) | [COMPLETAR] |
| HU-PAY-001 | Process charge on ReservaCreada | done (documented) | [COMPLETAR] |
| HU-PAY-002 | Deduplicate events for idempotent processing | done (documented) | [COMPLETAR] |
| HU-PAY-003 | Maintain the transaction ledger | done (documented) | [COMPLETAR] |
| HU-QA-001 | Consumer-driven contract tests for Saga events | doing (documented, not yet groomed) | [COMPLETAR] |

> Note: no HU has code yet — this week's "done" refers to the HU being fully specified
> (Gherkin acceptance criteria + story points + dependencies), not implemented.

## 2. My individual contribution
- [COMPLETAR: describe which parts you personally wrote/reviewed — e.g. "I drafted the functional
  requirements for Catalog and Booking services (FR-CAT-001 to FR-BOOK-007) and reviewed the
  Gherkin acceptance criteria for the Booking epic against the PDR's Saga description."]
- [COMPLETAR: mention any team discussion you led or participated in, e.g. resolving the
  scope conflict between `scope.md` (Identity/Notification out of MVP 1) and `domain-map.md`
  (which already models both as full bounded contexts).]

## 3. Blockers and risks
- The message broker choice (RabbitMQ vs. Kafka) is still undecided in the PDR/scope, and
  FR-SAGA-001 / HU-BOOK-001 / HU-PAY-001 all depend on it — needs a decision before Sprint 1
  implementation can start on the Saga.
- The payment gateway provider (Stripe, PayU, etc.) is also undecided (`scope.md`), blocking
  FR-PAY-001/003/004 implementation details (webhook format, SDK, sandbox credentials).
- Scope inconsistency: `scope.md` marks Identity and Notification as out of MVP 1, but
  `domain-map.md` already designs them as full bounded contexts. Documented as HU-IAM-001 /
  HU-NOTIF-001 in the "Post-MVP" epic to avoid losing the design work, but the team needs to
  confirm with the professor whether they stay deferred or enter MVP 1.1.
- [COMPLETAR: any other blocker specific to your part, e.g. access to a repo, environment setup, etc.]

## 4. Plan for next week
- Resolve the broker and payment-gateway decisions with the team so Sprint 1 stories are
  unblocked for implementation.
- Move HU-BOOK-001, HU-BOOK-002, HU-CAT-001, and HU-CAT-002 from "documented" to "in progress"
  (branch + first commits) — these are the Sprint 1 target per `user-stories.md`.
- Start filling test skeletons referenced in `traceability-matrix.md` (e.g.
  `booking.create.spec.ts`, `catalog.search.spec.ts`) so the 🔴 Pending statuses start moving.
- [COMPLETAR: your specific task for next week]

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria — Gherkin Given/When/Then written for all Sprint 1-2 HUs in `user-stories.md`
- [ ] Tests added/updated (unit / integration) — pending, no code written yet this week
- [x] DDD / hexagonal boundaries respected (domain has no I/O) — reflected in NFR-006 and the service boundaries defined per bounded context
- [x] No secrets; config via environment variables — documented as a constraint in `non-functional.md` (NFR-004, NFR-007)

## 6. Evidence links
- Functional requirements: `04-requirements/functional.md`
- User stories: `04-requirements/user-stories.md`
- Traceability matrix: `04-requirements/traceability-matrix.md`
- Non-functional requirements: `04-requirements/non-functional.md`
- [COMPLETAR: link to the PR/commit where these files were merged into the team repo]
