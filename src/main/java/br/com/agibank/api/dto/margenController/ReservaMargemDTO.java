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
public class ReservaMargemDTO {
    private String codigoVerba;
    private String valorParcela;
    private String carencia;
    private String prazo;
    private String valorLiberado;
    private String valorOperacao;
    private String valorAuxilio;
    private String carenciaLong;
}
