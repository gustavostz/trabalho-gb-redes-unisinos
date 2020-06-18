package br.com.unisinos.redes.naruto.response;

import br.com.unisinos.redes.naruto.domain.Ninja;
import br.com.unisinos.redes.naruto.domain.StatusPartida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatalhaResponse {
    private List<Ninja> ninjaOponentes;
    private Ninja ninjaAtual;
    private StatusPartida statusPartida;
}
