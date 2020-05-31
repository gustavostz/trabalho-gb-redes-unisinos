package src.main.java.br.com.unisinos.redes.naruto.domain;

public class Ninja {

    private Jutso poder;
    private int vida;
    private int chackra;

    private Jutso jutsoAtivo;

    public Ninja(Jutso poder, int vida, int chackra) {
        this.poder = poder;
        this.vida = vida;
        this.chackra = chackra;
    }
}