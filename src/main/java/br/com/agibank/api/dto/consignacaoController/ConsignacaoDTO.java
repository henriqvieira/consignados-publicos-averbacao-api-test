package br.com.agibank.api.dto.consignacaoController;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsignacaoDTO {
    private String identificadorConvenio;
    private String matricula;
    private String cpf;
    private int adeNumero;
    private int motivoOperacao;
    private String numeroContratoInterno;
    private int valorLiquidacao;
}
