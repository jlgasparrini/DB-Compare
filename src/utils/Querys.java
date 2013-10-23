package utils;

/**
 * @author Gasparrini - Torletti.
 *
 */
public class Querys {

	/**
	 * @param schema
	 * @return a string that represents the query for get the tables from schema "schema".
	 */
	public static String tablaQuery() {
		return "SELECT * FROM information_schema.tables;";
	}

	/**
	 * @param schema
	 * @param table
	 * @return a string that represents the query for get the attributes of the "table" from "schema".
	 */
	public static String attributesFromTableQuery(String schema, String table) {
		return "SELECT column_name, data_type "
						+ "FROM information_schema.columns "
						+ "WHERE table_schema = '"+ schema+"' AND table_name = '"+ table+"' " 
						+ "ORDER BY table_name, ordinal_position";
	}
	
	/**
	 * @param schema
	 * @param table
	 * @return a string that represents the query for get contraints from table "table" of schema "schema".
	 */
	public static String constraintsFromTableQuery(String schema, String table) {
		return "SELECT constraint_name, constraint_type FROM information_schema.table_constraints "
				+ "WHERE table_schema = '"+schema+"' AND table_name = '"+table+"';";
	}
}
