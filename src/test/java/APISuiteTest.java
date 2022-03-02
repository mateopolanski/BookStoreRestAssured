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
//
//
    String statusOK = "HTTP/1.1 200 OK";
    String jsonValidation = "{\"status\":\"OK\"}";
    RandomUserAsJson user1 = new RandomUserAsJson();

    String testName = Generators.genName();
    String testEmail = Generators.genEmail();


    @Test
    public void getApiServiceStatus(){

        given()
                .when().get(RestAssured.baseURI = statusURL)
                .then()
                .statusCode(HttpStatus.SC_OK).statusLine(statusOK).body(equalTo(jsonValidation));

    }

    @Test
    public void authenticate() {

//
////===========================================================================================================
////        JSONObject jsonObj = new JSONObject();
////        jsonObj.put("clientName", testName);
////        jsonObj.put("clientEmail", testEmail);
////        String jsonStr = jsonObj.toString();
////
////
////        given().contentType(ContentType.JSON)
////                .body(jsonObj.getJSONObject(jsonStr))
////                .when().post("https://simple-books-api.glitch.me/api-clients/")
////                .then()
////                .statusCode((HttpStatus.SC_ACCEPTED));
//
////        response = httpRequest.request(Method.POST);
////===========================================================================================================
//
//
//
////        given()
////                .contentType(ContentType.JSON)
////       //         .body(user1.mapToJson())
////                .body("{\"clientEmail\":\"OK@gmail.com\", \"clientName\":\"OKClient\"}")
////                .when().post("https://simple-books-api.glitch.me/api-clients/")
////                .then()
////                .statusCode(HttpStatus.SC_CONFLICT);
//
////        given()
////                .contentType(ContentType.JSON)
////                //         .body(user1.mapToJson())
////                .body("{\"clientEmail\":\"OK@gmail.com\", \"clientName\":\"OKClient\"}")
////                .when().post("https://simple-books-api.glitch.me/api-clients/")
////                .then()
////                .statusCode(HttpStatus.SC_CONFLICT);
//
//
        RestAssured.baseURI = baseURL;
        httpRequest = RestAssured.given();
//
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("clientName", testName);
        jsonObj.put("clientEmail", testEmail);

        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(jsonObj.toString());
        response = httpRequest.request(Method.POST, "/api-clients");


        String responseBody = response.getBody().prettyPrint();
        System.out.println(responseBody);

        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        String accessToken = jsonObject.get("accessToken").toString();

        System.out.println("Oauth Token with type " + accessToken);


//        String token = responseBody.toString();
//        String token2 = token.substring("accessToken".length()).trim();
//        System.out.println("TOKEN 2: -->  " +token2);
//        System.out.println("TOKEN GIVEN: "+ token);
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

//        String responseBody = response.getBody().asString();
//        System.out.println(responseBody);
    }

    public void createAnOrder (){

//        given().header("Bearer"+ acce)

    }

}
