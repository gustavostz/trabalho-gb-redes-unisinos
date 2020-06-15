package br.com.unisinos.redes.naruto.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
public class ColecaoNinja {


    public static List<Ninja> getColecaoNinja(){
        List<Ninja> colecaoNinja = new ArrayList<>();
        colecaoNinja.add(criarNaruto());
        colecaoNinja.add(criarSasuke());
        colecaoNinja.add(criarItachi());
        colecaoNinja.add(criarKakashi());
        colecaoNinja.add(criarLee());
        colecaoNinja.add(criarNeji());
        colecaoNinja.add(criarGaara());
        colecaoNinja.add(criarGuy());
        colecaoNinja.add(criarMinato());
        colecaoNinja.add(criarObito());
        colecaoNinja.add(criarSakura());
        return colecaoNinja;
    }

    public static List<String> stringListaPersonagens(List<Ninja> colecaoNinja){
        List<String> listaPersonagens = new ArrayList<>();
        
        listaPersonagens.add("==================Catalago Ninja==================");
        listaPersonagens.add("||                                              ||");
        listaPersonagens.add("||                                              ||");
        listaPersonagens.add("||             Escolha Seu personagem:          ||");
        listaPersonagens.add("||                 -Digite o Nome-              ||");
        listaPersonagens.add("||                                              ||");


        colecaoNinja.forEach(ninja -> {
            int numeroDeEspacos = Math.max(0, 42 - ninja.getName().length());

            StringBuilder espacos = new StringBuilder();
            for (int i = 0; i < numeroDeEspacos; i++) {
                espacos.append(" ");
            }

            listaPersonagens.add("|| -> "+ ninja.getName() + espacos.toString() + "||");
        });

        listaPersonagens.add("||                                              ||");
        listaPersonagens.add("==================================================");

        return listaPersonagens;
    }

    public static Ninja criarNaruto(){
        Ninja naruto = new Ninja();
        naruto.setAtaqueNormal(30);
        naruto.setChackra(200);
        naruto.setChackraMax(200);
        naruto.setName("Naruto");
        naruto.setVida(200);
        naruto.setVidaMax(200);
        Jutsu jutsu = new Jutsu("Rasengan",TIPOJUTSO.DANO,45,40);
        naruto.setJutsu(jutsu);
        return  naruto;
    }
    public static Ninja criarSasuke(){
        Ninja sasuke = new Ninja();
        sasuke.setAtaqueNormal(35);
        sasuke.setChackra(100);
        sasuke.setChackraMax(100);
        sasuke.setName("Sasuke");
        sasuke.setVida(200);
        sasuke.setVidaMax(200);
        Jutsu jutsu = new Jutsu("Chidori",TIPOJUTSO.DANO,60,60);
        sasuke.setJutsu(jutsu);
        return  sasuke;
    }
    public static Ninja criarNeji(){
        Ninja neji = new Ninja();
        neji.setAtaqueNormal(20);
        neji.setChackra(120);
        neji.setChackraMax(120);
        neji.setName("Neji");
        neji.setVida(170);
        neji.setVidaMax(170);
        Jutsu jutsu = new Jutsu("Oito Trigramas Sessenta e Quatro Palmas",TIPOJUTSO.DANO,40,40);
        neji.setJutsu(jutsu);
        return  neji;
    }
    public static Ninja criarKakashi(){
        Ninja kakashi = new Ninja();
        kakashi.setAtaqueNormal(40);
        kakashi.setChackra(100);
        kakashi.setChackraMax(100);
        kakashi.setName("Kakashi");
        kakashi.setVida(220);
        kakashi.setVidaMax(220);
        Jutsu jutsu = new Jutsu("Raikiri",TIPOJUTSO.DANO,45,50);
        kakashi.setJutsu(jutsu);
        return kakashi;
    }
public static Ninja criarGaara(){
        Ninja gaara = new Ninja();
        gaara.setAtaqueNormal(20);
        gaara.setChackra(110);
        gaara.setChackraMax(110);
        gaara.setName("Gaara");
        gaara.setVida(300);
        gaara.setVidaMax(300);
        Jutsu jutsu = new Jutsu("Caixão de areia",TIPOJUTSO.DANO,30,30);
        gaara.setJutsu(jutsu);
        return gaara;
    }
    public static Ninja criarLee(){
        Ninja rockLee = new Ninja();
        rockLee.setAtaqueNormal(50);
        rockLee.setChackra(70);
        rockLee.setChackraMax(70);
        rockLee.setName("Rock Lee");
        rockLee.setVida(180);
        rockLee.setVidaMax(180);
        Jutsu jutsu = new Jutsu("Furacão da folha",TIPOJUTSO.DANO,60,70);
        rockLee.setJutsu(jutsu);
        return rockLee;
    }
    public static Ninja criarGuy(){
        Ninja guy = new Ninja();
        guy.setAtaqueNormal(55);
        guy.setChackra(70);
        guy.setChackraMax(70);
        guy.setName("Might Guy");
        guy.setVida(160);
        guy.setVidaMax(160);
        Jutsu jutsu = new Jutsu("Pavão da Manhã",TIPOJUTSO.DANO,70,70);
        guy.setJutsu(jutsu);
        return guy;
    }
    public static Ninja criarObito(){
        Ninja obito = new Ninja();
        obito.setAtaqueNormal(40);
        obito.setChackra(55);
        obito.setChackraMax(55);
        obito.setName("Obito");
        obito.setVida(160);
        obito.setVidaMax(160);
        Jutsu jutsu = new Jutsu("Kamui",TIPOJUTSO.DANO,50,20);
        obito.setJutsu(jutsu);
        return obito;
    }
    public static Ninja criarMinato(){
        Ninja minato = new Ninja();
        minato.setAtaqueNormal(40);
        minato.setChackra(55);
        minato.setChackraMax(55);
        minato.setName("Minato");
        minato.setVida(160);
        minato.setVidaMax(160);
        Jutsu jutsu = new Jutsu("Teleporte",TIPOJUTSO.DANO,50,20);
        minato.setJutsu(jutsu);
        return minato;
    }
    public static Ninja criarSakura(){
        Ninja sakura = new Ninja();
        sakura.setAtaqueNormal(40);
        sakura.setChackra(100);
        sakura.setChackraMax(100);
        sakura.setName("Sakura");
        sakura.setVida(250);
        sakura.setVidaMax(250);
        Jutsu jutsu = new Jutsu("Impulso do Punho Certeiro",TIPOJUTSO.DANO,45,30);
        sakura.setJutsu(jutsu);
        return sakura;
    }
    public static Ninja criarItachi(){
        Ninja itachi = new Ninja();
        itachi.setAtaqueNormal(40);
        itachi.setChackra(100);
        itachi.setChackraMax(100);
        itachi.setName("Itachi");
        itachi.setVida(250);
        itachi.setVidaMax(250);
        Jutsu jutsu = new Jutsu("Susano",TIPOJUTSO.DANO,100,100);
        itachi.setJutsu(jutsu);
        return itachi;
    }
}
