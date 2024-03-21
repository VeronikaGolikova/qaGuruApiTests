package apiTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresInTest extends TestBase{

    String text = "To keep ReqRes free, contributions towards server costs are appreciated!";
    String bodyRegister = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
    String bodyForUpdate = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

    @Test
    void getListUsers() {
        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listUsersSchema.json"))
                .extract().response();

        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("2", response.path("page").toString()),
                () -> assertEquals("Michael", response.path("data.find{it.last_name == 'Lawson'}.first_name")),
                () -> assertEquals(text, response.path("support.text")));
    }

    @Test
    void getListUsersNegative() {
        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("users?page=3")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/emptyListUsersSchema.json"))
                .extract().response();

        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("3", response.path("page").toString()),
                () -> assertEquals("[]", response.path("data").toString()),
                () -> assertEquals(text, response.path("support.text")));
    }

    @Test
    void getSingleUser() {
        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get( "users/2" )
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/userSchema.json"))
                .extract().response();

        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("janet.weaver@reqres.in", response.path("data.email").toString()),
                () -> assertEquals("2", response.path("data.id").toString()),
                () -> assertEquals("Janet", response.path("data.first_name")),
                () -> assertEquals("Weaver", response.path("data.last_name")),
                () -> assertEquals("https://reqres.in/img/faces/2-image.jpg", response.path("data.avatar")),
                () -> assertEquals(text, response.path("support.text")));
    }

    @Test
    void getEmptyUser() {
        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get( "users/")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listUsersSchema.json"))
                .extract().response();

        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("janet.weaver@reqres.in", response.path("data.find{it.last_name == 'Weaver'}.email")),
                () -> assertEquals("1", response.path("data.find{it.last_name == 'Bluth'}.id").toString()),
                () -> assertEquals(text, response.path("support.text")));
    }

    @Test
    void getSingleMissingUser() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get( "users/102")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .extract().response();
    }

    @Test
    void postRegisterSuccessful() {
        Response response = given()
                .body(bodyRegister)
                .contentType(JSON)
                .log().uri()
                .log().method()
                .log().body()

                .when()
                .post("register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/registerSchema.json"))
                .extract().response();

        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("4", response.path("id").toString()),
                () -> assertEquals("QpwL5tke4Pnpja7X4", response.path("token")));
    }

    @Test
    void patchUpdateSuccessful() {
        Response response = given()
                .body(bodyForUpdate)
                .contentType(JSON)
                .log().uri()
                .log().method()
                .log().body()

                .when()
                .patch("users/2")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/patchUpdateSchema.json"))
                .extract().response();

        assertAll ("Проверка полей пользователя в ответе",
                () -> assertEquals("morpheus", response.path("name").toString()),
                () -> assertEquals("zion resident", response.path("job")));
    }

    @Test
    void deleteSuccessful() {
        Response response = given()
                .contentType(JSON)
                .log().uri()
                .log().method()
                .log().body()

                .when()
                .delete("users/2")

                .then()
                .log().status()
                .log().body()
                .statusCode(204)
                .extract().response();
    }
}
