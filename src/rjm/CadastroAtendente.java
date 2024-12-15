package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroAtendente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNome, textFieldCpf, textFieldTelefone, textFieldDataContratacao;
    private JTextField textFieldTurnoTrabalho;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroAtendente frame = new CadastroAtendente();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroAtendente() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Atendente");
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

        JLabel lblTurnoTrabalho = new JLabel("Turno de Trabalho:");
        lblTurnoTrabalho.setBounds(30, 230, 150, 25);
        contentPane.add(lblTurnoTrabalho);

        textFieldTurnoTrabalho = new JTextField();
        textFieldTurnoTrabalho.setBounds(150, 230, 200, 25);
        contentPane.add(textFieldTurnoTrabalho);

        JLabel lblTipoFuncionario = new JLabel("Tipo de Funcionário:");
        lblTipoFuncionario.setBounds(30, 270, 150, 25);
        contentPane.add(lblTipoFuncionario);

        JTextField textFieldTipoFuncionario = new JTextField();
        textFieldTipoFuncionario.setBounds(150, 270, 200, 25);
        textFieldTipoFuncionario.setText("Atendente");
        textFieldTipoFuncionario.setEditable(false);
        contentPane.add(textFieldTipoFuncionario);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 310, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String nome = textFieldNome.getText();
            String cpf = textFieldCpf.getText();
            String telefone = textFieldTelefone.getText();
            String dataContratacao = textFieldDataContratacao.getText();
            String turnoTrabalho = textFieldTurnoTrabalho.getText();
            addAtendente(nome, cpf, telefone, dataContratacao, turnoTrabalho);
        });
        contentPane.add(btnCadastrar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("IdAtendente");
        tableModel.addColumn("Nome");
        tableModel.addColumn("CPF");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Data Contratação");
        tableModel.addColumn("Turno de Trabalho");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 350, 720, 200);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 310, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int idAtendente = (int) tableModel.getValueAt(selectedRow, 0);
                deleteAtendente(idAtendente);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um atendente para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        loadAtendenteData();
    }

    private void addAtendente(String nome, String cpf, String telefone, String dataContratacao, String turnoTrabalho) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            int idFuncionario = -1;

            String insertFuncionario = "INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtFuncionario = conn.prepareStatement(insertFuncionario, Statement.RETURN_GENERATED_KEYS)) {
                stmtFuncionario.setString(1, nome);
                stmtFuncionario.setString(2, cpf);
                stmtFuncionario.setString(3, telefone);
                stmtFuncionario.setDate(4, java.sql.Date.valueOf(dataContratacao));
                stmtFuncionario.setString(5, "Atendente");

                stmtFuncionario.executeUpdate();
                ResultSet keys = stmtFuncionario.getGeneratedKeys();
                if (keys.next()) idFuncionario = keys.getInt(1);
            }

            String insertAtendente = "INSERT INTO Atendente (IdAtendente, `Turno de Trabalho`) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertAtendente)) {
                stmt.setInt(1, idFuncionario);
                stmt.setString(2, turnoTrabalho);
                stmt.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Atendente cadastrado com sucesso!");
            loadAtendenteData();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void loadAtendenteData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT a.IdAtendente, f.Nome, f.CPF, f.Telefone, f.DataContratação, a.`Turno de Trabalho` " +
                           "FROM Atendente a " +
                           "JOIN Funcionário f ON a.IdAtendente = f.CodFuncionário";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("IdAtendente"), rs.getString("Nome"), rs.getString("CPF"),
                        rs.getString("Telefone"), rs.getString("DataContratação"), 
                        rs.getString("Turno de Trabalho")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAtendente(int idAtendente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Atendente WHERE IdAtendente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, idAtendente);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Atendente excluído com sucesso!");
                loadAtendenteData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
