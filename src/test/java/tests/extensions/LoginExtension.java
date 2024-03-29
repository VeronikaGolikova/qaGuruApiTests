package tests.extensions;

import io.restassured.response.Response;
import model.demoqa.LoginRequestModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import page.AuthorizedWebPage;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.GeneralSpec.requestSpec;
import static specs.GeneralSpec.responseSpec200OkWithLogging;

public class LoginExtension implements BeforeEachCallback {
    LoginRequestModel loginRequestModel = new LoginRequestModel();
    AuthorizedWebPage authorizedWebPage = new AuthorizedWebPage();
    @Override
    public void beforeEach(ExtensionContext context) {
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

    }
}
