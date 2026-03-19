# 麻將 PK — Auto-Improve Loop

## 每輪執行步驟
1. 讀 improvement-log.md 了解上輪做了什麼
2. 分析 index.html 找最重要的 bug 或缺失功能
3. 修它（只動 <script> 區塊）
4. 執行 `node test/game-logic.test.mjs`
5. 測試過 → `git add -A && git commit -m "auto: 描述"`，append 一行到 improvement-log.md
6. 測試失敗 → 還原修改，記錄失敗原因到 improvement-log.md

## 禁止觸碰
- firebaseConfig
- PLAYER_IDS
- buildWall()

## 優先修復順序
1. canWin / _checkSets bug
2. Bot AI 邏輯（btn-add-bot 有 UI 但沒實作）
3. calcTai 台數計算完整性
4. 計時器到期自動出牌

## 待辦任務（依序執行）
- [x] 實作 bot AI 出牌邏輯
- [x] 搶槓胡判斷
- [x] 計時器到期自動出牌
- [x] 一炮多響處理
- [x] calcTai 缺少碰碰胡/大三元/小三元
- [x] bot 吃牌邏輯（目前 bot 不會吃）
- [x] 連莊（dealer 胡牌後繼續當莊）
- [x] bot AI 智慧碰牌（不盲目碰，保留門清機會）
- [x] 補花後摸牌的嶺上開花台數
- [x] 槓上開花台數（槓後摸到胡牌 +1台）
- [x] 介面改善 畫面很醜 搭配playwright進行持續優化
- [x] 介面仍然有待加強 而且方位與明星三缺一差距太多  要可以適應各種手機型號大小 (HTC、三星、Pixel、等等各大廠牌都要測試)
- [x]  房間代碼畫面 與橫式作業不符，除了可以設定名稱外 還可以設定初始金額1000(預設)、底50(預設)、台20(預設) 等待秒數，0表示無設限，最多60秒，碰的等待秒數(預設5秒) 搭配playwright進行持續優化
- [x] 整體遊戲畫面 人物坐位 要比照明星三缺一 C:\Users\USER\Pictures\maxresdefault.jpg
- [x] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg — bottom info bar, table color
- [] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg
- [] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg
- [] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg
- [] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg

## 任務執行規則
- 每輪取第一個 `[ ]` 任務來做
- 完成後把 `[ ]` 改成 `[x]`
- 如果所有任務都是 `[x]`，則自動分析 index.html
  找下100個最值得改善的功能加到清單，繼續執行
- 不需要重新分析整個 index.html，直接針對該任務找相關函式
- 只讀 index.html 的 `<script>` 區塊，跳過 CSS 和 HTML 部分

```

然後直接在 Claude Code 打：
```
/ralph-wiggum:ralph-loop