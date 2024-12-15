package rjm;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Pedidos extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable tablePedidos;
    private DefaultTableModel tableModelPedidos;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Pedidos frame = new Pedidos();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Pedidos() {
        setTitle("Pedidos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600); // Ajuste da largura da janela
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Visualização de Pedidos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 400, 30);
        contentPane.add(lblTitulo);

        tableModelPedidos = new DefaultTableModel(new Object[][] {},
                new String[] { "Código do Pedido", "Data/Hora", "Status", "Cliente", "Atendente", "Mesa", "Pratos" });
        tablePedidos = new JTable(tableModelPedidos);

        // Configuração do layout das colunas
        TableColumnModel columnModel = tablePedidos.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Código do Pedido
        columnModel.getColumn(1).setPreferredWidth(150); // Data/Hora
        columnModel.getColumn(2).setPreferredWidth(100); // Status
        columnModel.getColumn(3).setPreferredWidth(150); // Cliente
        columnModel.getColumn(4).setPreferredWidth(150); // Atendente
        columnModel.getColumn(5).setPreferredWidth(50);  // Mesa
        columnModel.getColumn(6).setPreferredWidth(300); // Pratos (Espaço maior reservado)

        JScrollPane scrollPane = new JScrollPane(tablePedidos);
        scrollPane.setBounds(30, 70, 920, 450); // Aumenta a largura para acomodar mais dados
        contentPane.add(scrollPane);

        JButton btnCarregarPedidos = new JButton("Carregar Pedidos");
        btnCarregarPedidos.setBounds(30, 530, 200, 30);
        btnCarregarPedidos.addActionListener(e -> carregarPedidos());
        contentPane.add(btnCarregarPedidos);

        carregarPedidos(); // Carrega automaticamente ao abrir o frame
    }

    private void carregarPedidos() {
        tableModelPedidos.setRowCount(0); // Limpa os dados existentes na tabela

        String query = """
                SELECT * FROM pedidos;
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int codPedido = rs.getInt("CodPedido");
                String dataHora = rs.getString("DataHora");
                String status = rs.getString("Status");
                String cliente = rs.getString("Cliente");
                String atendente = rs.getString("Atendente");
                int mesa = rs.getInt("Mesa");
                String pratos = rs.getString("Pratos");

                tableModelPedidos.addRow(new Object[] { codPedido, dataHora, status, cliente, atendente, mesa, pratos });
            }

            JOptionPane.showMessageDialog(this, "Pedidos carregados com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + e.getMessage());
        }
    }
}
