package ua.kpi.tzyapz.autorate.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тести головної сторінки (F-10) за тест-кейсами спринта 3 TC-001…TC-004.
 * TC-004 спершу записано у Selenium IDE (selenium-ide/autorate.side), експорт — selenium-ide/export/,
 * тут — адаптована версія для JUnit 5. Автор: Рзяніна Валерія.
 */
@Tag("ui")
@DisplayName("UI · Головна сторінка (F-10)")
class HomePageTest extends UiTestBase {

    private static final By POPULAR_MAKE_LINK = By.cssSelector("a[href^='/make/']");
    private static final By POPULAR_MODEL_LINK = By.cssSelector("a[href^='/model/']");
    private static final By OVERALL_LINK = By.cssSelector("a[href='/overall']");
    private static final By TABLE_ROWS = By.cssSelector("table tbody tr");

    @Test
    @DisplayName("TC-001: головна показує блоки «Popular Make», «Popular Model», «Overall Rating»")
    void tc001_homeShowsThreeBlocks() {
        open("/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(POPULAR_MAKE_LINK));
        String page = driver.findElement(By.tagName("body")).getText();
        assertAll("блоки головної сторінки",
                () -> assertTrue(page.contains("Popular Make"), "немає блоку Popular Make"),
                () -> assertTrue(page.contains("Popular Model"), "немає блоку Popular Model"),
                () -> assertTrue(page.contains("Overall Rating"), "немає блоку Overall Rating"),
                () -> assertTrue(page.matches("(?s).*\\(\\d+ votes\\).*"), "немає лічильника «(N votes)»"),
                () -> assertTrue(page.contains("List of all registered models."), "немає підпису Overall Rating"));
    }

    @Test
    @DisplayName("TC-002: логотип у блоці «Popular Make» відкриває сторінку марки з таблицею моделей")
    void tc002_popularMakeOpensMakePage() {
        open("/");
        wait.until(ExpectedConditions.elementToBeClickable(POPULAR_MAKE_LINK)).click();
        wait.until(ExpectedConditions.urlContains("/make/"));
        List<WebElement> rows = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE_ROWS, 0));
        assertAll("сторінка марки",
                () -> assertTrue(driver.getCurrentUrl().contains("/make/")),
                () -> assertFalse(rows.isEmpty(), "таблиця моделей марки порожня"));
    }

    @Test
    @DisplayName("TC-003: фото в блоці «Popular Model» відкриває сторінку моделі з блоками Specification і Votes")
    void tc003_popularModelOpensModelPage() {
        open("/");
        wait.until(ExpectedConditions.elementToBeClickable(POPULAR_MODEL_LINK)).click();
        wait.until(ExpectedConditions.urlContains("/model/"));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Votes"));
        String page = driver.findElement(By.tagName("body")).getText();
        assertAll("сторінка моделі",
                () -> assertTrue(driver.getCurrentUrl().contains("/model/")),
                () -> assertTrue(page.contains("Specification"), "немає блоку Specification"),
                () -> assertTrue(page.contains("Votes"), "немає блоку Votes"));
    }

    @Test
    @DisplayName("TC-004: зображення в блоці «Overall Rating» відкриває /overall із таблицею з шести стовпців")
    void tc004_overallRatingOpensTable() {
        open("/");
        wait.until(ExpectedConditions.elementToBeClickable(OVERALL_LINK)).click();
        wait.until(ExpectedConditions.urlContains("/overall"));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE_ROWS, 0));
        String header = driver.findElement(By.cssSelector("table thead")).getText();
        assertAll("таблиця Overall Rating",
                () -> assertEquals(BASE_URL + "/overall", driver.getCurrentUrl()),
                () -> assertTrue(header.contains("Make"), header),
                () -> assertTrue(header.contains("Model"), header),
                () -> assertTrue(header.contains("Rank"), header),
                () -> assertTrue(header.contains("Votes"), header),
                () -> assertTrue(header.contains("Engine"), header),
                () -> assertTrue(header.contains("Comments"), header));
    }
}
