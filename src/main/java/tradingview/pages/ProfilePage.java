package tradingview.pages;

import com.codeborne.selenide.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Sleeper;

import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;

public class ProfilePage extends BaseWebUI implements IndependentPage {


    private static final Logger logger = LogManager.getLogger(ProfilePage.class);


    private final SelenideElement pageId = $x("//*[@aria-label = 'Open user menu' and contains(@class, 'logged')]");
    private final SelenideElement addSymbolButton = $x("//*[@data-name = 'add-symbol-button']/span");
    private final SelenideElement addSymbolPopUp = $x("//*[@data-name = 'watchlist-symbol-search-dialog']//div[text() = 'Add symbol']");
    private final SelenideElement addSymbolPopUpCloseButton = $x("//*[@data-name = 'watchlist-symbol-search-dialog']//button[@data-name = 'close']");
    private final SelenideElement searchSymbolInput = $x("//input[@data-role = 'search']");
    protected SelenideElement watchListName = $x("//*[@data-name = 'watchlists-button']//*[contains(@class, 'titleRow')]");
    // symbol and symbol exchange should be passed to the template
    protected String symbolRowTemplate = "//div[@data-dialog-name = 'Add symbol']//*[text() = '%s']/ancestor::div[contains(@class, 'itemInfoCell')]/following-sibling::div[contains(@class, 'exchangeCell')]//div[text() = '%s']/ancestor::div[contains(@class, 'exchangeCell')]/following-sibling::div";
    // symbol exchange and symbol should be passed to the template
    protected String watchListSymbolRowTemplate = "//*[@data-name = 'symbol-list-wrap']//*[@data-symbol-full = '%s:%s']";


    @Override
    public boolean isThisPage() {
        pageId.shouldBe(Condition.visible, Duration.ofSeconds(timeoutLong));
        return WebDriverRunner.url().toLowerCase()
                .contains(TRADING_VIEW_PAGE_URL.toLowerCase()) && pageId.isDisplayed();
    }


    public String getWatchListName() {
        watchListName.shouldBe(Condition.visible);
        return watchListName.toWebElement().getText();
    }


    public ProfilePage clickOnAddSymbolButton() {
        addSymbolButton.click();
        addSymbolPopUp.shouldBe(Condition.visible);
        return this;
    }


    public ProfilePage clickOnAddSymbolPopUpCloseButton() {
        addSymbolPopUpCloseButton.click();
        addSymbolPopUp.shouldBe(Condition.not(Condition.visible));
        return this;
    }


    public ProfilePage fillSymbol(String symbol) {
        searchSymbolInput.shouldBe(Condition.visible, Duration.ofSeconds(timeoutLong));
        searchSymbolInput.clear();
        searchSymbolInput.setValue(symbol);
        return this;
    }


    public ProfilePage clickOnSymbol(String symbol, String symbolExchange) {
        SelenideElement symbolRowXpath = $x(String.format(symbolRowTemplate, symbol, symbolExchange));
        // Wait for the element to be completely loaded
        symbolRowXpath.shouldBe(Condition.visible, Duration.ofSeconds(timeout))
                .shouldBe(Condition.interactable, Duration.ofSeconds(timeout))
                .click();
        // check that symbol successfully added into watchlist
        $x(String.format(watchListSymbolRowTemplate, symbolExchange, symbol)).shouldBe(Condition.visible);
        return this;
    }


}
