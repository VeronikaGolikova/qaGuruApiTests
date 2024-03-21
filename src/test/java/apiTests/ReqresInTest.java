package apiTests;

import models.ListUsersModel;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class ReqresInTest{

    int numPage = 2;
    int enptyNumPage = 3;
    int numUser = 2;
    int missingUser = 102;
    String text = "To keep ReqRes free, contributions towards server costs are appreciated!";

    @Test
    void getListUsers() {
        ListUsersModel authData = new ListUsersModel();
        authData.setPageNumber(2);
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get(PATH + "api/users?page=" + numPage)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listUsersSchema.json"))
                .body("page", is(numPage))
                .body("data.id", contains(7,8,9,10,11,12))
                .body("data.first_name", hasItem("Michael"))
                .body("support.text", is(text));
    }

    @Test
    void getListUsersNegative() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get(PATH + "api/users?page=" + enptyNumPage)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/emptyListUsersSchema.json"))
                .body("page", is(enptyNumPage))
                .body("data", empty())
                .body("support.text", is(text));
    }

    @Test
    void getSingleUser() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get(PATH + "api/users/" + numUser)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/userSchema.json"))
                .body("data.id", is(numUser))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"))
                .body("support.text", is(text));
    }

    @Test
    void getEmptyUser() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get(PATH + "api/users/")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listUsersSchema.json"))
                .body("data.id", hasItem(1))
                .body("data.email", hasItem("george.bluth@reqres.in"))
                .body("data.first_name", hasItem("George"))
                .body("data.last_name", hasItem("Bluth"))
                .body("data.avatar", hasItem("https://reqres.in/img/faces/1-image.jpg"))
                .body("data.id", contains(1,2,3,4,5,6))
                .body("support.text", is(text));
    }

    @Test
    void getSingleMissingUser() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get(PATH + "api/users/" + missingUser)
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
}
