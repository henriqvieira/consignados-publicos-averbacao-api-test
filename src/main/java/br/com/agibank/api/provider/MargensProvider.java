package br.com.agibank.api.provider;

import br.com.agibank.api.dto.BeneficiarioDTO;
import br.com.agibank.api.dto.ConvenioDTO;
import br.com.agibank.api.dto.margenController.*;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class MargensProvider {
    public static GetMargensDTO retornaMargem(){
        return GetMargensDTO
                .builder()
                .idOriginacao("testeAuto858601436")
                .cpfBeneficiario("02356256989")
                //.matricula("00000452092")
                .identificadorConvenio("PREF_FLORIANOPOLIS")
                .build();
    }
    public static GetMargensDTO retornaMargemZetra(){
        return GetMargensDTO
                .builder()
                .cpfBeneficiario("00133571319")
                //.matricula("180968")
                .identificadorConvenio("AGIBANK-MODELO")
                .build();
    }

    public static PostMargensDTO geraMargemDTO(){
        return PostMargensDTO
                .builder()
                .beneficiario(geraBeneficiarioDTO())
                .reservaMargem(geraReservaMargem())
                .averbadora("CONSIGLOG")
                .build();
    }

    public static PostMargensDTO geraMargemZetraDTO(){
        BeneficiarioDTO beneficiarioDTO = geraBeneficiarioDTO();
        beneficiarioDTO.setMatricula("81634");
        beneficiarioDTO.setCpf("00133571319");
        beneficiarioDTO.setIdentificadorConvenio("AGIBANK-MODELO");
        ReservaMargemDTO reservaMargemDTO = geraReservaMargemZetra();
        reservaMargemDTO.setCodigoVerba("150");
        return PostMargensDTO
                .builder()
                .beneficiario(beneficiarioDTO)
                .reservaMargem(reservaMargemDTO)
                .averbadora("ZETRA")
                .build();
    }



    public static BeneficiarioDTO geraBeneficiarioDTO(){
        String cpf = "53033310087";
        return BeneficiarioDTO
                .builder()
                .cpf(cpf)
                .matricula("00000452726")
                //.nroContrato("0")
                .numeroContratoInterno("84074070")
                .identificadorConvenio("PREF_FLORIANOPOLIS")
                .banco("1")
                .agencia("0001")
                .conta("12345")
                .build();
    }

    public static ReservaMargemDTO geraReservaMargem(){
        return ReservaMargemDTO
                .builder()
                .codigoVerba("8135")
                .valorParcela("20.00")
                .valorLiberado("200.00")
                .valorOperacao("200.00")
                .valorAuxilio("0.0")
                .carencia("01/11/2021")
                .prazo("12")
                .build();
    }

    public static ReservaMargemDTO geraReservaMargemZetra(){
        return ReservaMargemDTO
                .builder()
                .codigoVerba("8135")
                .valorParcela("20.00")
                .valorLiberado("200.00")
                .valorOperacao("200.00")
                .valorAuxilio("0.0")
                .carencia("10/10/2021")
                .prazo("12")
                .build();
    }

    public static ResponsePostMargensDTO geraMargem(){
        PostMargensDTO postMargensDTO = geraMargemDTO();

        ResponsePostMargensDTO responsePostMargensDTO =
                given()
                        .body(postMargensDTO)
                        .log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .post("http://consignados-publicos-averbacao-service-consignaveis-publicos.agibank-hom.in/v1/margens")
                        .then()
                        .statusCode(200)
                        .log().all()
                        .body("cpf", CoreMatchers.containsString(postMargensDTO.getBeneficiario().getCpf()))
                        .body("situacao", CoreMatchers.containsString("0"))
                        .extract().as(ResponsePostMargensDTO.class);

        return responsePostMargensDTO;
    }
}
