import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import netscape.javascript.JSObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class APIStatusTest {


    RequestSpecification requestSpecification;
    Response response;
    ValidatableResponse validatableResponse;
    String statusOK = "HTTP/1.1 200 OK";
    String jsonValidation = "{\"status\":\"OK\"}";


    @Test
    public void getApiServiceStatus(){

        RestAssured.baseURI = "https://simple-books-api.glitch.me/status";
        requestSpecification = given();
        response = requestSpecification.get();

        String resString = response.asPrettyString();
        System.out.println("Response Message: " + resString);

/// ValidatableResponse Assertions As Optional
        validatableResponse = response.then();

        validatableResponse.statusCode(200);

        validatableResponse.statusLine(statusOK);

//JUnit assertions

        int statusCode = response.getStatusCode();
        Assertions.assertEquals(statusCode, 200);


        String statusLine = response.getStatusLine();
        Assertions.assertEquals(statusLine, statusOK);
    }
    
    
    
//same test below but using shorthand RestAssured Methods
    
    @Test
    public void getApiServiceStatusGWT(){

        given()
                .when().get("https://simple-books-api.glitch.me/status")
                .then()
                .statusCode(200).statusLine(statusOK).assertThat().body(equalTo(jsonValidation));

    }

}
