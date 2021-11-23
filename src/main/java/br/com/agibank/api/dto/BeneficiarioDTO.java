package br.com.agibank.api.dto;
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
public class BeneficiarioDTO {
    private String cpf;
    private String matricula;
    private String numeroContratoInterno;
    private String identificadorConvenio;
    private String conta;
    private String agencia;
    private String banco;
}
