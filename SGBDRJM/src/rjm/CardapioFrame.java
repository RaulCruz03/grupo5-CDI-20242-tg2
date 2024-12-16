package rjm;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.Locale;

public class CardapioFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable tableCardapio;
    private DefaultTableModel tableModelCardapio;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CardapioFrame frame = new CardapioFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CardapioFrame() {
        setTitle("Cardápio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cardápio");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        // Tabela para mostrar os pratos
        tableModelCardapio = new DefaultTableModel(new Object[][] {}, new String[] { "Código do Prato", "Nome do Prato", "Descrição", "Preço" });
        tableCardapio = new JTable(tableModelCardapio);
        JScrollPane scrollPane = new JScrollPane(tableCardapio);
        scrollPane.setBounds(30, 70, 720, 400);
        contentPane.add(scrollPane);

        // Adicionar MouseListener para mostrar a descrição ao clicar
        tableCardapio.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableCardapio.rowAtPoint(e.getPoint());
                int col = tableCardapio.columnAtPoint(e.getPoint());

                // Verifica se a coluna clicada é a "Descrição"
                if (col == 2) { // A coluna "Descrição" é a terceira (índice 2)
                    String descricao = tableModelCardapio.getValueAt(row, col).toString();
                    JOptionPane.showMessageDialog(contentPane, descricao, "Descrição do Prato", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Carregar os pratos do banco de dados
        carregarCardapio();
    }

    private void carregarCardapio() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT CodPrato, Nome, Descrição, Preço FROM Prato";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int codPrato = rs.getInt("CodPrato");
                    String nomePrato = rs.getString("Nome");
                    String descricao = rs.getString("Descrição");
                    double preco = rs.getDouble("Preço");

                    // Formatar o preço com "R$" e duas casas decimais
                    String precoFormatado = formatarPreco(preco);
                    
                    tableModelCardapio.addRow(new Object[] { codPrato, nomePrato, descricao, precoFormatado });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar o cardápio: " + e.getMessage());
        }
    }

    // Método para formatar o preço com "R$" e duas casas decimais
    private String formatarPreco(double preco) {
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoMoeda.format(preco);
    }
}
