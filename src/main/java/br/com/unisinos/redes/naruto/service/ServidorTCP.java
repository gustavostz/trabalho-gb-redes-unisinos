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

public class ServidorTCP implements Runnable{
    private static final String TENTATIVA_CONEXAO = "isConect";
    private Socket cliente;
    private List<Ninja> ninjasDaBatalha;
    private int idSocket;
    private ControleDeJogo controleDeJogo;

    public ServidorTCP(Socket cliente, List<Ninja> ninjasDaBatalha, int idSocket){
        this.cliente = cliente;
        this.ninjasDaBatalha = ninjasDaBatalha;
        this.idSocket = idSocket;
        this.controleDeJogo = new ControleDeJogo();
    }

    public static void main(String args[]) throws Exception {

        List<Ninja> ninjaBatalha = new ArrayList<>(4);
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Porta 6789 aberta!");
        int aberturaDeSocket = 0;
        while (aberturaDeSocket++ < 4) {
            Socket cliente = serverSocket.accept();
            // Cria uma thread do servidor para tratar a conexão
            ServidorTCP tratamento = new ServidorTCP(cliente, ninjaBatalha, aberturaDeSocket);
            Thread t = new Thread(tratamento);
            // Inicia a thread para o cliente conectado
            t.start();
        }
        while(ninjaBatalha.size() < 4){
            Thread.sleep(1000);
        }

        ControleDeJogo.setIdJogadorAtual(1);
        ServerSocket controleJogoSocket = new ServerSocket(6788);
        Socket cliente = controleJogoSocket.accept();
        BufferedReader doCliente =  new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        String fraseCliente = doCliente.readLine();
        if (TENTATIVA_CONEXAO.equals(fraseCliente)) {

        }

        System.out.println("iniciando o jogo -- thread principal");

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
                        idNinja(this.idSocket)), this.cliente);

                if(receberDoCliente(this.cliente).equalsIgnoreCase("ok")) {
                    List<Ninja> ninjasBatalhantes = ColecaoNinja.getColecaoNinja();
                    enviarParaCliente(new Gson().toJson(ColecaoNinja.stringListaPersonagens(ninjasBatalhantes)), this.cliente);
                    Ninja ninjaEscolhido;
                    do {
                        String personagem = receberDoCliente(this.cliente);
                        ninjaEscolhido = ninjasBatalhantes.stream().filter(x -> x.getName().equalsIgnoreCase(personagem))
                                .findFirst()
                                .orElse(null);

                    } while (ninjaEscolhido == null);
                    System.out.println("o jogador " + this.cliente.getInetAddress().getHostAddress() + " escolheu: " + ninjaEscolhido.getName());
                    this.ninjasDaBatalha.add(ninjaEscolhido);
                    ControleDeJogo.addUser(ninjaEscolhido.getIdNinja());
                    enviarParaCliente(new Gson().toJson(ninjaEscolhido), this.cliente);
                    int identificador = new Gson().fromJson(receberDoCliente(this.cliente), int.class);
                    ninjaEscolhido.setIdNinja(identificador);
                    jogar(ninjaEscolhido);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void jogar(Ninja ninjaEscolhido) throws IOException, InterruptedException {
        System.out.println("Iniciando o jogo " + this.cliente.getInetAddress().getHostAddress());
        int cont = 15;
        while(!ControleDeJogo.isVezDesteJogador(ninjaEscolhido.getIdNinja())){
            if(cont%15 == 0){
                System.out.println("Aguardando oponentes");
            }
            Thread.sleep(1000);
            cont++;
        }
//        System.out.println("Passei daqui");
        int aux = 4;
        for (Integer adversarioId : ControleDeJogo.getIdUsers()) {
            aux--;
            enviarParaCliente(new Gson().toJson(ninjasDaBatalha.get(adversarioId).getStatus()), this.cliente);
        }
        for (int i = 0; i < aux; i++) {
            enviarParaCliente("Oponente Derrotado", this.cliente);
        }

    }

    private void enviarParaCliente(String mensagem, Socket cliente) throws IOException {
        DataOutputStream paraCliente = new DataOutputStream(cliente.getOutputStream());
        paraCliente.writeBytes(mensagem+ '\n');
    }

    private String receberDoCliente(Socket cliente) throws IOException {
        BufferedReader doCliente = new BufferedReader(new
                InputStreamReader(cliente.getInputStream()));
        String resposta = doCliente.readLine();
        return resposta;
    }

}