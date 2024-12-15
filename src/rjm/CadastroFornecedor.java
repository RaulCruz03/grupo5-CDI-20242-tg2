package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroFornecedor extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNome, textFieldTelefone;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroFornecedor frame = new CadastroFornecedor();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroFornecedor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Fornecedor");
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

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 150, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String nome = textFieldNome.getText();
            String telefone = textFieldTelefone.getText();
            addFornecedor(nome, telefone);
        });
        contentPane.add(btnCadastrar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodFornecedor");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Telefone");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 200, 720, 200);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 150, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codFornecedor = (int) tableModel.getValueAt(selectedRow, 0);
                deleteFornecedor(codFornecedor);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um fornecedor para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        loadFornecedorData();
    }

    private void addFornecedor(String nome, String telefone) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String insertFornecedor = "INSERT INTO Fornecedor (Nome, Telefone) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertFornecedor, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nome);
                stmt.setString(2, telefone);
                stmt.executeUpdate();

                // Após a inserção, obtém o CodFornecedor gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int codFornecedor = rs.getInt(1);
                    System.out.println("Fornecedor cadastrado com sucesso! CodFornecedor: " + codFornecedor);
                }

                JOptionPane.showMessageDialog(null, "Fornecedor cadastrado com sucesso!");
                loadFornecedorData();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void loadFornecedorData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT CodFornecedor, Nome, Telefone FROM Fornecedor";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("CodFornecedor"), rs.getString("Nome"), rs.getString("Telefone")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFornecedor(int codFornecedor) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Fornecedor WHERE CodFornecedor = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codFornecedor);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Fornecedor excluído com sucesso!");
                loadFornecedorData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
