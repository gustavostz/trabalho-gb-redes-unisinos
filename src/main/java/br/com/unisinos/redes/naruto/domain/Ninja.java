package br.com.unisinos.redes.naruto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Ninja {
    private int idNinja;
    private String name;
    private int ataqueNormal;
    private Jutso poder;
    private int vida;
    private int chackra;
    private Jutso jutsoAtivo;
}