package com.example.sistema_de_funcionario.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Endereco {

    private final StringProperty logradouro = new SimpleStringProperty();
    private final StringProperty numero = new SimpleStringProperty();
    private final StringProperty complemento = new SimpleStringProperty();
    private final StringProperty bairro = new SimpleStringProperty();
    private final StringProperty cidade = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty cep = new SimpleStringProperty();

    public Endereco() {}

    // Construtor com todos os campos
    public Endereco(String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep) {
        this.logradouro.set(logradouro);
        this.numero.set(numero);
        this.complemento.set(complemento);
        this.bairro.set(bairro);
        this.cidade.set(cidade);
        this.estado.set(estado);
        this.cep.set(cep);
    }

    // Construtor adicional sem complemento
    public Endereco(String logradouro, String numero, String bairro, String cidade, String estado, String cep) {
        this(logradouro, numero, "", bairro, cidade, estado, cep);
    }

    // Getters e Setters
    public String getLogradouro() { return logradouro.get(); }
    public void setLogradouro(String logradouro) { this.logradouro.set(logradouro); }
    public StringProperty logradouroProperty() { return logradouro; }

    public String getNumero() { return numero.get(); }
    public void setNumero(String numero) { this.numero.set(numero); }
    public StringProperty numeroProperty() { return numero; }

    public String getComplemento() { return complemento.get(); }
    public void setComplemento(String complemento) { this.complemento.set(complemento); }
    public StringProperty complementoProperty() { return complemento; }

    public String getBairro() { return bairro.get(); }
    public void setBairro(String bairro) { this.bairro.set(bairro); }
    public StringProperty bairroProperty() { return bairro; }

    public String getCidade() { return cidade.get(); }
    public void setCidade(String cidade) { this.cidade.set(cidade); }
    public StringProperty cidadeProperty() { return cidade; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }

    public String getCep() { return cep.get(); }
    public void setCep(String cep) { this.cep.set(cep); }
    public StringProperty cepProperty() { return cep; }

    // Compatibilidade com serialize()
    public String getRua() {
        return getLogradouro();
    }

    // toString
    @Override
    public String toString() {
        return getLogradouro() + ", " + getNumero() +
                (getComplemento() != null && !getComplemento().isEmpty() ? " (" + getComplemento() + ")" : "") +
                " - " + getBairro() + ", " + getCidade() + " - " + getEstado() + " - " + getCep();
    }
}
