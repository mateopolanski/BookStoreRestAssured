import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class APIStatusTest {


    String statusOK = "HTTP/1.1 200 OK";
    String jsonValidation = "{\"status\":\"OK1\"}";
    
    @Test
    public void getApiServiceStatusGWT(){

        given()
                .when().get("https://simple-books-api.glitch.me/status")
                .then()
                .statusCode(200).statusLine(statusOK).assertThat().body(equalTo(jsonValidation));

    }
/*
    singleton/ builder

    main/java - konfiguracja np docker, wrappery, singletony
    tworzenie payloada w main
            ++++++++++
    w tescie test-> validatory
    zmienne w pakiecie w klasie test
    endpointy w CONST
    kwestia headerow w rest assured
    do pakietow wyrzucic np DataGenerator
    model payload'u adekwatna do tego co robi w pakiecie np model,
    dynamicznie wyciaganie z odpowiedzi asercji np "Jas Fasola",

    -wszystkie testy, wszystko zeby dzialalo jako wzorzec w folderach, pakietach
    -> branch framework ===>
    -> testy najpierw w rest asssurdzie,
    -> pozniej singleton jako refactor kodu,

    ->target Mon


 */
/*


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
