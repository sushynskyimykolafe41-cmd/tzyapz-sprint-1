# qa-autorate — автотести продукту «АвтоРейт» (Спринт 4, команда ФЕ-41)

Репозиторій автотестів модуля бізнес-логіки `autorate-module` v1 (пакет `ua.kpi.tzyapz.autorate`) і ключових
сценаріїв demo-застосунку [Buggy Cars Rating](https://buggy.justtestit.org).

- **Юніт-тести:** JUnit 5 для трьох класів модуля.
- **UI-тести:** Selenium WebDriver 4 за тест-кейсами спринта 3.
- **Джерело очікуваних значень:** розділ BL виправленої SRS «АвтоРейт».

| Що | Де |
|---|---|
| Трекер (дефекти demo і модуля) | [Issues](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues), дефекти модуля — мітка [`module`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=label%3Amodule) |

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

## 3. Структура

```
qa-autorate/
├── README.md
├── .gitignore
├── pom.xml                                   # JDK 17, JUnit 5.10.2, Selenium 4.21.0, Surefire 3.2.5
├── docs/                                     # матриця, UI-сценарії, протокол, досьє, аудит, задачі
├── selenium-ide/
│   ├── autorate.side                         # проєкт Selenium IDE: TC-004, TC-031
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

## 4. Автори

| Учасник | Роль у спринті 4 | Що написав |
|---|---|---|
| Рзяніна Валерія | Інженер з тестування | інфраструктура, `VoteValidatorTest` (BL-30, BL-80), UI-тести `ui/*`, Selenium IDE |
| Сушинський Микола | Керівник тестування | `RatingAggregatorTest` (BL-20), README, протокол запуску, розділ досьє |
