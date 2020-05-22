package br.com.unisinos.redes.naruto.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
    public static void main(String argv[]) throws Exception {
        String fraseCliente;
        String fraseMaiusculas;

        ServerSocket socketRecepcao = new ServerSocket(6789);
        while (true) {
            Socket socketConexao = socketRecepcao.accept();
            BufferedReader doCliente = new BufferedReader(new InputStreamReader(socketConexao.getInputStream()));

            DataOutputStream paraCliente = new DataOutputStream(socketConexao.getOutputStream());
            fraseCliente= doCliente.readLine();
            fraseMaiusculas= fraseCliente.toUpperCase() + '\n';
            paraCliente.writeBytes(fraseMaiusculas);
        }
    }
}