package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Controller;

/**
 * @author Gasparrini - Torletti
 * 
 */
public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelPrincipal;
	private JTextField titleBaseDeDatos1;
	private JTextField labelUsuario1;
	private JTextField usuarioBaseDeDatos1;
	private JTextField labelContrasenia;
	private JTextField labelBaseDeDatos1;
	private JTextField baseDeDatos1;
	private JPasswordField contraseniaBaseDeDatos1;
	private JPanel panelBaseDeDatos2;
	private JTextField titleBaseDeDatos2;
	private JTextField labelUsuario2;
	private JTextField usuarioBaseDeDatos2;
	private JTextField labelContrasenia2;
	private JTextField labelBaseDeDatos2;
	private JTextField baseDeDatos2;
	private JPasswordField contraseniaBaseDeDatos2;
	private JButton botonConectar2;
	private final TextArea textAreaResultados;
	private Controller controller;
	private JTextField labelEsquema1;
	private JTextField esquema1;
	private JTextField labelEsquema;
	private JTextField esquema2;
	private JLabel espacioEnBlanco1;
	private JLabel espacioEnBlanco2;
	private JLabel espacioEnBlanco4;
	private JTextField labelHostDB1;
	private JTextField hostDB1;
	private JTextField labelHostDB2;
	private JTextField hostDB2;

	/**
	 * Constructor de la clase.
	 * Me permite crear la ventana para interactuar con el usuario
	 */
	public View() {
		// Construyo los paneles principales.................
		this.controller = Controller.getInstance();
		this.controller.setView(this);
		setTitle("Comparador de dos bases de datos");
		setBackground(Color.RED);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 663, 461);
		panelPrincipal = new JPanel();
		panelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelPrincipal);

		// Inicio panel de entradas.......
		JPanel panelDeEntradas = new JPanel();
		panelDeEntradas.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panelBaseDeDatos1 = new JPanel();
		panelDeEntradas.add(panelBaseDeDatos1);
		panelBaseDeDatos1.setLayout(new GridLayout(0, 2, 0, 0));

		titleBaseDeDatos1 = new JTextField();
		titleBaseDeDatos1.setEditable(false);
		titleBaseDeDatos1.setText("Base de datos 1");
		panelBaseDeDatos1.add(titleBaseDeDatos1);
		titleBaseDeDatos1.setColumns(10);

		espacioEnBlanco1 = new JLabel("");
		panelBaseDeDatos1.add(espacioEnBlanco1);
		
		labelHostDB1 = new JTextField();
		labelHostDB1.setText("  HOST:");
		labelHostDB1.setEditable(false);
		labelHostDB1.setColumns(10);
		panelBaseDeDatos1.add(labelHostDB1);
		
		hostDB1 = new JTextField();
		hostDB1.setText("localhost:5432");
		hostDB1.setColumns(10);
		panelBaseDeDatos1.add(hostDB1);

		labelUsuario1 = new JTextField();
		labelUsuario1.setEditable(false);
		labelUsuario1.setText("  USUARIO:");
		panelBaseDeDatos1.add(labelUsuario1);
		labelUsuario1.setColumns(10);

		usuarioBaseDeDatos1 = new JTextField();
		usuarioBaseDeDatos1.setText("postgres");
		panelBaseDeDatos1.add(usuarioBaseDeDatos1);
		usuarioBaseDeDatos1.setColumns(10);

		labelContrasenia = new JTextField();
		labelContrasenia.setEditable(false);
		labelContrasenia.setText("  CONTRASE\u00D1A:");
		panelBaseDeDatos1.add(labelContrasenia);
		labelContrasenia.setColumns(10);

		contraseniaBaseDeDatos1 = new JPasswordField();
		contraseniaBaseDeDatos1.setText("root");
		panelBaseDeDatos1.add(contraseniaBaseDeDatos1);

		labelBaseDeDatos1 = new JTextField();
		labelBaseDeDatos1.setEditable(false);
		labelBaseDeDatos1.setText("  BASE DE DATOS:");
		panelBaseDeDatos1.add(labelBaseDeDatos1);
		labelBaseDeDatos1.setColumns(10);

		baseDeDatos1 = new JTextField();
		baseDeDatos1.setText("postgres");
		panelBaseDeDatos1.add(baseDeDatos1);
		baseDeDatos1.setColumns(10);

		labelEsquema1 = new JTextField();
		labelEsquema1.setText("  ESQUEMA:");
		labelEsquema1.setEditable(false);
		labelEsquema1.setColumns(10);
		panelBaseDeDatos1.add(labelEsquema1);

		esquema1 = new JTextField();
		esquema1.setColumns(10);
		esquema1.setText("centro_educativo");
		usuarioBaseDeDatos1.setText("postgres");
		panelBaseDeDatos1.add(esquema1);

		JButton botonConectar1 = new JButton("CONECTAR");
		botonConectar1.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				controller.connectBD(hostDB1.getText(), usuarioBaseDeDatos1.getText(),
						contraseniaBaseDeDatos1.getText(),
						baseDeDatos1.getText(), esquema1.getText(), 1);
			}
		});

		panelBaseDeDatos1.add(botonConectar1);

		// FIN PANEL BASE DE DATOS 1........................

		panelBaseDeDatos2 = new JPanel();
		panelDeEntradas.add(panelBaseDeDatos2);
		panelBaseDeDatos2.setLayout(new GridLayout(0, 2, 0, 0));

		titleBaseDeDatos2 = new JTextField();
		titleBaseDeDatos2.setText("Base de datos 2");
		titleBaseDeDatos2.setEditable(false);
		titleBaseDeDatos2.setColumns(10);
		panelBaseDeDatos2.add(titleBaseDeDatos2);

		espacioEnBlanco2 = new JLabel("");
		panelBaseDeDatos2.add(espacioEnBlanco2);
		
		labelHostDB2 = new JTextField();
		labelHostDB2.setText("   HOST:");
		labelHostDB2.setEditable(false);
		labelHostDB2.setColumns(10);
		panelBaseDeDatos2.add(labelHostDB2);
		
		hostDB2 = new JTextField();
		hostDB2.setText("localhost:5432");
		hostDB2.setColumns(10);
		panelBaseDeDatos2.add(hostDB2);

		labelUsuario2 = new JTextField();
		labelUsuario2.setText("  USUARIO:");
		labelUsuario2.setEditable(false);
		labelUsuario2.setColumns(10);
		panelBaseDeDatos2.add(labelUsuario2);

		usuarioBaseDeDatos2 = new JTextField();
		usuarioBaseDeDatos2.setColumns(10);
		usuarioBaseDeDatos2.setText("postgres");
		panelBaseDeDatos2.add(usuarioBaseDeDatos2);

		labelContrasenia2 = new JTextField();
		labelContrasenia2.setText("  CONTRASE\u00D1A:");
		labelContrasenia2.setEditable(false);
		labelContrasenia2.setColumns(10);
		panelBaseDeDatos2.add(labelContrasenia2);

		contraseniaBaseDeDatos2 = new JPasswordField();
		contraseniaBaseDeDatos2.setText("root");
		panelBaseDeDatos2.add(contraseniaBaseDeDatos2);

		labelBaseDeDatos2 = new JTextField();
		labelBaseDeDatos2.setText("  BASE DE DATOS:");
		labelBaseDeDatos2.setEditable(false);
		labelBaseDeDatos2.setColumns(10);
		panelBaseDeDatos2.add(labelBaseDeDatos2);

		baseDeDatos2 = new JTextField();
		baseDeDatos2.setColumns(10);
		baseDeDatos2.setText("postgres");
		panelBaseDeDatos2.add(baseDeDatos2);

		labelEsquema = new JTextField();
		labelEsquema.setText("  ESQUEMA:");
		labelEsquema.setEditable(false);
		labelEsquema.setColumns(10);
		panelBaseDeDatos2.add(labelEsquema);

		esquema2 = new JTextField();
		esquema2.setColumns(10);
		esquema2.setText("educational_center");
		panelBaseDeDatos2.add(esquema2);

		espacioEnBlanco4 = new JLabel("");
		panelBaseDeDatos2.add(espacioEnBlanco4);

		botonConectar2 = new JButton("CONECTAR");
		botonConectar2.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				controller.connectBD(hostDB2.getText(), usuarioBaseDeDatos2.getText(),
						contraseniaBaseDeDatos2.getText(),
						baseDeDatos2.getText(), esquema2.getText(), 2);
			}
		});

		panelBaseDeDatos2.add(botonConectar2);

		JPanel panelDeSalidas = new JPanel();

		textAreaResultados = new TextArea();
		textAreaResultados
				.setText("Los resultados se podran encontrar aqui!!!");
		textAreaResultados.setEditable(false);
		panelPrincipal.setLayout(new GridLayout(0, 1, 0, 0));
		panelPrincipal.add(panelDeEntradas);
		panelPrincipal.add(panelDeSalidas);
		panelDeSalidas.setLayout(new BoxLayout(panelDeSalidas, BoxLayout.X_AXIS));
		panelDeSalidas.add(textAreaResultados);

		JButton botonComparar = new JButton("COMPARAR");
		panelDeSalidas.add(botonComparar);
		botonComparar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.Compare();
			}
		});
	}

	/**
	 * Toma un string y lo inserta en el text area de resultados.
	 * 
	 * @param string
	 */
	public void addText(String string) {
		textAreaResultados.setText("\n" + string);
		textAreaResultados.setCaretPosition(0);
	}

	/**
	 * Limpia el text area en donde se almacenan los resultados.
	 */
	public void clearText() {
		textAreaResultados.setText("");
	}
}
