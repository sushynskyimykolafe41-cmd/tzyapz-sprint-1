# Матриця покриття «BL-вимога → правило → тести» · qa-autorate

Автор: Баришполь Іван (Тест-аналітик).
- **Джерело правил:** розділ BL виправленої SRS «АвтоРейт».
- **Модуль:** `autorate-module` v1.
- **Ключ стовпця «Результат v1»:** `mvn clean test`.

Для кожного правила є тест на нормальний випадок, тести на межі (значення на межі й поруч із нею з обох боків) і на заборонений вхід. Параметризований метод рахується як один тест; у дужках — кількість запусків.

## 1. Юніт-тести модуля

| BL | Правило | Дані / межі | Тест (клас#метод) | Автор | Результат v1 | Дефект | Рецензент |
|---|---|---|---|---|---|---|---|
| BL-10 | рейтинг = Σ(оцінка×репутація) / Σ(репутацій) | 4 пари голосів з різними вагами; результати без округлювальних неоднозначностей | [`RatingAggregatorTest#average_isWeightedByReputation`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) (4) | Баришполь І. | passed | — | — |
| BL-10 | округлення до 0,1 за HALF_UP | 14/3 = 4,666… → 4,7 (межа округлення) | [`RatingAggregatorTest#average_roundsHalfUpToOneDecimal`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) | Баришполь І. | **failed** | [BUG-014](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-014) | — |
| BL-10 | один голос — рейтинг = його оцінка | (1,3), (5,1), (3,2): межі оцінки і репутації | [`RatingAggregatorTest#average_singleVoteEqualsItsRating`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) (3) | Баришполь І. | passed | — | — |
| BL-10 | порожній список → 0,0 | 0 голосів | [`RatingAggregatorTest#average_emptyVotesReturnsZero`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) | Баришполь І. | passed | — | — |
| BL-10 | оцінка поза 1..5, репутація поза 1..3 → IllegalArgumentException | оцінка 0 / 6, репутація 0 / 4 (за межами з обох боків) | [`RatingAggregatorTest#average_outOfRangeVoteThrows`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) (4) | Баришполь І. | passed | — | — |
| BL-20 | Top-N за рейтингом за спаданням, не більше n | 4 моделі, n = 2 | [`RatingAggregatorTest#topN_returnsBestModelsByRatingDescending`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) | Сушинський М. | passed | — | — |
| BL-20 | рівний рейтинг → за повною назвою за алфавітом | 3 моделі з рейтингом 4,5 | [`RatingAggregatorTest#topN_equalRatingsOrderedByFullName`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) | Сушинський М. | passed | — | — |
| BL-20 | у Top-N лише моделі з ≥ 5 голосами | 3 / 4 / 5 / 6 голосів (межа 4 | 5) | [`RatingAggregatorTest#topN_includesOnlyModelsWithEnoughVotes`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) (4) | Сушинський М. | **failed** | [BUG-015](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-015) | — |
| BL-20 | n ≤ 0 → порожній список | n = 0, n = −1 | [`RatingAggregatorTest#topN_nonPositiveSizeReturnsEmptyList`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) (2) | Сушинський М. | passed | — | — |
| BL-20 | n більше за кількість придатних → усі придатні | 2 моделі, n = 10 | [`RatingAggregatorTest#topN_sizeLargerThanEligibleReturnsAllEligible`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/RatingAggregatorTest.java) | Сушинський М. | passed | — | — |
| BL-30 | повторний голос за ту саму модель → «повторний голос» | u1→m1 уже є; u1→m1 ще раз | [`VoteValidatorTest#validate_secondVoteForSameModelRejected`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Рзяніна В. | passed | — | — |
| BL-30 | голос того самого користувача за іншу модель приймається | u1→m1 уже є; u1→m2 | [`VoteValidatorTest#validate_voteForAnotherModelAccepted`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Рзяніна В. | **failed** | [BUG-016](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-016) | — |
| BL-30 | голос іншого користувача за ту саму модель приймається | u1→m1 уже є; u2→m1 | [`VoteValidatorTest#validate_otherUserSameModelAccepted`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Рзяніна В. | passed | — | — |
| BL-40 | оцінки на межах 1 і 5 приймаються | 1, 5 (межі діапазону) | [`VoteValidatorTest#validate_boundaryRatingAccepted`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) (2) | Потеряйло В. | **failed** | [BUG-017](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-017) | — |
| BL-40 | оцінка поза 1..5 → «оцінка поза межами 1..5» | 0, 6 (за межами з обох боків) | [`VoteValidatorTest#validate_outOfRangeRatingRejected`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) (2) | Потеряйло В. | passed | — | — |
| BL-50 | коментар ≤ 200 символів приймається | 200 символів (межа) | [`VoteValidatorTest#validate_comment200CharsAccepted`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Потеряйло В. | passed | — | — |
| BL-50 | коментар > 200 → «коментар задовгий» | 201 символ (межа + 1) | [`VoteValidatorTest#validate_comment201CharsRejected`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Потеряйло В. | passed | — | — |
| BL-50 | порожній коментар і null дозволені | "" і null | [`VoteValidatorTest#validate_emptyOrNullCommentAccepted`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Потеряйло В. | passed | — | — |
| BL-80 | стоп-слово окремим словом у будь-якому регістрі → «недопустиме слово» | spam, SPAM!, Казино, casino з розділовими знаками | [`VoteValidatorTest#validate_stopWordRejected`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) (4) | Рзяніна В. | passed | — | — |
| BL-80 | стоп-слово як частина слова — не порушення | spammer, scampi, casinos | [`VoteValidatorTest#containsStopWord_partOfWordIgnored`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) (3) | Рзяніна В. | passed | — | — |
| BL-80 | власний стоп-список; null → IllegalArgumentException | список [Lada]; null | [`VoteValidatorTest#customStopList_replacesDefaultAndRejectsNull`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/VoteValidatorTest.java) | Рзяніна В. | passed | — | — |
| BL-60 | пошук за префіксом марки або моделі без урахування регістру | lam, LAN, f4, hUaY | [`ModelSearchTest#search_prefixIgnoresCase`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ModelSearchTest.java) (4) | Куценко Д. | passed | — | — |
| BL-60 | кілька збігів — у порядку каталогу | префікс «la» → 2 марки | [`ModelSearchTest#search_commonPrefixReturnsAllMatchesInCatalogOrder`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ModelSearchTest.java) | Куценко Д. | passed | — | — |
| BL-60 | збіг усередині назви — не префікс | «blo» (у Diablo) | [`ModelSearchTest#search_substringIsNotPrefix`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ModelSearchTest.java) | Куценко Д. | passed | — | — |
| BL-60 | порожній або пробільний запит → порожній результат | "", "   " | [`ModelSearchTest#search_blankQueryReturnsEmpty`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ModelSearchTest.java) (2) | Куценко Д. | **failed** | [BUG-018](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-018) | — |
| BL-70 | sortByRating — за спаданням, рівні — за повною назвою | 4 моделі, дві з рейтингом 4,6 | [`ModelSearchTest#sortByRating_descendingThenByFullName`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ModelSearchTest.java) | Куценко Д. | **failed** | [BUG-019](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/issues?q=is%3Aissue+BUG-019) | — |
| BL-70 | новий список; вхідний не змінюється | копія каталогу до і після виклику | [`ModelSearchTest#sortByRating_returnsNewListAndKeepsInput`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ModelSearchTest.java) | Куценко Д. | passed | — | — |

Поза нормативом команди: [`ExampleTest`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ExampleTest.java) — зразок із пакета модуля (BL-10, BL-40, BL-60; 3 методи, 5 запусків), не змінювався; v1 — passed.

## 2. UI-тести: тест-кейс спринта 3 → WebDriver-тест

Усі тести — у пакеті `ui`, тег `@Tag("ui")`, `@DisplayName` починається з ID тест-кейсу. Кроки й локатори — [ui-scenarios.md](ui-scenarios.md).

| TC | Вимога | Що перевіряє | WebDriver-тест | Автор | Результат | Рецензент |
|---|---|---|---|---|---|---|
| TC-001 | F-10 | головна показує три блоки і «(N votes)» | [`HomePageTest#tc001_homeShowsThreeBlocks`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/HomePageTest.java) | Рзяніна В. | passed | — |
| TC-002 | F-10 | Popular Make → сторінка марки з таблицею | [`HomePageTest#tc002_popularMakeOpensMakePage`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/HomePageTest.java) | Рзяніна В. | passed | — |
| TC-003 | F-10 | Popular Model → сторінка моделі (Specification, Votes) | [`HomePageTest#tc003_popularModelOpensModelPage`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/HomePageTest.java) | Рзяніна В. | passed | — |
| TC-004 | F-10 | Overall Rating → /overall, 6 стовпців (записано в Selenium IDE) | [`HomePageTest#tc004_overallRatingOpensTable`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/HomePageTest.java) | Рзяніна В. | passed | — |
| TC-006 | F-20 | Cancel → головна, обліковий запис не створено | [`RegistrationTest#tc006_cancelReturnsHomeWithoutCreatingAccount`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/RegistrationTest.java) | Рзяніна В. | passed | — |
| TC-007 | F-30 | незбіг паролів → повідомлення, реєстрації немає | [`RegistrationTest#tc007_passwordMismatchRejected`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/RegistrationTest.java) | Рзяніна В. | passed | — |
| TC-015 | F-40 | вхід на /overall → «Hi, Tester», лишається на /overall | [`OverallRatingTest#tc015_loginKeepsUserOnOverallPage`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/OverallRatingTest.java) | Рзяніна В. | passed | — |
| TC-016 | GUI-30 | невірний пароль → помилка; вірний → помилка зникає | [`LoginTest#tc016_wrongPasswordShowsErrorThenValidLoginClearsIt`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/LoginTest.java) | Рзяніна В. | passed | — |
| TC-021 | F-70 | ранги першої сторінки зростають від 1 | [`OverallRatingTest#tc021_firstPageSortedByRankAscending`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/OverallRatingTest.java) | Рзяніна В. | passed | — |
| TC-031 | F-90 | відвідувач бачить «You need to be logged in to vote.» | [`ModelPageTest#tc031_visitorSeesLoginRequiredInsteadOfVoteForm`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/ModelPageTest.java) | Рзяніна В. | passed | — |
| TC-040 | GUI-20 | Register неактивна, доки не заповнено 5 полів | [`RegistrationTest#tc040_registerDisabledUntilAllFieldsFilled`](https://github.com/sushynskyimykolafe41-cmd/tzyapz-sprint-1/blob/main/qa-autorate/src/test/java/ua/kpi/tzyapz/autorate/ui/RegistrationTest.java) | Рзяніна В. | passed | — |

## 3. Норматив команди з 5 осіб

| Норматив | Потрібно | План | Факт |
|---|---|---|---|
| Юніт-тести JUnit 5 (параметризований = 1) | ≥ 20 | 27 | 27 (50 запусків) |
| UI-сценарії WebDriver | ≥ 8 | 11 | 11 |
| Дефекти модуля (звіт + тест) | ≥ 5 | 6 | 6: BUG-014…BUG-019 |
| Сценарії Selenium IDE | 1–2 | 2 | 1: TC-004 (TC-031 — обмеження Firefox, див. ui-scenarios.md) |
| Юніт-тести на кожного | ≥ 3 | 5–6 | див. розділ 4 |

## 4. Розподіл юніт-тестів

| Учасник | Клас | Правила | Тестів |
|---|---|---|---|
| Баришполь Іван | RatingAggregatorTest | BL-10 | 5 |
| Сушинський Микола | RatingAggregatorTest | BL-20 | 5 |
| Потеряйло Вероніка | VoteValidatorTest | BL-40, BL-50 | 5 |
| Рзяніна Валерія | VoteValidatorTest | BL-30, BL-80 | 6 |
| Куценко Дарʼя | ModelSearchTest | BL-60, BL-70 | 6 |
| **Разом** | | BL-10…BL-80 (8 правил) | **27** |
