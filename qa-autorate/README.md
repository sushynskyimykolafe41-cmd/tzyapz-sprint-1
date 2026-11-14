# qa-autorate — автотести продукту «АвтоРейт» (Спринт 4, команда ФЕ-41)

Репозиторій автотестів модуля бізнес-логіки `autorate-module` v1 (пакет `ua.kpi.tzyapz.autorate`) і ключових
сценаріїв demo-застосунку [Buggy Cars Rating](https://buggy.justtestit.org).

- **Юніт-тести:** JUnit 5 для трьох класів модуля.
- **UI-тести:** Selenium WebDriver 4 за тест-кейсами спринта 3.
- **Джерело очікуваних значень:** розділ BL виправленої SRS «АвтоРейт».

| Що | Де |
|---|---|
| Трекер (дефекти demo і модуля) | [Issues](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues), дефекти модуля — мітка [`module`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=label%3Amodule) |
| Матриця покриття BL → тести | [docs/coverage-matrix.md](docs/coverage-matrix.md) |
| UI-сценарії з локаторами | [docs/ui-scenarios.md](docs/ui-scenarios.md) |
| Протокол запуску (Surefire) | [docs/surefire-summary.md](docs/surefire-summary.md) |
| Розділ досьє «Автоматизація» | [docs/automation.md](docs/automation.md) |
| Протокол відтворення «з нуля» | [docs/audit.md](docs/audit.md) |
| Таблиця задач і стендапи | [docs/task-board.md](docs/task-board.md) |

## 1. Що потрібно на комп'ютері

| Що | Версія | Перевірка |
|---|---|---|
| JDK (Eclipse Temurin) | 17 | `java -version` → `openjdk version "17.…"` |
| Apache Maven | 3.9 | `mvn -v` → `Java version: 17.…` |
| Google Chrome | актуальна | лише для UI-тестів; драйвер завантажує Selenium Manager |
| Git | будь-яка | `git --version` |

Інтернет потрібен під час першого збирання (Maven завантажує залежності) і для UI-тестів (вони відкривають demo).

## 2. Як зібрати і запустити

```bash
git clone https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1.git
cd tzyapz-sprint-1/qa-autorate

# юніт-тести (UI-тести з тегом "ui" за замовчуванням виключено)
mvn clean test

# лише UI-тести (headless Chrome)
mvn test -DexcludedGroups=none -Dgroups=ui

# усі тести: юніт + UI
mvn test -DexcludedGroups=none

# один клас або один метод
mvn test -Dtest=VoteValidatorTest
mvn test -Dtest=VoteValidatorTest#validate_voteForAnotherModelAccepted

# один UI-тест із видимим вікном браузера (видно, що робить тест)
mvn test -DexcludedGroups=none -Dtest=HomePageTest#tc004_overallRatingOpensTable -Dheaded=true
```

Якщо `mvn -v` показує Java не 17:
- macOS: `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`;
- Windows PowerShell: `$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17…"` (точна назва папки — у `C:\Program Files\Eclipse Adoptium`).

## 3. Очікуваний результат

`mvn clean test` для модуля v1 завершується **BUILD FAILURE** — так і має бути:

```
Tests run: 55, Failures: 7, Errors: 0, Skipped: 0
```

**Чому 7 падінь.** 6 закладених дефектів модуля v1 зафіксовано червоними тестами. Дефект BUG-018 падає у двох запусках параметризованого тесту: `""` і `"   "`.

**Чому тести не вимикаємо.** На кожен червоний тест є звіт у трекері з міткою `module`. «Підганяти» очікувані значення під код і вимикати такі тести заборонено. У спринті 5 ці самі тести перевірять виправлення у v2.

| Червоний тест | Вимога | Дефект |
|---|---|---|
| `RatingAggregatorTest#average_roundsHalfUpToOneDecimal` | BL-10 | [BUG-014](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-014) |
| `RatingAggregatorTest#topN_includesOnlyModelsWithEnoughVotes` [4, false] | BL-20 | [BUG-015](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-015) |
| `VoteValidatorTest#validate_voteForAnotherModelAccepted` | BL-30 | [BUG-016](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-016) |
| `VoteValidatorTest#validate_boundaryRatingAccepted` [5] | BL-40 | [BUG-017](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-017) |
| `ModelSearchTest#search_blankQueryReturnsEmpty` [""], ["   "] | BL-60 | [BUG-018](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-018) |
| `ModelSearchTest#sortByRating_descendingThenByFullName` | BL-70 | [BUG-019](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-019) |

UI-набір (`-Dgroups=ui`) — 11 тестів, усі мають проходити: `Tests run: 11, Failures: 0`.

Звіти Surefire лежать у `target/surefire-reports/`:
- `*.txt` — підсумок по класу;
- `TEST-*.xml` — усі тести з назвами `@DisplayName` і часом.

Прогін на GitHub (Actions → «qa-autorate tests») запускається після кожного коміту в `qa-autorate/`: підсумок — у Summary, звіти — в Artifacts.

## 4. Структура

```
qa-autorate/
├── README.md
├── .gitignore
├── pom.xml                                   # JDK 17, JUnit 5.10.2, Selenium 4.21.0, Surefire 3.2.5
├── docs/                                     # матриця, UI-сценарії, протокол, досьє, аудит, задачі
├── selenium-ide/
│   ├── autorate.side                         # проєкт Selenium IDE: TC-004
│   └── export/                               # експорт IDE (JUnit 4) — чернетка, не збирається
└── src/
    ├── main/java/ua/kpi/tzyapz/autorate/     # модуль v1: RatingAggregator, VoteValidator, ModelSearch, Vote, CarModel
    └── test/java/ua/kpi/tzyapz/autorate/
        ├── RatingAggregatorTest.java         # BL-10, BL-20
        ├── VoteValidatorTest.java            # BL-30, BL-40, BL-50, BL-80
        ├── ModelSearchTest.java              # BL-60, BL-70
        ├── ExampleTest.java                  # зразок із пакета модуля (без змін)
        └── ui/                               # WebDriver-тести (@Tag("ui")) + Page Object TopBar
```

## 5. Передумови UI-тестів

- **Обліковий запис.** TC-015 і TC-016 входять обліковим записом **TD-01**: `tzfe41_user01` / `Autorate2026!`, First Name `Tester`.
  - Власник demo може очистити дані. Якщо вхід не вдається, зареєструйте цей обліковий запис на https://buggy.justtestit.org/register: Last Name `Autorate`, решта — як вище.
- **Реєстраційні тести** (TC-006, TC-007, TC-040) обліковий запис **не створюють**, базу demo не засмічують.
- **Повільне demo.** Таблиці завантажуються до 30 с, тому в усіх очікуваннях тайм-аут 30 с.

## 6. Автори

| Учасник | Роль у спринті 4 | Що написав |
|---|---|---|
| Рзяніна Валерія | Інженер з тестування | інфраструктура, `VoteValidatorTest` (BL-30, BL-80), UI-тести `ui/*`, Selenium IDE |
| Сушинський Микола | Керівник тестування | `RatingAggregatorTest` (BL-20), README, протокол запуску, розділ досьє |
| Баришполь Іван | Тест-аналітик | `RatingAggregatorTest` (BL-10), матриця покриття, UI-сценарії |
| Куценко Дарʼя | Аудитор-опонент | `ModelSearchTest` (BL-60, BL-70), протокол відтворення «з нуля» |
| Потеряйло Вероніка | Скрам-майстер / рецензент | `VoteValidatorTest` (BL-40, BL-50), code review, таблиця задач, ретро |

Внесок кожного видно в історії комітів (`git log --format='%an %s' -- qa-autorate`).
