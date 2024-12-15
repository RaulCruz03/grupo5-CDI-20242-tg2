package rjm;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Font;

public class CadastroIngrediente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNome, textFieldQuantidade, textFieldUnidadeMedida, textFieldDataValidade;
    private JTable table;
    private DefaultTableModel tableModel;
    private int currentCodIngrediente = -1;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroIngrediente frame = new CadastroIngrediente();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroIngrediente() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Ingrediente");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 70, 100, 25);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 70, 200, 25);
        contentPane.add(textFieldNome);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setBounds(30, 110, 100, 25);
        contentPane.add(lblQuantidade);

        textFieldQuantidade = new JTextField();
        textFieldQuantidade.setBounds(150, 110, 200, 25);
        contentPane.add(textFieldQuantidade);

        JLabel lblUnidadeMedida = new JLabel("Unidade de Medida:");
        lblUnidadeMedida.setBounds(30, 150, 150, 25);
        contentPane.add(lblUnidadeMedida);

        textFieldUnidadeMedida = new JTextField();
        textFieldUnidadeMedida.setBounds(150, 150, 200, 25);
        contentPane.add(textFieldUnidadeMedida);

        JLabel lblDataValidade = new JLabel("Data de Validade:");
        lblDataValidade.setBounds(30, 190, 150, 25);
        contentPane.add(lblDataValidade);

        textFieldDataValidade = new JTextField();
        textFieldDataValidade.setBounds(150, 190, 200, 25);
        contentPane.add(textFieldDataValidade);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(150, 230, 100, 25);
        btnCadastrar.addActionListener(e -> {
            String nome = textFieldNome.getText();
            int quantidade = Integer.parseInt(textFieldQuantidade.getText());
            String unidadeMedida = textFieldUnidadeMedida.getText();
            String dataValidade = textFieldDataValidade.getText();
            addIngrediente(nome, quantidade, unidadeMedida, dataValidade);
        });
        contentPane.add(btnCadastrar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(270, 230, 100, 25);
        btnAtualizar.addActionListener(e -> {
            if (currentCodIngrediente != -1) {
                String nome = textFieldNome.getText();
                int quantidade = Integer.parseInt(textFieldQuantidade.getText());
                String unidadeMedida = textFieldUnidadeMedida.getText();
                String dataValidade = textFieldDataValidade.getText();
                updateIngrediente(currentCodIngrediente, nome, quantidade, unidadeMedida, dataValidade);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um ingrediente para atualizar.");
            }
        });
        contentPane.add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(390, 230, 100, 25);
        btnExcluir.addActionListener(e -> {
            if (currentCodIngrediente != -1) {
                deleteIngrediente(currentCodIngrediente);
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um ingrediente para excluir.");
            }
        });
        contentPane.add(btnExcluir);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("CodIngrediente");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Quantidade");
        tableModel.addColumn("Unidade de Medida");
        tableModel.addColumn("Data de Validade");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 270, 720, 200);
        contentPane.add(scrollPane);

        loadIngredienteData();

        // Ação de seleção de linha para edição
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Preenche os campos de texto com os dados da linha selecionada
                currentCodIngrediente = (int) tableModel.getValueAt(selectedRow, 0);
                textFieldNome.setText((String) tableModel.getValueAt(selectedRow, 1));
                textFieldQuantidade.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                textFieldUnidadeMedida.setText((String) tableModel.getValueAt(selectedRow, 3));
                textFieldDataValidade.setText((String) tableModel.getValueAt(selectedRow, 4));
            }
        });
    }

    private void addIngrediente(String nome, int quantidade, String unidadeMedida, String dataValidade) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);
            String insertIngrediente = "INSERT INTO Ingrediente (Nome, Quantidade, `Unidade de Medida`, `Data de Validade`) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertIngrediente, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nome);
                stmt.setInt(2, quantidade);
                stmt.setString(3, unidadeMedida);
                stmt.setDate(4, java.sql.Date.valueOf(dataValidade));
                stmt.executeUpdate();

                // Após a inserção, obtém o CodIngrediente gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int codIngrediente = rs.getInt(1);
                    System.out.println("Ingrediente cadastrado com sucesso! CodIngrediente: " + codIngrediente);
                }

                JOptionPane.showMessageDialog(null, "Ingrediente cadastrado com sucesso!");
                loadIngredienteData(); // Recarrega os dados da tabela
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void loadIngredienteData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT CodIngrediente, Nome, Quantidade, `Unidade de Medida`, `Data de Validade` FROM Ingrediente";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[] {
                            rs.getInt("CodIngrediente"), rs.getString("Nome"), rs.getInt("Quantidade"),
                            rs.getString("Unidade de Medida"), rs.getString("Data de Validade")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateIngrediente(int codIngrediente, String nome, int quantidade, String unidadeMedida, String dataValidade) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE Ingrediente SET Nome = ?, Quantidade = ?, `Unidade de Medida` = ?, `Data de Validade` = ? WHERE CodIngrediente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, nome);
                stmt.setInt(2, quantidade);
                stmt.setString(3, unidadeMedida);
                stmt.setDate(4, java.sql.Date.valueOf(dataValidade));
                stmt.setInt(5, codIngrediente);

                int rowsAffected = stmt.executeUpdate();  // Executa a atualização no banco
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Ingrediente atualizado com sucesso!");
                    loadIngredienteData(); // Recarrega os dados da tabela
                    clearFields(); // Limpa os campos após a atualização
                } else {
                    JOptionPane.showMessageDialog(null, "Erro: Nenhuma linha foi atualizada.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar ingrediente: " + e.getMessage());
        }
    }

    private void clearFields() {
        textFieldNome.setText("");
        textFieldQuantidade.setText("");
        textFieldUnidadeMedida.setText("");
        textFieldDataValidade.setText("");
        currentCodIngrediente = -1; // Reset the CodIngrediente variable
    }

    private void deleteIngrediente(int codIngrediente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "DELETE FROM Ingrediente WHERE CodIngrediente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, codIngrediente);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Ingrediente excluído com sucesso!");
                loadIngredienteData(); // Recarrega a tabela após a exclusão
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
