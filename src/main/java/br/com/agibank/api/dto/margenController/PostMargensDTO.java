package br.com.agibank.api.dto.margenController;
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
public class PostMargensDTO {
    private BeneficiarioDTO beneficiario;
    private ReservaMargemDTO reservaMargem;
    private String averbadora;
}
