# Досьє якості v3 — «АвтоРейт». Спринт 3 «Тест-кейси та звіти про дефекти»

Команда ФЕ-41 · Дисципліна «Технології забезпечення якості програмних засобів»
Еталон: виправлена SRS 1.1 · Demo: https://buggy.justtestit.org · Дата здачі: 09.11.2026

**Інкремент:** Набір тест-кейсів + Журнал виконання + Реєстр дефектів.

## 1. Склад команди і ролі

| Роль | Виконавець | Артефакти |
|---|---|---|
| Керівник тестування | Баришполь Іван | README.md, зведення інкременту, демо |
| Тест-аналітик | Рзяніна Валерія | test-cases.xlsx (Test cases, Coverage) |
| Інженер з тестування | Потеряйло Вероніка | execution-log.xlsx, issues, attachments/ |
| Скрам-майстер / рецензент | Куценко Дарʼя | аркуш Review, позначки Reviewer, task-board.md, retro.md |
| Аудитор-опонент | Сушинський Микола | audit.md |

## 2. Зміст досьє v3

| Файл | Опис |
|---|---|
| `README.md` | Цей файл: зміст, трекер, статистика, покриття |
| `test-cases.xlsx` | Аркуші «Test cases» (51 кейс), «Coverage», «Review», «Test data» |
| `execution-log.xlsx` | Аркуші «Run 1» (журнал виконання) і «Environment» |
| `attachments/` | Знімки екрана дефектів, іменування за Bug ID (`BUG-007-1.png`) |
| `audit.md` | Відтворення трьох дефектів за текстом, перевірка DoD, питання до демо |
| `task-board.md` | Таблиця розподілу задач і журнал стендапів |
| `task-board.xlsx` | Повна таблиця Скрам-майстра: рецензії, статистика, DoD, план, ретро |
| `retro.md` | Протокол ретроспективи (Start / Stop / Continue) |

## 3. Трекер дефектів

GitHub Issues: [звіти спринта 3 (мітка `sprint:3`)](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=label%3A%22sprint%3A3%22)

- Звітів про дефекти: 12 — issues [#18](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/18)…[#29](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/29) (BUG-001…BUG-012); кожен із мітками `severity:*`, `priority:*`, `status:*`, `sprint:3`.
- Шаблон звіту: [`.github/ISSUE_TEMPLATE/bug_report.md`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/.github/ISSUE_TEMPLATE/bug_report.md).
- Знімки екрана: [`attachments/`](attachments/) — `BUG-001-1.png` … `BUG-012-1.png`, кожен згадано в розділі Attachments свого issue.
- Рецензія звітів: поле **Reviewer** у тексті кожного issue (Куценко Дарʼя, 06.11.2026); після рецензії мітка `status:new` замінена на `status:open`.

## 4. Статистика

Середовище: demo https://buggy.justtestit.org (стан 25–26.10.2026), Chrome 141.0 (інкогніто), Windows 11 Pro 23H2. Деталі — аркуш «Environment» у `execution-log.xlsx`.

### 4.1. Тест-кейси

| Усього | passed | failed | blocked | skipped |
|---|---|---|---|---|
| 51 | 34 | 12 | 3 | 2 |

Норматив для команди з 5 осіб: ≥ 25 кейсів. Усі 51 кейс мають статус `reviewed`.

- **failed (12):** TC-011, TC-012, TC-018, TC-023, TC-026, TC-027, TC-030, TC-034, TC-035, TC-036, TC-039, TC-043.
- **blocked (3):** TC-028, TC-029, TC-033 — блокує BUG-006 (P2).
- **skipped (2):** TC-050, TC-051 — вимоги F-150 і F-160 у SRS 1.1 позначені «потребує підтвердження замовником»; рішення Керівника тестування від 22.10.2026.

### 4.2. Дефекти за severity

Норматив: ≥ 10 звітів, щонайменше три різні рівні severity (фактично — 4 рівні: Critical, Major, Normal, Minor).

| Blocker | Critical | Major | Normal | Minor | Trivial | Усього |
|---|---|---|---|---|---|---|
| 0 | 1 | 4 | 5 | 2 | 0 | 12 |

### 4.3. Дефекти за priority

| P1 | P2 | P3 | Усього |
|---|---|---|---|
| 0 | 1 | 11 | 12 |

### 4.4. Реєстр дефектів

| Bug ID | Issue | Кейс | Вимога | Severity | Priority | Знімок |
|---|---|---|---|---|---|---|
| BUG-001 | [#18](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/18) | TC-011 | F-30 | Major | P3 | [BUG-001-1.png](attachments/BUG-001-1.png) |
| BUG-002 | [#19](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/19) | TC-012 | F-30 | Major | P3 | [BUG-002-1.png](attachments/BUG-002-1.png) |
| BUG-003 | [#20](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/20) | TC-018 | F-50 | Major | P3 | [BUG-003-1.png](attachments/BUG-003-1.png) |
| BUG-004 | [#21](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/21) | TC-023 | F-70 | Normal | P3 | [BUG-004-1.png](attachments/BUG-004-1.png) |
| BUG-005 | [#22](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/22) | TC-026 | F-80 | Minor | P3 | [BUG-005-1.png](attachments/BUG-005-1.png) |
| BUG-006 | [#23](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/23) | TC-027 | GUI-80 | Critical | P2 | [BUG-006-1.png](attachments/BUG-006-1.png) |
| BUG-007 | [#24](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/24) | TC-030 | F-90, BL-50 | Normal | P3 | [BUG-007-1.png](attachments/BUG-007-1.png) |
| BUG-008 | [#25](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/25) | TC-034 | F-140 | Normal | P3 | [BUG-008-1.png](attachments/BUG-008-1.png) |
| BUG-009 | [#26](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/26) | TC-035 | F-140 | Normal | P3 | [BUG-009-1.png](attachments/BUG-009-1.png) |
| BUG-010 | [#27](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/27) | TC-036 | F-140 | Minor | P3 | [BUG-010-1.png](attachments/BUG-010-1.png) |
| BUG-011 | [#28](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/28) | TC-039 | GUI-10 | Normal | P3 | [BUG-011-1.png](attachments/BUG-011-1.png) |
| BUG-012 | [#29](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/29) | TC-043 | F-110 | Major | P3 | [BUG-012-1.png](attachments/BUG-012-1.png) |

Усі 12 звітів пройшли рецензування за сімома правилами і трьома пунктами DoD (поле Reviewer: Куценко Дарʼя, 06.11.2026). Стан звітів у спринті 3: Open.

## 5. Покриття вимог SRS 1.1

Усього вимог 43; вилучено 1 (F-120); актуальних 42.
Покрито 29 (69 %); не покрито з причиною 13.
Вимоги F і GUI: покрито 24 з 25 (96 %); не покрито GUI-70 з причиною.

| Вимога | Кейси | Результат Run 1 / Bug ID |
|---|---|---|
| F-10 | TC-001, TC-002, TC-003, TC-004 | passed |
| F-20 | TC-005, TC-006 | passed |
| F-30 | TC-007 … TC-014 | TC-011 failed → BUG-001; TC-012 failed → BUG-002; решта passed |
| F-40 | TC-015 | passed |
| F-50 | TC-017, TC-018, TC-019 | TC-018 failed → BUG-003; решта passed |
| F-60 | TC-020, TC-033 | TC-033 blocked → BUG-006; TC-020 passed |
| F-70 | TC-021, TC-022, TC-023 | TC-023 failed → BUG-004; решта passed |
| F-80 | TC-025, TC-026 | TC-026 failed → BUG-005; TC-025 passed |
| F-90 | TC-028, TC-029, TC-030, TC-031 | TC-028, TC-029 blocked → BUG-006; TC-030 failed → BUG-007; TC-031 passed |
| F-100 | TC-032 | passed |
| F-110 | TC-042, TC-043 | TC-043 failed → BUG-012; TC-042 passed |
| F-120 | — | не покрито: вимогу вилучено в SRS 1.1 (дефект №19) |
| F-130 | TC-033 | blocked → BUG-006 |
| F-140 | TC-034, TC-035, TC-036 | failed → BUG-008, BUG-009, BUG-010 |
| F-150 | TC-050 | skipped (рішення КТ) |
| F-160 | TC-051 | skipped (рішення КТ) |
| GUI-10 | TC-038, TC-039 | TC-039 failed → BUG-011; TC-038 passed |
| GUI-20 | TC-040 | passed |
| GUI-30 | TC-016 | passed |
| GUI-40 | TC-041 | passed |
| GUI-45 | TC-044, TC-045 | passed |
| GUI-50 | TC-024 | passed |
| GUI-60 | TC-037 | passed |
| GUI-70 | — | не покрито: юзабіліті-сесія з 5 учасниками (план тестування, тип 3); перенесено на спринт 5 |
| GUI-80 | TC-027 | failed → BUG-006 |
| GUI-90 | TC-046 | passed |
| BL-10 | — | не покрито: правило не видно через інтерфейс demo (оцінок немає, BUG-006); юніт-тести спринта 4 |
| BL-20 | — | не покрито: topN і поріг 5 голосів не видно через інтерфейс; юніт-тести спринта 4 |
| BL-30 | TC-032 | passed |
| BL-40 | — | не покрито: вибору оцінки в demo немає (BUG-006); юніт-тести спринта 4 |
| BL-50 | TC-028, TC-030 | TC-028 blocked → BUG-006; TC-030 failed → BUG-007 |
| BL-60 | — | не покрито: пошуку в demo немає; юніт-тести спринта 4 |
| BL-70 | — | не покрито: метод модуля без інтерфейсу; юніт-тести спринта 4 |
| BL-80 | — | не покрито: стоп-слова у публічних коментарях порушують етику demo (розд. 5.4); юніт-тести спринта 4 |
| P-10 | TC-048 | passed |
| P-20 | — | не покрито: 95 % запитів вимагає серії вимірів, тобто навантаження на demo (заборонено, розд. 5.4) |
| S-10 | — | не покрито: 5 невдалих входів — спроба обійти захист і блокування спільного логіна (заборонено, розд. 5.4) |
| S-15 | TC-047 | passed |
| S-20 | — | не покрито: потребує 30 хв простою сеансу; перенесено на регресію спринта 5 |
| U-10 | — | не покрито: посилається на метрики GUI-70; перевіряється разом із GUI-70 у спринті 5 |
| U-20 | TC-049 | passed |
| D-10 | — | не покрито: шаблон довідки замовника не надано; вимога потребує підтвердження |
| D-20 | — | не покрито: README модуля перевіряється оглядом у спринті 4 |

## 6. Definition of Done

Виконано 9 з 9 пунктів (аркуш «DoD» у `task-board.xlsx`).

## 7. Зміни після рев'ю чернетки (05.11.2026)

| Зауваження рев'ю | Що зроблено | Хто, коли |
|---|---|---|
| Issues, шаблону і міток немає | Шаблон `.github/ISSUE_TEMPLATE/bug_report.md`, 16 міток, 12 issues [#18](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/18)…[#29](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues/29) за шаблоном 4.11 (звіти, складені під час Run 1 25–26.10.2026, внесено в трекер) | Потеряйло Вероніка, 06.11.2026 |
| Немає `attachments/` | 12 знімків, знятих під час повторного відтворення кожного дефекту за текстом issue | Потеряйло Вероніка, 06.11.2026 |
| Рецензія звітів не підтверджена | Кожен issue перевірено за 7 правилами + 3 пунктами DoD, у тексті — поле Reviewer, мітка `status:open` | Куценко Дарʼя, 06.11.2026 |
| Аудит не помітив відсутніх артефактів | Три відтворення повторено за фактичними issues, перевірку DoD — за посиланнями на артефакти | Сушинський Микола, 07.11.2026 |
| Неконкретні коментарі TC-018, TC-026 у журналі | Записано, що саме спостерігалося на кроці | Потеряйло Вероніка, 06.11.2026 |
| TC-001 прив'язаний до конкретної марки | Очікуване за правилом F-10, назви — довідка | Рзяніна Валерія, 06.11.2026 |
| Кореневий README описував неіснуючі файли | Розділ `docs/sprint3/` переписано за фактичною структурою | Баришполь Іван, 07.11.2026 |

## 8. Відомі обмеження

- F-150, F-160, D-10: вимоги потребують підтвердження замовником; запит буде надіслано в спринті 4.
- BUG-006 блокує TC-028, TC-029, TC-033; після виправлення їх потрібно виконати повторно (спринт 5).
- Публічне demo оновлюється і скидає дані: перед повторним виконанням звіряйте адреси й тексти елементів з екраном.
