package utils;

/**
 * @author Gasparrini - Torletti.
 *
 */
public class Querys {
	
	public static String existsSchema(String schema){
		return "SELECT * FROM pg_namespace WHERE nspname = '"+schema+"';";
	}
}
