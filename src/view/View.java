package view;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import net.miginfocom.swing.MigLayout;

public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtBaseDeDatos;
	private JTextField txtUsuario;
	private JTextField textField;
	private JTextField txtContrasea;
	private JTextField txtBaseDeDato;
	private JTextField textField_2;
	private JPasswordField passwordField;
	private JPanel panel_2;
	private JTextField txtBaseDeDatos_1;
	private JTextField txtUsuario_1;
	private JTextField textField_4;
	private JTextField txtContrasea_1;
	private JTextField txtBaseDeDato_1;
	private JTextField textField_7;
	private JPasswordField passwordField_1;
	private JButton button;
	private final TextArea textArea_1;
	private Controller controller;
	private JTextField txtEsquema;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_5;
	private JLabel label;
	private JLabel label_2;
	private JLabel label_5;

	/**
	 * Create the frame.
	 */
	public View() {
		this.controller = Controller.getInstance();
		this.controller.setView(this);
		setTitle("Comparador de base de datos");
		setBackground(Color.RED);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 663, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
				txtBaseDeDatos = new JTextField();
				txtBaseDeDatos.setEditable(false);
				txtBaseDeDatos.setText("Base de datos 1");
				panel_1.add(txtBaseDeDatos);
				txtBaseDeDatos.setColumns(10);
		
		label = new JLabel("");
		panel_1.add(label);

		txtUsuario = new JTextField();
		txtUsuario.setEditable(false);
		txtUsuario.setText("  USUARIO:");
		panel_1.add(txtUsuario);
		txtUsuario.setColumns(10);

		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);

		txtContrasea = new JTextField();
		txtContrasea.setEditable(false);
		txtContrasea.setText("  CONTRASE\u00D1A:");
		panel_1.add(txtContrasea);
		txtContrasea.setColumns(10);

		passwordField = new JPasswordField();
		panel_1.add(passwordField);

		JButton btnConectar = new JButton("CONECTAR");
		btnConectar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				controller.connectBD("postgres", "root", "postgres", "educational_center", 1);
				//controller.connectBD(textField.getText(),
				//		passwordField.getText(), textField_2.getText(), textField_1.getText(),1);
			}
		});
				
						txtBaseDeDato = new JTextField();
						txtBaseDeDato.setEditable(false);
						txtBaseDeDato.setText("  BASE DE DATOS:");
						panel_1.add(txtBaseDeDato);
						txtBaseDeDato.setColumns(10);
		
				textField_2 = new JTextField();
				panel_1.add(textField_2);
				textField_2.setColumns(10);
		
		txtEsquema = new JTextField();
		txtEsquema.setText("  ESQUEMA:");
		txtEsquema.setEditable(false);
		txtEsquema.setColumns(10);
		panel_1.add(txtEsquema);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		panel_1.add(textField_1);
		panel_1.add(btnConectar);
		
		panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
				txtBaseDeDatos_1 = new JTextField();
				txtBaseDeDatos_1.setText("Base de datos 2");
				txtBaseDeDatos_1.setEditable(false);
				txtBaseDeDatos_1.setColumns(10);
				panel_2.add(txtBaseDeDatos_1);
		
		label_2 = new JLabel("");
		panel_2.add(label_2);

		txtUsuario_1 = new JTextField();
		txtUsuario_1.setText("  USUARIO:");
		txtUsuario_1.setEditable(false);
		txtUsuario_1.setColumns(10);
		panel_2.add(txtUsuario_1);
				
						textField_7 = new JTextField();
						textField_7.setColumns(10);
						panel_2.add(textField_7);
		
				txtContrasea_1 = new JTextField();
				txtContrasea_1.setText("  CONTRASE\u00D1A:");
				txtContrasea_1.setEditable(false);
				txtContrasea_1.setColumns(10);
				panel_2.add(txtContrasea_1);
						
								passwordField_1 = new JPasswordField();
								panel_2.add(passwordField_1);
		
				txtBaseDeDato_1 = new JTextField();
				txtBaseDeDato_1.setText("  BASE DE DATOS:");
				txtBaseDeDato_1.setEditable(false);
				txtBaseDeDato_1.setColumns(10);
				panel_2.add(txtBaseDeDato_1);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		panel_2.add(textField_5);
		
		textField_3 = new JTextField();
		textField_3.setText("  ESQUEMA:");
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		panel_2.add(textField_3);
		
				textField_4 = new JTextField();
				textField_4.setColumns(10);
				panel_2.add(textField_4);
		
				button = new JButton("CONECTAR");
				button.addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent arg0) {
						controller.connectBD("postgres", "root", "postgres", "centro_educativo", 2);
//				controller.connectBD(textField_4.getText(),
//						passwordField_1.getText(), textField_7.getText(), textField_5.getText(),2);
					}
				});
				
				label_5 = new JLabel("");
				panel_2.add(label_5);
				panel_2.add(button);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);

		textArea_1 = new TextArea();
		textArea_1.setBounds(10, 10, 621, 171);
		textArea_1.setText("Los resultados se podrán encontrar aqui!!!");
		textArea_1.setEditable(false);
		panel_3.add(textArea_1);
		
				JButton btnComparar = new JButton("COMPARAR");
				btnComparar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						controller.Compare();
					}
				});
		contentPane.setLayout(new MigLayout("", "[653px]", "[211px][191px][25px]"));
		contentPane.add(panel, "cell 0 0,grow");
		contentPane.add(panel_3, "cell 0 1,grow");
		contentPane.add(btnComparar, "cell 0 2,alignx right,aligny top");
	}

	public void addText(String string) {
		textArea_1.setText("\n" + string);
	}

	public void clearText() {
		textArea_1.setText("");
	}
}
