package controller;

import model.Comparator;
import model.DBConnection;
import view.View;

/**
 * @author Gasparrini - Torletti
 *
 */
public class Controller {
	private DBConnection firstInstance;
	private DBConnection secondInstance;
	private View view;
	private Comparator comp;
	private static Controller instance;

	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		return instance;
	}

	public void connectBD(String host, String user, String password, String DB, String schema, int instance) {
		if (instance == 1) {
			firstInstance = new DBConnection(host, user, password, DB, schema);
			controlStatus(firstInstance);
		}
		if (instance == 2) {
			secondInstance = new DBConnection(host, user, password, DB, schema);
			controlStatus(secondInstance);
		}
	}

	public void Compare() {
		if (firstInstance != null && secondInstance != null)
			if (firstInstance.getStatusConnection() == 0
					&& secondInstance.getStatusConnection() == 0) {
				this.comp = new Comparator(firstInstance, secondInstance);
				view.addText(this.comp.runComparison());
				return;
			}
		view.clearText();
		view.addText("ERROR: alguna de las bases de datos no se encuentra disponibles.");
	}

	public void setView(View view) {
		this.view = view;
	}

	public void controlStatus(DBConnection c) {
		int status = c.getStatusConnection();
		String DB = c.getDb();
		if (status == 0) {
			view.addText("Base de Datos \"" + DB + "\" conectada al esquema "+c.getSchema()+".");
		}
		if (status == 1) {
			view.addText("ERROR: Fallo al cargar el driver.");
			if (c.getDb().compareTo(this.firstInstance.getDb()) == 0) {
				this.firstInstance = null;
			}
			else
				this.secondInstance = null;
		}
		if (status == 2) {
			view.addText("ERROR: No pudo establecerse la conexion. La base de datos o el esquema no se encuentra.\n");
			c = null;
		}
	}
}
