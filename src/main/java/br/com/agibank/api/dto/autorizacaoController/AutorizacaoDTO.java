package br.com.agibank.api.dto.autorizacaoController;

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
public class AutorizacaoDTO {
    private String cpf;
    private String matricula;
    private String loginServidor;
    private String dataNascimento;
    private String convenio;
    private TokenDTO token;
}
