package br.com.unisinos.redes.naruto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Jutsu {

    private String nome;
    private String atributo;
    private int dano;
    private int chackaConsumido;
//    private TIPOJUTSO tipo;
}