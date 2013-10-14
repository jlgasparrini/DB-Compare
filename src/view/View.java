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
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 647, 135);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 303, 132);
		panel.add(panel_1);
		panel_1.setLayout(null);

		txtBaseDeDatos = new JTextField();
		txtBaseDeDatos.setEditable(false);
		txtBaseDeDatos.setText("Base de datos 1");
		txtBaseDeDatos.setBounds(10, 0, 251, 20);
		panel_1.add(txtBaseDeDatos);
		txtBaseDeDatos.setColumns(10);

		txtUsuario = new JTextField();
		txtUsuario.setEditable(false);
		txtUsuario.setText("  USUARIO:");
		txtUsuario.setBounds(10, 31, 108, 20);
		panel_1.add(txtUsuario);
		txtUsuario.setColumns(10);

		textField = new JTextField();
		textField.setBounds(118, 31, 143, 20);
		panel_1.add(textField);
		textField.setColumns(10);

		txtContrasea = new JTextField();
		txtContrasea.setEditable(false);
		txtContrasea.setText("  CONTRASE\u00D1A:");
		txtContrasea.setBounds(10, 53, 108, 20);
		panel_1.add(txtContrasea);
		txtContrasea.setColumns(10);

		txtBaseDeDato = new JTextField();
		txtBaseDeDato.setEditable(false);
		txtBaseDeDato.setText("  BASE DE DATOS:");
		txtBaseDeDato.setBounds(10, 76, 108, 20);
		panel_1.add(txtBaseDeDato);
		txtBaseDeDato.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setBounds(118, 76, 143, 20);
		panel_1.add(textField_2);
		textField_2.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(118, 53, 143, 20);
		panel_1.add(passwordField);

		JButton btnConectar = new JButton("CONECTAR");
		btnConectar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				controller.connectBD(textField.getText(),
						passwordField.getText(), textField_2.getText(), 1);
			}
		});
		btnConectar.setBounds(69, 109, 108, 23);
		panel_1.add(btnConectar);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(325, 0, 312, 132);
		panel.add(panel_2);

		txtBaseDeDatos_1 = new JTextField();
		txtBaseDeDatos_1.setText("Base de datos 2");
		txtBaseDeDatos_1.setEditable(false);
		txtBaseDeDatos_1.setColumns(10);
		txtBaseDeDatos_1.setBounds(10, 0, 249, 20);
		panel_2.add(txtBaseDeDatos_1);

		txtUsuario_1 = new JTextField();
		txtUsuario_1.setText("  USUARIO:");
		txtUsuario_1.setEditable(false);
		txtUsuario_1.setColumns(10);
		txtUsuario_1.setBounds(10, 31, 110, 20);
		panel_2.add(txtUsuario_1);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(119, 31, 140, 20);
		panel_2.add(textField_4);

		txtContrasea_1 = new JTextField();
		txtContrasea_1.setText("  CONTRASE\u00D1A:");
		txtContrasea_1.setEditable(false);
		txtContrasea_1.setColumns(10);
		txtContrasea_1.setBounds(10, 53, 110, 20);
		panel_2.add(txtContrasea_1);

		txtBaseDeDato_1 = new JTextField();
		txtBaseDeDato_1.setText("  BASE DE DATOS:");
		txtBaseDeDato_1.setEditable(false);
		txtBaseDeDato_1.setColumns(10);
		txtBaseDeDato_1.setBounds(10, 76, 110, 20);
		panel_2.add(txtBaseDeDato_1);

		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(118, 76, 141, 20);
		panel_2.add(textField_7);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(118, 53, 141, 20);
		panel_2.add(passwordField_1);

		button = new JButton("CONECTAR");
		button.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				controller.connectBD(textField_4.getText(),
						passwordField_1.getText(), textField_7.getText(), 2);
			}
		});
		button.setBounds(71, 109, 110, 23);
		panel_2.add(button);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 148, 647, 275);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		textArea_1 = new TextArea();
		textArea_1.setBounds(0, 0, 647, 237);
		textArea_1.setEditable(false);
		panel_3.add(textArea_1);

		JButton btnComparar = new JButton("COMPARAR");
		btnComparar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.Compare();
			}
		});
		btnComparar.setBounds(247, 243, 123, 23);
		panel_3.add(btnComparar);
	}

	public void addText(String string) {
		textArea_1.setText("\n" + string);
	}

	public void clearText() {
		textArea_1.setText("");
	}
}
