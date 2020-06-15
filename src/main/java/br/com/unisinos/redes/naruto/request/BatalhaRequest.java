package br.com.unisinos.redes.naruto.request;

import br.com.unisinos.redes.naruto.domain.Jutsu;
import br.com.unisinos.redes.naruto.domain.TipoAtaque;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatalhaRequest {
    private TipoAtaque ataque;
}
