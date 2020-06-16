package br.com.unisinos.redes.naruto.service;

import br.com.unisinos.redes.naruto.domain.*;
import br.com.unisinos.redes.naruto.request.BatalhaRequest;
import br.com.unisinos.redes.naruto.response.BatalhaResponse;
import br.com.unisinos.redes.naruto.response.IdentificadorResponse;
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

    public ServidorTCP(Socket cliente, List<Ninja> ninjasDaBatalha, int idSocket,ControleDeJogo controleDeJogo){
        this.cliente = cliente;
        this.ninjasDaBatalha = ninjasDaBatalha;
        this.idSocket = idSocket;
        this.controleDeJogo = controleDeJogo;
    }

    public static void main(String args[]) throws Exception {

        List<Ninja> ninjaBatalha = new ArrayList<>(2);
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Porta 6789 aberta!");
        int aberturaDeSocket = 0;
        ControleDeJogo controle = new ControleDeJogo();


        while (aberturaDeSocket++ < 2) {
            Socket cliente = serverSocket.accept();
            // Cria uma thread do servidor para tratar a conexão
            ServidorTCP tratamento = new ServidorTCP(cliente, ninjaBatalha, aberturaDeSocket,controle);
            Thread t = new Thread(tratamento);
            // Inicia a thread para o cliente conectado
            t.start();
        }
        while(ninjaBatalha.size() < 2){
            Thread.sleep(1000);
        }

//        ControleDeJogo.setIdJogadorAtual(1);
//        ServerSocket controleJogoSocket = new ServerSocket(6788);
//        Socket cliente = controleJogoSocket.accept();
//        BufferedReader doCliente =  new BufferedReader(new InputStreamReader(cliente.getInputStream()));
//        String fraseCliente = doCliente.readLine();
//        if (TENTATIVA_CONEXAO.equals(fraseCliente)) {
//
//        }

        System.out.println("iniciando o jogo -- thread principal");
        while(controle.getNinjaVencedor() == null){
            Thread.sleep(1000);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());

            BufferedReader doCliente =  new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
            String fraseCliente = doCliente.readLine();
            if (TENTATIVA_CONEXAO.equals(fraseCliente)){
                //estabelecida a conexão envia um identificador ao qual o objeto deve se associar
                enviarParaCliente(new Gson().toJson( new IdentificadorResponse(this.idSocket)), this.cliente);

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
                    enviarParaCliente(new Gson().toJson(ninjaEscolhido), this.cliente);
                    int identificador = new Gson().fromJson(receberDoCliente(this.cliente),Ninja.class).getIdNinja();
                    this.controleDeJogo.addUser(identificador);
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
        while(ninjasDaBatalha.size() < 2){
            Thread.sleep(1000);
        }
        boolean ninguemPerdeu = true;
        do {
            while (!this.controleDeJogo.isVezDesteJogador(ninjaEscolhido.getIdNinja())) {
                Thread.sleep(1000);
            }
            if(controleDeJogo.getNinjaVencedor() == null) {

                //minha vez de jogar
                //buscar o jogador atualizado
                Ninja ninjaSendoJogadoAtualmente = this.ninjasDaBatalha.stream().
                        filter(ninja -> ninja.equals(ninjaEscolhido)).
                                findFirst().
                                get();

                BatalhaResponse batalhaResponse = new BatalhaResponse();

                batalhaResponse.setNinjaAtual(ninjaSendoJogadoAtualmente);
                Ninja ninjaAdversario = this.ninjasDaBatalha.stream().
                        filter(item -> item.getIdNinja() == controleDeJogo.getIdAdversario())
                        .findFirst().
                                get();
                batalhaResponse.setNinjaOponente(ninjaAdversario);
                batalhaResponse.setStatusPartida(StatusPartida.SUA_VEZ);

                enviarParaCliente(new Gson().toJson(batalhaResponse), this.cliente);

                Thread.sleep(1000);

                //estou mantendo para caso o ninja tenha mais de um jutso
                BatalhaRequest batalhaRequest = new Gson().fromJson(receberDoCliente(this.cliente), BatalhaRequest.class);

                //atualiza estado do oponente
                String resultadoAtaque = "";

                if(batalhaRequest.getAtaque() == TipoAtaque.ATAQUE_BASICO){
                    resultadoAtaque = ninjaSendoJogadoAtualmente.atacar(ninjaAdversario);
                }else{
                    resultadoAtaque = ninjaSendoJogadoAtualmente.usarJutsu(ninjaAdversario);
                }
                enviarParaCliente(resultadoAtaque,this.cliente);
                ninjaSendoJogadoAtualmente.recuperaPoucoChackra();
                //verifica condição do oponente
                ninguemPerdeu = ninjaAdversario.isVivo();
                //atualiza o controle do jogo para ser a vez do adversario
                this.controleDeJogo.atualizaJogadorAtual();
            }
            else{
                enviarParaCliente(new Gson().
                                toJson(new BatalhaResponse(null,null,StatusPartida.PERDEU)),
                        this.cliente);
                return;
            }
        }while(ninguemPerdeu);

        controleDeJogo.finalizarPartida(ninjaEscolhido);
        System.out.println("acabou e o jogador do ninja " + this.controleDeJogo.getNinjaVencedor() + "Ganhou!");
        enviarParaCliente(new Gson().
                toJson(new BatalhaResponse(null,null,StatusPartida.GANHOU)),
                this.cliente);
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