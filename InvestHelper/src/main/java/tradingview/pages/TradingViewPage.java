package tradingview.pages;

import com.codeborne.selenide.*;
import dev.failsafe.internal.util.Assert;
import lombok.extern.java.Log;

import static com.codeborne.selenide.Selenide.*;

public class TradingViewPage extends BaseWebUI implements IndependentPage{


    private final SelenideElement pageId = $x("//*[@aria-label = 'TradingView main page' and contains(@class, 'link')]");
    private final SelenideElement marketsMenu = $x("//button[@data-main-menu-root-track-id = 'markets' and text () = 'Markets']");
    private final SelenideElement dropDownStocksOption = $x("//a[@data-main-menu-dropdown-track-id = 'Stocks']");
    private final SelenideElement dropDownStocksTopGainersOption = $x("//a[@data-main-menu-dropdown-track-id = 'Top gainers']//span[text() = 'Top gainers']");
    private final SelenideElement signInButton = $x("//button[@aria-label = 'Open user menu' and contains(@class, 'anonymous')]");
    private final SelenideElement signInPopUp = $x("//div[@data-name = 'popup-menu-container']");
    private final SelenideElement signInLinkText = $x("//div[@data-name = 'popup-menu-container']//span[contains(@class, 'link-title') and text() = 'Sign in']");
    private final SelenideElement signInWithGoogleButton = $x("//span[@data-overflow-tooltip-text = 'Google ']");


    public TradingViewPage() {
        Selenide.open(TRADING_VIEW_PAGE_URL);
        Assert.isTrue(isThisPage(), "Current page is invalid. Trading View page expected.");
    }


    @Override
    public boolean isThisPage() {
        return WebDriverRunner.url().toLowerCase()
                .contains(TRADING_VIEW_PAGE_URL.toLowerCase()) && pageId.isDisplayed();
    }


    public TopGainerStocksMarketsPage navigateToTopGainerStocksMarketsPage() {
        marketsMenu.hover();
        dropDownStocksOption.hover();
        dropDownStocksTopGainersOption.click();
        TopGainerStocksMarketsPage topGainerStocksMarketsPage = new TopGainerStocksMarketsPage();
        Assert.isTrue(topGainerStocksMarketsPage.isThisPage(),
                "Current page is invalid. Top Gainers Stocks Markets page expected.");
        return topGainerStocksMarketsPage;
    }

    public TradingViewPage clickOnSignInButton(){
        signInButton.click();
        signInPopUp.shouldBe(Condition.visible);
        return this;
    }


    public TradingViewPage clickOnSignInLinkedText(){
        signInLinkText.click();
        signInWithGoogleButton.shouldBe(Condition.visible);
        return this;
    }


    public LoginPage clickOnSignInWithGoogle(){
        signInWithGoogleButton.click();

        LoginPage loginPage = new LoginPage();
        Assert.isTrue(loginPage.isThisPage(),
                "Current page is invalid. Login page expected.");
        return loginPage;
    }


}
