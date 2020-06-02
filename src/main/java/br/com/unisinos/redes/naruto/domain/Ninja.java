package br.com.unisinos.redes.naruto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Ninja {

    private String name;
    private int ataqueNormal;
    private Jutso poder;
    private int vida;
    private int chackra;
    private Jutso jutsoAtivo;
}