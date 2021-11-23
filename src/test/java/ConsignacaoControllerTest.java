import br.com.agibank.api.dto.consignacaoController.CancelaConsignacaoDTO;
import br.com.agibank.api.dto.margenController.ResponsePostMargensDTO;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static br.com.agibank.api.provider.ConsignacoesProvider.*;
import static br.com.agibank.api.provider.MargensProvider.geraMargem;
import static br.com.agibank.api.provider.Utils.geraAutorizacao;
import static io.restassured.RestAssured.given;

public class ConsignacaoControllerTest {
    private static ResponsePostMargensDTO responsePostMargensDTO;

    @BeforeClass
    public void preCondicao(){
        responsePostMargensDTO = geraMargem();
        geraAutorizacao(responsePostMargensDTO.getCpf()
                , responsePostMargensDTO.getMatricula()
                ,"PREF_FLORIANOPOLIS");
    }

    @Test
    public void cancelaConsignacaoAdeInexistente(){
        CancelaConsignacaoDTO cancelaConsignacaoAdeInexistente =
                geraCancelaConsignacaoDTO("PREF_FLORIANOPOLIS", "123456", "01234567890", "123456", "1", "5000");
        given()
                .header("idOriginacao", "teste")
                .body(cancelaConsignacaoAdeInexistente)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .put(BaseTest.urlBaseMargem + "/v1/consignacoes/cancelar")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", CoreMatchers.containsString("Ade não Localizada"));
    }

    @Test(priority = 1)
    public void cancelaConsignacaoMotivoInvalido(){
        CancelaConsignacaoDTO cancelaConsignacaoMotivoInvalido =
                geraCancelaConsignacaoDTO("PREF_FLORIANOPOLIS"
                        , responsePostMargensDTO.getMatricula()
                        , responsePostMargensDTO.getCpf()
                        , responsePostMargensDTO.getAdeNumero()
                        , "0"
                        , "5000");
        given()
                .header("idOriginacao", "teste")
                .body(cancelaConsignacaoMotivoInvalido)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .put(BaseTest.urlBaseMargem + "/v1/consignacoes/cancelar")
                .then()
                .log().all()
                .statusCode(500)
                .body(CoreMatchers.containsString("Motivo Inválido"));
    }


    @Test(priority = 2)
    public void cancelaConsignacaoJaQuitada(){
        CancelaConsignacaoDTO cancelaConsignacaoJaQuitada =
                geraCancelaConsignacaoDTO("PREF_FLORIANOPOLIS", "00000452092", "01622666771", "6181696", "1", "5000");
        given()
                .header("idOriginacao", "teste")
                .body(cancelaConsignacaoJaQuitada)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .put(BaseTest.urlBaseMargem + "/v1/consignacoes/cancelar")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", CoreMatchers.containsString("Consignação já quitada"));
    }

    @Test(priority = 3)
    public void cancelaConsignacaoComSucesso(){
        CancelaConsignacaoDTO cancelaConsignacaoMotivoInvalido =
                geraCancelaConsignacaoDTO("PREF_FLORIANOPOLIS"
                        , responsePostMargensDTO.getMatricula()
                        , responsePostMargensDTO.getCpf()
                        , responsePostMargensDTO.getAdeNumero()
                        , "1"
                        , "5000");
        given()
                .header("idOriginacao", "teste")
                .body(cancelaConsignacaoMotivoInvalido)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .put(BaseTest.urlBaseMargem + "/v1/consignacoes/cancelar")
                .then()
                .log().all()
                .statusCode(200)
                .body(CoreMatchers.containsString("Consignação cancelada com sucesso"));
    }
}
