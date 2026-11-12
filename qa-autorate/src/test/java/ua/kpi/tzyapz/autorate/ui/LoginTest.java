package ua.kpi.tzyapz.autorate.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тест входу (GUI-30, F-40, F-50) за тест-кейсом спринта 3 TC-016. Автор: Рзяніна Валерія.
 */
@Tag("ui")
@DisplayName("UI · Вхід у верхній панелі (GUI-30, F-40, F-50)")
class LoginTest extends UiTestBase {

    @Test
    @DisplayName("TC-016: невірний пароль — повідомлення біля форми; після вірного входу воно зникає")
    void tc016_wrongPasswordShowsErrorThenValidLoginClearsIt() {
        open("/");
        TopBar topBar = new TopBar(driver, wait);

        // крок 2: невірний пароль — жорстка перевірка: без повідомлення далі перевіряти немає сенсу
        topBar.loginAs(LOGIN, "Wrong2026!");
        String error = topBar.waitForLoginError();
        assertFalse(error.isBlank(), "текст помилки порожній");
        assertFalse(topBar.isGreetingShown(), "після невірного пароля користувач увійшов");

        // крок 3: вірний пароль у ті самі поля (спершу очищаємо)
        driver.findElement(TopBar.LOGIN_FIELD).clear();
        driver.findElement(TopBar.PASSWORD_FIELD).clear();
        topBar.loginAs(LOGIN, PASSWORD);
        String greeting = topBar.waitForGreeting();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(TopBar.LOGIN_ERROR));
        assertEquals("Hi, " + FIRST_NAME, greeting.trim());

        // крок 4: вихід
        topBar.logout();
        assertTrue(topBar.waitForVisitorPanel());
    }
}
