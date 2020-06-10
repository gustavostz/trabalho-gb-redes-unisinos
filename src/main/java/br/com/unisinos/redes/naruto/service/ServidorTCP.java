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
//    private Socket clienteControleJogo;
    private List<Ninja> ninjasDaBatalha;
    private int idSocket;

    public ServidorTCP(Socket cliente, List<Ninja> ninjasDaBatalha, int idSocket){
        this.cliente = cliente;
        this.ninjasDaBatalha = ninjasDaBatalha;
        this.idSocket = idSocket;
//        this.clienteControleJogo = clienteControleJogo;
    }

    public static void main(String args[]) throws Exception {

        List<Ninja> ninjaBatalha = new ArrayList<>(4);
         new Socket();
        ServerSocket serverSocket = new ServerSocket(6789);
        //Cria thread para abrir outra porta para o controle do jogo
        final ServerSocket[] controleJogoSocket = new ServerSocket[1];
        Thread t = new Thread(() -> {
            try {
                controleJogoSocket[0] = new ServerSocket(6788);
                System.out.println("Porta 6788 para ControleDeJogo!");
                Socket clienteControleJogo = getSocket(ninjaBatalha, controleJogoSocket[0], false);
                System.out.println("mandei");
//                enviarParaCliente(new Gson().toJson(4), clienteControleJogo); Por algum motivo q desconheco funciona no quarto player se descomentar aqui e comentar ali em baixo
//                enviarParaCliente(new Gson().toJson(4), clienteControleJogo);
//                enviarParaCliente(new Gson().toJson(4), clienteControleJogo);
//                enviarParaCliente(new Gson().toJson(4), clienteControleJogo);

                while(ninjaBatalha.size() < 4){
                    Thread.sleep(1000);
                }
                System.out.println("Atualizando Controle Jogo");
                ControleDeJogo.setIdJogadorAtual(1);
                enviarParaCliente(new Gson().toJson(1), clienteControleJogo);
                enviarParaCliente(new Gson().toJson(ControleDeJogo.getIdJogadorAtual()), clienteControleJogo);
                enviarParaCliente(new Gson().toJson(ControleDeJogo.getIdJogadorAtual()), clienteControleJogo);
                enviarParaCliente(new Gson().toJson(ControleDeJogo.getIdJogadorAtual()), clienteControleJogo);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        System.out.println("Porta 6789 aberta para Ninjas!");

        Socket cliente = getSocket(ninjaBatalha, serverSocket, true);
        while(ninjaBatalha.size() < 4){
            Thread.sleep(1000);
        }

        System.out.println("iniciando o jogo -- thread principal");

    }

    private static Socket getSocket(List<Ninja> ninjaBatalha, ServerSocket serverSocket, boolean threadBatalha) throws IOException {
        Socket cliente = null;
        for (int aberturaDeSocket = 1; aberturaDeSocket < 5; aberturaDeSocket++) {
            cliente = serverSocket.accept();
            // Cria uma thread do servidor para tratar a conexão
            if(threadBatalha){
                ServidorTCP tratamento = new ServidorTCP(cliente, ninjaBatalha, aberturaDeSocket);
                Thread t2 = new Thread(tratamento);
                // Inicia a thread para o cliente conectado
                t2.start();
            }
        }
        return cliente;
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
                System.out.println("recebendo-----------------");

                if(receberDoCliente(this.cliente).equalsIgnoreCase("ok")) {
                    List<Ninja> ninjasBatalhantes = ColecaoNinja.getColecaoNinja();
                    System.out.println("Enviando-----------------");
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

    private static void enviarParaCliente(String mensagem, Socket cliente) throws IOException {
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