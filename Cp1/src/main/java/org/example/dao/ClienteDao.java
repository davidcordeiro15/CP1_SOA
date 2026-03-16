package org.example.dao;

import org.example.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private List<Cliente> clientes = new ArrayList<>();

    public void adicionaCliente(String nome, String email, String cpf, String end) {
        Cliente client = new Cliente(email, cpf, nome, end);
        clientes.add(client);
        consultaCliente(email);

    }

    public Cliente consultaCliente(String email) {
        return clientes.stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Cliente consultaClienteCpf(String cpf) {
        return clientes.stream()
                .filter(c -> c.getEmail().equals(cpf))
                .findFirst()
                .orElse(null);
    }
    public void excluiCliente(String email) {
        Cliente client = consultaCliente(email);


        clientes.remove(client);

    }

    public void alteraDadosCliente(String nome, String email, String cpf, String end) {
        Cliente client = consultaClienteCpf(cpf);

        if (client != null) {
            client.setNome(nome);
            client.setEmail(email);
            client.setEnd(end);
        }
    }
}
