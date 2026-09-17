<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       06-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 06

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Charith Nikool Chavarro, Juan Esteban Oliveros Duran, Daniel Stiven Poveda, Angie Valentina Flores
- SPRINT_GOAL: Consolidate the system's final architecture before implementation starts — confirm the 5 business domains (Catalog/Property, Booking, Payment, User/Identity, Notification) and define the cross-cutting (transversal) microservices — Workflow, API Gateway, Infrastructure, and Worker — that support them. Waiting on repository delivery (one per domain, plus the transversal ones) to begin coding.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | No HU was coded this week — work was architectural: finalizing the 5 domains and designing the transversal microservices ahead of repository delivery | done (design) | [COMPLETAR] |

> Note: implementation on the existing HU backlog (`HU-CAT-XXX`, `HU-BOOK-XXX`, `HU-PAY-XXX`, etc.)
> has not started yet — the team is still waiting for the domain and transversal repositories
> to be provisioned before opening the first `hu-xxx-dev` branches.

## 2. My individual contribution
- [COMPLETAR: describe tu parte específica — p. ej. "Redacté/revisé la definición de los
  microservicios transversales (Workflow, API Gateway, Infraestructura, Worker) y cómo se
  relacionan con los 5 dominios de negocio ya definidos."]
- [COMPLETAR: p. ej. "Documenté el flujo de ejemplo Workflow → Booking/Payment/Notification/
  Property para el caso de una reserva, evitando que cada dominio tenga que conocer a los demás."]

## 3. Blockers and risks
- **Blocker:** the team cannot start coding yet — waiting for the repositories to be delivered,
  one per business domain (Catalog/Property, Booking, Payment, User/Identity, Notification)
  plus the transversal ones (Workflow, API Gateway, Infrastructure, Worker).
- Risk: the transversal services (especially Workflow and API Gateway) sit on the critical path
  of every domain-to-domain interaction (e.g. Booking → Payment → Notification), so any delay
  in their repo/setup blocks integration work across all 5 domains, not just one.
- Risk: "Infrastructure" as documented is conceptual (databases, messaging, storage, config,
  monitoring) rather than a single deployable service — needs to be translated into concrete
  technical decisions (which broker, which DB per domain, etc.) that are still open per
  `scope.md` (RabbitMQ vs. Kafka, MongoDB vs. Elasticsearch).
- [COMPLETAR: cualquier bloqueador específico de tu parte]

## 4. Plan for next week
- Receive and set up the domain repositories (Catalog/Property, Booking, Payment, User/Identity,
  Notification) and the transversal repositories (Workflow, API Gateway, Infrastructure, Worker).
- Open the first `hu-xxx-dev` branches for the Sprint 1 stories already documented in
  `user-stories.md` (HU-CAT-001, HU-CAT-002, HU-BOOK-001, HU-BOOK-002).
- Start resolving the still-open infrastructure decisions (message broker, catalog data store)
  now that Workflow/API Gateway make those dependencies more concrete.
- [COMPLETAR: tu tarea puntual para la próxima semana]

## 5. Compliance self-check
- [ ] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [ ] Testable acceptance criteria — already written in `user-stories.md`, but no new HU was coded this week
- [ ] Tests added/updated (unit / integration) — no code written yet, still pending repo delivery
- [x] DDD / hexagonal boundaries respected (domain has no I/O) — reinforced this week: Workflow/API Gateway/Worker absorb orchestration and I/O concerns precisely so each domain's core stays free of them
- [x] No secrets; config via environment variables — reaffirmed as part of the Infrastructure layer definition (configuration management: ports, service addresses, credentials)

## 6. Evidence links
- Cross-cutting microservices design: `DIFERENTES_MICROSERVICIOS_TRANSVERSALES.docx`
- Domain/backlog baseline referenced: `functional.md`, `user-stories.md`, `scope.md`
- [COMPLETAR: link al PR/commit donde se subió el documento de transversales al repo del equipo]