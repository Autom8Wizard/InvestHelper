package tradingview.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import dev.failsafe.internal.util.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage extends BaseWebUI implements IndependentPage {


    private static final Logger logger = LogManager.getLogger(LoginPage.class);


    private final SelenideElement pageId = $x("//*[@id = 'yDmH0d']");
    private final SelenideElement emailInput = $x("//input[@id = 'identifierId']");
    private final SelenideElement passwordInput = $x("//input[@name = 'Passwd']");


    @Override
    public boolean isThisPage() {
        switchTo().window(1);
        pageId.shouldBe(Condition.visible);
        return WebDriverRunner.url().toLowerCase()
                .contains(LOGIN_PAGE_URL.toLowerCase());
    }


    public LoginPage fillEmail(String email) {
        emailInput.shouldBe(Condition.visible).shouldBe(Condition.interactable);
        emailInput.setValue(email);
        emailInput.pressEnter();
        return this;
    }


    public ProfilePage fillPassword(String password) {
        passwordInput.shouldBe(Condition.visible).shouldBe(Condition.interactable);
        passwordInput.setValue(password);
        passwordInput.pressEnter();

        switchTo().window(0);

        ProfilePage profilePage = new ProfilePage();
        Assert.isTrue(profilePage.isThisPage(),
                "Current page is invalid. Profile page expected.");
        return profilePage;
    }


}
