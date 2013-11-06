package model;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import utils.Queries;
import utils.TuplesOfStrings;

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
		String result = "";
		result = compareTableNames(schema1, schema2);
		if (!schema1.isEmpty())
			result += compareTablesEqualsName(schema1);
		if (result.length() == 0)
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
					tmp += "El esquema " + this.firstDB.getSchema()	+ " contiene las tablas adicionales: \n";
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
					tmp += "El esquema " + this.secondDB.getSchema() + " contiene las tablas adicionales: \n";
					different = true;
				}
				tmp += "- " + tableName2 + "\n";

			}
		}
		if (different)
			tmp += "\n";
		return tmp;
	}

	/**
	 * compare the two schemas columns where the name of the tables are the same
	 * @param schema"set containing the names of the tables with the same name in both schemes"
	 * @return String "containing the differences"
	 */
	private String compareTablesEqualsName(HashSet<String> schema) {
		String tmp = "";
		boolean different = false;
		// compares the columns of the tables with the same name
		for (Iterator<String> iterator = schema.iterator(); iterator.hasNext();) {
			String tableName = (String) iterator.next();
			//retorna las diferecias en los atributos de la tabla actual
			String difAttribute = compareTheAttributesOfTheTable(tableName);
			String difPrimarykey = comparePrimaryKey(tableName);
			String difForeignKeys = compareForeignKeys(tableName);
			if(difAttribute.length() != 0 || difPrimarykey.length() != 0 || difForeignKeys.length() != 0){
				if (!different){
					tmp += "La tabla " + tableName + " que se encuentra en ambos esquemas, tiene de diferente: \n";
					different = true;
				}
				tmp+= difAttribute + difPrimarykey + difForeignKeys + "\n";
				difAttribute = "";
				difForeignKeys = "";
				difPrimarykey = "";
			}
		}
		return tmp;
	}
	
	
	@SuppressWarnings("unchecked")
	private String compareTheAttributesOfTheTable(String tableName){
		String tmp = "";
		// set that stored the information in the columns of the current table
		HashSet<TuplesOfStrings> table1 = new HashSet<TuplesOfStrings>();
		HashSet<TuplesOfStrings> table2 = new HashSet<TuplesOfStrings>();
		// cargo los conjuntos con la informacion de los 
		//atributos de la tabla pasada por parametro
		table1 = Queries.getAttributes(metaDataFirstDB, firstDB.getSchema(), tableName);
		table2 = Queries.getAttributes(metaDataSecondDB, secondDB.getSchema(), tableName);
		// auxiliary sets used for the iteration
		HashSet<TuplesOfStrings> aux = (HashSet<TuplesOfStrings>) table1.clone();
		HashSet<TuplesOfStrings> aux2 = (HashSet<TuplesOfStrings>) table2.clone();
		for (Iterator<TuplesOfStrings> iterator = aux.iterator(); iterator.hasNext();) {
			TuplesOfStrings tupleOfAttribute1 = (TuplesOfStrings) iterator.next();

			for (Iterator<TuplesOfStrings> iterator2 = aux2.iterator(); iterator2.hasNext();) {
				TuplesOfStrings tupleOfAttribute2 = (TuplesOfStrings)  iterator2.next();
				//comparo si los nombres de los atributos son iguales
				if (tupleOfAttribute1.getIndex(0).compareTo(tupleOfAttribute2.getIndex(0)) == 0) {
					//comparo si los atributos con mismo nombre, tienen distinto tipo
					if (tupleOfAttribute1.getIndex(1).compareTo(tupleOfAttribute2.getIndex(1)) != 0) {
						tmp += "- La columna " + tupleOfAttribute1.getIndex(0) + " en el esquema " + firstDB.getSchema()
								+ " el tipo es " + tupleOfAttribute1.getIndex(1) + " y en el esquema "
								+ secondDB.getSchema() + " es de tipo " + tupleOfAttribute2.getIndex(1) + "\n";
					}
					//elimino los atributos de ambos conjuntos, porque ya estan comparados
					table1.remove(tupleOfAttribute1);
					table2.remove(tupleOfAttribute2);
				}
			}
		}
		// save the columns with distinct name in the first schema
		for (Iterator<TuplesOfStrings> iterator3 = table1.iterator(); iterator3.hasNext();) {
			TuplesOfStrings tuple1 = (TuplesOfStrings) iterator3.next();
			tmp += "\t- La columna " + tuple1.getIndex(0)
					+ " de tipo: " + tuple1.getIndex(1)	+ " solo se encuentra en la tabla " + tableName
					+ " del esquema " + firstDB.getSchema() + "\n";
		}
		// save the columns with distinct name in the second schema
		for (Iterator<TuplesOfStrings> iterator4 = table2.iterator(); iterator4.hasNext();) {
			TuplesOfStrings tuple2 = (TuplesOfStrings) iterator4.next();
			tmp += "\t- La columna " + tuple2.getIndex(0) + " de tipo: " + tuple2.getIndex(1)
					+ " solo se encuentra en la tabla " + tableName
					+ " del esquema " + secondDB.getSchema() + "\n";
		}
		table1.clear();
		table2.clear();
		return tmp;
	}
	
	private String comparePrimaryKey(String tableName){
		String tmp = "";
		HashSet<String> table1 = new HashSet<String>();
		HashSet<String> table2 = new HashSet<String>();
		// cargo los conjuntos con la informacion de los 
		//atributos de la tabla pasada por parametro
		table1 = Queries.getPrimaryKeys(metaDataFirstDB, firstDB.getSchema(), tableName);
		table2 = Queries.getPrimaryKeys(metaDataSecondDB, secondDB.getSchema(), tableName);
		//comparo las claves primarias del primer esquema con las del segundo
		for (Iterator<String> iterator = table1.iterator(); iterator.hasNext();) {
			String primaryKey1 = (String) iterator.next();
			//si se encuentra en ambos esquemas la elimino del segundo
			if(table2.contains(primaryKey1)){
				table2.remove(primaryKey1);
			}else //si se encuetra solo en el primer esquema, lo informo
				tmp += "\t- La clave primaria " + primaryKey1 + " de la tabla " + tableName 
					+ " solo se encuentra en el esquema " + firstDB.getSchema() + "\n";
		}
		//informo las claves primarias que se encuentran unicamente en el segundo esquema
		for (Iterator<String> iterator = table2.iterator(); iterator.hasNext();) {
			String primaryKey2 = (String) iterator.next();
			tmp += "\t- La clave primaria " + primaryKey2 + " de la tabla " + tableName 
					+ " solo se encuentra en el esquema " + secondDB.getSchema() + "\n";
		}
		return tmp;
	}
	
	@SuppressWarnings("unchecked")
	public String compareForeignKeys(String tableName){
		String tmp = ""; //variable donde se setean los resultados
		// set that stored the information in the columns of the current table
		HashSet<TuplesOfStrings> table1 = new HashSet<TuplesOfStrings>();
		HashSet<TuplesOfStrings> table2 = new HashSet<TuplesOfStrings>();
		// cargo los conjuntos con la informacion de los 
		//atributos de la tabla pasada por parametro
		table1 = Queries.getForeignKeys(metaDataFirstDB, firstDB.getSchema(), tableName);
		table2 = Queries.getForeignKeys(metaDataSecondDB, secondDB.getSchema(), tableName);
		// auxiliary sets used for the iteration
		HashSet<TuplesOfStrings> aux = (HashSet<TuplesOfStrings>) table1.clone();
		HashSet<TuplesOfStrings> aux2 = (HashSet<TuplesOfStrings>) table2.clone();
		//banderas utilizadas para saber cuales son las diferencias con claves del mismo nombre
		boolean different = false;
		//variables para notificar diferencias
		String differencesSchemaFirst = "";
		String differencesSchemaSecond = "";
		//comparo los elementos de los conjuntos, buscando claves con mismo nombre
		for (Iterator<TuplesOfStrings> iterator = aux.iterator(); iterator.hasNext();) {
			TuplesOfStrings tupleOfAttribute1 = (TuplesOfStrings) iterator.next();
			for (Iterator<TuplesOfStrings> iterator2 = aux2.iterator(); iterator2.hasNext();) {
				TuplesOfStrings tupleOfAttribute2 = (TuplesOfStrings)  iterator2.next();
				//comparo si los nombres de las claves foraneas son iguales
				if (tupleOfAttribute1.getIndex(0).compareTo(tupleOfAttribute2.getIndex(0)) == 0) {
					//comparo si las claves son de distintos atributo 
					if (tupleOfAttribute1.getIndex(1).compareTo(tupleOfAttribute2.getIndex(1)) != 0){
						differencesSchemaFirst += " el atributo es " + tupleOfAttribute1.getIndex(1);
						differencesSchemaSecond += " el atributo es " + tupleOfAttribute2.getIndex(1);
						different = true;
					}
					//comparo si las claves hacen referecia a distintas tablas
					if (tupleOfAttribute1.getIndex(2).compareTo(tupleOfAttribute2.getIndex(2)) != 0){
						if(different){
							differencesSchemaFirst += ",";
							differencesSchemaSecond += ",";
						}
						differencesSchemaFirst += " la tabla referenciada es " + tupleOfAttribute1.getIndex(2);
						differencesSchemaSecond += " la tabla referenciada es " + tupleOfAttribute2.getIndex(2);
						different = true;
					}
					//comparo si las claves hacen referecia a distintos atributos de las tablas refer
					if (tupleOfAttribute1.getIndex(3).compareTo(tupleOfAttribute2.getIndex(3)) != 0){
						if(different){
							differencesSchemaFirst += " y";
							differencesSchemaSecond += " y";
						}
						differencesSchemaFirst += " el atributo referenciado es " + tupleOfAttribute1.getIndex(3);
						differencesSchemaSecond += " el atributo referenciado es " + tupleOfAttribute2.getIndex(3);
						different = true;
					}
					//notifico las diferencias
					if(different){
						tmp += "- La clave foranea " + tupleOfAttribute1.getIndex(0)
								+ " de la tabla " + tableName
								+ " en el esquema " + firstDB.getSchema()
								+ differencesSchemaFirst
								+ " y en el esquema " + secondDB.getSchema()
								+ differencesSchemaSecond + "\n";
					}
					//elimino las claves foraneas de ambos conjuntos, porque ya estan comparados
					table1.remove(tupleOfAttribute1);
					table2.remove(tupleOfAttribute2);
					//limpio las variables utilizadas para notificar cambios
					different = false;
					differencesSchemaFirst = "";
					differencesSchemaSecond = "";
				}
			}
		}
		// save the columns with distinct name in the first schema
		for (Iterator<TuplesOfStrings> iterator3 = table1.iterator(); iterator3.hasNext();) {
			TuplesOfStrings tuple1 = (TuplesOfStrings) iterator3.next();
			tmp += "\t- La clave foranea " + tuple1.getIndex(0)
					+ " del atributo: " + tuple1.getIndex(1)
					+ " que hace referencia a la tabla " + tuple1.getIndex(2)
					+ " del campo " + tuple1.getIndex(3) 
					+ " solo se encuentra en la tabla " + tableName
					+ " del esquema " + firstDB.getSchema() + "\n";
		}
		// save the columns with distinct name in the second schema
		for (Iterator<TuplesOfStrings> iterator4 = table2.iterator(); iterator4.hasNext();) {
			TuplesOfStrings tuple2 = (TuplesOfStrings) iterator4.next();
			tmp += "\t- La clave foranea " + tuple2.getIndex(0)
					+ " del atributo: " + tuple2.getIndex(1)
					+ " que hace referencia a la tabla " + tuple2.getIndex(2)
					+ " del campo " + tuple2.getIndex(3) 
					+ " solo se encuentra en la tabla " + tableName
					+ " del esquema " + firstDB.getSchema() + "\n";
		}
		table1.clear();
		table2.clear();
		return tmp;
	}
}
