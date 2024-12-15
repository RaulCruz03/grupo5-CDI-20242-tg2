package rjm;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CadastroPedido extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField textFieldCodCliente, textFieldCodAtendente, textFieldNumMesa;
    private JTable tableItens;
    private DefaultTableModel tableModelItens;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CadastroPedido frame = new CadastroPedido();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CadastroPedido() {
        setTitle("Cadastro de Pedido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Pedido");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitulo.setBounds(30, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblCodCliente = new JLabel("Código do Cliente:");
        lblCodCliente.setBounds(30, 70, 150, 25);
        contentPane.add(lblCodCliente);

        textFieldCodCliente = new JTextField();
        textFieldCodCliente.setBounds(180, 70, 200, 25);
        contentPane.add(textFieldCodCliente);

        JLabel lblCodAtendente = new JLabel("Código do Atendente:");
        lblCodAtendente.setBounds(30, 110, 150, 25);
        contentPane.add(lblCodAtendente);

        textFieldCodAtendente = new JTextField();
        textFieldCodAtendente.setBounds(180, 110, 200, 25);
        contentPane.add(textFieldCodAtendente);

        JLabel lblNumMesa = new JLabel("Número da Mesa:");
        lblNumMesa.setBounds(30, 150, 150, 25);
        contentPane.add(lblNumMesa);

        textFieldNumMesa = new JTextField();
        textFieldNumMesa.setBounds(180, 150, 200, 25);
        contentPane.add(textFieldNumMesa);

        JLabel lblItens = new JLabel("Itens (Pratos):");
        lblItens.setBounds(30, 190, 150, 25);
        contentPane.add(lblItens);

        tableModelItens = new DefaultTableModel(new Object[][] {}, new String[] { "Código do Prato", "Nome do Prato" });
        tableItens = new JTable(tableModelItens);
        JScrollPane scrollPane = new JScrollPane(tableItens);
        scrollPane.setBounds(30, 220, 350, 150);
        contentPane.add(scrollPane);

        JButton btnAdicionarItem = new JButton("Adicionar Prato");
        btnAdicionarItem.setBounds(400, 220, 150, 25);
        btnAdicionarItem.addActionListener(e -> adicionarPrato());
        contentPane.add(btnAdicionarItem);

        JButton btnRemoverItem = new JButton("Remover Prato");
        btnRemoverItem.setBounds(400, 260, 150, 25);
        btnRemoverItem.addActionListener(e -> {
            int selectedRow = tableItens.getSelectedRow();
            if (selectedRow != -1) {
                tableModelItens.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um prato para remover.");
            }
        });
        contentPane.add(btnRemoverItem);

        JButton btnCadastrarPedido = new JButton("Cadastrar Pedido");
        btnCadastrarPedido.setBounds(30, 400, 200, 30);
        btnCadastrarPedido.addActionListener(e -> cadastrarPedido());
        contentPane.add(btnCadastrarPedido);
    }

    private void adicionarPrato() {
        String codPrato = JOptionPane.showInputDialog(this, "Digite o código do prato:");
        if (codPrato != null && !codPrato.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT Nome FROM Prato WHERE CodPrato = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, Integer.parseInt(codPrato));
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String nomePrato = rs.getString("Nome");
                        tableModelItens.addRow(new Object[] { codPrato, nomePrato });
                    } else {
                        JOptionPane.showMessageDialog(this, "Prato não encontrado!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar prato: " + e.getMessage());
            }
        }
    }

    private void cadastrarPedido() {
        String codCliente = textFieldCodCliente.getText();
        String codAtendente = textFieldCodAtendente.getText();
        String numMesa = textFieldNumMesa.getText();

        if (codCliente.isEmpty() || codAtendente.isEmpty() || numMesa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // 1. Inserir o pedido
            String insertPedido = "INSERT INTO Pedido (`Data/Hora`, Status, `n°Mesa`, CodCliente, CodAtendente) VALUES (NOW(), 'Aberto', ?, ?, ?)";
            int codPedido;
            try (PreparedStatement stmt = conn.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, Integer.parseInt(numMesa));
                stmt.setInt(2, Integer.parseInt(codCliente));
                stmt.setInt(3, Integer.parseInt(codAtendente));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    codPedido = rs.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Erro ao obter o código do pedido.");
                }
            }

            // 2. Inserir os itens do pedido
            String insertItem = "INSERT INTO Item (CodPedido, CodPrato) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertItem)) {
                for (int i = 0; i < tableModelItens.getRowCount(); i++) {
                    int codPrato = Integer.parseInt(tableModelItens.getValueAt(i, 0).toString());
                    stmt.setInt(1, codPedido);
                    stmt.setInt(2, codPrato);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Pedido cadastrado com sucesso!");
            limparCampos();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar pedido: " + e.getMessage());
        }
    }

    private void limparCampos() {
        textFieldCodCliente.setText("");
        textFieldCodAtendente.setText("");
        textFieldNumMesa.setText("");
        tableModelItens.setRowCount(0);
    }
}
