package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

public class DBConnection {

	private String driver = "org.postgresql.Driver";
	private String dataBase;
	private String url = "jdbc:postgresql://localhost:5432/";
	private static java.sql.Connection con;
	private static int statusConnection;

	/**
	 * Class constructor
	 * 
	 * @param dataBase
	 *            "name of database to connect"
	 * @param user
	 *            "user name of the database to connect"
	 * @param password
	 *            "password of the database to connect"
	 * @param statusConnection
	 *            "currently status of connection of my database"
	 */
	public DBConnection(String user, String password, String dataBase) {
		this.dataBase = dataBase;
		try {
			// Load the driver.
			Class.forName(driver);
			// Getting a connection.
			con = DriverManager.getConnection(url, user, password);
			statusConnection = 0; // Rigth connection.
		} catch (ClassNotFoundException e) {
			statusConnection = 1; // Fail to load the driver.
		} catch (PSQLException e) {
			statusConnection = 2; // Fail to getting the database.
		} catch (SQLException e) {
			statusConnection = 2; // Fail to getting the database.
		}
	}

	public int getStatusConnection() {
		return statusConnection;
	}

	/**
	 * Returns the connection of database.
	 */
	public Connection getConnection() {
		return con;
	}

	/**
	 * Return the database's name.
	 */
	public String getBd() {
		return dataBase;
	}
}
