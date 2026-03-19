
## 2026-03-19 08:34 FAIL round 1
- fix: calcTai now scores flower tiles (正花 +1台 per matching seat flower, 花槓 +2台 per full group, 八仙過海 +8台 for all 8)
- fix: dealer self-draw scoring — when dealer wins self-draw, all losers now correctly pay double
- fix: bot concurrency guard — prevent double draws/discards from overlapping async bot turns
- fix: fireSkip only clears lastDiscard when called by currentTurn player, preventing premature claim window closure
- fix: bot AI now values honor tile pairs/triplets instead of always discarding them (dragons/winds scored by count)
- feat: 搶槓胡 — when a player does 加槓, others get a 3s window to rob the kong and win
- feat: 計時器到期自動出牌 — 15s discard timer starts after drawing; auto-discards last tile on expiry
- feat: 一炮多響 — priority-based resolution (closer to discarder wins first), prevents race condition overwrites
- feat: calcTai add 大三元(+8台), 小三元(+4台), 碰碰胡(+4台) scoring patterns
- feat: bot can now chow (吃) tiles from the left player, with botChow function and canChow accepting claimerPid
- feat: 連莊 — dealer stays when they win or draw; rotates to next player otherwise; round number advances
- fix: clean up _checkSets dead code; add bot kong logic (暗槓/加槓) and botKong function
- feat: calcTai add 門風 (seat wind) and 圈風 (round wind) triplet scoring +1台 each
- fix: claimed tiles (pong/chow) now removed from discarder's pile in all 4 claim functions
- fix: discard win scoring now correctly doubles when dealer is involved (was ignoring dealerInvolved)
- feat: 暗槓 bonus scoring +1台 each in calcTai
- feat: win screen now shows score changes for all players after each game
