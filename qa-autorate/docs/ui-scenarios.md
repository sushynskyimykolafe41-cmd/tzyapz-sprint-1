# UI-сценарії для автоматизації · qa-autorate

Автор: Баришполь Іван (Тест-аналітик).
- **Demo:** https://buggy.justtestit.org.
- **Джерело:** тест-кейси спринта 3 (`docs/sprint3/test-cases.xlsx`).

## Як обрано сценарії

- **Пріоритет і повторюваність.** Усі обрані кейси мають пріоритет High і виконуються в кожному регресійному прогоні: головна, навігація, вхід, реєстрація, сторінка моделі.
- **Лише кейси, що пройшли в спринті 3.** Кейси зі статусом failed (BUG-002…BUG-013) не автоматизуємо: UI-набір має бути стабільно зеленим.
- **Без засмічення demo.** Сценарії не створюють облікових записів і не голосують: дані demo спільні (каталог продуктів, 7.8).
- **Тестові дані.** Обліковий запис для входу — TD-01: `tzfe41_user01` / `Autorate2026!`, First Name `Tester`. Логіни реєстраційних кейсів мають суфікс `_s4` і ніколи не реєструються.

## Правила локаторів

- **Пріоритет:** `id` / `name` → CSS за атрибутом (`a[href^='/make/']`) → відносний XPath за видимим текстом (`//button[normalize-space()='Login']`). Абсолютних XPath немає.
- **Перевірка унікальності:** кожен локатор перевірено в DevTools (Ctrl/Cmd + F у вкладці Elements → «1 of 1»).
- **Очікування:** лише явні — `WebDriverWait` + `ExpectedConditions`, тайм-аут 30 с (demo повільне). `Thread.sleep` і неявних очікувань немає.
- **TC-015 без кроку Logout.** Другий прогін (12.11) показав, що після Logout на /overall панель відвідувача не з'являється — це відомий дефект demo BUG-003 (issue #2, кейс TC-018 спринта 3). Кейси з дефектами в UI-набір не беремо, тому TC-015 автоматизовано до кроку 2 (вхід і привітання на /overall), а Logout на /overall лишається за TC-018.
- **Приховані дублікати.** Перший прогін (12.11) показав, що на /overall і /register у розмітці є приховані елементи з тими самими локаторами, тому `visibilityOfElementLocated` (перевіряє лише перший знайдений) падав за тайм-аутом. Для полів входу й повідомлення TC-007 використано умову-лямбду «перший видимий елемент» (`TopBar.visible`); текст повідомлення TC-007 зафіксовано на живій сторінці: «Passwords do not match».

## Сценарії

| TC | WebDriver-тест | Кроки | Очікування перед перевіркою | Перевірка | Локатори |
|---|---|---|---|---|---|
| TC-001 | HomePageTest#tc001_homeShowsThreeBlocks | `get(/)` | `visibilityOfElementLocated(css=a[href^='/make/'])` | текст сторінки містить «Popular Make», «Popular Model», «Overall Rating», «(N votes)», «List of all registered models.» | `By.cssSelector("a[href^='/make/']")`, `By.tagName("body")` |
| TC-002 | HomePageTest#tc002_popularMakeOpensMakePage | `get(/)` → клік на логотип Popular Make | `elementToBeClickable`, `urlContains("/make/")`, `numberOfElementsToBeMoreThan(table tbody tr, 0)` | адреса містить /make/, таблиця моделей непорожня | `a[href^='/make/']`, `table tbody tr` |
| TC-003 | HomePageTest#tc003_popularModelOpensModelPage | `get(/)` → клік на фото Popular Model | `urlContains("/model/")`, `textToBePresentInElementLocated(body, "Votes")` | адреса містить /model/; є «Specification» і «Votes» | `a[href^='/model/']` |
| TC-004 | HomePageTest#tc004_overallRatingOpensTable | `get(/)` → клік на зображення Overall Rating | `urlContains("/overall")`, `numberOfElementsToBeMoreThan(table tbody tr, 0)` | адреса = …/overall; заголовок таблиці містить Make, Model, Rank, Votes, Engine, Comments (assertAll) | `a[href='/overall']`, `table thead` |
| TC-006 | RegistrationTest#tc006_cancelReturnsHomeWithoutCreatingAccount | `get(/register)` → заповнити 5 полів (логін tzfe41_r06_s4) → Cancel → вхід цим логіном | `urlToBe(…/)`, `visibilityOfElementLocated(помилка входу)` | головна сторінка; вхід не виконано — є повідомлення про помилку, немає «Hi, …» | `id=username`, `id=firstName`, `id=lastName`, `id=password`, `id=confirmPassword`, `//a[normalize-space()='Cancel']` |
| TC-007 | RegistrationTest#tc007_passwordMismatchRejected | `get(/register)` → Password `Autorate2026!`, Confirm `Autorate2026?` | умова-лямбда: перший **видимий** елемент із текстом «Passwords do not match» | текст = «Passwords do not match»; Register неактивна; видимого повідомлення про успіх немає (assertAll) | `//*[contains(normalize-space(text()),'Passwords do not match')]`, `//form//button[normalize-space()='Register']`, `.alert-success` |
| TC-015 | OverallRatingTest#tc015_loginKeepsUserOnOverallPage | `get(/overall)` → дочекатися таблиці → вхід TD-01 (крок 3 Logout не автоматизовано — BUG-003) | `numberOfElementsToBeMoreThan(table tbody tr, 0)`; поля входу — перший **видимий** елемент (`TopBar.visible`) | «Hi, Tester»; адреса закінчується на /overall; є Profile | `name=login`, `(//input[@name='password'])[1]`, `//button[normalize-space()='Login']`, `linkText=Logout` |
| TC-016 | LoginTest#tc016_wrongPasswordShowsErrorThenValidLoginClearsIt | `get(/)` → TD-01 з паролем Wrong2026! → TD-01 з вірним паролем → Logout | `visibilityOfElementLocated(помилка)`, `invisibilityOfElementLocated(помилка)` | є «Invalid username/password», немає «Hi, …»; після вірного входу помилка зникла, «Hi, Tester» | `//*[contains(normalize-space(text()),'Invalid username/password')]` |
| TC-021 | OverallRatingTest#tc021_firstPageSortedByRankAscending | `get(/overall)` | `numberOfElementsToBeMoreThan(table tbody tr, 0)` | є стовпець Rank; перший рядок — 1; ранги строго зростають | `table thead th`, `table tbody tr`, `td` |
| TC-031 | ModelPageTest#tc031_visitorSeesLoginRequiredInsteadOfVoteForm | `get(/)` → клік на фото Popular Model | `visibilityOfElementLocated(текст «You need to be logged in to vote.»)` | є текст; немає поля Comment і кнопки Vote! (assertAll) | `id=comment`, `//button[normalize-space()='Vote!']` |
| TC-040 | RegistrationTest#tc040_registerDisabledUntilAllFieldsFilled | `get(/register)` → 4 поля → 5-те поле | `elementToBeClickable(Register)` після 5-го поля | порожня форма і 4 поля — Register неактивна; 5 полів — активна | `//form//button[normalize-space()='Register']`, `id=confirmPassword` |

## Selenium IDE

- **Записано:** TC-004 (`selenium-ide/autorate.side`), відтворено двічі — passed. Експорт у Java — `selenium-ide/export/`; адаптована версія — `HomePageTest#tc004_overallRatingOpensTable`.
- **TC-031 у Selenium IDE не записано.** Selenium IDE зараз доступний лише як доповнення Firefox (у Chrome Web Store його прибрано). У Firefox сторінка моделі Lamborghini Diablo не завантажується — нескінченний індикатор, також із вимкненим захистом від стеження; у Chrome та сама сторінка відкривається за ~4 с. Тому TC-031 автоматизовано лише WebDriver-тестом у Chrome (`ModelPageTest`). Норматив 1–2 сценарії IDE виконано (1).
