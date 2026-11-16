# Протокол відтворення «з нуля» · qa-autorate · Спринт 4

Аудитор-опонент: Куценко Дарʼя. Дата: 16.11.2026.

**Машина:** Windows 11, раніше проєкту на ній не було. Java і Maven окремо не встановлювала — запускала через IntelliJ IDEA 2026.3 (вбудований Maven).

**Правило:** README виконується буквально, без підказок автора. Усе, що довелося з'ясовувати поза README, записано в розділ 3.

## 1. Хід відтворення

| № | Крок за README | Очікувано | Фактично | Спрацювало? |
|---|---|---|---|---|
| 1 | `java -version`, `mvn -v` | Java 17, Maven 3.9 | `mvn`: «Имя "mvn" не распознано как имя командлета» (CommandNotFoundException). Команд у терміналі немає, Java 17 — через IntelliJ | ні |
| 2 | `git clone …` → `cd tzyapz-sprint-1/qa-autorate` | папка з `pom.xml` | Клонування через IntelliJ (Clone Repository); папка `qa-autorate` з `pom.xml` є | так |
| 3 | `mvn clean test` | `Tests run: 55, Failures: 7, Errors: 0`, BUILD FAILURE | У терміналі `mvn` не запустився. Через панель Maven IntelliJ (clean → test): `Tests run: 55, Failures: 7, Errors: 0, Skipped: 0`, BUILD FAILURE | так (через IntelliJ) |
| 4 | Звірити 7 падінь із таблицею README, розділ 3 | ті самі 6 тестів, BUG-014…BUG-019 | 7 падінь = 6 різних тестів (`search_blankQueryReturnsEmpty` падає двічі через параметризацію): ModelSearchTest ×2 (blankQuery, sortByRating), RatingAggregatorTest ×2 (average_roundsHalfUp, topN), VoteValidatorTest ×2 (boundaryRating, voteForAnotherModel). Збігається з таблицею README, розділ 3 (BUG-014…BUG-019) | так |
| 5 | `mvn test -DexcludedGroups=none -Dgroups=ui` | `Tests run: 11, Failures: 0` | `Tests run: 11, Failures: 0, Errors: 0, Skipped: 0`, BUILD SUCCESS, 1:54 хв | так |
| 6 | Повторити крок 5 | знову 11 / 0 (стабільність) | `Tests run: 11, Failures: 0, Errors: 0, Skipped: 0`, BUILD SUCCESS, 2:01 хв | так |
| 7 | `mvn test -DexcludedGroups=none -Dtest=HomePageTest#tc004_overallRatingOpensTable -Dheaded=true` | відкривається вікно Chrome, тест passed | `Tests run: 1, Failures: 0, Errors: 0`, BUILD SUCCESS, 12,98 с. Вікно Chrome відкрилось, тест пройшов на очах | так |
| 8 | Відкрити `target/surefire-reports/` | `*.txt` і `TEST-*.xml` для кожного класу | У `qa-autorate/target/surefire-reports/` є `*.txt` і `TEST-*.xml` для всіх 9 класів: 4 юніт (`ExampleTest`, `ModelSearchTest`, `RatingAggregatorTest`, `VoteValidatorTest`) і 5 UI (`HomePageTest`, `LoginTest`, `ModelPageTest`, `OverallRatingTest`, `RegistrationTest`) | так |

## 2. Простежуваність

| Перевірка | Як перевіряла | Результат |
|---|---|---|
| Кожен юніт-тест → BL-вимога | `@DisplayName` кожного методу починається з BL-ID; звірено з матрицею (27 рядків) | так — 27 методів ↔ 27 рядків матриці, BL-ID є в кожному `@DisplayName` |
| Кожен UI-тест → тест-кейс спринта 3 | `@DisplayName` починається з TC-ID; звірено з `docs/sprint3/test-cases.xlsx` (11 кейсів) | так — 11 методів ↔ 11 кейсів (TC-001…004, 006, 007, 015, 016, 021, 031, 040), усі є в test-cases.xlsx з тими самими вимогами |
| Кожен червоний тест → звіт | 6 тестів ↔ issues BUG-014…BUG-019 з міткою `module`; у звіті є тест, вхід, очікувано / фактично, severity | так — 6 червоних тестів ↔ issues #13–#18, над кожним тестом коментар із BUG-ID; у кожному звіті є тест, команда, виклик із даними, Actual / Expected, severity і priority |
| Кожен звіт відтворюється | команда зі Steps to reproduce дає саме цей Actual result (перевірено BUG-016 і BUG-019) | так — у прогоні кроку 3: BUG-016 → `отримано: [повторний голос]`, BUG-019 → `expected: <Ferrari F40> but was: <Pagani Huayra>`, як в Actual result звітів |
| Очікувані значення — з BL, а не з коду | BL-10 4,7; BL-20 поріг 5; BL-40 оцінка 5; BL-60 порожній запит; BL-70 спадання — звірено з SRS | так — очікувані значення в тестах збігаються з правилами BL, а не з кодом модуля: саме тому ці 6 тестів червоні на v1 |

## 3. Що довелося з'ясовувати поза README

1. README вимагає Java і Maven у терміналі (`mvn` не розпізнається — немає в PATH). Використала IntelliJ: вбудований Maven, панель Maven (Lifecycle `clean`, `test` та Execute Maven Goal).
2. Як запускати Maven-команди в IntelliJ (іконка «Execute Maven Goal» на панелі Maven), README не пояснює — довелося з'ясовувати поза README.
3. Під час UI-тестів у лозі повторюється WARNING про CDP для Chrome 157.0.8103.51 (потрібна залежність `selenium-devtools`). На результат тестів не вплинуло.

## 4. Слабкі місця

1. **Залежність від стану demo.** TC-015 і TC-016 залежать від облікового запису TD-01. Якщо власник demo очистить дані, ці тести впадуть не через дефект. Передумову описано в README, розділ 5, але автоматичного відновлення облікового запису немає.
2. **TC-002 без перевірки назви марки.** Тест перевіряє перехід на /make/ і непорожню таблицю, але не назву «Lamborghini». За F-10 популярна марка змінюється з голосами, тому жорстко прописана назва зробила б тест нестабільним. Ризик: якщо посилання вестиме на іншу марку, тест цього не помітить.
3. **Повільне demo.** Тайм-аут 30 с у кожному очікуванні: на повільній мережі UI-набір триває кілька хвилин.
4. **Прогін на GitHub ігнорує падіння** (`-Dmaven.test.failure.ignore=true`), щоб завжди зберегти звіти. Тому зелений значок Actions не означає «усі тести пройшли» — дивитися треба Summary.
5. **Selenium IDE — лише Firefox.** Сценарій TC-031 не записано в IDE: у Firefox сторінка моделі Diablo не завантажується. Регресію сторінки моделі в браузері Firefox набір не перевіряє (WebDriver-тести — лише Chrome).
6. **`ExampleTest` дублює частину перевірок** (BL-10, BL-40, BL-60). Це зразок із пакета модуля, у нормативі він не врахований.

## 5. Питання від імені замовника

1. Чому `mvn clean test` завершується BUILD FAILURE і чи можна з таким модулем іти у випуск?
2. Як ви доведете, що 4,7 у BUG-014 — вимога, а не ваше припущення? Де це в SRS?
3. Скільки голосів потрібно моделі для рейтингу тижня і як тест перевіряє саме межу?
4. Що станеться з UI-тестами, якщо demo очистить облікові записи?
5. Як у спринті 5 ви зрозумієте, що v2 щось виправила, а щось зламала?
