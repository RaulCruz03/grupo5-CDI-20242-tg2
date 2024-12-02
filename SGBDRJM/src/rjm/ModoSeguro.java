package rjm;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ModoSeguro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtTelefone;
	private JTextField txtData;
	private JTextField txtCPF;
	private JTextField txtID;
	private JTextField txtDepartamento;
	private JTextField txtNivel;
	private JButton btnCadastrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModoSeguro frame = new ModoSeguro();
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
	public ModoSeguro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 422, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Cadastrar gerente");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		
		JLabel lblNewLabel_1 = new JLabel("Nome:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtNome = new JTextField();
		txtNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNome.setColumns(15);
		
		JLabel lblNewLabel_1_2 = new JLabel("Telefone:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtTelefone = new JTextField();
		txtTelefone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTelefone.setColumns(15);
		
		JLabel lblNewLabel_1_3 = new JLabel("Data de contratação:");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtData = new JTextField();
		txtData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtData.setColumns(15);
		
		JLabel lblNewLabel_1_4 = new JLabel("CPF:");
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtCPF = new JTextField();
		txtCPF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCPF.setColumns(15);
		
		JLabel lblNewLabel_1_1 = new JLabel("Identificador:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtID = new JTextField();
		txtID.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtID.setColumns(15);
		
		JLabel lblNewLabel_1_5 = new JLabel("Departamento:");
		lblNewLabel_1_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtDepartamento = new JTextField();
		txtDepartamento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDepartamento.setColumns(15);
		
		JLabel lblNewLabel_1_6 = new JLabel("Nível de gestão:");
		lblNewLabel_1_6.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtNivel = new JTextField();
		txtNivel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNivel.setColumns(15);
		
		btnCadastrar = new JButton("Cadastrar gerente");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = txtNome.getText();
                String cpf = txtCPF.getText();
                String telefone = txtTelefone.getText();
                String data = txtData.getText();
                Integer id = Integer.parseInt(txtID.getText());
                String departamento = txtDepartamento.getText();
                String nivel = txtNivel.getText();
                
                try (Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/mydb", "root", "senha")) {
                	
                	Statement statement = connection.createStatement();
                	String funcionarioQuery = String.format("INSERT INTO funcionário (CodFuncionário, Nome, CPF, Telefone, DataContratação, TipodeFuncionário)"
                			                              + " VALUES ('%d', '%s', '%s', '%s', '%s', 'Gerente')", id, nome, cpf, telefone, data);
                    statement.executeUpdate(funcionarioQuery);

                    String gerenteQuery = String.format("INSERT INTO gerente (Departamento, `Nível de Gestão`, CodGerente) VALUES ('%s', '%s', '%d')",
                    		                            departamento, nivel, id);
                    statement.executeUpdate(gerenteQuery);

                    txtNome.setText("");
                    txtCPF.setText("");
                    txtTelefone.setText("");
                    txtData.setText("");
                    txtID.setText("");
                    txtDepartamento.setText("");
                    txtNivel.setText("");
                    
                    JOptionPane.showMessageDialog(null, "Gerente cadastrado com sucesso!");
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar gerente.");
                }
			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Menu menu = new Menu();
				menu.setVisible(true);
				dispose();
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnVoltar, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addComponent(lblNewLabel_1_3, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
									.addComponent(lblNewLabel_1_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblNewLabel_1_4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1_5, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1_6, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
							.addGap(62)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txtCPF, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
								.addComponent(txtNome, 0, 0, Short.MAX_VALUE)
								.addComponent(txtTelefone, 0, 0, Short.MAX_VALUE)
								.addComponent(txtData, 0, 0, Short.MAX_VALUE)
								.addComponent(txtID, 0, 0, Short.MAX_VALUE)
								.addComponent(txtDepartamento, 0, 0, Short.MAX_VALUE)
								.addComponent(txtNivel, 0, 0, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(104)
							.addComponent(btnCadastrar, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(51, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnVoltar))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(lblNewLabel_1_4, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(lblNewLabel_1_2, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_1_3, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(lblNewLabel_1_5, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(lblNewLabel_1_6, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(txtCPF, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtTelefone, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtData, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtID, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtDepartamento, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtNivel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addGap(22)
					.addComponent(btnCadastrar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
