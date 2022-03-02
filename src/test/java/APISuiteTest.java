import basetest.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.ResponseBody;
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
    String orderId = "JJb-1vW3ZNjK5vYzCOBVb";

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

        given().when().get(baseURL + "/books/2")
                .then().statusCode(HttpStatus.SC_OK).body("id", equalTo(2))
                .extract().response().prettyPrint();

        //  String responseBody = httpRequest.response().toString();
    }

    @Test
    public void createANewOrder () {


        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + hardCodedToken )
                .body(orderPayload)
                .post(baseURL + "/orders")
                .then()
                .statusCode(201)
                .body("created", equalTo(true))
                .body(containsString("orderId"))
                .extract()
                .response().prettyPrint();

    }

    @Test
    public void getAnExistingSingleOrder (){

        given()
                .when()
                .header("Authorization",
                        "Bearer " + hardCodedToken )
                .get(baseURL+ "/orders/" +orderId)
                .prettyPrint();

    }

    @Test
    public void getAnExistingAllOrders (){}

    

    @Test
    public void UpdateAnExistingOrder (){}

    @Test
    public void DeleteAnExistingOrder (){}

}
