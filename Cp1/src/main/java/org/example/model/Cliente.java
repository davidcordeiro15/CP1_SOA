package org.example.model;

import java.util.UUID;

public class Cliente {


    private String id;
    private String nome;
    private String email;
    private String cpf;


    public Cliente(String email, String cpf, String nome) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;

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



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
