package ua.kpi.tzyapz.autorate.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тести сторінки загального рейтингу (F-70, F-40) за тест-кейсами спринта 3 TC-021 і TC-015.
 * Автор: Рзяніна Валерія.
 */
@Tag("ui")
@DisplayName("UI · Overall Rating (F-70, F-40)")
class OverallRatingTest extends UiTestBase {

    private static final By TABLE_ROWS = By.cssSelector("table tbody tr");
    private static final By HEADER_CELLS = By.cssSelector("table thead th");

    @Test
    @DisplayName("TC-021: таблиця Overall Rating має стовпець Rank, ранги першої сторінки зростають від 1")
    void tc021_firstPageSortedByRankAscending() {
        open("/overall");
        List<WebElement> rows = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE_ROWS, 0));

        int rankColumn = -1;
        List<WebElement> headers = driver.findElements(HEADER_CELLS);
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equals("Rank")) {
                rankColumn = i;
            }
        }
        assertTrue(rankColumn >= 0, "немає стовпця Rank");

        List<Integer> ranks = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            ranks.add(Integer.parseInt(cells.get(rankColumn).getText().trim()));
        }
        assertEquals(1, ranks.get(0).intValue(), "перший рядок має ранг 1, ранги: " + ranks);
        for (int i = 1; i < ranks.size(); i++) {
            assertTrue(ranks.get(i) > ranks.get(i - 1), "ранги не зростають: " + ranks);
        }
    }

    @Test
    @DisplayName("TC-015: вхід зі сторінки Overall Rating показує «Hi, Tester» і лишає користувача на /overall")
    void tc015_loginKeepsUserOnOverallPage() {
        open("/overall");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE_ROWS, 0)); // сторінка завантажилась
        TopBar topBar = new TopBar(driver, wait);

        topBar.loginAs(LOGIN, PASSWORD);
        String greeting = topBar.waitForGreeting();
        assertAll("після входу",
                () -> assertEquals("Hi, " + FIRST_NAME, greeting.trim()),
                () -> assertTrue(driver.getCurrentUrl().endsWith("/overall"), driver.getCurrentUrl()),
                () -> assertTrue(topBar.visible(TopBar.PROFILE_LINK).isDisplayed(), "немає Profile"));

        topBar.logout();
        assertTrue(topBar.waitForVisitorPanel(), "після Logout немає панелі відвідувача");
    }
}
