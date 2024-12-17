package rjm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PedidoGroupByFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel tableModel;

    // Configurações do banco de dados
    private static final String URL = "jdbc:mariadb://localhost:3306/mydb"; // Atualize com sua URL
    private static final String USER = "root";                           // Seu usuário do BD
    private static final String PASSWORD = "senha";                   // Sua senha

    public PedidoGroupByFrame() {
        setTitle("Pedidos Agrupados por Atendentes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuração da tabela
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("Atendente");
        tableModel.addColumn("Número de Pedidos");

        // Adiciona a tabela ao JFrame
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Carrega os dados do banco
        carregarDados();
    }

    private void carregarDados() {
        String sql = "SELECT Atendente, COUNT(CodPedido) AS TotalPedidos FROM pedidos GROUP BY Atendente";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Limpa a tabela antes de carregar novos dados
            tableModel.setRowCount(0);

            // Preenche os dados na tabela
            while (rs.next()) {
                String atendente = rs.getString("Atendente");
                int totalPedidos = rs.getInt("TotalPedidos");
                tableModel.addRow(new Object[]{atendente, totalPedidos});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Configuração da conexão JDBC
        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Carrega o driver MariaDB
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Driver não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cria e exibe o JFrame
        SwingUtilities.invokeLater(() -> {
            new PedidoGroupByFrame().setVisible(true);
        });
    }
}
