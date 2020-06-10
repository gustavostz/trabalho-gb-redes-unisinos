package br.com.unisinos.redes.naruto.response;

import br.com.unisinos.redes.naruto.domain.Ninja;
import br.com.unisinos.redes.naruto.domain.StatusPartida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatalhaResponse {
    private Ninja ninjaOponente;
    private Ninja ninjaAtual;
    private StatusPartida statusPartida;
}
