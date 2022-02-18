import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class APIStatusTest {


    String statusOK = "HTTP/1.1 200 OK";
    String jsonValidation = "{\"status\":\"OK\"}";
    
    @Test
    public void getApiServiceStatusGWT(){

        given()
                .when().get("https://simple-books-api.glitch.me/status")
                .then()
                .statusCode(200).statusLine(statusOK).assertThat().body(equalTo(jsonValidation));

    }

    /*
    2.
    //architektura
    - wzorzec projektowy ( dla przykladu - utils, models itd):
    - np dekorator, builder, most itd.
    --> url
    --> asercje
    --> serializacja, deserializacja
    --> pattern
    1.
    -->send http dla BDD
    -->poprawic na send request (create)




     */
}
