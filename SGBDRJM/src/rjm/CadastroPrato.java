package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroPrato extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNome, textFieldTempoPreparo, textFieldPreco, textFieldDescricao;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroPrato frame = new CadastroPrato();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroPrato() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Prato");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 70, 100, 25);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldNome);

        JLabel lblTempoPreparo = new JLabel("Tempo de Preparo:");
        lblTempoPreparo.setBounds(30, 110, 200, 25);
        contentPane.add(lblTempoPreparo);

        textFieldTempoPreparo = new JTextField();
        textFieldTempoPreparo.setBounds(150, 110, 150, 25);
        contentPane.add(textFieldTempoPreparo);

        JLabel lblPreco = new JLabel("Preço (R$):");
        lblPreco.setBounds(30, 150, 100, 25);
        contentPane.add(lblPreco);

        textFieldPreco = new JTextField();
        textFieldPreco.setBounds(150, 150, 150, 25);
        contentPane.add(textFieldPreco);

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setBounds(30, 190, 100, 25);
        contentPane.add(lblDescricao);

        textFieldDescricao = new JTextField();
        textFieldDescricao.setBounds(150, 190, 200, 25);
        contentPane.add(textFieldDescricao);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 230, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String nome = textFieldNome.getText();
            String tempoPreparo = textFieldTempoPreparo.getText();
            String precoStr = textFieldPreco.getText();
            String descricao = textFieldDescricao.getText();
            addPrato(nome, tempoPreparo, precoStr, descricao);
        });
        contentPane.add(btnCadastrar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodPrato");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Tempo de Preparo");
        tableModel.addColumn("Preço");
        tableModel.addColumn("Descrição");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 270, 720, 200);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 230, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codPrato = (int) tableModel.getValueAt(selectedRow, 0);
                deletePrato(codPrato);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um prato para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(390, 230, 100, 25);
        btnAtualizar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codPrato = (int) tableModel.getValueAt(selectedRow, 0);
                String nome = (String) tableModel.getValueAt(selectedRow, 1);
                String tempoPreparo = (String) tableModel.getValueAt(selectedRow, 2);
                String precoStr = (String) tableModel.getValueAt(selectedRow, 3);
                String descricao = (String) tableModel.getValueAt(selectedRow, 4);

                // Atualiza os campos
                textFieldNome.setText(nome);
                textFieldTempoPreparo.setText(tempoPreparo);
                textFieldPreco.setText(precoStr);
                textFieldDescricao.setText(descricao);

                // Atualiza o prato no banco de dados
                updatePrato(codPrato, nome, tempoPreparo, precoStr, descricao);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um prato para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        loadPratoData();
    }

    private void addPrato(String nome, String tempoPreparo, String precoStr, String descricao) {
        // Valida o preço
        double preco;
        try {
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O preço deve ser um número válido.");
            return; // Interrompe a execução se a entrada for inválida
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String insertPrato = "INSERT INTO Prato (Nome, `Tempo de Preparo`, Preço, Descrição) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertPrato, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nome);
                stmt.setString(2, tempoPreparo);
                stmt.setDouble(3, preco);
                stmt.setString(4, descricao);
                stmt.executeUpdate();

                // Após a inserção, obtém o CodPrato gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int codPrato = rs.getInt(1);
                    System.out.println("Prato cadastrado com sucesso! CodPrato: " + codPrato);
                }

                JOptionPane.showMessageDialog(null, "Prato cadastrado com sucesso!");
                loadPratoData();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void loadPratoData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT CodPrato, Nome, `Tempo de Preparo`, Preço, Descrição FROM Prato";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("CodPrato"), rs.getString("Nome"), rs.getString("Tempo de Preparo"),
                        rs.getDouble("Preço"), rs.getString("Descrição")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePrato(int codPrato) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Prato WHERE CodPrato = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codPrato);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Prato excluído com sucesso!");
                loadPratoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePrato(int codPrato, String nome, String tempoPreparo, String precoStr, String descricao) {
        // Valida o preço
        double preco;
        try {
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O preço deve ser um número válido.");
            return; // Interrompe a execução se a entrada for inválida
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updatePrato = "UPDATE Prato SET Nome = ?, `Tempo de Preparo` = ?, Preço = ?, Descrição = ? WHERE CodPrato = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updatePrato)) {
                stmt.setString(1, nome);
                stmt.setString(2, tempoPreparo);
                stmt.setDouble(3, preco);
                stmt.setString(4, descricao);
                stmt.setInt(5, codPrato);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Prato atualizado com sucesso!");
                loadPratoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
