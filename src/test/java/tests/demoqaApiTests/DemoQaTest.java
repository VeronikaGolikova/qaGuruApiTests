package tests.demoqaApiTests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.response.Response;
import model.demoqa.LoginRequestModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import page.AuthorizedWebPage;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.GeneralSpec.requestSpec;
import static specs.GeneralSpec.responseSpec200OkWithLogging;

@Tag("demoQa")
@Owner("golikovavi")
@Feature("Проверка веб-страниц авторизованным через API пользователем")
public class DemoQaTest extends TestBase {
    LoginRequestModel loginRequestModel = new LoginRequestModel();
    AuthorizedWebPage authorizedWebPage = new AuthorizedWebPage();

    @Test
    void successfulLoginWithApiAndUiTest() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        loginRequestModel
                .setUserName("ShocoArts")
                .setPassword("QaqA19091992!");

        Response response = step("Сделать запрос через Api для получения token, id, expires", ()->
                given()
                .spec(requestSpec)
                .body(loginRequestModel)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(responseSpec200OkWithLogging)
                .extract().response());

        authorizedWebPage.openWebBrowserAndAddCookies(response);
        authorizedWebPage.openProfilePage(loginRequestModel.getUserName());
    }

    @Test
    void successfulLoginWithApiAndUiLogoutTest() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        loginRequestModel
                .setUserName("ShocoArts")
                .setPassword("QaqA19091992!");

        Response response = step("Сделать запрос через Api для получения token, id, expires", ()->
                given()
                        .spec(requestSpec)
                        .body(loginRequestModel)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(responseSpec200OkWithLogging)
                        .extract().response());

        authorizedWebPage.openWebBrowserAndAddCookies(response);
        authorizedWebPage.openProfilePage(loginRequestModel.getUserName());
        authorizedWebPage.clickLogout();
        authorizedWebPage.disableLogoutButton();
    }
}
