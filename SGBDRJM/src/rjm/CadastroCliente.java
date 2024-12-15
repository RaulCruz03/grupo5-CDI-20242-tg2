package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroCliente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNome, textFieldTelefone, textFieldNumeroFidelidade;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroCliente frame = new CadastroCliente();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroCliente() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Cliente");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 70, 100, 25);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldNome);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 110, 100, 25);
        contentPane.add(lblTelefone);

        textFieldTelefone = new JTextField();
        textFieldTelefone.setBounds(150, 110, 200, 25);
        contentPane.add(textFieldTelefone);

        JLabel lblNumeroFidelidade = new JLabel("Número de Fidelidade:");
        lblNumeroFidelidade.setBounds(30, 150, 180, 25);
        contentPane.add(lblNumeroFidelidade);

        textFieldNumeroFidelidade = new JTextField();
        textFieldNumeroFidelidade.setBounds(200, 150, 150, 25);
        contentPane.add(textFieldNumeroFidelidade);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 190, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String nome = textFieldNome.getText();
            String telefone = textFieldTelefone.getText();
            String numeroFidelidadeStr = textFieldNumeroFidelidade.getText();
            addCliente(nome, telefone, numeroFidelidadeStr);
        });
        contentPane.add(btnCadastrar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodCliente");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Número de Fidelidade");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 230, 720, 200);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 190, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codCliente = (int) tableModel.getValueAt(selectedRow, 0);
                deleteCliente(codCliente);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um cliente para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(390, 190, 100, 25);
        btnAtualizar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codCliente = (int) tableModel.getValueAt(selectedRow, 0);
                String nome = (String) tableModel.getValueAt(selectedRow, 1);
                String telefone = (String) tableModel.getValueAt(selectedRow, 2);
                String numeroFidelidadeStr = (String) tableModel.getValueAt(selectedRow, 3);

                // Atualiza os campos
                textFieldNome.setText(nome);
                textFieldTelefone.setText(telefone);
                textFieldNumeroFidelidade.setText(numeroFidelidadeStr);

                // Atualiza o cliente no banco de dados
                updateCliente(codCliente, nome, telefone, numeroFidelidadeStr);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um cliente para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        loadClienteData();
    }

    private void addCliente(String nome, String telefone, String numeroFidelidadeStr) {
        // Valida o número de fidelidade
        int numeroFidelidade;
        try {
            numeroFidelidade = Integer.parseInt(numeroFidelidadeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O número de fidelidade deve ser um número inteiro.");
            return; // Interrompe a execução se a entrada for inválida
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String insertCliente = "INSERT INTO Cliente (Nome, Telefone, `Número de Fidelidade`) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCliente, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nome);
                stmt.setString(2, telefone);
                stmt.setInt(3, numeroFidelidade);
                stmt.executeUpdate();

                // Após a inserção, obtém o CodCliente gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int codCliente = rs.getInt(1);
                    System.out.println("Cliente cadastrado com sucesso! CodCliente: " + codCliente);
                }

                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                loadClienteData();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void loadClienteData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT CodCliente, Nome, Telefone, `Número de Fidelidade` FROM Cliente";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("CodCliente"), rs.getString("Nome"), rs.getString("Telefone"),
                        rs.getInt("Número de Fidelidade")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCliente(int codCliente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Cliente WHERE CodCliente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codCliente);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!");
                loadClienteData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCliente(int codCliente, String nome, String telefone, String numeroFidelidadeStr) {
        // Valida o número de fidelidade
        int numeroFidelidade;
        try {
            numeroFidelidade = Integer.parseInt(numeroFidelidadeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O número de fidelidade deve ser um número inteiro.");
            return; // Interrompe a execução se a entrada for inválida
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateCliente = "UPDATE Cliente SET Nome = ?, Telefone = ?, `Número de Fidelidade` = ? WHERE CodCliente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateCliente)) {
                stmt.setString(1, nome);
                stmt.setString(2, telefone);
                stmt.setInt(3, numeroFidelidade);
                stmt.setInt(4, codCliente);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!");
                loadClienteData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
