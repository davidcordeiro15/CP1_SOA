package org.example.model;

import java.util.UUID;

public class Cliente {


    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String end; // Endereço do cliente

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Cliente(String email, String cpf, String nome, String end) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;
        this.end = end;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
