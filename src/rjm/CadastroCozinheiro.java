package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Font;

public class CadastroCozinheiro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNome, textFieldCpf, textFieldTelefone, textFieldDataContratacao;
    private JTextField textFieldEspecializacao, textFieldCodChef;
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

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 70, 100, 25);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(30, 110, 100, 25);
        contentPane.add(lblCpf);

        textFieldCpf = new JTextField();
        textFieldCpf.setBounds(150, 110, 200, 25);
        contentPane.add(textFieldCpf);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 150, 100, 25);
        contentPane.add(lblTelefone);

        textFieldTelefone = new JTextField();
        textFieldTelefone.setBounds(150, 150, 200, 25);
        contentPane.add(textFieldTelefone);

        JLabel lblDataContratacao = new JLabel("Data de Contratação:");
        lblDataContratacao.setBounds(30, 190, 150, 25);
        contentPane.add(lblDataContratacao);

        textFieldDataContratacao = new JTextField();
        textFieldDataContratacao.setBounds(150, 190, 200, 25);
        contentPane.add(textFieldDataContratacao);

        JLabel lblEspecializacao = new JLabel("Especialização:");
        lblEspecializacao.setBounds(30, 230, 100, 25);
        contentPane.add(lblEspecializacao);

        textFieldEspecializacao = new JTextField();
        textFieldEspecializacao.setBounds(150, 230, 200, 25);
        contentPane.add(textFieldEspecializacao);

        JLabel lblCodChef = new JLabel("Código do Chef:");
        lblCodChef.setBounds(30, 270, 100, 25);
        contentPane.add(lblCodChef);

        textFieldCodChef = new JTextField();
        textFieldCodChef.setBounds(150, 270, 200, 25);
        contentPane.add(textFieldCodChef);

        JLabel lblTipoFuncionario = new JLabel("Tipo de Funcionário:");
        lblTipoFuncionario.setBounds(30, 310, 150, 25);
        contentPane.add(lblTipoFuncionario);

        JTextField textFieldTipoFuncionario = new JTextField();
        textFieldTipoFuncionario.setBounds(150, 310, 200, 25);
        textFieldTipoFuncionario.setText("Cozinheiro");
        textFieldTipoFuncionario.setEditable(false);
        contentPane.add(textFieldTipoFuncionario);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 350, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String nome = textFieldNome.getText();
            String cpf = textFieldCpf.getText();
            String telefone = textFieldTelefone.getText();
            String dataContratacao = textFieldDataContratacao.getText();
            String especializacao = textFieldEspecializacao.getText();
            String codChef = textFieldCodChef.getText();
            addCozinheiro(nome, cpf, telefone, dataContratacao, especializacao, codChef.isEmpty() ? null : Integer.parseInt(codChef));
        });
        contentPane.add(btnCadastrar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("IdCozinheiro");
        tableModel.addColumn("Nome");
        tableModel.addColumn("CPF");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Data Contratação");
        tableModel.addColumn("Especialização");
        tableModel.addColumn("CodChef");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 400, 720, 150);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 350, 100, 25);
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

        loadCozinheiroData();
    }

    private void addCozinheiro(String nome, String cpf, String telefone, String dataContratacao, String especializacao, Integer codChef) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            int idFuncionario = -1;

            String insertFuncionario = "INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtFuncionario = conn.prepareStatement(insertFuncionario, Statement.RETURN_GENERATED_KEYS)) {
                stmtFuncionario.setString(1, nome);
                stmtFuncionario.setString(2, cpf);
                stmtFuncionario.setString(3, telefone);
                stmtFuncionario.setDate(4, java.sql.Date.valueOf(dataContratacao));
                stmtFuncionario.setString(5, "Cozinheiro");

                stmtFuncionario.executeUpdate();
                ResultSet keys = stmtFuncionario.getGeneratedKeys();
                if (keys.next()) idFuncionario = keys.getInt(1);
            }

            String insertCozinheiro = "INSERT INTO Cozinheiro (IdCozinheiro, Especialização, CodChef) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCozinheiro)) {
                stmt.setInt(1, idFuncionario);
                stmt.setString(2, especializacao);
                stmt.setObject(3, codChef, Types.INTEGER);
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

    private void loadCozinheiroData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT c.IdCozinheiro, f.Nome, f.CPF, f.Telefone, f.DataContratação, c.Especialização, c.CodChef " +
                           "FROM Cozinheiro c " +
                           "JOIN Funcionário f ON c.IdCozinheiro = f.CodFuncionário";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("IdCozinheiro"), rs.getString("Nome"), rs.getString("CPF"), 
                        rs.getString("Telefone"), rs.getString("DataContratação"), 
                        rs.getString("Especialização"), rs.getInt("CodChef")
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
