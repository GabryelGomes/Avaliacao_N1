package com.example.sistema_de_funcionario.utils;

import com.example.sistema_de_funcionario.model.Funcionario;
import com.example.sistema_de_funcionario.model.Endereco;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class ValidadorFuncionario {

    public static boolean validarMatricula(String matricula) {
        return matricula != null && matricula.matches("\\d{6}");
    }

    public static boolean validarNome(String nome) {
        return nome != null && nome.trim().length() >= 3;
    }

    public static boolean validarCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    public static boolean validarIdade(LocalDate nascimento) {
        if (nascimento == null) return false;
        return Period.between(nascimento, LocalDate.now()).getYears() >= 18;
    }

    public static boolean validarSalario(BigDecimal salario) {
        return salario != null && salario.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean validarCEP(String cep) {
        return cep != null && cep.matches("\\d{8}");
    }

    public static boolean isValido(Funcionario f) {
        if (f == null) return false;

        if (!validarMatricula(f.getMatricula())) return false;
        if (!validarNome(f.getNome())) return false;
        if (!validarCPF(f.getCpf())) return false;
        if (!validarIdade(f.getDataNascimento())) return false;
        if (!validarSalario(f.getSalario())) return false;
        if (f.getDataContratacao() == null) return false;

        Endereco e = f.getEndereco();
        if (e == null) return false;
        if (e.getLogradouro() == null || e.getNumero() == null || e.getBairro() == null ||
                e.getCidade() == null || e.getEstado() == null || !validarCEP(e.getCep())) {
            return false;
        }

        return true;
    }
}
