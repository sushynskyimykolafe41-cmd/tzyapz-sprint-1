package ua.kpi.tzyapz.autorate.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Спільний життєвий цикл UI-тестів: новий браузер на кожен тест, закриття після тесту (навіть після падіння).
 * Не є тест-класом (ім'я не закінчується на Test) — Surefire його не запускає.
 *
 * Запуск UI-набору:            mvn test -DexcludedGroups=none -Dgroups=ui
 * Запуск із видимим браузером: mvn test -DexcludedGroups=none -Dgroups=ui -Dheaded=true
 */
abstract class UiTestBase {

    /** Поточна версія продукту — demo Buggy Cars Rating. */
    static final String BASE_URL = "https://buggy.justtestit.org";

    /** TD-01 зі спринта 3. Якщо власник demo очистив дані — зареєструвати заново (README, розділ 5). */
    static final String LOGIN = "tzfe41_user01";
    static final String PASSWORD = "Autorate2026!";
    static final String FIRST_NAME = "Tester";

    /** Demo відповідає повільно (таблиці — до 30 с), тому тайм-аут явних очікувань збільшено. */
    static final Duration TIMEOUT = Duration.ofSeconds(30);

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (!Boolean.getBoolean("headed")) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1280,900");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Відкриває сторінку demo за відносним шляхом, наприклад "/overall". */
    protected void open(String path) {
        driver.get(BASE_URL + path);
    }
}
