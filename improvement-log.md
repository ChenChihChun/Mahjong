
## 2026-03-19 08:34 FAIL round 1
- fix: calcTai now scores flower tiles (正花 +1台 per matching seat flower, 花槓 +2台 per full group, 八仙過海 +8台 for all 8)
- fix: dealer self-draw scoring — when dealer wins self-draw, all losers now correctly pay double
- fix: bot concurrency guard — prevent double draws/discards from overlapping async bot turns
- fix: fireSkip only clears lastDiscard when called by currentTurn player, preventing premature claim window closure
