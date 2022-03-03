import basetest.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utilities.Generators;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class APISuiteTest extends BaseTest {


    String statusOK = "HTTP/1.1 200 OK";
    String jsonValidation = "{\"status\":\"OK\"}";

    String testName = Generators.genName();
    String testEmail = Generators.genEmail();
    String authToken;
    String hardCodedToken = "201fd320d66f967d1c9200ef9c5ef3b317fc6339e182c351180b8f1ebf86f0be";

    String orderPayload = "{\n" +
            "  \"bookId\": 1,\n" +
            "  \"customerName\": \"John\"\n" +
            "}";
    String orderPayloadOutOfStock = "{\n" +
            "  \"bookId\": 2,\n" +
            "  \"customerName\": \"John\"\n" +
            "}";
    String updateCustomer = "{\n" +
            "  \"customerName\": \"Changed Customer\"\n" +
            "}";
    String alreadyUsedData = "{\n" +
            "  \"clientName\": \"Admin\",\n" +
            "  \"clientEmail\": \"a@wp.pl\"\n" +
            "}";
    String hardcodedData = "{\n" +
            "  \"clientName\": \"Admin1234test\",\n" +
            "  \"clientEmail\": \"testa123@wp.pl\"\n" +
            "}";

    String orderId = "dbvfXtvNBBdYZY6LWSrqd";
    String orderIdNegTestCases ="I8vGpyG_jxMV4AvB3L6Xv";

    //---------------------------------------------------------------------------
    //POSITIVE TEST CASES (+)
    //---------------------------------------------------------------------------

    @Test
    public void getApiServiceStatus(){

        given()
                .when().get(RestAssured.baseURI = statusURL)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .statusLine(statusOK)
                .body(equalTo(jsonValidation))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void authenticate() {

        RestAssured.baseURI = baseURL;
        httpRequest = RestAssured.given();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("clientName", testName);
        jsonObj.put("clientEmail", testEmail);

        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(jsonObj.toString());
        response = httpRequest.request(Method.POST, "/api-clients");

        String responseBody = response.getBody().prettyPrint();
        System.out.println(responseBody);

        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        String accessToken = jsonResponse.get("accessToken").toString();
        System.out.println("Current AuthToken : " + authToken);
        authToken = accessToken;

        System.out.println("Oauth Token extracted is: " + accessToken);
        System.out.println("authToken= " + "Bearer " + authToken);

        Assertions.assertTrue(responseBody.contains("accessToken"));
        int status = response.getStatusCode();
        Assertions.assertEquals(status, HttpStatus.SC_CREATED);
    }

    @Test
    public void authenticateWithHardcodedData (){

        given()
                .header("Content-type", "application/json")
                .when()
                .body(hardcodedData)
                .and()
                .post(baseURL+ "/api-clients/")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("accessToken", matchesRegex("^[A-Fa-f0-9]{64}$"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void listAllNonFictionBooks (){

        given().
                when().get("https://simple-books-api.glitch.me/books?type=non-fiction")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("type", hasItem("non-fiction"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void listAllFictionBooks (){

        given()
                .when()
                .get( baseURL+ "/books?type=fiction")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("type", hasItem("fiction"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void getBookByIdNumber2 () {

        given()
                .when()
                .get(baseURL + "/books/2")
                .then().
                statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(2))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void createANewOrder () {

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + hardCodedToken )
                .body(orderPayload)
                .post(baseURL + "/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("created", equalTo(true))
                .body(containsString("orderId"))
                .extract()
                .response().prettyPrint();

    }

    @Test
    public void getAnExistingSingleOrder (){

        given()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .get(baseURL+ "/orders/" +orderId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void getAnExistingAllOrders () {

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken)
                .get(baseURL + "/orders/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                //assert - list not empty
                .extract()
                .response()
                .prettyPrint();

    }

    @Test
    public void UpdateAnExistingOrder (){

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .header("Content-type", "application/json")
                .and()
                .body(updateCustomer)
                .when()
                .patch(baseURL+ "/orders/" +orderId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void DeleteAnExistingOrder (){

        given()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .header("Content-type", "application/json")
                .when()
                .delete(baseURL+ "/orders/" +orderId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response()
                .prettyPrint();
    }

    //---------------------------------------------------------------------------
    //NEGATIVE TEST CASES (-)
    //---------------------------------------------------------------------------

    @Test
    public void authenticateWithAlreadyUsedCredentials (){

        given()
                .header("Content-type", "application/json")
                .when()
                .body(alreadyUsedData)
                .and()
                .post(baseURL+ "/api-clients/")
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("error", containsString("API client already registered. Try a different email"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void listAllFantasyBooks (){

        given().
                when().get("https://simple-books-api.glitch.me/books?type=fantasy")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("error", containsString("Invalid value for query parameter 'type'. Must be one of: fiction, non-fiction."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void getBookByIdNumber99 () {

        given()
                .when()
                .get(baseURL + "/books/99")
                .then().
                statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("No book with id 99"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void createANewOrderForItemOutOfStock () {

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + hardCodedToken )
                .body(orderPayloadOutOfStock)
                .post(baseURL + "/orders")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("This book is not in stock. Try again later."))
                .extract()
                .response().prettyPrint();
    }

    @Test
    public void createANewOrderForUnauthorizedAccess () {

        given().contentType(ContentType.JSON)
                .header("Content-type", "application/json")
                .body(orderPayload)
                .post(baseURL + "/orders")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("error", containsString("Missing Authorization header."))
                .extract()
                .response().prettyPrint();
    }

    @Test
    public void getAnExistingSingleOrderUnauthorized (){

        given()
                .when()
                .get(baseURL+ "/orders/" +orderId)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("error", containsString("Missing Authorization header."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void getAnExistingAllOrdersUnauthorized () {

        given()
                .when()
                .get(baseURL + "/orders")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("error", containsString("Missing Authorization header."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void getAnNonExistingSingleOrder (){

        given()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .get(baseURL+ "/orders/" +"unknownId")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("No order with id unknownId."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void UpdateExistingOrderWithoutAuthorization (){

        given()
                .when()
                .header("Content-type", "application/json")
                .and()
                .body(updateCustomer)
                .when()
                .patch(baseURL+ "/orders/" +orderIdNegTestCases)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("error", containsString("Missing Authorization header."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void UpdateAnNonExistingOrder (){

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .header("Content-type", "application/json")
                .and()
                .body(updateCustomer)
                .when()
                .patch(baseURL+ "/orders/" +"test123")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("No order with id test123."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void UpdateAnExistingOrderOfAnotherUser () {

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken)
                .header("Content-type", "application/json")
                .and()
                .body(updateCustomer)
                .when()
                .patch(baseURL + "/orders/" + "7lQbJ94wQYdZbDiprzF8i")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("No order with id 7lQbJ94wQYdZbDiprzF8i."))
                .extract()
                .response()
                .prettyPrint();
    }
    //
    @Test
    public void DeleteExistingOrderWithoutAuthorization (){

        given()
                .when()
                .header("Content-type", "application/json")
                .when()
                .delete(baseURL+ "/orders/" +orderIdNegTestCases)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("error", containsString("Missing Authorization header."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void DeleteAnNonExistingOrder (){

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .header("Content-type", "application/json")
                .and()
                .body(updateCustomer)
                .when()
                .patch(baseURL+ "/orders/" +"test123")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("No order with id test123."))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    public void DeleteAnExistingOrderOfAnotherUser () {

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken)
                .header("Content-type", "application/json")
                .and()
                .body(updateCustomer)
                .when()
                .delete(baseURL + "/orders/" + "7lQbJ94wQYdZbDiprzF8i")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", containsString("No order with id 7lQbJ94wQYdZbDiprzF8i."))
                .extract()
                .response()
                .prettyPrint();
    }


}
