package tests;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import models.JSONAuth;


public class BaseTest {

    Response response;
    JSONAuth jsonAuth = new JSONAuth();
    RequestSpecification requestSpecification;
    ResponseBody responseBody;

}
