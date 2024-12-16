package rjm;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CadastroFornecimento extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> comboBoxFornecedor;
    private JComboBox<String> comboBoxIngrediente;
    private JTextField textFieldQuantidade;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroFornecimento frame = new CadastroFornecimento();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroFornecimento() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Fornecimento");
        lblTitulo.setBounds(30, 20, 300, 30);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(24f));
        contentPane.add(lblTitulo);

        JLabel lblFornecedor = new JLabel("Fornecedor:");
        lblFornecedor.setBounds(30, 70, 100, 25);
        contentPane.add(lblFornecedor);

        comboBoxFornecedor = new JComboBox<>();
        comboBoxFornecedor.setBounds(150, 70, 200, 25);
        contentPane.add(comboBoxFornecedor);

        JLabel lblIngrediente = new JLabel("Ingrediente:");
        lblIngrediente.setBounds(30, 110, 100, 25);
        contentPane.add(lblIngrediente);

        comboBoxIngrediente = new JComboBox<>();
        comboBoxIngrediente.setBounds(150, 110, 200, 25);
        contentPane.add(comboBoxIngrediente);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setBounds(30, 150, 100, 25);
        contentPane.add(lblQuantidade);

        textFieldQuantidade = new JTextField();
        textFieldQuantidade.setBounds(150, 150, 200, 25);
        contentPane.add(textFieldQuantidade);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 190, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String fornecedor = (String) comboBoxFornecedor.getSelectedItem();
            String ingrediente = (String) comboBoxIngrediente.getSelectedItem();
            String quantidadeStr = textFieldQuantidade.getText();

            if (fornecedor != null && ingrediente != null && !quantidadeStr.isEmpty()) {
                int codFornecedor = Integer.parseInt(fornecedor.split(" - ")[0]);
                int codIngrediente = Integer.parseInt(ingrediente.split(" - ")[0]);
                int quantidade;
                try {
                    quantidade = Integer.parseInt(quantidadeStr);
                    addFornecimento(codFornecedor, codIngrediente, quantidade);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Quantidade deve ser um número válido.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos.");
            }
        });
        contentPane.add(btnCadastrar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(270, 190, 100, 25);
        btnAtualizar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String fornecedor = (String) tableModel.getValueAt(selectedRow, 0);
                String ingrediente = (String) tableModel.getValueAt(selectedRow, 1);

                int codFornecedor = Integer.parseInt(fornecedor.split(" - ")[0]);
                int codIngrediente = Integer.parseInt(ingrediente.split(" - ")[0]);

                String quantidadeStr = textFieldQuantidade.getText();
                if (!quantidadeStr.isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(quantidadeStr);
                        updateFornecimento(codFornecedor, codIngrediente, quantidade);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Quantidade deve ser um número válido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "O campo Quantidade não pode estar vazio.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um fornecimento para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Fornecedor");
        tableModel.addColumn("Ingrediente");
        tableModel.addColumn("Quantidade");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 230, 720, 200);
        contentPane.add(scrollPane);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(390, 190, 100, 25);
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String fornecedor = (String) tableModel.getValueAt(selectedRow, 0);
                String ingrediente = (String) tableModel.getValueAt(selectedRow, 1);

                int codFornecedor = Integer.parseInt(fornecedor.split(" - ")[0]);
                int codIngrediente = Integer.parseInt(ingrediente.split(" - ")[0]);

                deleteFornecimento(codFornecedor, codIngrediente);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um fornecimento para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        loadComboBoxData();
        loadFornecimentoData();
    }

    private void loadComboBoxData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Carregar fornecedores
            comboBoxFornecedor.removeAllItems();
            String queryFornecedor = "SELECT CodFornecedor, Nome FROM Fornecedor";
            try (PreparedStatement stmt = conn.prepareStatement(queryFornecedor);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comboBoxFornecedor.addItem(rs.getInt("CodFornecedor") + " - " + rs.getString("Nome"));
                }
            }

            // Carregar ingredientes
            comboBoxIngrediente.removeAllItems();
            String queryIngrediente = "SELECT CodIngrediente, Nome FROM Ingrediente";
            try (PreparedStatement stmt = conn.prepareStatement(queryIngrediente);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comboBoxIngrediente.addItem(rs.getInt("CodIngrediente") + " - " + rs.getString("Nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFornecimentoData() {
        tableModel.setRowCount(0); // Limpa a tabela antes de carregar os dados
        String query = "SELECT Fornecedor, Ingrediente, Quantidade, `Unidade de Medida`, `Data de Validade` FROM fornecimentos";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fornecedor = rs.getString("Fornecedor");
                String ingrediente = rs.getString("Ingrediente");
                int quantidade = rs.getInt("Quantidade");
                String unidadeMedida = rs.getString("Unidade de Medida");
                Date dataValidade = rs.getDate("Data de Validade");

                // Adiciona os dados na tabela (DefaultTableModel)
                tableModel.addRow(new Object[]{fornecedor, ingrediente, quantidade, unidadeMedida, dataValidade});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addFornecimento(int codFornecedor, int codIngrediente, int quantidade) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Fornecimento (CodFornecedor, CodIngrediente, Quantidade) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codFornecedor);
                stmt.setInt(2, codIngrediente);
                stmt.setInt(3, quantidade);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Fornecimento cadastrado com sucesso!");
                loadFornecimentoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar fornecimento: " + e.getMessage());
        }
    }

    private void updateFornecimento(int codFornecedor, int codIngrediente, int quantidade) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE Fornecimento SET Quantidade = ? WHERE CodFornecedor = ? AND CodIngrediente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, quantidade); // Nova quantidade
                stmt.setInt(2, codFornecedor); // ID do fornecedor
                stmt.setInt(3, codIngrediente); // ID do ingrediente

                int rowsAffected = stmt.executeUpdate(); // Executa a atualização no banco
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecimento atualizado com sucesso!");
                    loadFornecimentoData(); // Recarrega os dados da tabela
                } else {
                    JOptionPane.showMessageDialog(null, "Erro: Nenhuma linha foi atualizada.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar fornecimento: " + e.getMessage());
        }
    }




    private void deleteFornecimento(int codFornecedor, int codIngrediente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Fornecimento WHERE CodFornecedor = ? AND CodIngrediente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codFornecedor);
                stmt.setInt(2, codIngrediente);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Fornecimento excluído com sucesso!");
                loadFornecimentoData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir fornecimento: " + e.getMessage());
        }
    }
}



