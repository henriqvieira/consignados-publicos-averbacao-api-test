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
public class ResponsePostMargensDTO {
    private String servidor;
    private String cpf;
    private String matricula;
    private String nroContrato;
    private String dataReserva;
    private String adeNumero;
    private String situacao;
}
