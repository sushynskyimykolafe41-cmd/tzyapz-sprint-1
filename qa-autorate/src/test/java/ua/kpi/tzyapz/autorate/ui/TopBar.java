package ua.kpi.tzyapz.autorate.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object верхньої панелі (GUI-10): форма входу, привітання, Profile, Logout.
 * Локатори — за атрибутом name і видимим текстом; абсолютних XPath немає.
 */
class TopBar {

    static final By LOGIN_FIELD = By.name("login");
    static final By PASSWORD_FIELD = By.xpath("(//input[@name='password'])[1]");
    static final By LOGIN_BUTTON = By.xpath("//button[normalize-space()='Login']");
    static final By GREETING = By.xpath("//*[starts-with(normalize-space(text()),'Hi,')]");
    static final By PROFILE_LINK = By.linkText("Profile");
    static final By LOGOUT_LINK = By.linkText("Logout");
    static final By LOGIN_ERROR = By.xpath("//*[contains(normalize-space(text()),'Invalid username/password')]");
    static final By BRAND_LINK = By.linkText("Buggy Rating");

    private final WebDriver driver;
    private final WebDriverWait wait;

    TopBar(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /** Вводить логін і пароль у верхній панелі та натискає «Login». */
    void loginAs(String login, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_FIELD)).sendKeys(login);
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }

    /** Чекає на привітання «Hi, …» і повертає його текст. */
    String waitForGreeting() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(GREETING)).getText();
    }

    /** Чекає на повідомлення про невдалий вхід і повертає його текст. */
    String waitForLoginError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR)).getText();
    }

    void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_LINK)).click();
    }

    /** Панель відвідувача: видно поле «Login». */
    boolean waitForVisitorPanel() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_FIELD)).isDisplayed();
    }

    boolean isGreetingShown() {
        return !driver.findElements(GREETING).isEmpty();
    }
}
