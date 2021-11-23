package br.com.agibank.api.dto.margenController;
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
public class GetMargensDTO {
    private String idOriginacao;
    private String cpfBeneficiario;
    //private String matricula;
    private String identificadorConvenio;
}
