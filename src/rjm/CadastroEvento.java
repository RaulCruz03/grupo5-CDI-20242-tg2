package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroEvento extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldData, textFieldDescricao, textFieldNumeroParticipantes, textFieldCodOrganizador;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroEvento frame = new CadastroEvento();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroEvento() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Evento");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(30, 70, 100, 25);
        contentPane.add(lblData);

        textFieldData = new JTextField();
        textFieldData.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldData);

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setBounds(30, 110, 100, 25);
        contentPane.add(lblDescricao);

        textFieldDescricao = new JTextField();
        textFieldDescricao.setBounds(150, 110, 200, 25);
        contentPane.add(textFieldDescricao);

        JLabel lblNumeroParticipantes = new JLabel("Número de Participantes:");
        lblNumeroParticipantes.setBounds(30, 150, 180, 25);
        contentPane.add(lblNumeroParticipantes);

        textFieldNumeroParticipantes = new JTextField();
        textFieldNumeroParticipantes.setBounds(200, 150, 150, 25);
        contentPane.add(textFieldNumeroParticipantes);

        JLabel lblCodOrganizador = new JLabel("Código do Organizador:");
        lblCodOrganizador.setBounds(30, 190, 180, 25);
        contentPane.add(lblCodOrganizador);

        textFieldCodOrganizador = new JTextField();
        textFieldCodOrganizador.setBounds(200, 190, 150, 25);
        contentPane.add(textFieldCodOrganizador);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 230, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String data = textFieldData.getText();
            String descricao = textFieldDescricao.getText();
            int numeroParticipantes = Integer.parseInt(textFieldNumeroParticipantes.getText());
            int codOrganizador = Integer.parseInt(textFieldCodOrganizador.getText());
            addEvento(data, descricao, numeroParticipantes, codOrganizador);
        });
        contentPane.add(btnCadastrar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodEvento");
        tableModel.addColumn("Data");
        tableModel.addColumn("Descrição");
        tableModel.addColumn("Número de Participantes");
        tableModel.addColumn("CodOrganizador");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 270, 720, 200);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(270, 230, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int codEvento = (int) tableModel.getValueAt(selectedRow, 0);
                deleteEvento(codEvento);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um evento para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        loadEventoData();
    }

    private void addEvento(String data, String descricao, int numeroParticipantes, int codOrganizador) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String insertEvento = "INSERT INTO Evento (Data, Descrição, `N° de Participantes`, codOrganizador) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertEvento, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, data);
                stmt.setString(2, descricao);
                stmt.setInt(3, numeroParticipantes);
                stmt.setInt(4, codOrganizador);
                stmt.executeUpdate();

                // Após a inserção, obtém o codEvento gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int codEvento = rs.getInt(1);
                    System.out.println("Evento cadastrado com sucesso! CodEvento: " + codEvento);
                }

                JOptionPane.showMessageDialog(null, "Evento cadastrado com sucesso!");
                loadEventoData();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void loadEventoData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT codEvento, Data, Descrição, `N° de Participantes`, codOrganizador FROM Evento";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("codEvento"), rs.getString("Data"), rs.getString("Descrição"),
                        rs.getInt("N° de Participantes"), rs.getInt("codOrganizador")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEvento(int codEvento) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Evento WHERE codEvento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codEvento);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Evento excluído com sucesso!");
                loadEventoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
