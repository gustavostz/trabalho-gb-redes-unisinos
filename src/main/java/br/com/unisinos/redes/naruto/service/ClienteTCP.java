package src.main.java.br.com.unisinos.redes.naruto.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTCP {
    private static final Scanner TECLADO = new Scanner(System.in);
    private static Socket socketConexao;
    public static void main (String args[]) throws Exception{

        System.out.println("Bem vindo ao naruto Battle!");

        System.out.println("Tentando conexão com o servidor...");
        while(!conectarServidor()){
            System.out.println("Erro na conexão, tentando novamente...");
        }

        System.out.println("Escolha com quem irá jogar:");
        //ListarPersonagem
        String escolha = TECLADO.nextLine();
        System.out.println(escolha + '-');
        enviarParaServidor(escolha);
        String resposta = receberDoServidor();
        if(resposta == "iniciar"){
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

            String frase = "isConect";
            String fraseModificada;
            socketConexao = new Socket("127.0.0.1", 6789);
            DataOutputStream paraServidor =
                    new DataOutputStream(socketConexao.getOutputStream());
            BufferedReader doServidor = new BufferedReader(new
                    InputStreamReader(socketConexao.getInputStream()));
            paraServidor.writeBytes(frase + '\n');
            fraseModificada = doServidor.readLine();
            return Boolean.parseBoolean(fraseModificada);
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
