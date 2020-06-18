package br.com.unisinos.redes.naruto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Jutsu {

    private String nome;
    private TIPOJUTSO tipo;
    private int dano;
    private int chackaConsumido;

    @Override
    public String toString() {
        return String.format("nome Justo: %s\n Dano: %d \nChacka Consumido: %d", this.nome, this.dano, this.chackaConsumido);
    }

}