package br.com.unisinos.redes.naruto.domain;


import java.util.ArrayList;
import java.util.List;

public class ControleDeJogo {
    private static int idJogadorAtual = -1;
    private static List<Integer> idUsers = new ArrayList<>();

    public static void setIdJogadorAtual(int idJogadorAtual) {
        ControleDeJogo.idJogadorAtual = idJogadorAtual;
    }

    public static int getIdJogadorAtual() {
        return idJogadorAtual;
    }

    public static List<Integer> getIdUsers() {
        return idUsers;
    }

    public static void atualizaJogadorAtual(){
        if(idJogadorAtual == idUsers.get(idUsers.size()-1)){
            idJogadorAtual = idUsers.get(0);
        } else {
            idJogadorAtual = idUsers.get(idUsers.indexOf(idJogadorAtual) + 1);
        }
    }

    public static void addUser(int userId) {
        idUsers.add(userId);
    }

    public static boolean isVezDesteJogador(int idUser){
        return idJogadorAtual == idUser;
    }
}
