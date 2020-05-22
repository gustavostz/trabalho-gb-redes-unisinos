package br.com.unisinos.redes.naruto.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClienteTCP {

    public static void main(String argv[]) throws Exception
    {
        String frase;
        String fraseModificada;

        BufferedReader doUsuario = new BufferedReader(new InputStreamReader(System.in));
        Socket socketCliente = new Socket("localhost", 6789);
        DataOutputStream paraServidor = new DataOutputStream(socketCliente.getOutputStream());

        BufferedReader doServidor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        frase = doUsuario.readLine();
        paraServidor.writeBytes(frase + '\n');
        fraseModificada = doServidor.readLine();
        System.out.println("Do Servidor: " + fraseModificada);
        socketCliente.close();


    }
}
