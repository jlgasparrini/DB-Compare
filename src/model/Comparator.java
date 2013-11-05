package model;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import utils.AttributeTable;
import utils.Queries;

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
	 * Constructor de la clase comparador.
	 * @param db1
	 * @param db2
	 */
	public Comparator(DBConnection db1, DBConnection db2) {
		this.firstDB = db1;
		this.secondDB = db2;
		try {
			//Traigo las metadatas.
			this.metaDataFirstDB = db1.getConnection().getMetaData();
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
		HashSet<String> schema1 = Queries.getTables(this.metaDataFirstDB, this.firstDB.getSchema());
		HashSet<String> schema2 = Queries.getTables(this.metaDataSecondDB, this.secondDB.getSchema());
		String result = null;
		System.out.println(Queries.getIndexs(this.metaDataSecondDB, "educational_center", "asiste"));
		result = compareTableNames(schema1, schema2);
		Queries.getProfilesOfStoreProcedures(this.metaDataFirstDB, this.firstDB.getSchema(), "sp_damepaises");
		if (!schema1.isEmpty())
			result += compareTableAttributes(schema1);
		if (result == null)
			result = "Las bases de datos de los esquemas son iguales";
		return result;
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
			table1 = Queries.getAttributes(this.metaDataFirstDB, this.firstDB.getSchema(), tableName);
			table2 = Queries.getAttributes(this.metaDataSecondDB, this.secondDB.getSchema(), tableName);
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
