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
import java.text.ParseException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CadastroFuncionario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb?useUnicode=true&characterEncoding=UTF-8";  // Altere para o seu banco de dados
    private static final String DB_USER = "root";  // Seu usuário do banco de dados
    private static final String DB_PASSWORD = "senha";  // Sua senha do banco de dados
    
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroFuncionario frame = new CadastroFuncionario();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        
        textField = new JTextField();
        textField.setBounds(131, 88, 241, 19);
        contentPane.add(textField);
        textField.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("Nome");
        lblNewLabel_1.setBounds(76, 91, 45, 13);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("CPF");
        lblNewLabel_2.setBounds(76, 124, 45, 13);
        contentPane.add(lblNewLabel_2);
        
        textField_1 = new JTextField();
        textField_1.setBounds(131, 121, 155, 19);
        contentPane.add(textField_1);
        textField_1.setColumns(10);
        
        JLabel lblNewLabel_3 = new JLabel("Telefone");
        lblNewLabel_3.setBounds(66, 157, 55, 13);
        contentPane.add(lblNewLabel_3);
        
        textField_2 = new JTextField();
        textField_2.setBounds(131, 154, 155, 19);
        contentPane.add(textField_2);
        textField_2.setColumns(10);
        
        textField_3 = new JTextField();
        textField_3.setBounds(151, 183, 106, 19);
        contentPane.add(textField_3);
        textField_3.setColumns(10);
        
        JLabel lblDataDeContratao = new JLabel("Data de Contratação");
        lblDataDeContratao.setBounds(24, 186, 121, 13);
        contentPane.add(lblDataDeContratao);
        
        JLabel lblNewLabel_4 = new JLabel("Tipo de Funcionário");
        lblNewLabel_4.setBounds(10, 215, 135, 13);
        contentPane.add(lblNewLabel_4);
        
        textField_4 = new JTextField();
        textField_4.setBounds(151, 212, 184, 19);
        contentPane.add(textField_4);
        textField_4.setColumns(10);
        
        JButton btnNewButton = new JButton("Cadastre");
        btnNewButton.setBounds(131, 258, 85, 21);
        contentPane.add(btnNewButton);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodFuncionário"); // Adicionando coluna CodFuncionário
        tableModel.addColumn("Nome");
        tableModel.addColumn("CPF");
        tableModel.addColumn("Telefone");
        tableModel.addColumn("Data Contratação");
        tableModel.addColumn("Tipo Funcionário");
        
        table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setBounds(66, 349, 853, 381);
        contentPane.add(table);
        
        JButton btnExcluirFuncionario = new JButton("Excluir Funcionário");
        btnExcluirFuncionario.setBounds(131, 289, 144, 21);
        contentPane.add(btnExcluirFuncionario);
        
        JLabel lblNewLabel_5 = new JLabel("Código do Funcionário");
        lblNewLabel_5.setBounds(66, 332, 135, 13);
        contentPane.add(lblNewLabel_5);
        
        JLabel lblNewLabel_6 = new JLabel("CPF");
        lblNewLabel_6.setBounds(362, 332, 45, 13);
        contentPane.add(lblNewLabel_6);
        
        JLabel lblNewLabel_7 = new JLabel("Telefone");
        lblNewLabel_7.setBounds(498, 332, 70, 13);
        contentPane.add(lblNewLabel_7);
        
        JLabel lblNewLabel_8 = new JLabel("Data de Contratação");
        lblNewLabel_8.setBounds(636, 332, 121, 13);
        contentPane.add(lblNewLabel_8);
        
        JLabel lblNewLabel_9 = new JLabel("Tipo de Funcionário");
        lblNewLabel_9.setBounds(778, 332, 141, 13);
        contentPane.add(lblNewLabel_9);
        
        JLabel lblNewLabel_10 = new JLabel("Nome");
        lblNewLabel_10.setBounds(211, 332, 45, 13);
        contentPane.add(lblNewLabel_10);
        
        // Action for the "Cadastre" button
        btnNewButton.addActionListener(e -> {
            // Get the values from the text fields
            String nome = textField.getText();
            String cpf = textField_1.getText();
            String telefone = textField_2.getText();
            String dataContratacao = textField_3.getText();
            String tipoFuncionario = textField_4.getText();

            // Call method to insert the employee into the database
            addFuncionario(nome, cpf, telefone, dataContratacao, tipoFuncionario);
        });

        // Action for the "Excluir Funcionário" button
        btnExcluirFuncionario.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Get CodFuncionário from the selected row
                int codFuncionario = (int) tableModel.getValueAt(selectedRow, 0); // Get CodFuncionario from the first column
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja excluir este funcionário?", "Excluir", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteFuncionario(codFuncionario); // Call method to delete the employee
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um funcionário para excluir.");
            }
        });

        // Load data into table when the frame is opened
        loadFuncionarioData();
    }

    private void addFuncionario(String nome, String cpf, String telefone, String dataContratacao, String tipoFuncionario) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nome);
                stmt.setString(2, cpf);
                stmt.setString(3, telefone);
                
                // Convert the date of hiring to SQL format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date sqlDate = new java.sql.Date(sdf.parse(dataContratacao).getTime());
                stmt.setDate(4, sqlDate);
                
                stmt.setString(5, tipoFuncionario);

                // Execute the insert query
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Funcionário cadastrado com sucesso!");

                // Refresh the table data
                loadFuncionarioData();
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    private void loadFuncionarioData() {
        // Clear the existing data in the table
        tableModel.setRowCount(0);

        // Attempt to load the data from the database, excluding certain types
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT CodFuncionário, Nome, CPF, Telefone, DataContratação, TipodeFuncionário FROM Funcionário " +
                           "WHERE TipodeFuncionário NOT IN ('Gerente', 'Cozinheiro', 'Atendente')";  // Filtra os tipos indesejados
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                // Check if there are any results
                if (!rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(null, "Nenhum funcionário encontrado.");
                }

                // Add the rows from the result set into the table model
                while (rs.next()) {
                    int codFuncionario = rs.getInt("CodFuncionário");
                    String nome = rs.getString("Nome");
                    String cpf = rs.getString("CPF");
                    String telefone = rs.getString("Telefone");
                    String dataContratacao = rs.getString("DataContratação");
                    String tipoFuncionario = rs.getString("TipodeFuncionário");

                    // Add each row to the table
                    tableModel.addRow(new Object[]{codFuncionario, nome, cpf, telefone, dataContratacao, tipoFuncionario});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados de funcionários: " + e.getMessage());
        }
    }

    private void deleteFuncionario(int codFuncionario) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Funcionário WHERE CodFuncionário = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codFuncionario);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Funcionário excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Funcionário não encontrado.");
                }

                // Refresh the table data
                loadFuncionarioData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir funcionário: " + e.getMessage());
        }
    }
}
