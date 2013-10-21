package utils;

public class Querys {

	public static String tablaQuery() {
		return "SELECT * FROM information_schema.tables";
	}

	public static String attributesFromTableQuery(String schema, String table) {
		return "SELECT column_name, data_type "
						+ "FROM information_schema.columns "
						+ "WHERE table_schema = '"+ schema+"' AND table_name = '"+ table+"' " 
						+ "ORDER BY table_name, ordinal_position";
	}
	
	public static String constraintsFromTableQuery(String schema, String table) {
		return "SELECT constraint_name, constraint_type FROM information_schema.table_constraints "
				+ "WHERE table_schema = '"+schema+"' AND table_name = '"+table+"';";
	}
}
