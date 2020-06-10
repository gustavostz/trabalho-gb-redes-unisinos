package br.com.unisinos.redes.naruto.domain;


import java.util.ArrayList;
import java.util.List;

public class ControleDeJogo {
    private  int idJogadorAtual = -1;
    private  List<Integer> idUsers = new ArrayList<>();
    private Ninja ninjaVencedor;

    public  void setIdJogadorAtual(int idJogadorAtual) {
        this.idJogadorAtual = idJogadorAtual;
    }

    public  int getIdJogadorAtual() {
        return idJogadorAtual;
    }

    public  List<Integer> getIdUsers() {
        return idUsers;
    }

    public Integer getIdAdversario(){
        return idUsers.stream().filter(item -> item != idJogadorAtual).findFirst().get();
    }

    public  void atualizaJogadorAtual(){
       this.idJogadorAtual = idUsers.stream().filter(item -> item != idJogadorAtual).findFirst().get();
    }

    public  void addUser(int userId) {
        if(this.idJogadorAtual == -1){
            this.idJogadorAtual = userId;
        }
        idUsers.add(userId);
    }

    public  boolean isVezDesteJogador(int idUser){
        return idJogadorAtual == idUser;
    }

    public void finalizarPartida(Ninja ninjaVencedor) {
        this.ninjaVencedor = ninjaVencedor;
    }

    public Ninja getNinjaVencedor() {
        return ninjaVencedor;
    }
}
