package ua.kpi.tzyapz.autorate.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тест сторінки моделі (F-90) за тест-кейсом спринта 3 TC-031. Автор: Рзяніна Валерія.
 */
@Tag("ui")
@DisplayName("UI · Сторінка моделі (F-90)")
class ModelPageTest extends UiTestBase {

    private static final By POPULAR_MODEL_LINK = By.cssSelector("a[href^='/model/']");
    private static final By LOGIN_REQUIRED = By.xpath("//*[contains(normalize-space(text()),'You need to be logged in to vote.')]");
    private static final By COMMENT_FIELD = By.id("comment");
    private static final By VOTE_BUTTON = By.xpath("//button[normalize-space()='Vote!']");

    @Test
    @DisplayName("TC-031: відвідувач бачить «You need to be logged in to vote.» замість форми голосування")
    void tc031_visitorSeesLoginRequiredInsteadOfVoteForm() {
        open("/");
        wait.until(ExpectedConditions.elementToBeClickable(POPULAR_MODEL_LINK)).click();
        wait.until(ExpectedConditions.urlContains("/model/"));
        String message = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_REQUIRED)).getText();
        assertAll("блок Votes для відвідувача",
                () -> assertTrue(message.contains("You need to be logged in to vote.")),
                () -> assertTrue(driver.findElements(COMMENT_FIELD).isEmpty(), "є поле Comment"),
                () -> assertTrue(driver.findElements(VOTE_BUTTON).isEmpty(), "є кнопка Vote!"));
    }
}
