package br.com.agibank.api.dto.consignacaoController;

import br.com.agibank.api.dto.BeneficiarioDTO;
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
public class CancelaConsignacaoDTO {
    private String identificadorConvenio;
    private String matricula;
    private String cpf;
    private String adeNumero;
    private String motivoOperacao;
    private String numeroContratoInterno;
    private String valorLiquidacao;

}
