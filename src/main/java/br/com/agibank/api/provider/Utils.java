package br.com.agibank.api.provider;

import br.com.agibank.api.dto.autorizacaoController.AutorizacaoDTO;
import br.com.agibank.api.dto.autorizacaoController.TokenDTO;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;

import static io.restassured.RestAssured.given;

public class Utils {
    private static String calcDigVerif(String num) {
        Integer primDig, segDig;
        int soma = 0, peso = 10;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
        if (soma % 11 == 0 | soma % 11 == 1)
            primDig = new Integer(0);
        else
            primDig = new Integer(11 - (soma % 11));
        soma = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
        soma += primDig.intValue() * 2;
        if (soma % 11 == 0 | soma % 11 == 1)
            segDig = new Integer(0);
        else
            segDig = new Integer(11 - (soma % 11));
        return primDig.toString() + segDig.toString();
    }

    public static String geraCPF() {
        String iniciais = "";
        Integer numero;
        for (int i = 0; i < 9; i++) {
            numero = new Integer((int) (Math.random() * 10));
            iniciais += numero.toString();
        }
        return iniciais + calcDigVerif(iniciais);
    }

    public static void geraAutorizacao(String cpf, String matricula, String identificadorConvenio) {
        switch (identificadorConvenio) {
            case ("PREF_FLORIANOPOLIS"):
                AutorizacaoDTO consig =AutorizacaoDTO
                        .builder()
                        .cpf(cpf)
                        .matricula(matricula)
                        .loginServidor("044784")
                        .dataNascimento("1970-07-06")
                        .convenio(identificadorConvenio)
                        .token(TokenDTO.builder().token("consig#2589").build())
                        .build();
                postaAutorizacao(consig);
                break;
            case ("AGIBANK-MODELO"):
                AutorizacaoDTO zetra = AutorizacaoDTO
                        .builder()
                        .cpf(cpf)
                        .matricula(matricula)
                        .dataNascimento("1970-07-06")
                        .convenio(identificadorConvenio)
                        .token(TokenDTO.builder().token("abc12345").build())
                        .build();
                postaAutorizacao(zetra);
                break;
        }
    }

    public static void postaAutorizacao(AutorizacaoDTO autorizacaoDTO){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(autorizacaoDTO)
                .post("https://consignados-publicos-averbacao-service-consignaveis-publicos.agibank-hom.in/v1/autorizacoes/convenios/tokens")
                .then()
                .log().all()
                .statusCode(201);
    }
}
