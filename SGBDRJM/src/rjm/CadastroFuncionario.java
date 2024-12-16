package rjm;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CadastroFuncionario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField, textField_1, textField_2, textField_3, textField_4;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb"; // Alterar conforme seu BD
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroFuncionario frame = new CadastroFuncionario();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroFuncionario() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1298, 834);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Funcionário Geral");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblNewLabel.setBounds(66, 24, 350, 54);
        contentPane.add(lblNewLabel);

        // Labels e TextFields
        addInputFields();

        // Botões
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(131, 258, 100, 21);
        contentPane.add(btnCadastrar);

        JButton btnExcluir = new JButton("Excluir Funcionário");
        btnExcluir.setBounds(240, 258, 150, 21);
        contentPane.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar Funcionário");
        btnAtualizar.setBounds(400, 258, 170, 21);
        contentPane.add(btnAtualizar);

        // Tabela
        setupTable();

        // Ações dos botões
        btnCadastrar.addActionListener(e -> addFuncionario());
        btnExcluir.addActionListener(e -> deleteFuncionario());
        btnAtualizar.addActionListener(e -> updateFuncionario());

        // Carregar os dados da tabela ao iniciar
        loadFuncionarioData();
    }

    private void addInputFields() {
        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(76, 91, 45, 13);
        contentPane.add(lblNome);
        textField = new JTextField();
        textField.setBounds(131, 88, 241, 19);
        contentPane.add(textField);

        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setBounds(76, 124, 45, 13);
        contentPane.add(lblCpf);
        textField_1 = new JTextField();
        textField_1.setBounds(131, 121, 155, 19);
        contentPane.add(textField_1);

        JLabel lblTelefone = new JLabel("Telefone");
        lblTelefone.setBounds(66, 157, 55, 13);
        contentPane.add(lblTelefone);
        textField_2 = new JTextField();
        textField_2.setBounds(131, 154, 155, 19);
        contentPane.add(textField_2);

        JLabel lblData = new JLabel("Data de Contratação (A-M-D)");
        lblData.setBounds(24, 186, 200, 13);
        contentPane.add(lblData);
        textField_3 = new JTextField();
        textField_3.setBounds(195, 183, 141, 19);
        contentPane.add(textField_3);

        JLabel lblTipo = new JLabel("Tipo de Funcionário");
        lblTipo.setBounds(10, 215, 135, 13);
        contentPane.add(lblTipo);
        textField_4 = new JTextField();
        textField_4.setBounds(151, 212, 184, 19);
        contentPane.add(textField_4);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodFuncionário");
        tableModel.addColumn("Nome");
        tableModel.addColumn("CPF");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Data Contratação");
        tableModel.addColumn("Tipo Funcionário");

        table = new JTable(tableModel);
        table.setBounds(66, 349, 853, 381);
        contentPane.add(table);
        
        JLabel lblNewLabel_1 = new JLabel("Código");
        lblNewLabel_1.setBounds(66, 326, 91, 13);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("Nome");
        lblNewLabel_2.setBounds(207, 326, 79, 13);
        contentPane.add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("CPF");
        lblNewLabel_3.setBounds(352, 326, 91, 13);
        contentPane.add(lblNewLabel_3);
        
        JLabel lblNewLabel_4 = new JLabel("Telefone");
        lblNewLabel_4.setBounds(493, 326, 77, 13);
        contentPane.add(lblNewLabel_4);
        
        JLabel lblNewLabel_5 = new JLabel("DataContratação");
        lblNewLabel_5.setBounds(637, 326, 109, 13);
        contentPane.add(lblNewLabel_5);
        
        JLabel lblNewLabel_6 = new JLabel("Tipo de Funcionário");
        lblNewLabel_6.setBounds(777, 326, 170, 13);
        contentPane.add(lblNewLabel_6);
    }

    private void addFuncionario() {
        String nome = textField.getText();
        String cpf = textField_1.getText();
        String telefone = textField_2.getText();
        String dataContratacao = textField_3.getText();
        String tipoFuncionario = textField_4.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nome);
                stmt.setString(2, cpf);
                stmt.setString(3, telefone);
                stmt.setDate(4, java.sql.Date.valueOf(dataContratacao));
                stmt.setString(5, tipoFuncionario);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Funcionário cadastrado com sucesso!");
                loadFuncionarioData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void deleteFuncionario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int codFuncionario = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "DELETE FROM Funcionário WHERE CodFuncionário = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, codFuncionario);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Funcionário excluído!");
                    loadFuncionarioData();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao excluir: " + e.getMessage());
            }
        }
    }

    private void updateFuncionario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int codFuncionario = (int) tableModel.getValueAt(selectedRow, 0);
            String nome = (String) tableModel.getValueAt(selectedRow, 1);
            String cpf = (String) tableModel.getValueAt(selectedRow, 2);
            String telefone = (String) tableModel.getValueAt(selectedRow, 3);
            String dataContratacao = (String) tableModel.getValueAt(selectedRow, 4);
            String tipoFuncionario = (String) tableModel.getValueAt(selectedRow, 5);

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "UPDATE Funcionário SET Nome = ?, CPF = ?, Telefone = ?, DataContratação = ?, TipodeFuncionário = ? WHERE CodFuncionário = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, cpf);
                    stmt.setString(3, telefone);
                    stmt.setDate(4, java.sql.Date.valueOf(dataContratacao));
                    stmt.setString(5, tipoFuncionario);
                    stmt.setInt(6, codFuncionario);

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Funcionário atualizado!");
                    loadFuncionarioData();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um funcionário para atualizar.");
        }
    }

    private void loadFuncionarioData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM Funcionário";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[] {
                        rs.getInt("CodFuncionário"),
                        rs.getString("Nome"),
                        rs.getString("CPF"),
                        rs.getString("Telefone"),
                        rs.getString("DataContratação"),
                        rs.getString("TipodeFuncionário")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados: " + e.getMessage());
        }
    }
}
