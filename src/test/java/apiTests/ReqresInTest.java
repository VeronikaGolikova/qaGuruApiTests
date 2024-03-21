package apiTests;

import models.ListUsersResponseModel;
import models.RegisterRequestModel;
import models.RegisterResponseModel;
import models.UpdateRequestModel;
import models.UpdateResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.GeneralSpec.deleteResponseSpec;
import static specs.GeneralSpec.missingUserResponseSpec;
import static specs.GeneralSpec.responseSpec;
import static specs.GeneralSpec.requestSpec;

public class ReqresInTest extends TestBase{

    String text = "To keep ReqRes free, contributions towards server costs are appreciated!";

    @Test
    void getListUsers() {

        ListUsersResponseModel response = step("Make request", ()->
                given()
                        .spec(requestSpec)
                .when()
                .get("users?page=2")
                .then()
                        .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/listUsersSchema.json"))
                .extract().as(ListUsersResponseModel.class));

        step("Check response", ()->
        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals(2, response.getPage()),
                () -> assertEquals("Michael", response.getData().get(0).getFirst_name()),
                () -> assertEquals(text, response.getSupport().getText())));
    }

    @Test
    void getListUsersNegative() {
        ListUsersResponseModel response = step("Make request", ()->
                given()
                        .spec(requestSpec)
                .when()
                .get("users?page=3")
                .then()
                        .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/emptyListUsersSchema.json"))
                .extract().as(ListUsersResponseModel.class));

        step("Check response", ()->
        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals(3, response.getPage()),
                () -> assertEquals(text, response.getSupport().getText())));
    }

    @Test
    void getSingleUser() {
        UserResponseModel response = step("Make request", ()->
                given()
                        .spec(requestSpec)
                .when()
                .get( "users/2" )
                .then()
                        .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/userSchema.json"))
                .extract().as(UserResponseModel.class));

        step("Check response", ()->
        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("janet.weaver@reqres.in", response.getData().getEmail()),
                () -> assertEquals(2, response.getData().getId()),
                () -> assertEquals("Janet", response.getData().getFirst_name()),
                () -> assertEquals("Weaver", response.getData().getLast_name()),
                () -> assertEquals("https://reqres.in/img/faces/2-image.jpg", response.getData().getAvatar()),
                () -> assertEquals(text, response.getSupport().getText())));
    }

    @Test
    void getEmptyUser() {
        ListUsersResponseModel response = step("Make request", ()->
                given()
                        .spec(requestSpec)
                .when()
                .get( "users/")
                .then()
                        .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/listUsersSchema.json"))
                .extract().as(ListUsersResponseModel.class));

        step("Check response", ()->
        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("janet.weaver@reqres.in", response.getData().get(1).getEmail()),
                () -> assertEquals(1, response.getData().get(0).getId()),
                () -> assertEquals(text, response.getSupport().getText())));
    }

    @Test
    void getSingleMissingUser() {
        step("Make request", ()->
        given()
                .spec(requestSpec)
                .when()
                .get( "users/102")
                .then()
                .spec(missingUserResponseSpec)
                .extract().response());
    }

    @Test
    void postRegisterSuccessful() {
        RegisterRequestModel registerBody = new RegisterRequestModel();
        registerBody.setEmail("eve.holt@reqres.in");
        registerBody.setPassword("pistol");
        RegisterResponseModel response = step("Make request", ()->
                given()
                        .spec(requestSpec)
                .body(registerBody)
                .when()
                .post("register")
                .then()
                        .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/registerSchema.json"))
                .extract().as(RegisterResponseModel.class));

        step("Check response", ()->
        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("4", response.getId()),
                () -> assertEquals("QpwL5tke4Pnpja7X4", response.getToken())));
    }

    @Test
    void patchUpdateSuccessful() {
        UpdateRequestModel updateBody = new UpdateRequestModel();
        updateBody.setJob("zion resident");
        updateBody.setName("morpheus");

        UpdateResponseModel response = step("Make request", ()->
                given()
                        .spec(requestSpec)
                .body(updateBody)
                .when()
                .patch("users/2")
                .then()
                        .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/patchUpdateSchema.json"))
                .extract().as(UpdateResponseModel.class));

        step("Check response", ()->
        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("morpheus", response.getName()),
                () -> assertEquals("zion resident", response.getJob())));
    }

    @Test
    void deleteSuccessful() {
        step("Make request", ()->
        given()
                .spec(requestSpec)
                .when()
                .delete("users/2")
                .then()
                .spec(deleteResponseSpec)
                .extract().response());
    }
}
