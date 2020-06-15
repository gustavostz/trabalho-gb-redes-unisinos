package br.com.unisinos.redes.naruto.domain;

import lombok.*;

import java.util.Random;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idNinja")
public class Ninja {
    private int idNinja;
    private String name;
    private int ataqueNormal;
    private Jutsu jutsu;
    private int vida;
    private int chackra;
    private int chackraMax;
    private int vidaMax;

    public Ninja atacar(Ninja oponente){
//        Colocar uma probabilidade de 50% de errar o golpe por exemplo
//        Colocar uma probabilidade de 10% de dar dano critico(1.5x o dano)
        oponente.setVida(oponente.getVida() - ataqueNormal);
        return oponente;
    }

    public Ninja usarJutsu(Ninja oponente) {
        if(jutsu.getChackaConsumido() > chackra){
            System.out.println("Você tentou usar um jutsu sem chackra e por não administrar seu chackra perdeu sua vez!");
            return oponente;
        }
//        Colocar uma probabilidade de 10% de dar dano critico(2x o dano)
        System.out.println("Você lançou o Jutsu "+ jutsu.getNome() +" do tipo "+ jutsu.getTipo() + " e tirou " + jutsu.getDano() + " de vida!!");
        oponente.setVida(oponente.getVida() - jutsu.getDano());
        return oponente;
    }

    public boolean isVivo(){
        return vida > 0;
    }

    public void printaAtaquesDisponiveisEStatus(){
        System.out.println(getStatus().replace("quebraLinha", "\n"));
        System.out.println("Você pode usar os seguintes ataques:");
        System.out.println("[1] " + "Ataque Normal  -  Dano: " + ataqueNormal + " -> Tens 50% de chance de errar e 10% de crítico");
        System.out.println("[2] " + jutsu.getNome() +"  -  Dano: " + jutsu.getDano() + " -> Tens 10% de mega crítico e gasta " + jutsu.getChackaConsumido() + " de chackra \n");
    }

    public String getStatus(){
        String status = ("Personagem: " + name + " quebraLinha");
        return status + ("Vida Atual: " + vida + "/" + vidaMax + " \t-\t Chackra Atual: " + chackra + "/" + chackraMax);
    }

    public void recuperaPoucoChackra() {
        int chackraASerRecuperado =  new Random().nextInt(11); //Recupera entre 0 e 10 de chackra
        chackra = chackra + Math.min(chackraASerRecuperado, chackraMax-chackra); //Ou recupera o esperado ou recupera oq faltava para o chackra maximo.
    }


    public String printNinja(){
        String retorno = String.format("nome: %s \nvida: %d \nchackra: %d\nAtaque Basico: %d(dano)\nJutso:\n%s: ",
                this.name,this.vida,this.chackra,this.ataqueNormal,this.jutsu.toString());
        return retorno;
    }
}