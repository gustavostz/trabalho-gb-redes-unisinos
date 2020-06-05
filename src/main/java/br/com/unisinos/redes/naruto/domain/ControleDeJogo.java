package br.com.unisinos.redes.naruto.domain;

public class ControleDeJogo {
    boolean iniciarJogo;

    public ControleDeJogo(boolean iniciarJogo) {
        this.iniciarJogo = iniciarJogo;
    }

    public boolean isIniciarJogo() {
        return iniciarJogo;
    }

    public void setIniciarJogo(boolean iniciarJogo) {
        this.iniciarJogo = iniciarJogo;
    }
}
