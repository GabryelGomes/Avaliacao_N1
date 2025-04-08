package com.example.sistema_de_funcionario.model;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Funcionario {

    private static int contadorId = 1;

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty matricula = new SimpleStringProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty cpf = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>();
    private final StringProperty cargo = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> salario = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dataContratacao = new SimpleObjectProperty<>();
    private final ObjectProperty<Endereco> endereco = new SimpleObjectProperty<>();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telefone = new SimpleStringProperty();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Funcionario() {
        this.id.set(contadorId++);
    }

    public Funcionario(String matricula, String nome, String cpf, LocalDate dataNascimento,
                       String cargo, BigDecimal salario, LocalDate dataContratacao, Endereco endereco) {
        this.id.set(contadorId++);
        setMatricula(matricula);
        setNome(nome);
        setCpf(cpf);
        setDataNascimento(dataNascimento);
        setCargo(cargo);
        setSalario(salario);
        setDataContratacao(dataContratacao);
        setEndereco(endereco);
    }

    public Funcionario(String matricula, String nome, String cpf, LocalDate dataNascimento,
                       String cargo, BigDecimal salario, LocalDate dataContratacao, Endereco endereco,
                       String email, String telefone) {
        this(matricula, nome, cpf, dataNascimento, cargo, salario, dataContratacao, endereco);
        setEmail(email);
        setTelefone(telefone);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getMatricula() { return matricula.get(); }
    public void setMatricula(String matricula) {
        if (!matricula.matches("\\d{6}")) {
            throw new IllegalArgumentException("Matrícula deve conter 6 dígitos numéricos.");
        }
        this.matricula.set(matricula);
    }
    public StringProperty matriculaProperty() { return matricula; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome.set(nome.trim());
    }
    public StringProperty nomeProperty() { return nome; }

    public String getCpf() { return cpf.get(); }
    public void setCpf(String cpf) {
        if (!validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        this.cpf.set(cpf);
    }
    public StringProperty cpfProperty() { return cpf; }

    public LocalDate getDataNascimento() { return dataNascimento.get(); }
    public void setDataNascimento(LocalDate dataNascimento) {
        if (Period.between(dataNascimento, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Funcionário deve ter pelo menos 18 anos.");
        }
        this.dataNascimento.set(dataNascimento);
    }
    public void setDataNascimento(String data) {
        setDataNascimento(LocalDate.parse(data, formatter));
    }
    public ObjectProperty<LocalDate> dataNascimentoProperty() { return dataNascimento; }

    public String getDataNascimentoFormatada() {
        return (dataNascimento.get() != null) ? dataNascimento.get().format(formatter) : "";
    }

    public LocalDate getNascimento() {
        return getDataNascimento();
    }

    public String getCargo() { return cargo.get(); }
    public void setCargo(String cargo) { this.cargo.set(cargo); }
    public StringProperty cargoProperty() { return cargo; }

    public BigDecimal getSalario() { return salario.get(); }
    public void setSalario(BigDecimal salario) {
        if (salario == null || salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário deve ser positivo.");
        }
        this.salario.set(salario);
    }
    public void setSalario(String salarioStr) {
        setSalario(new BigDecimal(salarioStr.replace(",", ".")));
    }
    public ObjectProperty<BigDecimal> salarioProperty() { return salario; }

    public double getSalarioDouble() {
        return (salario.get() != null) ? salario.get().doubleValue() : 0.0;
    }

    public LocalDate getDataContratacao() { return dataContratacao.get(); }
    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao.set(dataContratacao);
    }
    public void setDataContratacao(String data) {
        setDataContratacao(LocalDate.parse(data, formatter));
    }
    public ObjectProperty<LocalDate> dataContratacaoProperty() { return dataContratacao; }

    public String getDataContratacaoFormatada() {
        return (dataContratacao.get() != null) ? dataContratacao.get().format(formatter) : "";
    }

    public Endereco getEndereco() { return endereco.get(); }
    public void setEndereco(Endereco endereco) { this.endereco.set(endereco); }
    public ObjectProperty<Endereco> enderecoProperty() { return endereco; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.email.set(email);
    }
    public StringProperty emailProperty() { return email; }

    public String getTelefone() { return telefone.get(); }
    public void setTelefone(String telefone) {
        if (telefone == null || !telefone.matches("\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}")) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.telefone.set(telefone);
    }
    public StringProperty telefoneProperty() { return telefone; }

    private boolean validarCpf(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) return false;

        int soma = 0, peso = 10;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * peso--;

        int dig1 = 11 - (soma % 11);
        dig1 = (dig1 > 9) ? 0 : dig1;

        soma = 0; peso = 11;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * peso--;

        int dig2 = 11 - (soma % 11);
        dig2 = (dig2 > 9) ? 0 : dig2;

        return cpf.charAt(9) - '0' == dig1 && cpf.charAt(10) - '0' == dig2;
    }

    @Override
    public String toString() {
        return nome.get() + " - " + cpf.get();
    }
}
