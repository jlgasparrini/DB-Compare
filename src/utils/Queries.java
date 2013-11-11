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
		HashSet<String> set = new HashSet<String>();
		try {
			ResultSet res = conn.getTables(conn.getConnection().getCatalog(), schema, null, new String[] { "TABLE" });
			while (res.next())
				set.add(res.getString(3)); //Nombre de la tabla.
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}

	/**
	 * Dada una conexion y una tabla devuelvo un conjunto de atributos que pertenecen a dicha tabla.
	 * @param connection
	 * @param tableName
	 * @param attributes
	 */
	public static HashSet<TuplesOfStrings> getAttributes(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<TuplesOfStrings> attributes = new HashSet<TuplesOfStrings>();
		try {
			ResultSet result = conn.getColumns(conn.getConnection().getCatalog(), schema, tableName, "%");
			while (result.next()) {
				TuplesOfStrings tmp = new TuplesOfStrings(2);
				tmp.setIndex(0, result.getString(4));
				tmp.setIndex(1, result.getString(6));
				attributes.add(tmp);
			}
		} 
		catch (SQLException e) {
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
			ResultSet result = conn.getPrimaryKeys(conn.getConnection().getCatalog(), schema, tableName);
			while (result.next())
				res.add(result.getString(4));//Atributo al cual se le aplica la clave primaria.
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static HashSet<String> getUniques (DatabaseMetaData conn, String schema, String tableName){
		HashSet<String> result = new HashSet<String>();
		try {
			ResultSet res = conn.getIndexInfo(conn.getConnection().getCatalog(), schema, tableName, true, false);
			while (res.next()){
					if (!getPrimaryKeys(conn, schema, tableName).contains(res.getString(9)) && res.getString(4).compareTo("f")==0)
						result.add(res.getString(9));// Guardo el nombre de la columna que tiene la clave unique.
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Dada una conexion y una tabla devuelvo un conjunto con todas las claves
	 * foraneas de esa tabla.
	 * 
	 * @param conn
	 * @param tableName
	 * @return HashSet<String>
	 */
	public static HashSet<TuplesOfStrings> getForeignKeys(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<TuplesOfStrings> res = new HashSet<TuplesOfStrings>();
		try {
			ResultSet result = conn.getImportedKeys(conn.getConnection().getCatalog(), schema, tableName);
			while (result.next()) {
				TuplesOfStrings tmp = new TuplesOfStrings(6);
				tmp.setIndex(0, result.getString(12)); //nombre de constraign
				tmp.setIndex(1, result.getString(8)); //atributo tuyo 
				tmp.setIndex(2, result.getString(3)); // tabla referencia
				tmp.setIndex(3, result.getString(4)); // atributo referencia
				tmp.setIndex(4, result.getString("UPDATE_RULE")); // regla de actualizacion
				tmp.setIndex(5, result.getString("DELETE_RULE")); // regla de eliminacion
				res.add(tmp);
			}
		} 
		catch (Exception e) {
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
	public static HashSet<TuplesOfStrings> getIndexs(DatabaseMetaData conn, String schema, String tableName) {
		HashSet<TuplesOfStrings> res = new HashSet<TuplesOfStrings>();
		try {
			ResultSet indexInformation = conn.getIndexInfo(conn.getConnection().getCatalog(), schema, tableName, false, false);
			while (indexInformation.next()) {
				if(indexInformation.getString(4).compareTo("t") == 0){
					TuplesOfStrings tmp = new TuplesOfStrings(2);
					tmp.setIndex(0, indexInformation.getString("INDEX_NAME")); // Nombre del indice.
					tmp.setIndex(1, indexInformation.getString("COLUMN_NAME")); // Nombre del atributo al cual esta aplicado el indice.
					res.add(tmp);
				}
			}
		} 
		catch (SQLException e) {
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
	public static HashSet<TuplesOfStrings> getTriggers(DatabaseMetaData conn, String schema){
		HashSet<TuplesOfStrings> result = new HashSet<TuplesOfStrings>();
		try {
			//Ejecuto la consulta en la db.
			ResultSet res = conn.getConnection().prepareCall(Queries.getTriggerQuery(schema)).executeQuery();
			while (res.next()){
				TuplesOfStrings tmp = new TuplesOfStrings(5);
				tmp.setIndex(0, res.getString(1)); // Nombre del trigger.
				tmp.setIndex(1, res.getString(2)); // BEFORE o AFTER (en que momento se ejecuta el trigger).
				tmp.setIndex(2, res.getString(3)); // Operacion que se realiza sobre la tabla (por ejemlo UPDATE)
				tmp.setIndex(3, res.getString(4)); // Tabla sobre la que se esta analizando la ejecucion del trigger.
				tmp.setIndex(4, res.getString(5)); // Accion que realiza el trigger
				result.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Dada una conexion y un esquema devuelve un conjunto con cada uno de los procedimientos de la base de datos.
	 * @param conn
	 * @param schema
	 * @return HashSet<String>
	 */
	public static HashSet<String> getNamesOfStoredProcedures(DatabaseMetaData conn, String schema){
		// En este caso los procedimientos son funciones ya que estamos trabajando con postgreSQL.
		// Por eso primero al obtener todas las funciones, tenemos que filtrar los triggers.
		HashSet<String> result = new HashSet<String>();
		try {
			ResultSet res = conn.getProcedures(conn.getConnection().getCatalog(), schema, null);
			while (res.next()){
				result.add(res.getString(3)); //Devuelve el nombre de una funcion encontrada en la db.
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param conn
	 * @param schema
	 * @param procedureName
	 * @return
	 */
	public static HashSet<String> getProfilesOfStoreProcedures(DatabaseMetaData conn, String schema, String procedureName){
		HashSet<String> result = new HashSet<String>();
		try {
			ResultSet res = conn.getProcedureColumns(conn.getConnection().getCatalog(), schema, procedureName, null);
			while (res.next()){
				String tmp = "";
				tmp+= res.getString(4)+" "; //nombre del parametro...
				tmp+= res.getString(7); //tipo del parametro...
				result.add(tmp);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
