package ua.kpi.tzyapz.autorate.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тести сторінки реєстрації (F-20, F-30, GUI-20) за тест-кейсами спринта 3 TC-006, TC-007, TC-040.
 * Жоден тест не створює обліковий запис — базу demo не засмічуємо (каталог продуктів, 7.8).
 * Автор: Рзяніна Валерія.
 */
@Tag("ui")
@DisplayName("UI · Реєстрація (F-20, F-30, GUI-20)")
class RegistrationTest extends UiTestBase {

    private static final By USERNAME = By.id("username");
    private static final By FIRST_NAME_FIELD = By.id("firstName");
    private static final By LAST_NAME = By.id("lastName");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By CONFIRM_PASSWORD = By.id("confirmPassword");
    private static final By REGISTER_BUTTON = By.xpath("//form//button[normalize-space()='Register']");
    private static final By CANCEL_LINK = By.xpath("//a[normalize-space()='Cancel']");
    private static final By ERROR_MESSAGE = By.cssSelector(".alert-danger");
    private static final By SUCCESS_MESSAGE = By.cssSelector(".alert-success");

    private void fillForm(String login, String firstName, String lastName, String password, String confirm) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME)).sendKeys(login);
        driver.findElement(FIRST_NAME_FIELD).sendKeys(firstName);
        driver.findElement(LAST_NAME).sendKeys(lastName);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        driver.findElement(CONFIRM_PASSWORD).sendKeys(confirm);
    }

    @Test
    @DisplayName("TC-006: «Cancel» повертає на головну, обліковий запис не створено")
    void tc006_cancelReturnsHomeWithoutCreatingAccount() {
        open("/register");
        fillForm("tzfe41_r06_s4", "Cancel", "Test", "Autorate2026!", "Autorate2026!");

        driver.findElement(CANCEL_LINK).click();
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/"));

        TopBar topBar = new TopBar(driver, wait);
        topBar.loginAs("tzfe41_r06_s4", "Autorate2026!");
        assertAll("обліковий запис не створено",
                () -> assertFalse(topBar.waitForLoginError().isBlank()),
                () -> assertFalse(topBar.isGreetingShown(), "вхід виконано — обліковий запис створено"));
    }

    @Test
    @DisplayName("TC-007: різні Password і Confirm Password — повідомлення про незбіг, реєстрації немає")
    void tc007_passwordMismatchRejected() {
        open("/register");
        fillForm("tzfe41_r07_s4", "Mismatch", "Test", "Autorate2026!", "Autorate2026?");

        WebElement register = driver.findElement(REGISTER_BUTTON);
        if (register.isEnabled()) {
            register.click();
        }
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)).getText();
        assertAll("незбіг паролів",
                () -> assertTrue(error.toLowerCase().contains("match"), "текст помилки: " + error),
                () -> assertTrue(driver.findElements(SUCCESS_MESSAGE).isEmpty(), "є повідомлення про успіх"));
    }

    @Test
    @DisplayName("TC-040: кнопка «Register» неактивна, доки не заповнено всі п'ять полів")
    void tc040_registerDisabledUntilAllFieldsFilled() {
        open("/register");
        WebElement register = wait.until(ExpectedConditions.visibilityOfElementLocated(REGISTER_BUTTON));
        assertFalse(register.isEnabled(), "порожня форма: Register активна");

        fillForm("tzfe41_r40_s4", "Gui", "Test", "Autorate2026!", "");
        assertFalse(driver.findElement(REGISTER_BUTTON).isEnabled(), "Confirm Password порожнє: Register активна");

        driver.findElement(CONFIRM_PASSWORD).sendKeys("Autorate2026!");
        assertTrue(wait.until(ExpectedConditions.elementToBeClickable(REGISTER_BUTTON)).isEnabled(),
                "усі поля заповнено: Register неактивна");
    }
}
