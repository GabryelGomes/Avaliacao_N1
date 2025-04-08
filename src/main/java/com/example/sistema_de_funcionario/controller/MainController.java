package com.example.sistema_de_funcionario.controller;

import com.example.sistema_de_funcionario.model.Endereco;
import com.example.sistema_de_funcionario.model.Funcionario;
import com.example.sistema_de_funcionario.utils.FuncionarioRepository;
import com.example.sistema_de_funcionario.utils.ValidadorFuncionario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    // Campos de formulário
    @FXML private TextField campoNome, campoCpf, campoMatricula, campoCargo, campoSalario;
    @FXML private DatePicker campoNascimento, campoContratacao;
    @FXML private TextField campoRua, campoNumero, campoBairro, campoCidade, campoEstado, campoCep;

    // Labels de relatório
    @FXML private Label labelMediaSalario, labelQtdFuncionarios;

    // Tabela e colunas
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, String> colunaId, colunaNome, colunaMatricula, colunaCargo, colunaSalario;
    @FXML private TableColumn<Funcionario, String> colunaCpf, colunaNascimento, colunaContratacao;

    private final ObservableList<Funcionario> funcionarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarFuncionarios();
    }

    private void configurarTabela() {
        colunaId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colunaNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));
        colunaMatricula.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMatricula()));
        colunaCargo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCargo()));
        colunaSalario.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSalario().toString()));
        colunaCpf.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCpf()));
        colunaNascimento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNascimento().toString()));
        colunaContratacao.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDataContratacao().toString()));
    }

    @FXML
    public void salvarFuncionario() {
        try {
            String matricula = campoMatricula.getText().trim();
            String nome = campoNome.getText().trim();
            String cpf = campoCpf.getText().trim();
            LocalDate nascimento = campoNascimento.getValue();
            String cargo = campoCargo.getText().trim();
            BigDecimal salario = new BigDecimal(campoSalario.getText().trim());
            LocalDate contratacao = campoContratacao.getValue();
            String cep = campoCep.getText().trim();

            if (!ValidadorFuncionario.validarMatricula(matricula)) {
                mostrarAlerta("Matrícula inválida. Use 6 dígitos numéricos.");
                return;
            }
            if (!ValidadorFuncionario.validarNome(nome)) {
                mostrarAlerta("Nome inválido. Deve ter pelo menos 3 letras.");
                return;
            }
            if (!ValidadorFuncionario.validarCPF(cpf)) {
                mostrarAlerta("CPF inválido.");
                return;
            }
            if (!ValidadorFuncionario.validarIdade(nascimento)) {
                mostrarAlerta("Funcionário deve ter pelo menos 18 anos.");
                return;
            }
            if (!ValidadorFuncionario.validarSalario(salario)) {
                mostrarAlerta("Salário inválido.");
                return;
            }
            if (!ValidadorFuncionario.validarCEP(cep)) {
                mostrarAlerta("CEP inválido.");
                return;
            }

            Endereco endereco = new Endereco(
                    campoRua.getText().trim(),
                    campoNumero.getText().trim(),
                    "",
                    campoBairro.getText().trim(),
                    campoCidade.getText().trim(),
                    campoEstado.getText().trim(),
                    cep
            );

            Funcionario funcionario = new Funcionario(
                    matricula, nome, cpf, nascimento, cargo, salario, contratacao, endereco
            );

            FuncionarioRepository.salvar(funcionario);
            limparCampos();
            carregarFuncionarios();
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar funcionário: " + e.getMessage());
        }
    }

    @FXML
    public void novoFuncionario() {
        limparCampos();
    }

    @FXML
    public void excluirFuncionario() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            FuncionarioRepository.excluir(selecionado.getMatricula());
            carregarFuncionarios();
        } else {
            mostrarAlerta("Selecione um funcionário para excluir.");
        }
    }

    private void carregarFuncionarios() {
        List<Funcionario> lista = FuncionarioRepository.listarTodos();
        funcionarios.setAll(lista);
        tabelaFuncionarios.setItems(funcionarios);
        atualizarRelatorios(lista);
    }

    private void atualizarRelatorios(List<Funcionario> lista) {
        double media = lista.stream()
                .map(Funcionario::getSalario)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(0.0);

        labelMediaSalario.setText(String.format("%.2f", media));
        labelQtdFuncionarios.setText(String.valueOf(lista.size()));
    }

    private void limparCampos() {
        campoNome.clear();
        campoCpf.clear();
        campoMatricula.clear();
        campoCargo.clear();
        campoSalario.clear();
        campoNascimento.setValue(null);
        campoContratacao.setValue(null);
        campoRua.clear();
        campoNumero.clear();
        campoBairro.clear();
        campoCidade.clear();
        campoEstado.clear();
        campoCep.clear();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Extras

    public List<Funcionario> filtrarPorCargo(String cargo) {
        return funcionarios.stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .collect(Collectors.toList());
    }

    public List<Funcionario> filtrarPorFaixaSalarial(BigDecimal min, BigDecimal max) {
        return funcionarios.stream()
                .filter(f -> f.getSalario().compareTo(min) >= 0 && f.getSalario().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public double mediaSalarialPorCargo(String cargo) {
        return funcionarios.stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .mapToDouble(f -> f.getSalario().doubleValue())
                .average()
                .orElse(0.0);
    }

    public List<String> listarFuncionariosPorEstado() {
        return funcionarios.stream()
                .map(f -> f.getEndereco().getEstado())
                .distinct()
                .collect(Collectors.toList());
    }
}
