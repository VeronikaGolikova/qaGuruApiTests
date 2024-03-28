package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class AuthorizedWebPage {
    private final SelenideElement
            userNameValue = $("#userName-value"),
            logOutButton = $(byText("Log out")),
            userNameField = $("#userName"),
            passwordField = $("#password");

    @Step("Добавить cookies в браузер")
    public AuthorizedWebPage openWebBrowserAndAddCookies(Response response) {
        String param1 = "token";
        String param2 = "expires";
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", response.path("userId")));
        getWebDriver().manage().addCookie(new Cookie(param1, response.path(param1)));
        getWebDriver().manage().addCookie(new Cookie(param2, response.path(param2)));
        return this;
    }
    @Step("Открыть страницу регистрации в браузере")
    public AuthorizedWebPage openProfilePage(String login) {
        open("/profile");
        $(userNameValue).shouldHave(text(login));
        return this;
    }

    @Step("Нажать на кнопку Logout в браузере")
    public AuthorizedWebPage clickLogout() {
        logOutButton.click();
        return this;
    }

    @Step("Проверить станицу после разлогинивания")
    public AuthorizedWebPage disableLogoutButton() {
        logOutButton.shouldNot(visible);
        userNameField.shouldBe(visible);
        passwordField.shouldBe(visible);
        return this;
    }
}
