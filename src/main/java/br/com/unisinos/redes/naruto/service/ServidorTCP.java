package br.com.unisinos.redes.naruto.service;

import br.com.unisinos.redes.naruto.domain.ColecaoNinja;
import br.com.unisinos.redes.naruto.domain.ControleDeJogo;
import br.com.unisinos.redes.naruto.domain.Ninja;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ServidorTCP implements Runnable{
    private static final String TENTATIVA_CONEXAO = "isConect";
    private Socket cliente;
    private List<Ninja> colecaoNinja;
    private ControleDeJogo controleDeJogo;
    private int idSocket;


    public ServidorTCP(Socket cliente,List<Ninja> colecaoNinja,ControleDeJogo controleDeJogo,int idSocket){
        this.cliente = cliente;
        this.colecaoNinja = colecaoNinja;
        this.controleDeJogo = controleDeJogo;
        this.idSocket = idSocket;
    }

    public static void main(String args[]) throws Exception {

        ControleDeJogo controle = new ControleDeJogo(false);
        List<Ninja> ninjaBatalha = new ArrayList<>(2);
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Porta 6789 aberta!");
        int aberturaDeSocket = 0;
        while (aberturaDeSocket++ < 2) {
            Socket cliente = serverSocket.accept();
            // Cria uma thread do servidor para tratar a conexão
            ServidorTCP tratamento = new ServidorTCP(cliente,ninjaBatalha,controle,aberturaDeSocket);
            Thread t = new Thread(tratamento);
            // Inicia a thread para o cliente conectado
            t.start();
        }
        while(ninjaBatalha.size() < 2){
            Thread.sleep(1000);
        }
        controle.setIniciarJogo(true);
        System.out.println("inciando o jogo -- thread principal");
    }

    private static void sleep(int i) {
        try {
            Thread.sleep((long) i);
        }catch (Exception e ){
            e.getStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());

            BufferedReader doCliente =  new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
            String fraseCliente = doCliente.readLine();
            if (TENTATIVA_CONEXAO.equals(fraseCliente)) {
                //estabelecida a conexão envia um identificador ao qual o objeto deve se associar
                enviarParaCliente(new Gson().toJson(Ninja.builder().
                        idNinja(this.idSocket)));
                if(receberDoCliente().equalsIgnoreCase("ok")) {
                    List<Ninja> ninjasBatalhantes = ColecaoNinja.getColecaoNinja();
                    enviarParaCliente(new Gson().toJson(ColecaoNinja.stringListaPersonagens(ninjasBatalhantes)));
                    Ninja ninjaEscolhido;
                    do {
                        String personagem = receberDoCliente();
                        ninjaEscolhido = ninjasBatalhantes.stream().filter(x -> x.getName().equalsIgnoreCase(personagem))
                                .findFirst()
                                .orElse(null);

                    } while (ninjaEscolhido == null);
                    System.out.println("o jogador " + this.cliente.getInetAddress().getHostAddress() + "escolheu: " + ninjaEscolhido.getName());
                    this.colecaoNinja.add(ninjaEscolhido);
                    enviarParaCliente(new Gson().toJson(ninjaEscolhido));
                    iniciarJogo();
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void iniciarJogo() {
        while(!this.controleDeJogo.isIniciarJogo()){
            System.out.println("Aguardando o oponente");
            sleep(10000);
        }
        System.out.println("inciando o jogo" + this.cliente.getInetAddress().getHostAddress());
    }

    private void enviarParaCliente(String mensagem) throws IOException {
        DataOutputStream paraCliente = new DataOutputStream(this.cliente.getOutputStream());
        paraCliente.writeBytes(mensagem+ '\n');
    }



    private String receberDoCliente() throws IOException {
        BufferedReader doCliente = new BufferedReader(new
                InputStreamReader(this.cliente.getInputStream()));
        String resposta = doCliente.readLine();
        return resposta;
    }

}