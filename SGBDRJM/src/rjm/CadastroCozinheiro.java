package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroCozinheiro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldEspecialização, textFieldNome, textFieldCpf, textFieldTelefone, textFieldDataContratação;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroCozinheiro frame = new CadastroCozinheiro();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroCozinheiro() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Cozinheiro");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 310, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String Especialização = textFieldEspecialização.getText();
            String nome = textFieldNome.getText();
            String cpf = textFieldCpf.getText();
            String telefone = textFieldTelefone.getText();
            String dataContratação = textFieldDataContratação.getText();
            addCozinheiro(Especialização, nome, cpf, telefone, dataContratação);
        });
        contentPane.add(btnCadastrar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 310, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int idCozinheiro = (int) tableModel.getValueAt(selectedRow, 0);
                deleteCozinheiro(idCozinheiro);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um cozinheiro para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(390, 310, 100, 25);
        btnAtualizar.addActionListener(e -> {
            // Obter a linha selecionada na tabela
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Obter os valores da linha selecionada
                int idCozinheiro = (int) tableModel.getValueAt(selectedRow, 0);
                String Especialização = (String) tableModel.getValueAt(selectedRow, 1);
                String nome = (String) tableModel.getValueAt(selectedRow, 2);
                String cpf = (String) tableModel.getValueAt(selectedRow, 3);
                String telefone = (String) tableModel.getValueAt(selectedRow, 4);
                String dataContratação = (String) tableModel.getValueAt(selectedRow, 5);

                // Carregar os dados da linha selecionada nos campos de texto
                textFieldEspecialização.setText(Especialização);
                textFieldNome.setText(nome);
                textFieldCpf.setText(cpf);
                textFieldTelefone.setText(telefone);
                textFieldDataContratação.setText(dataContratação);
                
                // Atualizar no banco de dados
                updateCozinheiro(idCozinheiro, Especialização, nome, cpf, telefone, dataContratação);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um cozinheiro para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        setupFormFields();
        setupTable();
        loadCozinheiroData();
    }

    private void setupFormFields() {
        JLabel lblEspecialização = new JLabel("Especialização:");
        lblEspecialização.setBounds(30, 70, 120, 25);
        contentPane.add(lblEspecialização);

        textFieldEspecialização = new JTextField();
        textFieldEspecialização.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldEspecialização);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 110, 100, 25);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 110, 200, 25);
        contentPane.add(textFieldNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 150, 100, 25);
        contentPane.add(lblCpf);

        textFieldCpf = new JTextField();
        textFieldCpf.setBounds(150, 150, 200, 25);
        contentPane.add(textFieldCpf);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 190, 100, 25);
        contentPane.add(lblTelefone);

        textFieldTelefone = new JTextField();
        textFieldTelefone.setBounds(150, 190, 200, 25);
        contentPane.add(textFieldTelefone);

        JLabel lblDataContratação = new JLabel("Data de Contratação:");
        lblDataContratação.setBounds(30, 230, 150, 25);
        contentPane.add(lblDataContratação);

        textFieldDataContratação = new JTextField();
        textFieldDataContratação.setBounds(150, 230, 200, 25);
        contentPane.add(textFieldDataContratação);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("IdCozinheiro");
        tableModel.addColumn("Especialização");
        tableModel.addColumn("Nome");
        tableModel.addColumn("CPF");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Data Contratação");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 350, 720, 200);
        contentPane.add(scrollPane);
    }

    private void addCozinheiro(String Especialização, String nome, String cpf, String telefone, String dataContratação) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            int idFuncionário = -1;
            String insertFuncionário = "INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtFuncionário = conn.prepareStatement(insertFuncionário, Statement.RETURN_GENERATED_KEYS)) {
                stmtFuncionário.setString(1, nome);
                stmtFuncionário.setString(2, cpf);
                stmtFuncionário.setString(3, telefone);
                stmtFuncionário.setDate(4, java.sql.Date.valueOf(dataContratação));
                stmtFuncionário.setString(5, "Cozinheiro");

                stmtFuncionário.executeUpdate();
                ResultSet keys = stmtFuncionário.getGeneratedKeys();
                if (keys.next()) idFuncionário = keys.getInt(1);
            }

            String insertCozinheiro = "INSERT INTO Cozinheiro (IdCozinheiro, Especialização) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCozinheiro)) {
                stmt.setInt(1, idFuncionário);
                stmt.setString(2, Especialização);
                stmt.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Cozinheiro cadastrado com sucesso!");
            loadCozinheiroData();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void updateCozinheiro(int idCozinheiro, String Especialização, String nome, String cpf, String telefone, String dataContratação) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE Funcionário f JOIN Cozinheiro c ON f.CodFuncionário = c.IdCozinheiro " +
                                 "SET f.Nome = ?, f.CPF = ?, f.Telefone = ?, f.DataContratação = ?, c.Especialização = ? " +
                                 "WHERE c.IdCozinheiro = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, nome);
                stmt.setString(2, cpf);
                stmt.setString(3, telefone);
                stmt.setDate(4, java.sql.Date.valueOf(dataContratação));
                stmt.setString(5, Especialização);
                stmt.setInt(6, idCozinheiro);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cozinheiro atualizado com sucesso!");
                loadCozinheiroData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o cozinheiro: " + e.getMessage());
        }
    }

    private void loadCozinheiroData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT c.IdCozinheiro, c.Especialização, f.Nome, f.CPF, f.Telefone, f.DataContratação " +
                           "FROM Cozinheiro c JOIN Funcionário f ON c.IdCozinheiro = f.CodFuncionário";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("IdCozinheiro"),
                        rs.getString("Especialização"),
                        rs.getString("Nome"),
                        rs.getString("CPF"),
                        rs.getString("Telefone"),
                        rs.getString("DataContratação")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCozinheiro(int idCozinheiro) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Cozinheiro WHERE IdCozinheiro = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, idCozinheiro);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cozinheiro excluído com sucesso!");
                loadCozinheiroData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
