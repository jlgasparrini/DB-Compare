package utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * @author Gasparrini - Torletti.
 * 
 */
public class Queries {

	/**
	 * Devuelvo un string con la consulta utilizada para verificar que el
	 * esquema se encuentra dentro de la base de datos.
	 * 
	 * @param schema
	 * @return String
	 */
	public static String existsSchema(String schema) {
		return "SELECT * FROM pg_namespace WHERE nspname = '" + schema + "';";
	}

	/**
	 * Devuelvo un string con la consulta utilizada para obtener los triggers
	 * que se encuentran en mi base de datos.
	 * 
	 * @param db
	 * @param schema
	 * @return String
	 */
	private static String getTriggerQuery(String schema) {
		return "SELECT trigger_name, action_timing, event_manipulation, event_object_table, action_statement "
				+ "FROM information_schema.triggers "
				+ "WHERE trigger_schema='"+ schema + "';";
	}

	/**
	 * Dada una conexion devuelvo todas las tablas de dicha conexion.
	 * 
	 * @param conn
	 * @return HashSet<String>
	 */
	public static HashSet<String> getTables(DatabaseMetaData conn, String schema) {
		HashSet<String> tables = new HashSet<String>();
		try {
			ResultSet res = conn.getConnection().getMetaData().getTables(conn.getConnection().getCatalog(), schema, null, new String[] { "TABLE" });
			while (res.next())
				tables.add(res.getString(3)); //Nombre de la tabla.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	/**
	 * Dada una conexion y una tabla devuelvo un conjunto de atributos que pertenecen a dicha tabla.
	 * @param connection
	 * @param tableName
	 * @param attributes
	 */
	public static HashSet<AttributeTable> getAttributes(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<AttributeTable> attributes = new HashSet<AttributeTable>();
		try {
			ResultSet result = conn.getColumns(conn.getConnection().getCatalog(), schema, tableName, "%");
			while (result.next()) {
				AttributeTable tmp = new AttributeTable(result.getString(4), result.getString(6));
				attributes.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attributes;
	}
	
	/**
	 * Dada una conexion y una tabla devuelvo un conjunto con todas las claves
	 * primarias de esa tabla.
	 * 
	 * @param conn
	 * @param tableName
	 * @return HashSet<String>
	 */
	public static HashSet<String> getPrimaryKeys(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<String> res = new HashSet<String>();
		try {
			ResultSet result = conn.getConnection().getMetaData().getPrimaryKeys(conn.getConnection().getCatalog(), schema, tableName);
			while (result.next())
				res.add(result.getString(4));//Atributo al cual se le aplica la clave primaria.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * Dada una conexion y una tabla devuelvo un conjunto con todas las claves
	 * foraneas de esa tabla.
	 * 
	 * @param conn
	 * @param tableName
	 * @return HashSet<String>
	 */
	public static HashSet<String> getForeignKeys(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<String> res = new HashSet<String>();
		try {
			ResultSet result = conn.getConnection().getMetaData().getImportedKeys(conn.getConnection().getCatalog(), schema, tableName);
			while (result.next()) {
				String aux = result.getString(12) + " - "; // Guardo el nombre del constraint.
				aux += result.getString(8) + " - "; // Nombre del atributo que tiene la referencia hacia otra tabla.
				aux += result.getString(3) + " - ";// Tabla a la que hago referencia...
				aux += result.getString(4);// Nombre del atritributo de la tabla a la que hago referencia
				res.add(aux);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Dada una conexion y una tabla devuelvo todos los indices asociados con
	 * dicha tabla.
	 * 
	 * @param conn
	 * @param tableName
	 * @return HashSet<String>
	 */
	public static HashSet<String> getIndexs(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<String> res = new HashSet<String>();
		try {
			ResultSet indexInformation = conn.getIndexInfo(conn.getConnection().getCatalog(), schema, tableName, false, false);
			while (indexInformation.next()) {
				String aux = indexInformation.getString("INDEX_NAME") + " - "; // Nombre del indice.
				aux += indexInformation.getString("COLUMN_NAME"); // Nombre del atributo al cual esta aplicado el indice.
				res.add(aux);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * Dado una conexion y un esquema devuelvo un conjunto de triggers que se encuentran en la base de datos.
	 * @param conn
	 * @param schema
	 * @return HashSet<String>
	 */
	public static HashSet<String> getTriggers(DatabaseMetaData conn, String schema){
		HashSet<String> result = new HashSet<String>();
		try {
			//Ejecuto la consulta en la db.
			ResultSet res = conn.getConnection().prepareCall(Queries.getTriggerQuery(schema)).executeQuery();
			while (res.next()){
				String aux = res.getString(1); // Nombre del trigger.
				aux+= res.getString(2); // BEFORE o AFTER (en que momento se ejecuta el trigger).
				aux+= res.getString(3); // Operacion que se realiza sobre la tabla (por ejemlo UPDATE)
				aux+= res.getString(4); // Tabla sobre la que se esta analizando la ejecucion del trigger.
				aux+= res.getString(5); // Accion que realiza el trigger
				result.add(aux);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Dada una conexion y un esquema devuelve un conjunto con cada uno de los procedimientos de la base de datos.
	 * (En este caso los procedimientos son funciones ya que estamos trabajando con postgreSQL.)
	 * @param conn
	 * @param schema
	 * @return HashSet<String>
	 */
	private static HashSet<String> getNamesOfProcedures(DatabaseMetaData conn, String schema){
		HashSet<String> result = new HashSet<String>();
		try {
			ResultSet res = conn.getProcedures(conn.getConnection().getCatalog(), schema, null);
			while (res.next()) {
				result.add(res.getString(3)); //Devuelve el nombre de una funcion encontrada en la db.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static HashSet<String> getProfilesOfStoreProcedures(DatabaseMetaData conn, String schema, String procedureName){
		HashSet<String> result = new HashSet<String>();
		try {
			ResultSet res = conn.getProcedureColumns(conn.getConnection().getCatalog(), "public", procedureName, null);
			while (res.next()){
				System.out.println(res.getString(1));
				System.out.println(res.getString(2));
				System.out.println(res.getString(3));
				System.out.println(res.getString(4));
				System.out.println(res.getString(5));
				System.out.println(res.getString(6));
				System.out.println(res.getString(7));
				System.out.println(res.getString(8));
				System.out.println(res.getString(9));
				System.out.println(res.getString(10));
				System.out.println(res.getString(11));
				System.out.println(res.getString(12));
				System.out.println(res.getString(13));
				System.out.println(res.getString(14));
				System.out.println(res.getString(15));
				System.out.println(res.getString(16));
				System.out.println(res.getString(17));
				System.out.println(res.getString(18));
				System.out.println(res.getString(19));
				System.out.println(res.getString(20));
				System.out.println("----------------------------");
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
