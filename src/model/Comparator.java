package model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import utils.AttributeTable;

/**
 * @author Gasparrini - Torletti.
 * 
 */
public class Comparator {

	DBConnection firstDB;
	DBConnection secondDB;
	DatabaseMetaData metaDataFirstDB;
	DatabaseMetaData metaDataSecondDB;

	/**
	 * @param db1
	 * @param db2
	 * 
	 *            Class constructor. Take two databases and save this
	 *            connections.
	 */
	public Comparator(DBConnection db1, DBConnection db2) {
		this.firstDB = db1;
		this.secondDB = db2;
		try {
			this.metaDataFirstDB = db1.getConnection().getMetaData();// trae las
																		// metadatas
			this.metaDataSecondDB = db2.getConnection().getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return the string with results of comparison.
	 */
	public String runComparison() {
		HashSet<String> schema1 = new HashSet<String>();
		HashSet<String> schema2 = new HashSet<String>();
		String result = null;
		loadTables(this.firstDB, schema1);
		loadTables(this.secondDB, schema2);
		result = compareTableNames(schema1, schema2);
		if (!schema1.isEmpty())
			result += compareTableAttributes(schema1);
		if (result == null)
			result = "Las bases de datos de los esquemas son iguales";
		return result;
	}

	/**
	 * Load all tables of the database "connection" in HashSet "tables".
	 * 
	 * @param connection
	 * @param tables
	 * 
	 */
	private void loadTables(DBConnection connection, HashSet<String> tables) {
		ResultSet result;
		try {
			result = connection
					.getConnection()
					.getMetaData()
					.getTables(connection.getDb(), connection.getSchema(),
							null, new String[] { "TABLE" });
			while (result.next()) {
				tables.add(result.getString(3));
			}
			System.out.println(primaryKeys(connection, "asiste"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private HashSet<String> primaryKeys(DBConnection conn, String tableName) {
		ResultSet result;
		HashSet<String> res = new HashSet<String>();
		try {
			result = conn.getConnection().getMetaData()
					.getPrimaryKeys(conn.getDb(), conn.getSchema(), tableName);
			while (result.next()) {
					res.add(result.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	private HashSet<String> foreignKeys(DBConnection conn, String tableName) {
		ResultSet result;
		HashSet<String> res = new HashSet<String>();
		try {
			result = conn.getConnection().getMetaData().getImportedKeys(conn.getDb(), conn.getSchema(), tableName);
			while (result.next()){
				System.out.println("---------------");
				System.out.println(result.getString(1));
				System.out.println(result.getString(2));
				System.out.println(result.getString(3));//Tabla a la que hace referencia la foreign key.
				System.out.println(result.getString(4));//Atributo de la tabla a la cual hace referencia.
				System.out.println(result.getString(5));
				System.out.println(result.getString(6));
				System.out.println(result.getString(7));
				System.out.println(result.getString(8));
				System.out.println(result.getString(9));
				System.out.println(result.getString(10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 
	 * @param connection
	 * @param tableName
	 * @param attributes
	 */
	private void loadAttributes(DBConnection connection, String tableName,
			HashSet<AttributeTable> attributes) {
		ResultSet result;
		try {
			result = connection
					.getConnection()
					.getMetaData()
					.getColumns(connection.getDb(), connection.getSchema(),
							tableName, "%");
			while (result.next()) {
				AttributeTable tmp = new AttributeTable(result.getString(4),
						result.getString(6));
				attributes.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param schema1
	 * @param schema2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String compareTableNames(HashSet<String> schema1,
			HashSet<String> schema2) {
		String tmp = "";
		boolean different = false; // determines whether early printed legend
		// auxiliary sets used for the iteration
		HashSet<String> aux = (HashSet<String>) schema1.clone();
		HashSet<String> aux2 = (HashSet<String>) schema2.clone();
		// determines the tables additional of the first schema
		for (Iterator<String> iterator = aux.iterator(); iterator.hasNext();) {
			String tableName = (String) iterator.next();
			if (!schema2.contains(tableName)) {
				schema1.remove(tableName);
				if (!different) {
					tmp += "El esquema " + this.firstDB.getSchema()
							+ " contiene las tablas adicionales: \n";
					different = true;
				}
				tmp += "- " + tableName + "\n";
			}
		}
		if (different) {
			tmp += "\n";
			different = false;
		}
		// determines the tables additional of the second schema
		for (Iterator<String> iterator2 = aux2.iterator(); iterator2.hasNext();) {
			String tableName2 = (String) iterator2.next();
			if (!schema1.contains(tableName2)) {
				schema2.remove(tableName2);
				if (!different) {
					tmp += "El esquema " + this.secondDB.getSchema()
							+ " contiene las tablas adicionales: \n";
					different = true;
				}
				tmp += "- " + tableName2 + "\n";

			}
		}
		if (different) {
			tmp += "\n";
			different = false;
		}
		return tmp;
	}

	/**
	 * compare the two schemas columns where the name of the tables are the same
	 * 
	 * @param schema
	 *            "set containing the names of the tables with the same name in both schemes"
	 * @return String "containing the differences"
	 */
	@SuppressWarnings("unchecked")
	private String compareTableAttributes(HashSet<String> schema) {
		String tmp = "";
		boolean different = false; // determines whether early printed legend
		// set that stored the information in the columns of the current table
		HashSet<AttributeTable> table1 = new HashSet<AttributeTable>();
		HashSet<AttributeTable> table2 = new HashSet<AttributeTable>();
		// compares the columns of the tables with the same name
		String tableName;
		for (Iterator<String> iterator = schema.iterator(); iterator.hasNext();) {
			tableName = (String) iterator.next();
			// get the columns information of the table concurrently
			loadAttributes(this.firstDB, tableName, table1);
			loadAttributes(this.secondDB, tableName, table2);
			AttributeTable attributeTable1;
			AttributeTable attributeTable2;
			// auxiliary sets used for the iteration
			HashSet<AttributeTable> aux = (HashSet<AttributeTable>) table1
					.clone();
			HashSet<AttributeTable> aux2 = (HashSet<AttributeTable>) table2
					.clone();
			// compares the columns with the same name and remove the columns of
			// the two sets
			for (Iterator<AttributeTable> iterator2 = aux.iterator(); iterator2
					.hasNext();) {
				attributeTable1 = (AttributeTable) iterator2.next();

				for (Iterator<AttributeTable> iterator3 = aux2.iterator(); iterator3
						.hasNext();) {
					attributeTable2 = (AttributeTable) iterator3.next();

					if (attributeTable1.getName() == attributeTable2.getName()) {
						if (attributeTable1.getType() != attributeTable2
								.getType()) {
							if (!different) {
								tmp += "La tabla "
										+ tableName
										+ " que se encuentra en ambos esquemas, tiene de diferente: \n";
								different = true;
							}
							tmp += "- La columna " + attributeTable1.getName()
									+ " en el esquema " + firstDB.getSchema()
									+ " el tipo es "
									+ attributeTable1.getType()
									+ " y en el esquema "
									+ secondDB.getSchema() + " es de tipo "
									+ attributeTable2.getType() + "\n";
						}
						table1.remove(attributeTable1);
						table2.remove(attributeTable2);
					}
				}
			}
			if (!different && (!table1.isEmpty() || !table2.isEmpty())) {
				tmp += "La tabla "
						+ tableName
						+ " que se encuentra en ambos esquemas, tiene de diferente: \n";
				different = true;
			}
			// save the columns with distinct name in the first schema
			for (Iterator<AttributeTable> iterator4 = table1.iterator(); iterator4
					.hasNext();) {
				attributeTable1 = (AttributeTable) iterator4.next();
				tmp += "\t- La columna " + attributeTable1.getName()
						+ " de tipo: " + attributeTable1.getType()
						+ " solo se encuentra en la tabla " + tableName
						+ " del esquema " + firstDB.getSchema() + "\n";
			}
			// save the columns with distinct name in the second schema
			for (Iterator<AttributeTable> iterator5 = table2.iterator(); iterator5
					.hasNext();) {
				attributeTable2 = (AttributeTable) iterator5.next();
				tmp += "\t- La columna " + attributeTable2.getName()
						+ " de tipo: " + attributeTable2.getType()
						+ " solo se encuentra en la tabla " + tableName
						+ " del esquema " + secondDB.getSchema() + "\n";
			}
			table1.clear();
			table2.clear();
			different = false;
		}
		return tmp;
	}
}
