package br.com.agibank.api.provider;

import br.com.agibank.api.dto.consignacaoController.CancelaConsignacaoDTO;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class ConsignacoesProvider {
    public static CancelaConsignacaoDTO geraCancelaConsignacaoDTO(String identificadorConvenio
    ,String matricula
    ,String cpf
    ,String adeNumero
    ,String motivoOperacao
    ,String valorLiquidacao){

        return CancelaConsignacaoDTO
                .builder()
                .identificadorConvenio(identificadorConvenio)
                .matricula(matricula)
                .cpf(cpf)
                .adeNumero(adeNumero)
                .motivoOperacao(motivoOperacao)
                .numeroContratoInterno("84074070")
                .valorLiquidacao(valorLiquidacao)
                .build();
    }

    public static void cancelaConsignacao(String identificadorConvenio
            ,String matricula
            ,String cpf
            ,String adeNumero
            ,String motivoOperacao
            ,String valorLiquidacao){
        CancelaConsignacaoDTO cancelaConsignacaoDTO = geraCancelaConsignacaoDTO(identificadorConvenio
                ,matricula
                ,cpf
                ,adeNumero
                ,motivoOperacao
                ,valorLiquidacao);
        given()
                .header("idOriginacao", "teste")
                .body(cancelaConsignacaoDTO)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .put("https://consignados-publicos-averbacao-service-consignaveis-publicos.agibank-hom.in/v1/consignacoes/cancelar")
                .then()
                .log().all()
                .statusCode(200);
    }
}
