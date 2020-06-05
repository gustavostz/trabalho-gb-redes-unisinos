package br.com.unisinos.redes.naruto.service;

import br.com.unisinos.redes.naruto.domain.ColecaoNinja;
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
    private static Ninja ninjaAtribuido;
    private static int identificador;
    public static void main (String args[]) throws Exception{

        System.out.println("Bem vindo ao naruto Battle!");

        System.out.println("Tentando conexão com o servidor...");
        while(!conectarServidor()){
            System.out.println("Erro na conexão, tentando novamente...");
        }
        enviarParaServidor("ok");
        identificador = ninjaAtribuido.getIdNinja();
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
        if(ninjaAtribuido != null){
            System.out.println("chegou " + identificador);
            boolean terminou = false;
            while(!terminou){
                enviarParaServidor("atualizaStatus");
                String atualizarStatus = receberDoServidor();
                //desfragmenta os lutadores e apropria seus atributos
                //exibe os lutadores e as habilidades
                System.out.println("Qual habilidade você ira usar?");
                String habilidadeEscolhida = TECLADO.nextLine();
                enviarParaServidor("habilidade:" + habilidadeEscolhida);
                String acabou = receberDoServidor();
                terminou = Boolean.parseBoolean(acabou);
            }
            enviarParaServidor("resultadoFinal");
            String resultado = receberDoServidor();
            System.out.println(resultado);
        }
        /*
        ver uma forma de desfragmentar os atibutos, biblioteca Json
        uma forma de identificar os 2 lutadores, provavel de utilizar id na frente de cada solicitação.
         */
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
