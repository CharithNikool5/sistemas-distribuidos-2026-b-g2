<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       11-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 11

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Pms_Property
- SPRINT_GOAL: Make the confirmation e-mail observable from the Saga to the guest's inbox — trace, logs, metrics, SLOs, alerts and incident response for notification-service — and prepare corte 3.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| W11-CP1 | Trace map from the Saga to the e-mail: booking outbox, RabbitMQ, notification-service, identity-service | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/01-journey.md |
| W11-CP2 | JSON log contract for deliveries, with the never-log list for personal data | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/02-log-contract.md |
| W11-CP3 | Java consumer middleware, logger, trace sent to Identity and delivery metrics | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/tree/main/11-week/workshop/03-instrumentation |
| W11-CP4 | Golden signals of the relay, the queue, Notification and its dependencies | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/04-metrics.md |
| W11-CP6 | SLOs: e-mail delivered, on time, and no event lost | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/05-slo.md |
| W11-CP7 | Burn-rate alerts and alert policy | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/06-alerts.yml |
| W11-CP8 | Runbook of `NotificationEmailFastBurn` | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/07-runbook.md |
| W11-CP9 | Drill: Identity slow under Notification, postmortem and three stories | done | https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/11-week/workshop/08-incident.md |

## 2. My individual contribution
- Mapped the confirmation e-mail journey and how the trace survives the outbox and RabbitMQ into notification-service.
- Wrote the log contract of a delivery, keeping the guest's e-mail and name out of the logs.
- Wrote and tested a Java module of `notification-service`: the consumer keeps the Saga's `trace_id` and sends it to `identity-service`; duplicates are skipped by `event_id`.
- Defined the metrics, three SLOs, the burn-rate alerts and the runbook of the page.
- Ran a drill with Identity slow; the postmortem showed our SLI counted retries as failures and turned it into three user stories.

## 3. Blockers and risks
- GAP-017 is still open: how Notification gets the guest's e-mail. The workshop assumes an internal read to Identity.
- The services are not built yet; the module is standalone and the SLO numbers are estimates.

## 4. Plan for next week
- Corte 3: the real consumer of `notification-service` with the tracing of CP3.
- Close GAP-017 in `property-docs` (postmortem story 3).

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration) — *(`ConfirmationTraceTest`)*
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables — *(service token and provider key from the environment)*

## 6. Evidence links
- Workshop: https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/tree/main/11-week/workshop
- property-docs: https://github.com/code-corhuila/property-docs
