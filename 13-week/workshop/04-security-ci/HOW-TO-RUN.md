# How to run the negative tests — property-api-gateway

Story: **[security] Stop credential stuffing on login** (STRIDE S).
Acceptance criterion covered: *the 11th POST /api/v1/login from the same IP within 5 minutes
answers 429 TOO_MANY_REQUESTS*, plus *a protected route without a credential answers 401*.

Endpoints: `POST /api/v1/login` (E-11) and `GET /api/v1/users/me`.

1. Clone `code-corhuila/property-api-gateway` (branch `develop`, with D5 merged).
2. Start the gateway (one image at a time):
   ```bash
   docker network create platform        # only if it does not exist
   docker build -f deploy/Dockerfile -t gateway .
   docker run -d --name gateway --network platform -p 8080:8080 gateway
   ```
3. Run the test (Git Bash):
   ```bash
   sh negative-auth.sh http://localhost:8080
   ```
4. Expected: `OK: ... -> 401` and `OK: 11th POST /api/v1/login from one IP -> 429`, exit code 0.
5. Clean up: `docker rm -f gateway`.

The services behind the gateway may be off: the gateway answers the 401 and the 429 by
itself.

## Real output (CP8)

```
$ sh "$FORK/13-week/workshop/04-security-ci/negative-auth.sh" http://localhost:8080
OK: /api/v1/users/me without a credential -> 401
OK: 11th POST /api/v1/login from one IP -> 429

ASUS@DESKTOP-2F1I0EJ MINGW64 ~/Desktop/clases 2026b/sistemas distribuidos/repoProyecto/property-api-gateway (develop)
$ docker rm -f w13-gateway
w13-gateway

```

## security.yml validation

```
PEGA AQUÍ LA SALIDA DE ACTIONLINT (si no imprimió nada, escribe: actionlint printed no errors)
```
