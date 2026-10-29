#!/usr/bin/env sh
# Story: [security] Stop credential stuffing on login (STRIDE S).
# Negative tests against property-api-gateway (default http://localhost:8080).
set -u
GW=${1:-http://localhost:8080}
fail=0

code=$(curl -s -o /dev/null -w '%{http_code}' "$GW/api/v1/users/me")
if [ "$code" = "401" ]; then echo "OK: /api/v1/users/me without a credential -> 401"
else echo "FAIL: /api/v1/users/me without a credential -> $code (expected 401)"; fail=1; fi

last=""
i=1
while [ $i -le 11 ]; do
  last=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$GW/api/v1/login" \
    -H 'Content-Type: application/json' -d '{"email":"nobody@example.com","password":"wrong-password"}')
  i=$((i + 1))
done
if [ "$last" = "429" ]; then echo "OK: 11th POST /api/v1/login from one IP -> 429"
else echo "FAIL: 11th POST /api/v1/login -> $last (expected 429)"; fail=1; fi

exit $fail
