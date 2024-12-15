package rjm;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TabelaEntidades extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    // Database connection details
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TabelaEntidades frame = new TabelaEntidades();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public TabelaEntidades() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setBounds(50, 50, 150, 25);
        contentPane.add(comboBox);
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

        // Button to search
        JButton btnProcurar = new JButton("Procurar");
        btnProcurar.setBounds(220, 50, 100, 25);
        contentPane.add(btnProcurar);

        table = new JTable();
        table.setBounds(50, 100, 700, 400);
        contentPane.add(table);

        // Populate the ComboBox with table names from the database
        populateComboBox(comboBox);

        // Set action for the button
        btnProcurar.addActionListener(e -> {
            String selectedTable = (String) comboBox.getSelectedItem();
            displayTableData(selectedTable);
        });
    }

    /**
     * Populate the JComboBox with table names from the database.
     */
    private void populateComboBox(JComboBox<String> comboBox) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Query to get all table names
            String query = "SHOW TABLES";
            ResultSet rs = stmt.executeQuery(query);

            // Add "Selecione" as the default option
            comboBox.addItem("Selecione");

            // Add each table name to the ComboBox dynamically
            while (rs.next()) {
                String tableName = rs.getString(1);
                comboBox.addItem(tableName); // Add table names dynamically
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayTableData(String tableName) {
        if (tableName == null || tableName.equals("Selecione")) {
            return; // Do nothing if no valid selection
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            // Get column count and column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Build column names (header for the table)
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i); // This gets the exact column names from the database
            }

            // Create a table model with the column names as headers
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0); // Set column names as header

            // Loop through the result set and add rows to the table model
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i); // Add each row data
                }
                tableModel.addRow(rowData); // Add the row to the table model
            }

            // Set the model to the table
            table.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
