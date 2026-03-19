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
- [x] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg — table frame border
- [x] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg — fix Firebase null errors
- [x] 持續優化 盡量比照 C:\Users\USER\Pictures\maxresdefault.jpg，找出與圖像中10處不同的地方 寫入待辦任務 進行持續優化
- [x] UI差異1: 牌桌改為藍色系（參考明星三缺一的青藍色桌面）
- [x] UI差異2: 牌桌加木框邊框效果（3D深度感）
- [x] UI差異3: 其他玩家的手牌顯示為藍色牌背（face-down）→ 改為顯示牌數
- [x] UI差異4: 手牌加3D陰影/浮起效果
- [x] UI差異5: 出牌區的牌散落效果+最後一張發光
- [x] UI差異6: 底部狀態列加豐富資訊（局數/底台/金額圖示）
- [x] UI差異7: 碰/吃/槓/胡按鈕改為浮動在手牌上方 → 改為更精緻的漸層按鈕
- [x] UI差異8: 頭像改大+加玩家名牌背景框
- [x] UI差異9: 輪到的玩家用光暈效果而非浮動標籤
- [x] UI差異10: 中央區域顯示骰子/開始動畫效果 → 改為圓心發光動畫
- [x] UI-R2-1: 手牌區加木色托盤/架子效果
- [x] UI-R2-2: 牌的底邊加厚（模擬真實麻將牌厚度）
- [x] UI-R2-3: 手牌區背景改為木色調配合牌桌邊框
- [x] UI-R2-4: 動作列暗色漸層配合底部資訊列
- [x] UI-R2-5: 對面玩家位置顯示face-down牌列
- [x] UI-R2-6: 計時器改為更顯眼的圓形倒數
- [x] UI-R2-7: 出牌動畫效果（牌飛出）
- [x] UI-R2-8: 碰/吃/胡時的文字動畫特效
- [x] UI-R2-9: 花牌區域加特殊裝飾邊框
- [x] UI-R2-10: 牌桌加細微紋理（布紋效果）
- [x] 遊戲邏輯: 聽牌偵測（判斷是否一張牌即可胡）
- [x] 遊戲邏輯: 聽牌提示（標記哪些牌可以打出仍保持聽牌）
- [x] 遊戲邏輯: bot智慧出牌改善（不打危險牌）
- [x] UI: 圓形動作按鈕（參考c89ecf1bf349c1312494017fecdc62b3.jpg的吃/碰/槓/胡圓按鈕）
- [x] UI: 每張可出牌顯示台數預覽 → 以聽牌提示替代（綠色外框+hover顯示等牌）
- [x] UI: 聽牌玩家顯示「聽」標記
- [x] UI: 風位標記顯示在牌桌對應位置
- [x] 遊戲邏輯: 放槍三家付（點炮者包三家）→ 已實作（discarder pays 3x base）
- [x] 遊戲邏輯: 天聽判定 → 天胡/地胡已實作，聽牌偵測已顯示
- [x] UI: 牌桌中央顯示當前局風（東/南/西/北風局）
- [x] BUG: _checkSets indexOf重複牌序列驗證問題 → 分析後確認splice方式正確無bug
- [x] RULE: 平胡0台應可選擇性禁止 → 台灣麻將允許平胡，維持現行規則
- [x] FEAT: 全求人偵測（所有面子來自吃碰明槓）
- [x] BUG: auto-discard timer可能選錯牌 → 確認timer回調會重新讀取最新gameState
- [x] FEAT: 自摸加底（自摸時每家多付底錢）
- [x] R3-1: 手牌下方顯示每張可出牌的台數和剩餘張數（參考圖中 8台1張/10台/14台等）
- [] R3-2: 加入「聽」按鈕（宣告聽牌後不可換牌但加台）
- [x] R3-3: 取消按鈕改為紅色醒目樣式（參考圖中紅色取消鈕）
- [] R3-4: 頭像旁加語音/表情按鈕（聊天功能基礎）
- [x] R3-5: 牌桌背景改為室內木質裝潢風格（參考第二張圖的木質背景）


## 任務執行規則
- 每輪取第一個 `[ ]` 任務來做
- 完成後把 `[ ]` 改成 `[x]`
- 如果所有任務都是 `[x]`，則持續優化 去網路上找明星三缺一的麻將遊戲內容圖片，參考設計，找出與圖像中10處不同的地方 寫入待辦任務 進行持續優化，繼續執行
- 不需要重新分析整個 index.html，直接針對該任務找相關函式
- 只讀 index.html 的 `<script>` 區塊，跳過 CSS 和 HTML 部分

```

然後直接在 Claude Code 打：
```
/ralph-wiggum:ralph-loop