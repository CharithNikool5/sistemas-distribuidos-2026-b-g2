# CP7 · Alert policy — confirmation e-mail

Every alert in `06-alerts.yml` compares the speed of spending the CP6 budget (burn
rate) over a long and a short window. The month has 720 hours.

**`NotificationEmailFastBurn` — page.** Burn rate 14.4 over 1 h, checked over 5 min,
for 2 min. Threshold: 14.4 × 1 % = **14.4 % of deliveries failing**.
It fires after spending 14.4 × 1 / 720 = **2 %** of SLO 1: 2 % of 45 = **0.9 failed
e-mails**, 2 % of 432 = **8.6 minutes**. If ignored, the month's budget is gone in
50 hours.

**`NotificationEmailSlowBurn` — ticket.** Burn rate 6 over 6 h, checked over 30 min.
Threshold **6 %**. Fires after **5 %** of the budget: **2.25 e-mails**, **21.6 minutes**.
Five days to empty the budget: it waits for working hours.

**`NotificationEmailLate` — ticket.** SLO 2, burn rate 6: more than **30 %** of e-mails
over 60 s for 6 h. 5 % of its budget: **11 late e-mails**, **108 minutes**.

Why the fast one pages at all: a guest who paid and gets nothing writes to support or
pays twice thinking it failed. With ~6 e-mails an hour, 14.4 % means one failure in the
hour is already near the line; we accept that while volume is low.

SLO 3 (nothing lost) has no burn-rate rule yet: comparing two counters from two services
needs both scraped in the same Prometheus, which we will have in corte 3. Until then
the DLQ is checked daily (policy at 75 %).

## Cause-based alerts we leave out

1. **"Queue `notification.reservas` has more than 50 messages."** After a deploy of
   Notification the queue fills for a minute and drains by itself; the messages are
   durable and nothing is lost. If it lasts, `NotificationEmailLate` fires with the
   symptom the guest feels (late e-mail).
2. **"The e-mail provider answered 429 (rate limit)."** A few 429 are retried and the
   e-mail still goes out a second later. Only when retries run out it becomes a
   `failed` delivery, and then `NotificationEmailFastBurn` covers it.
