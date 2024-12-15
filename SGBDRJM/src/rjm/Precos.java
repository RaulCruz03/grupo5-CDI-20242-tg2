package rjm;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Precos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Database connection details
    private static final String URL = "jdbc:mariadb://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "senha";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Precos frame = new Precos();
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
    public Precos() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 776, 466);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setBounds(134, 103, 203, 21);
        contentPane.add(comboBox);
        comboBox.addItem("Somar");
        comboBox.addItem("Contar");
        comboBox.addItem("Calcular Média");
        comboBox.addItem("Máximo");
        comboBox.addItem("Mínimo");

        JButton btnNewButton = new JButton("Confirmar");
        btnNewButton.setBounds(353, 103, 106, 21);
        contentPane.add(btnNewButton);

        JLabel lblNewLabel = new JLabel("Produtos e Preços");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        lblNewLabel.setBounds(86, 10, 386, 57);
        contentPane.add(lblNewLabel);

        // Add button action listener
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String option = (String) comboBox.getSelectedItem();
                if (option != null) {
                    performDatabaseOperation(option);
                }
            }
        });
    }

    private void performDatabaseOperation(String option) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "";
            String message = "";
            boolean isCurrency = true;

            switch (option) {
                case "Somar":
                    query = "SELECT SUM(Preço) AS resultado FROM Prato";
                    message = "Preço somado de todos os pratos: R$";
                    break;
                case "Contar":
                    query = "SELECT COUNT(*) AS resultado FROM Prato";
                    message = "Número de pratos: ";
                    isCurrency = false;
                    break;
                case "Calcular Média":
                    query = "SELECT AVG(Preço) AS resultado FROM Prato";
                    message = "Média: R$";
                    break;
                case "Máximo":
                    query = "SELECT MAX(Preço) AS resultado FROM Prato";
                    message = "Maior Preço: R$";
                    break;
                case "Mínimo":
                    query = "SELECT MIN(Preço) AS resultado FROM Prato";
                    message = "Menor Preço: R$";
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Operação inválida.");
                    return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double result = rs.getDouble("resultado");
                    String formattedResult;
                    if (isCurrency) {
                        formattedResult = String.format("%.2f", result);
                    } else {
                        formattedResult = String.format("%.0f", result);
                    }
                    JOptionPane.showMessageDialog(this, message + formattedResult);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
}
