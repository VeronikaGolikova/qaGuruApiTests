package tests.demoqaApiTests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import model.demoqa.LoginRequestModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import page.AuthorizedWebPage;
import extensions.WithLogin;

@Tag("demoQa")
@Owner("golikovavi")
@Feature("Проверка веб-страниц авторизованным через API пользователем")
public class DemoQaTest extends TestBase {
    LoginRequestModel loginRequestModel = new LoginRequestModel()
            .setUserName("ShocoArts")
            .setPassword("QaqA19091992!");
    AuthorizedWebPage authorizedWebPage = new AuthorizedWebPage();

    @Test
    @WithLogin
    @DisplayName("Авторизация через API с последующим открытием сайта авторизованным пользователем")
    void successfulLoginWithApiAndUiTest() {
        authorizedWebPage.openProfilePage();
        authorizedWebPage.openProfilePageShouldHaveLoginText(loginRequestModel.getUserName());
    }

    @Test
    @WithLogin
    @DisplayName("Авторизация через API с последующим открытием сайта авторизованным пользователем и разлогиниванием через UI")
    void successfulLoginWithApiAndUiLogoutTest() {
        authorizedWebPage.openProfilePage();
        authorizedWebPage.openProfilePageShouldHaveLoginText(loginRequestModel.getUserName());
        authorizedWebPage.clickLogout();
        authorizedWebPage.disableLogoutButton();
    }

    @Test
    @WithLogin
    @DisplayName("Проверить, что лист книг пустой")
    void bookListIsEmpty() {
        authorizedWebPage.openProfilePage();
        authorizedWebPage.openProfilePageShouldHaveLoginText(loginRequestModel.getUserName());
        authorizedWebPage.checkEmptyCells();
        authorizedWebPage.checkNoDataMessage();
    }
}
