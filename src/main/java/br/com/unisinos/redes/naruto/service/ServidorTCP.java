package src.main.java.br.com.unisinos.redes.naruto.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import src.main.java.br.com.unisinos.redes.naruto.domain.Ninja;

public class ServidorTCP {
    private static final String TENTATIVA_CONEXAO = "isConect";
    public static void main(String args[]) throws Exception {

        String fraseCliente;
        String fraseMaiusculas;
        List<Ninja> ninjaBatalha = new ArrayList<>(2);

        ServerSocket socketRecepcao = new ServerSocket(6789);
        Socket socketConexao = socketRecepcao.accept();
        while(ninjaBatalha.size() < 2){
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));
            fraseCliente = doCliente.readLine();
            if(TENTATIVA_CONEXAO.equals(fraseCliente)){
                enviarParaCliente("true",socketConexao);
                String personagem = null;
                do{
                    personagem = receberDoCliente(socketConexao);

                }while( personagem == null || personagem.isEmpty());

            }

        }
        enviarParaCliente("iniciar",socketConexao);

        while (true) {
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));

            DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());
            fraseCliente= doCliente.readLine();
            fraseMaiusculas= fraseCliente.toUpperCase() + '\n';
            paraCliente.writeBytes(fraseMaiusculas);
        }
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

}