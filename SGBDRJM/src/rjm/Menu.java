package rjm;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
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
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 457, 215);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JComboBox<String> comboBox = new JComboBox<>();
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
        
		
		JButton btnAbrir = new JButton("Abrir tela");
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedFrame = (String) comboBox.getSelectedItem();
				if (selectedFrame != null) {
	                openFrame(selectedFrame);
				}
			}
		});
		
		

		
		JLabel lblNewLabel = new JLabel("Menu de gerenciamento");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		
		JButton btnVerPedidos = new JButton("Ver Pedidos");
		btnVerPedidos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pedidos p = new Pedidos();
				p.setVisible(true);
				dispose();
			}
		});
		btnVerPedidos.setFont(new Font("Tahoma", Font.BOLD, 15));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(51)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
					.addComponent(btnAbrir, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addGap(45))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(59)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(61, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(86)
					.addComponent(btnVerPedidos)
					.addContainerGap(256, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAbrir, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnVerPedidos)
					.addContainerGap(136, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void openFrame(String frameName) {
	    switch (frameName) {
	        case "Funcionário":
	            CadastroFuncionario cf = new CadastroFuncionario();
	            cf.setVisible(true); // Abre a tela de cadastro de funcionário
	            dispose(); // Fecha a tela atual
	            break;
	        case "Gerente":
	            CadastroGerente cg = new CadastroGerente();
	            cg.setVisible(true); // Abre a tela de cadastro de gerente
	            dispose(); // Fecha a tela atual
	            break;
	        case "Cozinheiro":
	            CadastroCozinheiro cco = new CadastroCozinheiro();
	            cco.setVisible(true); // Abre a tela de cadastro de cozinheiro
	            dispose(); // Fecha a tela atual
	            break;
	        case "Atendente":
	            CadastroAtendente ca = new CadastroAtendente();
	            ca.setVisible(true); // Abre a tela de cadastro de atendente
	            dispose(); // Fecha a tela atual
	            break;
	        case "Evento":
	            CadastroEvento ce = new CadastroEvento();
	            ce.setVisible(true); // Abre a tela de cadastro de evento
	            dispose(); // Fecha a tela atual
	            break;
	        case "Fornecedor":
	            CadastroFornecedor cfz = new CadastroFornecedor();
	            cfz.setVisible(true); // Abre a tela de cadastro de fornecedor
	            dispose(); // Fecha a tela atual
	            break;
	        case "Ingrediente":
	            CadastroIngrediente ci = new CadastroIngrediente();
	            ci.setVisible(true); // Abre a tela de cadastro de ingrediente
	            dispose(); // Fecha a tela atual
	            break;
//	        case "Prato":
//	            CadastroPrato cp = new CadastroPrato();
//	            cp.setVisible(true); // Abre a tela de cadastro de prato
//	            dispose(); // Fecha a tela atual
//	            break;
	        case "Pedido":
	            CadastroPedido cpdo = new CadastroPedido();
	            cpdo.setVisible(true); // Abre a tela de cadastro de pedido
	            dispose(); // Fecha a tela atual
	            break;
	        case "Cliente":
	            CadastroCliente cc = new CadastroCliente();
	            cc.setVisible(true); // Abre a tela de cadastro de cliente
	            dispose(); // Fecha a tela atual
	            break;
	        case "Mesa":
	            CadastroMesa cm = new CadastroMesa();
	            cm.setVisible(true); // Abre a tela de cadastro de mesa
	            dispose(); // Fecha a tela atual
	            break;
//	        case "Item":
//	            CadastroItem ci2 = new CadastroItem();
//	            ci2.setVisible(true); // Abre a tela de cadastro de item
//	            dispose(); // Fecha a tela atual
//	            break;
//	        case "Fornecimento":
//	            CadastroFornecimento cf2 = new CadastroFornecimento();
//	            cf2.setVisible(true); // Abre a tela de cadastro de fornecimento
//	            dispose(); // Fecha a tela atual
//	            break;
//	        case "Funcionário/Evento":
//	            CadastroFuncionarioEvento cfe = new CadastroFuncionarioEvento();
//	            cfe.setVisible(true); // Abre a tela de cadastro de funcionário/evento
//	            dispose(); // Fecha a tela atual
//	            break;
	        default:
	            JOptionPane.showMessageDialog(this, "Selecione uma opção válida");
	            break;
	    }
	}
}
