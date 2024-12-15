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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CadastroMesa extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNumero;
    private JTextField textFieldCapacidade;
    private JTextField textFieldStatus;
    
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";  // Seu usuário do banco de dados
    private static final String DB_PASSWORD = "senha";  // Sua senha do banco de dados
    
    private JTable table;
    private DefaultTableModel tableModel;

    private boolean isUpdate = false;  // Flag to check if we are updating an existing mesa
    private int currentMesaNumero = -1; // Variable to hold the current mesa's number for update

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroMesa frame = new CadastroMesa();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CadastroMesa() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1298, 834);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblTitulo = new JLabel("Cadastro de Mesa");
        lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblTitulo.setBounds(66, 24, 350, 54);
        contentPane.add(lblTitulo);
        
        JLabel lblNumero = new JLabel("Número");
        lblNumero.setBounds(66, 91, 55, 13);
        contentPane.add(lblNumero);
        
        textFieldNumero = new JTextField();
        textFieldNumero.setBounds(131, 88, 155, 19);
        contentPane.add(textFieldNumero);
        textFieldNumero.setColumns(10);
        
        JLabel lblCapacidade = new JLabel("Capacidade");
        lblCapacidade.setBounds(66, 124, 80, 13);
        contentPane.add(lblCapacidade);
        
        textFieldCapacidade = new JTextField();
        textFieldCapacidade.setBounds(151, 121, 106, 19);
        contentPane.add(textFieldCapacidade);
        textFieldCapacidade.setColumns(10);
        
        JLabel lblStatus = new JLabel("Status");
        lblStatus.setBounds(66, 157, 45, 13);
        contentPane.add(lblStatus);
        
        textFieldStatus = new JTextField();
        textFieldStatus.setBounds(131, 154, 155, 19);
        contentPane.add(textFieldStatus);
        textFieldStatus.setColumns(10);
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(131, 188, 106, 21);
        contentPane.add(btnCadastrar);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Número");
        tableModel.addColumn("Capacidade");
        tableModel.addColumn("Status");
        
        table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setBounds(66, 349, 853, 381);
        contentPane.add(table);
        
        JButton btnExcluirMesa = new JButton("Excluir Mesa");
        btnExcluirMesa.setBounds(131, 220, 144, 21);
        contentPane.add(btnExcluirMesa);
        
        JButton btnAtualizarMesa = new JButton("Atualizar Mesa");
        btnAtualizarMesa.setBounds(131, 250, 144, 21);  // Adicionando o novo botão
        contentPane.add(btnAtualizarMesa);
        
        JLabel lblNewLabel = new JLabel("Número da Mesa");
        lblNewLabel.setBounds(66, 326, 95, 13);
        contentPane.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Capacidade");
        lblNewLabel_1.setBounds(348, 326, 73, 13);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("Status");
        lblNewLabel_2.setBounds(636, 326, 45, 13);
        contentPane.add(lblNewLabel_2);
        
        // Action for the "Cadastrar" button
        btnCadastrar.addActionListener(e -> {
            try {
                int numero = Integer.parseInt(textFieldNumero.getText());
                int capacidade = Integer.parseInt(textFieldCapacidade.getText());
                String status = textFieldStatus.getText();

                if (isUpdate) {
                    // Update the existing mesa
                    updateMesa(currentMesaNumero, numero, capacidade, status);
                    btnCadastrar.setText("Cadastrar"); // Reset the button text
                    isUpdate = false; // Reset the update flag
                } else {
                    // Add a new mesa
                    addMesa(numero, capacidade, status);
                }

                // Clear text fields after action
                textFieldNumero.setText("");
                textFieldCapacidade.setText("");
                textFieldStatus.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, insira números válidos para número e capacidade.");
            }
        });

        // Action for the "Excluir Mesa" button
        btnExcluirMesa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int numero = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja excluir esta mesa?", "Excluir", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteMesa(numero);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma mesa para excluir.");
            }
        });

        // Action for the "Atualizar Mesa" button
        btnAtualizarMesa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Get selected row data
                int numero = (int) tableModel.getValueAt(selectedRow, 0);
                String status = (String) tableModel.getValueAt(selectedRow, 2);
                int capacidade = (int) tableModel.getValueAt(selectedRow, 1);

                // Set the data into text fields for editing
                textFieldNumero.setText(String.valueOf(numero));
                textFieldCapacidade.setText(String.valueOf(capacidade));
                textFieldStatus.setText(status);

                // Set the current mesa's number for updating
                currentMesaNumero = numero;
                isUpdate = true; // Enable update flag
                btnCadastrar.setText("Salvar Alterações"); // Change button text to "Save Changes"
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma mesa para atualizar.");
            }
        });

        // Load data into table when the frame is opened
        loadMesaData();
    }

    private void addMesa(int numero, int capacidade, String status) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Mesa (Número, Capacidade, Status) VALUES (?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, numero);
                stmt.setInt(2, capacidade);
                stmt.setString(3, status);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Mesa cadastrada com sucesso!");
                loadMesaData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar mesa: " + e.getMessage());
        }
    }

    private void loadMesaData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Número, Capacidade, Status FROM Mesa";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int numero = rs.getInt("Número");
                    int capacidade = rs.getInt("Capacidade");
                    String status = rs.getString("Status");
                    tableModel.addRow(new Object[]{numero, capacidade, status});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados de mesas: " + e.getMessage());
        }
    }

    private void deleteMesa(int numero) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Mesa WHERE Número = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, numero);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Mesa excluída com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Mesa não encontrada.");
                }
                loadMesaData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir mesa: " + e.getMessage());
        }
    }

    private void updateMesa(int oldNumero, int newNumero, int newCapacidade, String newStatus) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE Mesa SET Número = ?, Capacidade = ?, Status = ? WHERE Número = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, newNumero);
                stmt.setInt(2, newCapacidade);
                stmt.setString(3, newStatus);
                stmt.setInt(4, oldNumero);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Mesa atualizada com sucesso!");
                loadMesaData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar mesa: " + e.getMessage());
        }
    }
}
