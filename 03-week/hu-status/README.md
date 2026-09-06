<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       03-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 03

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Charith Nikool Chavarro, Juan Esteban Oliveros Duran, Daniel Stiven Poveda, Angie Valentina Flores
- SPRINT_GOAL: Define the MVP 1 baseline through the Preliminary Design Review (PDR) — strategic DDD design (bounded contexts for Catalog, Booking, Payment), CAP/PACELC consistency trade-offs per operation, the hexagonal-architecture and choreographed-Saga decisions, and the formal in/out-of-scope boundaries for MVP 1.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | No HU backlog existed yet this week — work was foundational (PDR, DDD strategic design, scope definition), which the HU backlog (created in Week 4) was later derived from | done | [COMPLETAR] |

> Note: `functional.md` / `user-stories.md` did not exist yet at this stage. This week's
> output was the PDR (`PDR_Momento.pdf`) and `scope.md`, which fed directly into the FR/HU
> work delivered in Week 4.

## 2. My individual contribution
- [COMPLETAR: describe your specific part — e.g. "I contributed to the CAP/PACELC trade-off
  table, justifying AP/eventual consistency for Catalog search vs. CP/linearizable consistency
  for Booking's date-locking invariant."]
- [COMPLETAR: e.g. "I helped draft the scope.md in/out-of-scope tables and the assumptions
  section, translating the PDR's happy path into explicit MVP boundaries."]

## 3. Blockers and risks
- Message broker choice (RabbitMQ vs. Kafka) left as "TBD" in the PDR — needed before the
  Saga (Booking ↔ Payment) can be implemented.
- Catalog data store choice (MongoDB vs. Elasticsearch) also left as "TBD".
- Payment gateway provider not named in the PDR — the document only says "processing card
  charges" without specifying Stripe, PayU, etc.
- Per-environment deployment strategy (Docker Compose vs. Kubernetes, cloud provider) not
  yet detailed in the PDR.
- No deadline or budget defined for MVP 1 in the PDR's constraints — makes sprint-level
  planning harder until the professor/team fixes a timeline.
- [COMPLETAR: any blocker specific to your part of the design discussion]

## 4. Plan for next week
- Formalize the PDR and scope into concrete functional requirements (FR-XXX) and a user
  story backlog (HU-XXX) with Gherkin acceptance criteria — this became the Week 4 goal.
- Resolve at least the broker and catalog-engine "TBD" decisions before Sprint 1 coding starts.
- [COMPLETAR: your specific task for next week]

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [ ] Testable acceptance criteria — not applicable yet; HUs with Gherkin criteria were written in Week 4
- [ ] Tests added/updated (unit / integration) — no code written yet
- [x] DDD / hexagonal boundaries respected (domain has no I/O) — this was the core deliverable of the week: 3 bounded contexts (Catalog, Booking, Payment) with hexagonal architecture (ports/adapters) defined per service
- [x] No secrets; config via environment variables — documented as a technology constraint in `scope.md`

## 6. Evidence links
- Preliminary Design Review: `PDR_Momento.pdf`
- Scope definition (in/out of MVP 1, assumptions, constraints): `scope.md`
- [COMPLETAR: link to the PR/commit where the PDR and scope.md were added to the team repo]