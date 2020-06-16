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

    public String atacar(Ninja oponente){
        String retorno;
        boolean acertouOGolpe = (Math.random() <= 0.5);
        if(!acertouOGolpe){
            return "você errou o golpe!!";
        }
        if(Math.random() >= 0.1) {
            oponente.setVida(oponente.getVida() - ataqueNormal);
            retorno = String.format("Acertou e causou %d de dano no inimigo.", ataqueNormal);

        }
        else{
            int ataqueCritado = (ataqueNormal * 2);
            oponente.setVida(oponente.getVida() - ataqueCritado);
            retorno = String.format("Acertou um CRITICO e causou %d de dano no inimigo!", ataqueCritado);

        }
        return retorno;
    }

    public String usarJutsu(Ninja oponente) {
        String retorno = "";
        if(jutsu.getChackaConsumido() > chackra){
            retorno = "Você tentou usar um jutsu sem chackra e por não administrar seu chackra perdeu sua vez!";
            return retorno;
        }
        if(Math.random() >= 0.1) {
            oponente.setVida(oponente.getVida() - jutsu.getDano());
            retorno = String.format("Você lançou o Jutsu %s do tipo %s e tirou %d de vida!!",jutsu.getNome(),jutsu.getTipo(),jutsu.getDano());
        }
        else{
            int ataqueCritado = (jutsu.getDano() * 2);
            oponente.setVida(oponente.getVida() - ataqueCritado);
            retorno = String.format("Você lançou o Jutsu %s do tipo %s e acertou um CRITICO tirando %d de vida!!",jutsu.getNome(),jutsu.getTipo(),ataqueCritado);
        }
        int chackraRestante = this.chackra - this.jutsu.getChackaConsumido();
        this.setChackra(chackraRestante);
        return retorno;
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