package tests;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import utils.Converters;
import validators.Validators;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static models.JSONStringModel.*;
import static org.hamcrest.Matchers.*;
import static utils.ConstFields.*;
import static utils.ConstEndpoints.*;
import static utils.Converters.accessToken;


public class APISuiteTestPositive extends BaseTest {

    public String accessToken;

    @Test
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }



    //---------------------------------------------------------------------------
    //POSITIVE TEST CASES (+)
    //---------------------------------------------------------------------------

    @Test
    public void getApiServiceStatus() {
        response = given()
                .when()
                .get(BASE_URL + STATUS)
                .then()
                .extract()
                .response();

        Validators.validateStatus200(response.statusCode());
    }

    @Test
    public void authenticateWithGeneratedData() throws IOException {
        response = given()
                .header(CONTENT_TYPE, ContentType.JSON)
                .when()
                .body(jsonAuth.generateAuthenticationJson())
                .post(BASE_URL + API_CLIENTS)
                .then()
                .extract()
                .response();

        String res= response.path("accessToken").toString();
        setAccessToken(res);
        System.out.println();
        System.out.println(accessToken);
      //  responseBody = response.getBody().prettyPeek();

        Validators.validateStatus201(response.statusCode());
        Validators.matchingFormat(Converters.getAccessTokenValueFromJSONResponse(response.body().asString()));
    }

    @Test
    public void listAllNonFictionBooks() {
        response = given()
                .when()
                .get(BASE_URL + NON_FICTION)
                .then()
                .extract()
                .response();

        Validators.validateStatus200(response.getStatusCode());
        Validators.hasItemNonFictionBooks(response , CATEGORY_NON_FICTION);
    }

    @Test
    public void listAllFictionBooks() {
        response = given()
                .when()
                .get(BASE_URL + FICTION)
                .then()
                .extract()
                .response();

        Validators.validateStatus200(response.getStatusCode());
        Validators.hasItemFictionBooks(response, CATEGORY_FICTION);
    }

    @Test
    public void getBookByIdNumber2() {
        response = given()
                .when()
                .get(BASE_URL + BOOK_ID_2)
                .then()
                .extract()
                .response();

        int expectedBookId = response.path("id");

        Validators.validateStatus200(response.getStatusCode());
        Validators.validateBookIdResponse(response, expectedBookId);
    }

    @Test
    public void createANewOrder() {

       response =  given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + hardCodedToken )
                .body(orderPayload)
                .post(BASE_URL + ORDERS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("created", equalTo(true))
                .body(containsString("orderId"))
                .extract()
                .response();

        response.getBody().prettyPrint();
        Validators.validateStatus201(response.getStatusCode());
        Validators.ifOrderCreated(response.andReturn());
       // Validators.validateFieldString("dupacre6ated");
    }

    @Test
    public void getAnExistingSingleOrder() {

        response = given()
                .header(AUTHORIZATION,
                        BEARER + hardCodedToken)
                .get(BASE_URL + ORDERS + orderId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log()
                .all()
                .extract()
                .response();
        response.prettyPrint();
        Validators.validateStatus200(response.getStatusCode());
    }


    @Test
    public void getAnExistingAllOrders() {

        given()
                .when()
                .header(AUTHORIZATION,
                        BEARER + hardCodedToken)
                .get(BASE_URL + ORDERS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                //assert - list not empty
                .extract()
                .response()
                .prettyPrint();

    }

    @Test
    public void UpdateAnExistingOrder() {

        given()
                .when()
                .header(AUTHORIZATION,
                        BEARER + hardCodedToken)
                .header(CONTENT_TYPE, ContentType.JSON.toString())
                .and()
                .body(updateCustomer)
                .when()
                .patch(BASE_URL + ORDERS + orderId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void DeleteAnExistingOrder() {

        given()
                .header(AUTHORIZATION,
                        BEARER + hardCodedToken)
                .header(CONTENT_TYPE, ContentType.JSON)
                .when()
                .delete(BASE_URL + ORDERS + orderId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .prettyPrint();
    }
}