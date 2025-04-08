package com.example.sistema_de_funcionario.utils;

import com.example.sistema_de_funcionario.model.Funcionario;
import com.example.sistema_de_funcionario.model.Endereco;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FuncionarioRepository {
    private static final String CSV_PATH = "funcionarios.csv";
    private static final String DELIMITER = ";";

    public static void salvar(Funcionario f) {
        if (!ValidadorFuncionario.isValido(f)) {
            throw new IllegalArgumentException("Funcionário inválido.");
        }

        if (buscarPorMatricula(f.getMatricula()) != null) {
            throw new IllegalArgumentException("Matrícula já cadastrada.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH, true))) {
            writer.write(serialize(f));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void atualizar(Funcionario atualizado) {
        if (!ValidadorFuncionario.isValido(atualizado)) {
            throw new IllegalArgumentException("Funcionário inválido.");
        }

        List<Funcionario> funcionarios = listarTodos().stream()
                .map(f -> f.getMatricula().equals(atualizado.getMatricula()) ? atualizado : f)
                .collect(Collectors.toList());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (Funcionario f : funcionarios) {
                writer.write(serialize(f));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void excluir(String matricula) {
        List<Funcionario> atualizados = listarTodos().stream()
                .filter(f -> !f.getMatricula().equals(matricula))
                .collect(Collectors.toList());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH))) {
            for (Funcionario f : atualizados) {
                writer.write(serialize(f));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Funcionario> listarTodos() {
        if (!Files.exists(Paths.get(CSV_PATH))) return new ArrayList<>();
        try {
            return Files.lines(Paths.get(CSV_PATH))
                    .map(FuncionarioRepository::deserialize)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<Funcionario> listarOrdenadoPorNome() {
        return listarTodos().stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .collect(Collectors.toList());
    }

    public static Funcionario buscarPorMatricula(String matricula) {
        return listarTodos().stream()
                .filter(f -> f.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    public static void limpar() {
        try {
            Files.deleteIfExists(Paths.get(CSV_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String serialize(Funcionario f) {
        return String.join(DELIMITER,
                f.getMatricula(),
                f.getNome(),
                f.getCpf(),
                f.getDataNascimento().toString(),
                f.getCargo(),
                f.getSalario().toString(),
                f.getDataContratacao().toString(),
                f.getEndereco().getRua(),
                f.getEndereco().getNumero(),
                f.getEndereco().getBairro(),
                f.getEndereco().getCidade(),
                f.getEndereco().getEstado(),
                f.getEndereco().getCep()
        );
    }

    private static Funcionario deserialize(String linha) {
        String[] campos = linha.split(DELIMITER);
        Endereco e = new Endereco(
                campos[7], campos[8], campos[9], campos[10], campos[11], campos[12]
        );

        Funcionario f = new Funcionario();
        f.setMatricula(campos[0]);
        f.setNome(campos[1]);
        f.setCpf(campos[2]);
        f.setDataNascimento(LocalDate.parse(campos[3]));
        f.setCargo(campos[4]);
        f.setSalario(new BigDecimal(campos[5]));
        f.setDataContratacao(LocalDate.parse(campos[6]));
        f.setEndereco(e);

        return f;
    }
}
