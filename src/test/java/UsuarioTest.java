import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;


@DisplayName(" Validando Registro do usuario")
public class UsuarioTest {

    private String token;



@BeforeEach
    public void beforeEach() {
        //configurando dados da API rest
        RestAssured.baseURI = "https://reqres.in";


        // Obter o token do usuario
        this.token = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "     \"email\":  \"eve.holt@reqres.in\" ,\n" +
                        "     \"password\":  \"pistol\" \n" +
                        "}")
                .when()
                .post("/api/register")
                .then()
                .extract()
                .path("token");

        System.out.println(token);

    }

@Test
@DisplayName("Validar login do cliente")
    public void testValidarLoginDoCliente() {

        given()
                .contentType(ContentType.JSON)
                .header("token", token)
                .body("{\n" +
                        "     \"email\":  \"eve.holt@reqres.in\" ,\n" +
                        "     \"password\":  \"cityslicka\" \n" +
                        "}")
            .when()
                .post("/api/login")
            .then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));



    }

@Test
@DisplayName("Validar o single usuario")
    public void testValidarSingleUsuario() {


        given()
                //   .contentType(ContentType.JSON)
                //  .header("token", token)
                .when()
                .get("/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .assertThat()
                .body(containsString("data"))
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"))
                .body(containsString("support"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));


        //.body("id", is(2))
        //.body("email", equalTo("janet.weaver@reqres.in"))
    }

@Test
@DisplayName("Validar alteração de dados do usuario")
        public void testValidarAlteracaoDeDados() {

    given()
            .contentType(ContentType.JSON)
            .body("{\n" +
                    "     \"name\":  \"Teste APIzona\" ,\n" +
                    "     \"job\":  \"residente de zion\" \n" +
                    "}")
        .when()
            .put("/api/users/2")
        .then()
            .log().all()
            .statusCode(200)
            .assertThat()
            .body("name", is("Teste APIzona"))
            .body("job", is("residente de zion"));


}
@Test
@DisplayName("Validar Exclusão De Usuario")
    public void testValidarExclusaoDeUsuario() {

        given()
                .contentType(ContentType.JSON)
            .when()
                .delete("/api/users/2")
            .then()
                .log().all()
                .statusCode(204);


    }
}


