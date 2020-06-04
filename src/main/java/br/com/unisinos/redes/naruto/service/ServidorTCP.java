package br.com.unisinos.redes.naruto.service;

import br.com.unisinos.redes.naruto.domain.ColecaoNinja;
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

    public ServidorTCP(Socket cliente){
        this.cliente = cliente;
    }

    public static void main(String args[]) throws Exception {

        String fraseCliente;
        String fraseMaiusculas;
        List<Ninja> colecaoNinja = ColecaoNinja.getColecaoNinja();
        List<Ninja> ninjaBatalha = new ArrayList<>(2);
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Porta 6789 aberta!");

        while (true) {
            Socket cliente = serverSocket.accept();
            // Cria uma thread do servidor para tratar a conexão
            ServidorTCP tratamento = new ServidorTCP(cliente);
            Thread t = new Thread(tratamento);
            // Inicia a thread para o cliente conectado
            t.start();
        }
    }

    private static void sleep(int i) {
    }

    private static void enviarParaCliente(String mensagem,Socket socket) throws IOException {
        DataOutputStream paraCliente = new DataOutputStream(socket.getOutputStream());
        paraCliente.writeBytes(mensagem+ '\n');
    }

    private static String receberDoCliente(Socket socket) throws IOException {
        BufferedReader doCliente = new BufferedReader(new
                InputStreamReader(socket.getInputStream()));
        String resposta = doCliente.readLine();
        return resposta;
    }
    @Override
    public void run() {
        try {
            System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());

            BufferedReader doCliente =  new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
            String fraseCliente = doCliente.readLine();
            if (TENTATIVA_CONEXAO.equals(fraseCliente)) {
                enviarParaCliente("true");

                //enviarParaCliente(new Gson().toJson(ColecaoNinja.stringListaPersonagens(colecaoNinja)), socketConexao);

                String personagem = null;
                do {
                    personagem = receberDoCliente();
                } while (personagem == null || personagem.isEmpty());
                System.out.println("sucesso coneção "+this.cliente.getInetAddress().getHostAddress());
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
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