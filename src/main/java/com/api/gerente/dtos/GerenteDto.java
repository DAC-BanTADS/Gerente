package com.api.gerente.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GerenteDto {
    @NotBlank
    @Size(max = 11)
    private String cpf;
    @NotBlank
    private String nome;
    @NotBlank
    private String email;

    /*
     * GETTERS E SETTERS
     * */

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
