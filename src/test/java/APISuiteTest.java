import basetest.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import user.RandomUserAsJson;
import utilities.Generators;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class APISuiteTest extends BaseTest {


    String statusOK = "HTTP/1.1 200 OK";
    String jsonValidation = "{\"status\":\"OK\"}";
    RandomUserAsJson user1 = new RandomUserAsJson();

    String testName = Generators.genName();
    String testEmail = Generators.genEmail();
    String authToken;


    @Test
    public void getApiServiceStatus(){

        given()
                .when().get(RestAssured.baseURI = statusURL)
                .then()
                .statusCode(HttpStatus.SC_OK).statusLine(statusOK).body(equalTo(jsonValidation));

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
        authToken = accessToken;

        System.out.println("Oauth Token extracted is: " + accessToken);
        System.out.println("authToken = " + authToken);

        Assertions.assertTrue(responseBody.contains("accessToken"));

        int status = response.getStatusCode();
        Assertions.assertEquals(status, HttpStatus.SC_CREATED);


    }

    @Test
    public void listAllNonFictionBooks (){

        given().
                when().get("https://simple-books-api.glitch.me/books?type=non-fiction")
                .then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void listAllFictionBooks (){

        given().
                when().get( baseURL+ "/books?type=fiction")
                .then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void getBookbyIdNumber2 (){



        given().when().get(baseURL+ "/books/2")
                .then().statusCode(HttpStatus.SC_OK).body(containsString("id"));


    }

    @Test
    public void createANewOrder () {

//        ValidatableResponse response = given().header("Authorization", "Bearer " + authToken)
//                .when().get(baseURL + "/orders").then();
//        response.toString();
//        System.out.println(response);

        RestAssured.baseURI = baseURL;
        httpRequest = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("bookId", "1");
        requestParams.put("customerName", "Mati");

        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("Authorization", "Bearer" + authToken);
        httpRequest.body(requestParams.toString());

        response = httpRequest.post("/orders");
        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        System.out.println("The status received: " + response.statusLine());

    }
    @Test
    public void getAnExistingOrder (){}

    @Test
    public void getAnExistingSingleOrder (){}

    @Test
    public void getAnExistingAllOrders (){}

    @Test
    public void UpdateAnExistingOrder (){}

    @Test
    public void DeleteAnExistingOrder (){}

}
