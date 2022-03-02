package basetest;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import utilities.Generators;


public class BaseTest {

    public Response response;
    public RequestSpecification httpRequest;

    public String baseURL = "https://simple-books-api.glitch.me";
    public String statusURL = "https://simple-books-api.glitch.me/status";

    String testName = Generators.genName();
    String testEmail = Generators.genEmail();

    public void printResponse(){

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);

    }

    public String getAccessToken (){


        JSONObject requestParams = new JSONObject();
        requestParams.put("clientName", testName);
        requestParams.put("customerName", testEmail);
        httpRequest.body(requestParams.toString());
        response = httpRequest.request(Method.POST, "/api-clients");

        JsonPath jsonPath = new JsonPath(String.valueOf(requestParams)).setRootPath
                ("/api-clients");

        String token = jsonPath.getString("accessToken");
        String tokenId = jsonPath.get("accessToken");
        return tokenId;
    }






}
