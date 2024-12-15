package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroGerente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldDepartamento, textFieldNivelGestao, textFieldNome, textFieldCpf, textFieldTelefone, textFieldDataContratação;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroGerente frame = new CadastroGerente();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroGerente() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Gerente");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 310, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String departamento = textFieldDepartamento.getText();
            String nivelGestao = textFieldNivelGestao.getText();
            String nome = textFieldNome.getText();
            String cpf = textFieldCpf.getText();
            String telefone = textFieldTelefone.getText();
            String dataContratação = textFieldDataContratação.getText();
            addGerente(departamento, nivelGestao, nome, cpf, telefone, dataContratação);
        });
        contentPane.add(btnCadastrar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 310, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codGerente = (int) tableModel.getValueAt(selectedRow, 0);
                deleteGerente(codGerente);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um gerente para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(390, 310, 100, 25);
        btnAtualizar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codGerente = (int) tableModel.getValueAt(selectedRow, 0);
                String departamento = (String) tableModel.getValueAt(selectedRow, 1);
                String nivelGestao = (String) tableModel.getValueAt(selectedRow, 2);
                String nome = (String) tableModel.getValueAt(selectedRow, 3);
                String cpf = (String) tableModel.getValueAt(selectedRow, 4);
                String telefone = (String) tableModel.getValueAt(selectedRow, 5);
                String dataContratação = (String) tableModel.getValueAt(selectedRow, 6);

                textFieldDepartamento.setText(departamento);
                textFieldNivelGestao.setText(nivelGestao);
                textFieldNome.setText(nome);
                textFieldCpf.setText(cpf);
                textFieldTelefone.setText(telefone);
                textFieldDataContratação.setText(dataContratação);
                
                updateGerente(codGerente, departamento, nivelGestao, nome, cpf, telefone, dataContratação);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um gerente para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        setupFormFields();
        setupTable();
        loadGerenteData();
    }

    private void setupFormFields() {
        JLabel lblDepartamento = new JLabel("Departamento:");
        lblDepartamento.setBounds(30, 70, 120, 25);
        contentPane.add(lblDepartamento);

        textFieldDepartamento = new JTextField();
        textFieldDepartamento.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldDepartamento);

        JLabel lblNivelGestao = new JLabel("Nível de Gestão:");
        lblNivelGestao.setBounds(30, 110, 120, 25);
        contentPane.add(lblNivelGestao);

        textFieldNivelGestao = new JTextField();
        textFieldNivelGestao.setBounds(150, 110, 200, 25);
        contentPane.add(textFieldNivelGestao);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 150, 100, 25);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 150, 200, 25);
        contentPane.add(textFieldNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 190, 100, 25);
        contentPane.add(lblCpf);

        textFieldCpf = new JTextField();
        textFieldCpf.setBounds(150, 190, 200, 25);
        contentPane.add(textFieldCpf);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 230, 100, 25);
        contentPane.add(lblTelefone);

        textFieldTelefone = new JTextField();
        textFieldTelefone.setBounds(150, 230, 200, 25);
        contentPane.add(textFieldTelefone);

        JLabel lblDataContratação = new JLabel("Data de Contratação:");
        lblDataContratação.setBounds(30, 270, 150, 25);
        contentPane.add(lblDataContratação);

        textFieldDataContratação = new JTextField();
        textFieldDataContratação.setBounds(150, 270, 200, 25);
        contentPane.add(textFieldDataContratação);
    }

    private void setupTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodGerente");
        tableModel.addColumn("Departamento");
        tableModel.addColumn("Nível de Gestão");
        tableModel.addColumn("Nome");
        tableModel.addColumn("CPF");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Data Contratação");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 350, 720, 200);
        contentPane.add(scrollPane);
    }

    private void addGerente(String departamento, String nivelGestao, String nome, String cpf, String telefone, String dataContratação) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            int idFuncionário = -1;
            String insertFuncionário = "INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtFuncionário = conn.prepareStatement(insertFuncionário, Statement.RETURN_GENERATED_KEYS)) {
                stmtFuncionário.setString(1, nome);
                stmtFuncionário.setString(2, cpf);
                stmtFuncionário.setString(3, telefone);
                stmtFuncionário.setDate(4, java.sql.Date.valueOf(dataContratação));
                stmtFuncionário.setString(5, "Gerente");

                stmtFuncionário.executeUpdate();
                ResultSet keys = stmtFuncionário.getGeneratedKeys();
                if (keys.next()) idFuncionário = keys.getInt(1);
            }

            String insertGerente = "INSERT INTO Gerente (CodGerente, Departamento, `Nível de Gestão`) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertGerente)) {
                stmt.setInt(1, idFuncionário);
                stmt.setString(2, departamento);
                stmt.setString(3, nivelGestao);
                stmt.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Gerente cadastrado com sucesso!");
            loadGerenteData();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void updateGerente(int codGerente, String departamento, String nivelGestao, String nome, String cpf, String telefone, String dataContratação) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE Funcionário f JOIN Gerente g ON f.CodFuncionário = g.CodGerente " +
                                 "SET f.Nome = ?, f.CPF = ?, f.Telefone = ?, f.DataContratação = ?, g.Departamento = ?, g.`Nível de Gestão` = ? " +
                                 "WHERE g.CodGerente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, nome);
                stmt.setString(2, cpf);
                stmt.setString(3, telefone);
                stmt.setDate(4, java.sql.Date.valueOf(dataContratação));
                stmt.setString(5, departamento);
                stmt.setString(6, nivelGestao);
                stmt.setInt(7, codGerente);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Gerente atualizado com sucesso!");
                loadGerenteData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o gerente: " + e.getMessage());
        }
    }

    private void loadGerenteData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT g.CodGerente, g.Departamento, g.`Nível de Gestão`, f.Nome, f.CPF, f.Telefone, f.DataContratação " +
                           "FROM Gerente g JOIN Funcionário f ON g.CodGerente = f.CodFuncionário";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[] {
                        rs.getInt("CodGerente"),
                        rs.getString("Departamento"),
                        rs.getString("Nível de Gestão"),
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

    private void deleteGerente(int codGerente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Gerente WHERE CodGerente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codGerente);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Gerente excluído com sucesso!");
                loadGerenteData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
