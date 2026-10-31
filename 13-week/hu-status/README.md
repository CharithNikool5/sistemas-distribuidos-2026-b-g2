<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       13-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 13

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Charith Nikool Chavarro Meneses
- GITHUB_USER: CharithNikool5
- TEAM: Group 2 — Property
- SPRINT_GOAL: Threat model of the boundary internet -> api-gateway (identity public routes) and turn its top risks into security stories for the team backlog
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| #166 | Threat model: internet -> api-gateway (identity public routes) | done | https://github.com/code-corhuila/property-docs/pull/169 |
| #170 | [security] Stop credential stuffing on login | todo | https://github.com/code-corhuila/property-docs/issues/170 |
| #171 | [security] The login does not reveal which e-mails exist | todo | https://github.com/code-corhuila/property-docs/issues/171 |
| #172 | [security] Protected routes need a credential at the gateway | todo | https://github.com/code-corhuila/property-docs/issues/172 |

## 2. My individual contribution
- Attack surface map of Property with its trust boundaries, and my boundary internet -> api-gateway (identity public routes): https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/blob/main/13-week/workshop/01-attack-surface.md
- STRIDE analysis with risk ranking, residual risk and the library book [B], updated after the peer review: https://github.com/code-corhuila/property-docs/pull/169
- Three security stories in the team backlog: #170, #171, #172
- Security CI proposal (secret scan, dependency audit, negative test) with the real output of the test: https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/tree/main/13-week/workshop/04-security-ci

## 3. Blockers and risks
- The CI of the code-corhuila organization does not start (billing lock), so the security workflow is a proposal in my fork, run by hand.

## 4. Plan for next week
- Implement the highest-priority story of my boundary in batch 2 of the code.
- Move the negative test of CP8 into the team repository with its pull request.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration)
- [x] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

## 6. Evidence links
- Workshop folder: https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/tree/main/13-week/workshop
- Claim issue: https://github.com/code-corhuila/property-docs/issues/166
- STRIDE pull request and its commits: https://github.com/code-corhuila/property-docs/pull/169/commits
- Stories: https://github.com/code-corhuila/property-docs/issues/170, https://github.com/code-corhuila/property-docs/issues/171, https://github.com/code-corhuila/property-docs/issues/172
- Review comment I left: https://github.com/code-corhuila/property-docs/pull/177#issuecomment-6514038584
- Review received and my answer: https://github.com/code-corhuila/property-docs/pull/169
- Commits of the workshop in my fork: https://github.com/CharithNikool5/sistemas-distribuidos-2026-b-g2/commits/main/13-week
- Reflection document: https://docs.google.com/document/d/1JzIxjUXTD_ERx_a63TqkbB8BFZOcxuWSWTVWNR7dS08/edit?usp=sharing
