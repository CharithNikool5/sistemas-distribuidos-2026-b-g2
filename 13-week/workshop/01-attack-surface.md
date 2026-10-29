# CP1 — Attack surface of Property

Sources: property-docs 05-architecture/overview.md, 05-architecture/deployment.md and the
OpenAPI contracts in 07-api/contracts/. My boundary, in red: **internet -> api-gateway (identity public routes)**.

```mermaid
flowchart TB
  subgraph TB1["TB1 Internet (untrusted)"]
      user([Guest / Host / Admin])
      front[property-front + 5 portals]
  end
  subgraph TB2["TB2 Edge"]
      gw[property-api-gateway :8080]
  end
  subgraph TB3["TB3 Internal services"]
      wf[property-workflow :3006]
      booking[property-booking-api :3001]
      catalog[property-catalog-api :3002]
      payment[property-payment-api :3003]
      identity[property-identity-api :3004]
      notif[property-notification-api :3005]
      worker[property-worker]
      mq[[RabbitMQ]]
  end
  subgraph TB4["TB4 Data"]
      pg[(PostgreSQL 16: booking, payment, identity, notification, workflow)]
      mongo[(MongoDB 7: catalog)]
  end
    stripe[(Stripe sandbox)]
  user --> front
  front -->|HTTPS| gw
  gw -->|/api/v1/sagas| wf
  gw -->|/register /login /refresh /jwks| identity
  gw --> booking
  gw --> catalog
  gw --> payment
  gw --> notif
  wf -->|crear-reserva / confirmar| booking
  wf -->|cobrar / reembolso| payment
  payment -->|charge, refund| stripe
  worker -->|expire| booking
  booking -->|outbox| mq
  payment -->|outbox| mq
  mq --> notif
  mq --> catalog
  booking -->|booking_app| pg
  payment -->|payment_app| pg
  identity -->|identity_app| pg
  notif -->|notification_app| pg
  wf -->|workflow_app| pg
  catalog -->|catalog_app| mongo
  linkStyle 1 stroke:#d00,stroke-width:4px
```

## Assets of my boundary

| Asset | Where it lives | Classification |
|---|---|---|
| E-mail and password of the user | body of POST /register and /login | restricted |
| Access token (JWT RS256, roles GUEST/HOST/ADMIN) | answer of /login and /refresh; Authorization header | restricted |
| Refresh token | answer of /login; stored as a hash in identity.refresh_token | restricted |
| Public key set | GET /api/v1/jwks | public |
| Account lock state (failed_logins, locked_until) | identity.user_account | internal |

## Entry points of my boundary

| Entry point | Who calls it | Authentication today |
|---|---|---|
| POST /api/v1/login (E-11) | browser, any client | none (public); rate limit 10 per IP in 5 minutes; account lock after 5 failures |
| POST /api/v1/register (E-10) | browser | none (public) |
| POST /api/v1/refresh (E-12) | browser | refresh token in the body |
| GET /api/v1/jwks (E-15) | services, anyone | none (public key only) |
| Port 8080 | internet | the only published port of the platform |
