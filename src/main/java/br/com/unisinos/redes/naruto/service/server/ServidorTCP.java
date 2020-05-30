package br.com.unisinos.redes.naruto.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
    private const final String TENTATIVA_CONEXAO = "isConect"
    public static void main(String args[]) throws Exception {

        String fraseCliente;
        String fraseMaiusculas;
        Ninja[] ninjaBatalha = new Ninja[2];

        ServerSocket socketRecepcao = new ServerSocket(6789);
        Socket socketConexao = socketRecepcao.accept();
        while(ninjaBatalha.length < 2){
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));
            fraseCliente = doCliente.readLine();
            if(TENTATIVA_CONEXAO.equals(fraseCliente)){
                enviarMensagem("true");
            }

        }


        while (true) {
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));

            DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());
            fraseCliente= doCliente.readLine();
            fraseMaiusculas= fraseCliente.toUpperCase() + '\n';
            paraCliente.writeBytes(fraseMaiusculas);
        }
    }

    private static void enviarParaCliente(String mensagem) throws IOException {
        Socket socketServer = new Socket("idHosp", 6789);
        DataOutputStream paraCliente =
                new DataOutputStream(socketServer.getOutputStream());
        paraCliente.writeBytes(mensagem + '\n');
        socketServer.close();
    }

    private static String receberDoCliente() throws IOException {
        Socket socketServer = new Socket("idHosp", 6789);
        BufferedReader doCliente = new BufferedReader(new
                InputStreamReader(socketServer.getInputStream()));
        String resposta = doCliente.readLine();
        socketServer.close();
        return resposta;
    }

}