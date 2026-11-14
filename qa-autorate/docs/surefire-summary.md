# Протокол запуску автотестів · qa-autorate · Спринт 4

Склав: Сушинський Микола (Керівник тестування). Дата запуску: 14.11.2026.

**Середовище:**
- **Модуль:** `autorate-module` 1.0.0 (v1).
- **Збірка:** JDK 17 (Temurin), Maven 3.9, JUnit 5.10.2, Selenium 4.21.0, Surefire 3.2.5.
- **Браузер:** Chrome (headless).
- **Demo:** https://buggy.justtestit.org.

## 1. Юніт-тести — `mvn clean test`

Прогін на GitHub: Actions → «qa-autorate tests» **#16** (коміт `c84396b`, 12.11.2026), підсумок Surefire:

```
[ERROR] Tests run: 10, Failures: 3, Errors: 0, Skipped: 0, Time elapsed: 0.189 s <<< FAILURE! -- in ua.kpi.tzyapz.autorate.ModelSearchTest
[ERROR] Tests run: 18, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.053 s <<< FAILURE! -- in ua.kpi.tzyapz.autorate.VoteValidatorTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in ua.kpi.tzyapz.autorate.ExampleTest
[ERROR] Tests run: 22, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.089 s <<< FAILURE! -- in ua.kpi.tzyapz.autorate.RatingAggregatorTest
[ERROR] Tests run: 55, Failures: 7, Errors: 0, Skipped: 0
```

Локально без прапорця `-Dmaven.test.failure.ignore=true` ця сама команда завершується `BUILD FAILURE` (на GitHub прапорець увімкнено, щоб завжди зберігати звіти).

`Tests run` рахує кожен запуск параметризованого тесту окремо: 27 тест-методів команди + 3 методи `ExampleTest` дають 55 запусків.

**Чому BUILD FAILURE.** Усі 7 падінь — `Failures` (не пройшла перевірка), а не `Errors`, тобто тести виконалися. Кожне падіння відповідає закладеному дефекту модуля v1, зареєстрованому в трекері:

| Тест, що впав | Набір даних | Очікувано (з BL) | Фактично (v1) | Дефект |
|---|---|---|---|---|
| `RatingAggregatorTest.average_roundsHalfUpToOneDecimal` | — | 4.7 | 4.6 | BUG-014 (BL-10) |
| `RatingAggregatorTest.topN_includesOnlyModelsWithEnoughVotes` | [2] 4 голоси | false | true | BUG-015 (BL-20) |
| `VoteValidatorTest.validate_voteForAnotherModelAccepted` | — | порушень немає | [повторний голос] | BUG-016 (BL-30) |
| `VoteValidatorTest.validate_boundaryRatingAccepted` | [2] оцінка 5 | порушень немає | [оцінка поза межами 1..5] | BUG-017 (BL-40) |
| `ModelSearchTest.search_blankQueryReturnsEmpty` | [1] "", [2] "   " | порожній список | 4 моделі | BUG-018 (BL-60) |
| `ModelSearchTest.sortByRating_descendingThenByFullName` | — | Ferrari F40 першою | Pagani Huayra першою | BUG-019 (BL-70) |

Решта 48 запусків — passed. Тестів із `@Disabled` немає.

## 2. UI-тести — `mvn test -DexcludedGroups=none -Dgroups=ui`

Той самий прогін #16:

```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 15.82 s -- in ua.kpi.tzyapz.autorate.ui.OverallRatingTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 22.64 s -- in ua.kpi.tzyapz.autorate.ui.HomePageTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.409 s -- in ua.kpi.tzyapz.autorate.ui.ModelPageTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.352 s -- in ua.kpi.tzyapz.autorate.ui.LoginTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.972 s -- in ua.kpi.tzyapz.autorate.ui.RegistrationTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
```

**Як дійшли до 11 / 0.** Перші прогони UI (#13–#15, 12.11.2026) виявили дві причини падінь, обидві виправлено:
1. **Приховані дублікати елементів** на /overall і /register. Через них `visibilityOfElementLocated` чекав на прихований елемент; тепер чекаємо на перший видимий (TC-007, TC-015).
2. **Відомий дефект demo BUG-003.** Logout на /overall не завершує сеанс, тому крок Logout із TC-015 прибрано: це кейс TC-018.

Стабільність повторним запуском на чистій машині перевіряє аудитор (`docs/audit.md`).

## 3. Де лежать повні звіти

- **Локально:** `qa-autorate/target/surefire-reports/`:
  - `*.txt` — підсумок і трасування помилок по класу;
  - `TEST-*.xml` — усі тести з назвами `@DisplayName` і часом.
- **На GitHub:** Actions → «qa-autorate tests» → останній запуск → **Summary** (підсумок) і **Artifacts → surefire-reports**.
