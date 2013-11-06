package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.Queries;

/**
 * @authors Gasparrini - Torletti
 * 
 */
public class DBConnection {

	private String driver = "org.postgresql.Driver";
	private String url = "jdbc:postgresql://";
	private java.sql.Connection conn;
	private String schema;
	private String dataBase;
	private String user;
	private String password;
	private int statusConnection;

	/**
	 * Constructor de la clase.
	 * 
	 * @param dataBase
	 *            "name of database to connect"
	 * @param user
	 *            "user name of the database to connect"
	 * @param password
	 *            "password of the database to connect"
	 */
	public DBConnection(String host, String user, String password, String dataBase, String schema) {
		this.dataBase = dataBase;
		this.user = user;
		this.schema = schema;
		this.url+= host+"/";
		this.url+=this.dataBase;
		try {
			// Cargo el driver
			Class.forName(driver);
			// Obtengo una conexion.
			conn = DriverManager.getConnection(url, user, password);
			//Verifico que el esquema existe en mi base de datos.
			ResultSet r = preparateConsult(this, Queries.existsSchema(schema));
			statusConnection = (r.next())?0:2; // Devuelvo 0 si la conexion fue correcta.
		} catch (ClassNotFoundException e) {
			statusConnection = 1; // Fallo al cargar el driver.
		} catch (SQLException e) {
			statusConnection = 2; // Fallo al conectar a la base de datos o el esquema no se ha encontrado.
		}
	}

	/**
	 * Devuelve el estado de la conexion a la base de datos.
	 *     0 - Conexion correcta.
	 *     1 - Error con el driver o esquema no encontrado.
	 *     2 - Error base de datos no encontrada.
	 * @return int
	 */
	public int getStatusConnection() {
		return statusConnection;
	}

	/**
	 * Devuelve la conexion de a la base de datos.
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Devuelve la nombre de la base de datos.
	 * @return String
	 */
	public String getDb() {
		return dataBase;
	}
	
	/**
	 * Retorna el nombre del esquema.
	 * @return String
	 */
	public String getSchema(){
		return this.schema;
	}
	
	/**
	 * @return the user of this connection.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Devuelve la contrasenia del usuario.
	 * @return String.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Toma una conexion y un string y devuelve un ResultSet con la consulta de dicho string sobre la base de datos.
	 * @param db
	 * @param query
	 * @return ResultSet.
	 */
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
