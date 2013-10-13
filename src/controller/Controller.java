package controller;

import model.DBConnection;
import view.View;

public class Controller {
	DBConnection firstInstance;
	DBConnection secondInstance;
	View view;
	static Controller instance;

	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		return instance;
	}

	public void connectBD(String user, String password, String DB, int instance) {
		if (instance == 1) {
			if (firstInstance == null) {
				firstInstance = new DBConnection(user, password, DB);
			} else {
				view.addText("Base de Datos \"" + DB
						+ "\" ya se encuentra conectada.");
			}
			controlStatus(firstInstance);
		}
		if (instance == 2) {
			if (secondInstance == null) {
				secondInstance = new DBConnection(user, password, DB);
			} else {
				view.addText("Base de Datos \"" + DB
						+ "\" ya se encuentra conectada.");
			}
			controlStatus(secondInstance);
		}

	}

	public void Compare() {
		if (firstInstance != null && secondInstance != null)
			if (firstInstance.getStatusConnection() == 0
					&& secondInstance.getStatusConnection() == 0) {
				System.out.println("COMPARAR!!");
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
		String DB = c.getBd();
		if (status == 0) {
			view.addText("Base de Datos \"" + DB + "\" conectada.");
		}
		if (status == 1) {
			view.addText("ERROR: fallo conexion al cargar el driver en la base de datos.");
			c = null;
		}
		if (status == 2) {
			view.addText("ERROR: fallo la conexion al intentar obtener la base de datos \""
					+ DB + "\".");
			c = null;
		}
	}
}
