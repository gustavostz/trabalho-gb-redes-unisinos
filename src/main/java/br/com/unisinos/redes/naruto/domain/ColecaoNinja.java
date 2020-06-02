package br.com.unisinos.redes.naruto.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ColecaoNinja {


    public static List<Ninja> getColecaoNinja(){
        List<Ninja> colecaoNinja = new ArrayList<>();
        List<String> nomesNinja = Arrays.asList("Naruto", "Sasuke", "Neji", "Kakashi", "Gaara", "Rock Lee", "Gaya", "Obito", "Minato", "Sakura", "Itachi");

        nomesNinja.forEach(nomeNinja -> colecaoNinja.add(createNinjaRandom(nomeNinja)));

        return colecaoNinja;
    };

    public static Ninja createNinjaRandom(String name) {
        return Ninja.builder()
                .name(name)
                .ataqueNormal(new Random().nextInt(251) + 10) // vida entre 10 e 250
                .chackra(new Random().nextInt(101))// vida entre 0 e 100
//                .jutsoAtivo()
//                .poder()
                .vida(new Random().nextInt(301) + 10) // vida entre 10 e 300
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
}
