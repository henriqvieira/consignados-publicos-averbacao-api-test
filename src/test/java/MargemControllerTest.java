import br.com.agibank.api.dto.margenController.GetMargensDTO;
import br.com.agibank.api.dto.margenController.PostMargensDTO;
import br.com.agibank.api.dto.margenController.ResponsePostMargensDTO;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Random;

import static br.com.agibank.api.provider.ConsignacoesProvider.cancelaConsignacao;
import static br.com.agibank.api.provider.MargensProvider.*;
import static br.com.agibank.api.provider.Utils.geraAutorizacao;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MargemControllerTest {
    public static PostMargensDTO postMargensDTO;
    public static ResponsePostMargensDTO responsePostMargensDTO = new ResponsePostMargensDTO();

    @BeforeTest
    public void preCondicao(){
        postMargensDTO = geraMargemZetraDTO();
        geraAutorizacao(postMargensDTO.getBeneficiario().getCpf()
                , postMargensDTO.getBeneficiario().getMatricula()
                ,postMargensDTO.getBeneficiario().getIdentificadorConvenio());
    }

    @Test(priority=2)
    public void consultaMargemExistente(){
        GetMargensDTO margensDTO = retornaMargem();
        geraAutorizacao(margensDTO.getCpfBeneficiario()
                , "00000452092"
                ,margensDTO.getIdentificadorConvenio());
        given()
                .header("idOriginacao", "consultaMargemExistente")
                .queryParam("cpfBeneficiario", margensDTO.getCpfBeneficiario())
                .queryParam("identificadorConvenio", margensDTO.getIdentificadorConvenio())
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0].orgao", CoreMatchers.containsString("PREFEITURA FLORIANOPOLIS"))
                .body(matchesJsonSchema(new File("src/test/resources/json_schemas/get_margem.json")));
    }

    @Test(priority=3)
    public void consultaMargemInexistente(){
        GetMargensDTO margensDTO = retornaMargem();
        given()
                .header("idOriginacao", "consultaMargemInexistente")
                .queryParam("cpfBeneficiario", margensDTO.getCpfBeneficiario())
                .queryParam("identificadorConvenio", "126")
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", CoreMatchers.containsString("Convenio 126 não cadastrado na base"));
    }

    @Test(priority = 1)
    public void criaMargem(){
        PostMargensDTO postMargensDTO = geraMargemDTO();
        Random r = new Random();
        ResponsePostMargensDTO responsePostMargensDTO =
        given()
                //.header("idOriginacao", "testeAuto" + r.nextInt())
                .body(postMargensDTO)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .post(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(200)
                .body("cpf", CoreMatchers.containsString(postMargensDTO.getBeneficiario().getCpf()))
                .body("situacao", CoreMatchers.containsString("0"))
                .body(matchesJsonSchema(new File("src/test/resources/json_schemas/post_margens.json")))
                .extract().as(ResponsePostMargensDTO.class);
        System.out.println("ADENUMERO:" + responsePostMargensDTO.getAdeNumero());

        cancelaConsignacao(postMargensDTO.getBeneficiario().getIdentificadorConvenio(),
                postMargensDTO.getBeneficiario().getMatricula(),
                postMargensDTO.getBeneficiario().getCpf(),
                responsePostMargensDTO.getAdeNumero(),
                "1",
                "5000");
    }

    @Test(priority=4)
    public void criaMargemCarenciaInvalida(){
        PostMargensDTO postMargensDTO = geraMargemDTO();
        postMargensDTO.getReservaMargem().setCarencia("15/08/2021");
        given()
                .header("idOriginacao", "criaMargemCarenciaInvalida")
                .body(postMargensDTO)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .post(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", CoreMatchers.containsString("Carencia Inválida"));

    }

    @Test(priority=5)
    public void criaMargemSaldoInsuficiente(){
        PostMargensDTO postMargensDTO = geraMargemDTO();
        postMargensDTO.getBeneficiario().setCpf("02356256989");
        postMargensDTO.getBeneficiario().setMatricula("00000452580");
        postMargensDTO.getReservaMargem().setValorParcela("10000.00");
        postMargensDTO.getReservaMargem().setValorLiberado("10000.00");
        postMargensDTO.getReservaMargem().setValorOperacao("10000.00");
        geraAutorizacao(postMargensDTO.getBeneficiario().getCpf()
                , postMargensDTO.getBeneficiario().getMatricula()
                ,postMargensDTO.getBeneficiario().getIdentificadorConvenio());
        given()
                .header("idOriginacao", "criaMargemSaldoInsuficiente")
                .body(postMargensDTO)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .post(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(500);
//                .body("message", CoreMatchers.containsString("Saldo de margem insuficiente"));
    }

    @Test(priority=6)
    public void criaMargemQuantidadeContratosExcedida(){
        PostMargensDTO postMargensDTO = geraMargemDTO();
        postMargensDTO.getBeneficiario().setCpf("01622666771");
        postMargensDTO.getBeneficiario().setMatricula("00000452092");
        geraAutorizacao(postMargensDTO.getBeneficiario().getCpf()
                , postMargensDTO.getBeneficiario().getMatricula()
                ,postMargensDTO.getBeneficiario().getIdentificadorConvenio());
        given()
                .header("idOriginacao", "criaMargemQuantidadeContratosExcedida")
                .body(postMargensDTO)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .post(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", CoreMatchers.containsString("Quantidade máxima de contratos excedida para a consignatária"));
    }


    @Test(priority=7)
    public void criaMargemZetra(){
        //postMargensDTO = geraMargemZetraDTO();
        Random r = new Random();
        responsePostMargensDTO =
                given()
                        .header("idOriginacao", "testeAuto" + r.nextInt())
                        .body(postMargensDTO)
                        .log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .post(BaseTest.urlBaseMargem + "/v1/margens")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("cpf", CoreMatchers.containsString(postMargensDTO.getBeneficiario().getCpf()))
                        .body("situacao", CoreMatchers.containsString("Deferida"))
                        .extract().as(ResponsePostMargensDTO.class);
        System.out.println("ADENUMERO:" + responsePostMargensDTO.getAdeNumero());

    }

    @Test(priority=8)
    public void cancelaMargemZetra(){
        cancelaConsignacao(postMargensDTO.getBeneficiario().getIdentificadorConvenio(),
                postMargensDTO.getBeneficiario().getMatricula(),
                postMargensDTO.getBeneficiario().getCpf(),
                responsePostMargensDTO.getAdeNumero(),
                "1",
                "5000");
    }

    @Test(priority=9)
    public void consultaMargemZetra(){
        GetMargensDTO margensDTO = retornaMargemZetra();
        given()
                .header("idOriginacao", "origin12345")
                .queryParam("cpfBeneficiario", margensDTO.getCpfBeneficiario())
                .queryParam("identificadorConvenio", margensDTO.getIdentificadorConvenio())
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(BaseTest.urlBaseMargem + "/v1/margens")
                .then()
                .log().all()
                .statusCode(200);
                //.body("[0].orgao", CoreMatchers.containsString("PREFEITURA FLORIANOPOLIS"));
    }
}
