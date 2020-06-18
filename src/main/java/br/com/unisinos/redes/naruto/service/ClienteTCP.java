package br.com.unisinos.redes.naruto.service;

import br.com.unisinos.redes.naruto.domain.Ninja;
import br.com.unisinos.redes.naruto.domain.TipoAtaque;
import br.com.unisinos.redes.naruto.request.BatalhaRequest;
import br.com.unisinos.redes.naruto.response.BatalhaResponse;
import br.com.unisinos.redes.naruto.response.IdentificadorResponse;
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

    public static void main(String args[]) throws Exception {

        System.out.println("Bem vindo ao naruto Battle!");

        System.out.println("Tentando conexão com o servidor...");
        while (!conectarServidor()) {
            System.out.println("Erro na conexão, tentando novamente...");
        }

        IdentificadorResponse identificadorResponse = new Gson().fromJson(receberDoServidor(), IdentificadorResponse.class);
        int identificador = identificadorResponse.getIdentificador();
        enviarParaServidor("ok");

        System.out.println("Escolha com quem irá jogar:");
        List<String> listaPersonagem = Arrays.asList(new Gson().fromJson(receberDoServidor(), String[].class));
        listaPersonagem.forEach(System.out::println);


        validarNome();

        ninjaAtribuido = new Gson().fromJson(receberDoServidor(), Ninja.class);
        ninjaAtribuido.setIdNinja(identificador);
        enviarParaServidor(new Gson().toJson(ninjaAtribuido));
        System.out.println("Seu identificador: " + identificador);
        boolean terminou = false;
        while (!terminou) {

            BatalhaResponse batalha = new Gson().fromJson(receberDoServidor(), BatalhaResponse.class);
            switch (batalha.getStatusPartida()) {
                case SUA_VEZ:
                    System.out.println("informações da batalha:");
                    System.out.println("--------------------------------------------------------------");
                    System.out.println("Ninja oponentes: ");
                    batalha.getNinjaOponentes().forEach(ninjaOponente ->
                            System.out.println(String.format("[%d] nome: %s       |      vida: %d/%d %s",
                                    ninjaOponente.getIdNinja(), ninjaOponente.getName(),
                                    ninjaOponente.getVida() > 0 ? ninjaOponente.getVida() : 0,
                                    ninjaOponente.getVidaMax(),
                                    ninjaOponente.isVivo() ? "" : " | Morto")));

                    batalha.getNinjaAtual().printaAtaquesDisponiveisEStatus();

                    System.out.println("Digite o número correspondente ao ataque:"); //1 para ataque normal e 2 para jutsu
                    String ataqueEscolhido;
                    TipoAtaque ataque = null;
                    do {
                        ataqueEscolhido = TECLADO.nextLine();
                        ataque = selecaoTipoAtaque(ataqueEscolhido);
                    } while (ataque == null);

                    System.out.println("Digite o número correspondente ao Ninja que deseja atacar:"); //1 para ataque normal e 2 para jutsu
                    String ninjaASerAtacadoId;
                    Ninja ninjaASerAtacado = null;
                    do {
                        ninjaASerAtacadoId = TECLADO.nextLine();
                        ninjaASerAtacado = selecaoNinja(ninjaASerAtacadoId, batalha.getNinjaOponentes());
                    } while (ninjaASerAtacado == null);

                    BatalhaRequest batalhaRequest = new BatalhaRequest();
                    batalhaRequest.setAtaque(ataque);
                    batalhaRequest.setAdversario(ninjaASerAtacado);
                    enviarParaServidor(new Gson().toJson(batalhaRequest));
                    String resultadoDoAtaque = receberDoServidor();
                    System.out.println(resultadoDoAtaque);
                    break;
                case GANHOU:
                    System.out.println("você ganhou o jogo");
                    terminou = true;
                    break;
                case PERDEU:
                    System.out.println("você perdeu o jogo");
                    terminou = true;
                    break;
            }
        }
    }

    private static Ninja selecaoNinja(String ninjaASerAtacadoId, List<Ninja> oponentesNinjas) {
        return oponentesNinjas.stream().filter(ninja -> ninja.getIdNinja() == Integer.parseInt(ninjaASerAtacadoId)).findFirst().orElse(null);
    }

    private static void validarNome() throws IOException {
        boolean nomeInvalido = true;
        do {
            String escolha = TECLADO.nextLine();
            enviarParaServidor(escolha);
            String resposta = receberDoServidor();
            if (resposta.equals("ok")) {
                nomeInvalido = false;
            } else {
                System.out.println(resposta);
            }
        } while (nomeInvalido);
    }

    private static boolean conectarServidor() {
        try {
            socketConexao = new Socket("127.0.0.1", 6789);
            enviarParaServidor("isConect");
            return true;
        } catch (Exception ex) {
            System.out.println("Erro:" + ex.getMessage());
            return false;
        }
    }

    private static TipoAtaque selecaoTipoAtaque(String ataqueEscolhido) {
        TipoAtaque ataque;
        if (ataqueEscolhido.equals("1")) {
            ataque = TipoAtaque.ATAQUE_BASICO;
        } else if (ataqueEscolhido.equals("2")) {
            ataque = TipoAtaque.JUTSO;
        } else {
            ataque = null;
        }
        return ataque;
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
