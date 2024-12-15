package rjm;

import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Tabela extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Tabela frame = new Tabela();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Tabela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 938, 662);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Visualização de Entidades");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 35));
        lblNewLabel.setBounds(117, 34, 411, 55);
        contentPane.add(lblNewLabel);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setBounds(127, 103, 217, 21);
        contentPane.add(comboBox);
        comboBox.addItem("Selecione");
        comboBox.addItem("Funcionário");
        comboBox.addItem("Gerente");
        comboBox.addItem("Cozinheiro");
        comboBox.addItem("Atendente");
        comboBox.addItem("Evento");
        comboBox.addItem("Fornecedor");
        comboBox.addItem("Ingrediente");
        comboBox.addItem("Prato");
        comboBox.addItem("Pedido");
        comboBox.addItem("Cliente");
        comboBox.addItem("Mesa");
        comboBox.addItem("Item");
        comboBox.addItem("Fornecimento");
        comboBox.addItem("Funcionário/Evento");
        

        JButton btnNewButton = new JButton("Procurar");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnNewButton.setBounds(368, 103, 85, 21);
        contentPane.add(btnNewButton);

        table = new JTable();
        table.setBounds(50, 150, 800, 400);
        contentPane.add(table);

        // Populate combo box with database entities
        populateComboBox(comboBox);

        // Add action listener to the button
        btnNewButton.addActionListener(e -> {
            String selectedTable = (String) comboBox.getSelectedItem();
            if (selectedTable != null) {
                displayTableData(selectedTable);
            }
        });
    }

    /**
     * Populate the combo box with table names from the database.
     */
    private void populateComboBox(JComboBox<String> comboBox) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {

            while (rs.next()) {
                comboBox.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the data of the selected table in the JTable.
     */
    private void displayTableData(String tableName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            // Get metadata to build the table model
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Build column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Build table data
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Set the model to the table
            table.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
