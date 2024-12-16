package rjm;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CadastroFuncionarioEvento extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> comboBoxFuncionario;
    private JComboBox<String> comboBoxEvento;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroFuncionarioEvento frame = new CadastroFuncionarioEvento();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroFuncionarioEvento() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Funcionário/Evento");
        lblTitulo.setBounds(30, 20, 400, 30);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(24f));
        contentPane.add(lblTitulo);

        JLabel lblFuncionario = new JLabel("Funcionário:");
        lblFuncionario.setBounds(30, 70, 100, 25);
        contentPane.add(lblFuncionario);

        comboBoxFuncionario = new JComboBox<>();
        comboBoxFuncionario.setBounds(150, 70, 200, 25);
        contentPane.add(comboBoxFuncionario);

        JLabel lblEvento = new JLabel("Evento:");
        lblEvento.setBounds(30, 110, 100, 25);
        contentPane.add(lblEvento);

        comboBoxEvento = new JComboBox<>();
        comboBoxEvento.setBounds(150, 110, 200, 25);
        contentPane.add(comboBoxEvento);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 150, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String funcionario = (String) comboBoxFuncionario.getSelectedItem();
            String evento = (String) comboBoxEvento.getSelectedItem();

            if (funcionario != null && evento != null) {
                int codFuncionario = Integer.parseInt(funcionario.split(" - ")[0]);
                int codEvento = Integer.parseInt(evento.split(" - ")[0]);
                addFuncionarioEvento(codFuncionario, codEvento);
            } else {
                JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos.");
            }
        });
        contentPane.add(btnCadastrar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(270, 150, 100, 25);
        btnAtualizar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String funcionario = (String) comboBoxFuncionario.getSelectedItem();
                String evento = (String) comboBoxEvento.getSelectedItem();

                int codFuncionario = Integer.parseInt(funcionario.split(" - ")[0]);
                int codEvento = Integer.parseInt(evento.split(" - ")[0]);

                updateFuncionarioEvento(codFuncionario, codEvento);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um registro para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(390, 150, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String funcionario = (String) tableModel.getValueAt(selectedRow, 0);
                String evento = (String) tableModel.getValueAt(selectedRow, 1);

                int codFuncionario = Integer.parseInt(funcionario.split(" - ")[0]);
                int codEvento = Integer.parseInt(evento.split(" - ")[0]);

                deleteFuncionarioEvento(codFuncionario, codEvento);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um registro para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Funcionário");
        tableModel.addColumn("Evento");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 230, 720, 200);
        contentPane.add(scrollPane);

        loadComboBoxData();
        loadFuncionarioEventoData();
    }

    private void loadComboBoxData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Carregar funcionários
            comboBoxFuncionario.removeAllItems();
            String queryFuncionario = "SELECT CodFuncionário, Nome FROM Funcionário";
            try (PreparedStatement stmt = conn.prepareStatement(queryFuncionario);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comboBoxFuncionario.addItem(rs.getInt("CodFuncionário") + " - " + rs.getString("Nome"));
                }
            }

            // Carregar eventos
            comboBoxEvento.removeAllItems();
            String queryEvento = "SELECT codEvento, Descrição FROM Evento";
            try (PreparedStatement stmt = conn.prepareStatement(queryEvento);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comboBoxEvento.addItem(rs.getInt("codEvento") + " - " + rs.getString("Descrição"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFuncionarioEventoData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * from funcionario_evento";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("CodFuncionário") + " - " + rs.getString("NomeFuncionario"),
                        rs.getInt("CodEvento") + " - " + rs.getString("DescricaoEvento")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addFuncionarioEvento(int codFuncionario, int codEvento) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO `Funcionário/Evento` (CodFuncionário, CodEvento) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codFuncionario);
                stmt.setInt(2, codEvento);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Funcionário/Event registrado com sucesso!");
                loadFuncionarioEventoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar registro: " + e.getMessage());
        }
    }

    private void updateFuncionarioEvento(int codFuncionario, int codEvento) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE `Funcionário/Evento` SET CodEvento = ? WHERE CodFuncionário = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codEvento);
                stmt.setInt(2, codFuncionario);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Funcionário/Evento atualizado com sucesso!");
                loadFuncionarioEventoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar registro: " + e.getMessage());
        }
    }

    private void deleteFuncionarioEvento(int codFuncionario, int codEvento) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM `Funcionário/Evento` WHERE CodFuncionário = ? AND CodEvento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codFuncionario);
                stmt.setInt(2, codEvento);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!");
                loadFuncionarioEventoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir registro: " + e.getMessage());
        }
    }
}
