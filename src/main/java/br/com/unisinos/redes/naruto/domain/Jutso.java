package br.com.unisinos.redes.naruto.domain;

public class Jutso {
    private int atributo;
    private int chackaConsumido;
    private TIPOJUTSO tipo;

    public Jutso(int atributo,int chackaConsumido,TIPOJUTSO tipo){
        this.atributo = atributo;
        this.chackaConsumido = chackaConsumido;
        this.tipo = tipo;
    }
}