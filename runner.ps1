param([int]$Max = 5)

Write-Host "=== Mahjong Auto-Improve Start ===" -ForegroundColor Yellow

# 確保 scripts 資料夾存在
New-Item -ItemType Directory -Force -Path scripts | Out-Null

for ($i = 1; $i -le $Max; $i++) {
    Write-Host ""
    Write-Host "--- Round $i ---" -ForegroundColor Cyan

    git add -A
    git stash

    @"
Read index.html carefully. Find the most critical bug or missing feature in the game logic (canWin, calcTai, bot AI, timer, etc).

Fix it by editing index.html directly. Rules:
- Only modify inside the <script type="module"> block
- Do NOT touch firebaseConfig, PLAYER_IDS, buildWall()
- Change at most 80 lines
- After editing, append one line to improvement-log.md describing what you fixed

When done, say DONE.
"@ | Out-File scripts\prompt.tmp.txt -Encoding utf8

    Get-Content scripts\prompt.tmp.txt | claude --print --dangerously-skip-permissions

    node test\game-logic.test.mjs
    if ($LASTEXITCODE -eq 0) {
        Write-Host "PASS - committing" -ForegroundColor Green
        $ts = Get-Date -Format "yyyy-MM-dd HH:mm"
        git add -A
        git commit -m "auto: round $i ($ts)"
    } else {
        Write-Host "FAIL - reverting" -ForegroundColor Red
        git stash pop
        $ts = Get-Date -Format "yyyy-MM-dd HH:mm"
        Add-Content improvement-log.md "`n## $ts FAIL round $i" -Encoding utf8
        git add improvement-log.md
        git commit -m "log: failed round $i"
    }

    Start-Sleep -Seconds 2
}

Write-Host ""
git log --oneline -10