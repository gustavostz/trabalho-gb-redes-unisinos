package br.com.unisinos.redes.naruto.service;

import br.com.unisinos.redes.naruto.domain.ControleDeJogo;
import br.com.unisinos.redes.naruto.domain.Ninja;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClienteTCP {
    private static final Scanner TECLADO = new Scanner(System.in);
    private static Socket socketConexao;
    private static Socket socketControleJogo;
    private static Ninja ninjaAtribuido;

    public static void main (String args[]) throws Exception{

        System.out.println("Bem vindo ao naruto Battle!");

        System.out.println("Tentando conexão com o servidor...");
        while(!conectarServidor()){
            System.out.println("Erro na conexão, tentando novamente...");
        }

        enviarParaServidor("ok");
        int identificador = ninjaAtribuido.getIdNinja();
        System.out.println("Escolha com quem irá jogar:");
//        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
//
//        ArrayList<User> userArray = gson.fromJson(userJson, userListType);

        List<String> listaPersonagem = Arrays.asList(new Gson().fromJson(receberDoServidor(), String[].class));
        listaPersonagem.forEach(System.out::println);

        String escolha = TECLADO.nextLine();
        enviarParaServidor(escolha);

        ninjaAtribuido = new Gson().fromJson(receberDoServidor(),Ninja.class);
        ninjaAtribuido.setIdNinja(identificador);
        enviarParaServidor(new Gson().toJson(identificador));

        System.out.println("chegou " + identificador);
        boolean terminou = false;
        while(!terminou){
              if(minhaVezDeLutar(identificador)) {

                  System.out.println("Oponentes: ");
                  Thread.sleep(2000);
                  System.out.println(receberDoServidor().replace("quebraLinha", "\n"));
                  System.out.println(receberDoServidor().replace("quebraLinha", "\n"));
                  System.out.println(receberDoServidor().replace("quebraLinha", "\n"));
                  System.out.println(receberDoServidor().replace("quebraLinha", "\n\n"));

                  ninjaAtribuido.printaAtaquesDisponiveisEStatus();

                  System.out.println("Digite o número correspondente ao ataque:"); //1 para ataque normal e 2 para jutsu
                  String ataqueEscolhido = TECLADO.nextLine();
              }
//            enviarParaServidor("atualizaStatus");
//            String atualizarStatus = receberDoServidor();
            //desfragmenta os lutadores e apropria seus atributos
            //exibe os lutadores e as habilidades
            String habilidadeEscolhida = TECLADO.nextLine();
            enviarParaServidor("habilidade:" + habilidadeEscolhida);
            String acabou = receberDoServidor();
            terminou = Boolean.parseBoolean(acabou);

            enviarParaServidor("resultadoFinal");
            String resultado = receberDoServidor();
            System.out.println(resultado);
            break;
        }
        /*
        ver uma forma de desfragmentar os atibutos, biblioteca Json
        uma forma de identificar os 2 lutadores, provavel de utilizar id na frente de cada solicitação.
         */
    }

    private static boolean minhaVezDeLutar(int id) throws IOException, InterruptedException {
        conectarControleDeJogo();
        while(!ControleDeJogo.isVezDesteJogador(id)){
            Thread.sleep(1000);
            ControleDeJogo.setIdJogadorAtual(new Gson().fromJson(receberDoControleJogo(), int.class));
        }
        System.out.println("Passei daqui");
        return true;
    }

    private static boolean conectarServidor(){
        try{
            socketConexao = new Socket("127.0.0.1", 6789);
            enviarParaServidor("isConect");
            ninjaAtribuido = new Gson().fromJson(receberDoServidor(), Ninja.class);
            return ninjaAtribuido != null;
        }
        catch(Exception ex){
            System.out.println("Erro:" + ex.getMessage());
            return false;
        }
    }

    private static boolean conectarControleDeJogo(){
        try{
            socketControleJogo = new Socket("127.0.0.1", 6788);
            enviarParaServidor("isConect");
            return true;
        }
        catch(Exception ex){
            System.out.println("Erro:" + ex.getMessage());
            return false;
        }
    }

    private static void enviarParaControleJogo(String mensagem) throws IOException {

        DataOutputStream paraServidor =
                new DataOutputStream(socketControleJogo.getOutputStream());
        paraServidor.writeBytes(mensagem + '\n');
    }

    private static String receberDoControleJogo() throws IOException {
        BufferedReader doServidor = new BufferedReader(new
                InputStreamReader(socketControleJogo.getInputStream()));

        String resposta = doServidor.readLine();
        return resposta;
    }

    private static void enviarParaServidor(String mensagem) throws IOException {

        DataOutputStream paraServidor =
                new DataOutputStream(socketConexao.getOutputStream());
        paraServidor.writeBytes(mensagem + '\n');
    }

    private static String receberDoServidor() throws IOException {
        BufferedReader doServidor = new BufferedReader(new
                InputStreamReader(socketConexao.getInputStream()));

        String resposta = doServidor.readLine();
        return resposta;
    }
}
