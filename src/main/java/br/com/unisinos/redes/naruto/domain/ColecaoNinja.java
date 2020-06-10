package br.com.unisinos.redes.naruto.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class ColecaoNinja {


    public static List<Ninja> getColecaoNinja(){
        List<Ninja> colecaoNinja = new ArrayList<>();
        List<String> nomesNinja = Arrays.asList("Naruto", "Sasuke", "Neji", "Kakashi", "Gaara", "Rock Lee", "Gaya", "Obito", "Minato", "Sakura", "Itachi");

        nomesNinja.forEach(nomeNinja -> colecaoNinja.add(createNinjaRandom(nomeNinja)));

        return colecaoNinja;
    }

    public static Ninja createNinjaRandom(String name) {
        int chackraMax = new Random().nextInt(101); // chackra entre 0 e 100
        int vidaMax =  new Random().nextInt(301) + 10; // vida entre 10 e 300
        return Ninja.builder()
                .name(name)
                .ataqueNormal(new Random().nextInt(251) + 10) // ataque entre 10 e 250
                .chackra(chackraMax)
                .chackraMax(chackraMax)
                .jutsu(getRandomJutsu(chackraMax))
                .vida(vidaMax)
                .vidaMax(vidaMax)
                .build();
    };

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

    private static Jutsu getRandomJutsu(int chackraMax){
        List<String> jutsuName = Arrays.asList("Doryuuheki no Jutsu", "Suijinheki no Jutsu", "Goukakyuu no Jutsu", "Rasenshuriken", "Chidori");
        List<String> jutsuAtributo = Arrays.asList("Terra", "Água", "Fogo", "Vento", "Relâmpago");
        int nomeEAtributoAleatorio = new Random().nextInt(4);
        return Jutsu.builder()
                .nome(jutsuName.get(nomeEAtributoAleatorio))
                .atributo(jutsuAtributo.get(nomeEAtributoAleatorio))
                .chackaConsumido(new Random().nextInt(Math.max(1,chackraMax))) // random entre 0 e o max do chackra do ninja
                .dano(new Random().nextInt(281) + 35) // ataque entre 35 e 280)
                .build();
    }
}
