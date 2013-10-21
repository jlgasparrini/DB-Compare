package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

/**
 * @authors Gasparrini - Torletti
 * 
 */
public class DBConnection {

	private String driver = "org.postgresql.Driver";
	private String dataBase;
	private String user;
	private String password;
	private String url = "jdbc:postgresql://localhost:5432/";
	private java.sql.Connection conn;
	private int statusConnection;

	/**
	 * Class constructor
	 * 
	 * @param dataBase
	 *            "name of database to connect"
	 * @param user
	 *            "user name of the database to connect"
	 * @param password
	 *            "password of the database to connect"
	 */
	public DBConnection(String user, String password, String dataBase) {
		this.dataBase = dataBase;
		this.user = user;
		this.password = password;
		try {
			// Load the driver.
			Class.forName(driver);
			// Getting a connection.
			conn = DriverManager.getConnection(url, user, password);
			statusConnection = 0; // Rigth connection.
		} catch (ClassNotFoundException e) {
			statusConnection = 1; // Fail to load the driver.
		} catch (SQLException e) {
			statusConnection = 2; // Fail to getting the database.
		}
	}

	/**
	 * @return the currently status of this connection. 0 - Right connection. 1
	 *         - ERROR: Problem with the driver connection. 2 - ERROR: Database
	 *         not founded.
	 */
	public int getStatusConnection() {
		return statusConnection;
	}

	/**
	 * @return the connection of database.
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * @return the database's name.
	 */
	public String getBd() {
		return dataBase;
	}

	/**
	 * @return the user of this connection.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password of the user.
	 */
	public String getPassword() {
		return password;
	}

	public static ResultSet preparateConsult(DBConnection db, String query) {
		PreparedStatement p;
		ResultSet r = null;
		try {
			p = db.getConnection().prepareCall(query);
			r = p.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

}
