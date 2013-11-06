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
	 * realiza las comparaciones y las retorna en un string
	 * @return string con los resultados de la comparacion
	 */
	public String runComparison() {
		HashSet<String> schema1 = Queries.getTables(this.metaDataFirstDB, this.firstDB.getSchema());
		HashSet<String> schema2 = Queries.getTables(this.metaDataSecondDB, this.secondDB.getSchema());
		String result = "";
		result = compareTableNames(schema1, schema2);
		if (!schema1.isEmpty())
			result += compareTablesEqualsName(schema1);
		result += compareStoredProcedures();
		result += compareTriggers();
		if (result.length() == 0)
			result = "Las bases de datos de los esquemas son iguales";
		return result;
	}

	/**
	 * Compara los nombres de las tablas
	 * @param schema1
	 * @param schema2
	 * @return String con las diferencias con respecto
	 *  a nombres de las tablas de los esquemas
	 */
	@SuppressWarnings("unchecked")
	private String compareTableNames(HashSet<String> schema1,
			HashSet<String> schema2) {
		String tmp = "";
		boolean different = false; // determina si imprimir la leyenda
		// conjuntos auxiliares usados para las iteraciones
		HashSet<String> aux = (HashSet<String>) schema1.clone();
		HashSet<String> aux2 = (HashSet<String>) schema2.clone();
		// notifica las tablas adicionales del primer esquema
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
		// notifica las tablas adicionales del segundo esquema
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
	 * compara las tablas que tienen el mismo nombre en ambos esquemas
	 * @param schema"conjunto que contiene los nombres de las tablas
	 * @return String "retorna las diferencias"
	 */
	private String compareTablesEqualsName(HashSet<String> schema) {
		String tmp = "";
		boolean different = false;
		String tableName;
		String difAttribute; //notifica cambios en las columnas
		String difPrimarykey; //notifica cambios en las claves primarias
		String difIndex; //notifica cambios en los indices
		String difUniqueKey; //notifica cambios en las claves unicas
		String difForeignKeys; //notifica cambios en las claves foraneas
		// compara las tablas con mismo nombre
		for (Iterator<String> iterator = schema.iterator(); iterator.hasNext();) {
			tableName = (String) iterator.next();
			difAttribute = compareTheAttributesOfTheTable(tableName,1);
			difPrimarykey = compareKey(tableName,"primaria");
			difIndex = compareTheAttributesOfTheTable(tableName,2);
			difUniqueKey = compareKey(tableName,"unica");
			difForeignKeys = compareForeignKeys(tableName);
			//si existe alguna diferencia en la tabla lo notifico
			if(difAttribute.length() + difPrimarykey.length()+ difForeignKeys.length() + difUniqueKey.length() + difIndex.length() > 0){
				if (!different){
					tmp += "La tabla " + tableName + " que se encuentra en ambos esquemas, tiene de diferente: \n";
					different = true;
				}
				tmp+= difAttribute + difPrimarykey + difForeignKeys + difUniqueKey + difIndex + "\n";
				//limpio las variables
				difAttribute = "";
				difForeignKeys = "";
				difPrimarykey = "";
				difIndex = "";
				difUniqueKey = "";
				
			}
		}
		return tmp;
	}
	
	/**
	 * Compara las columnas o indices de la tabla pasada por parametro
	 * @param tableName "String"
	 * @param type 1: compara columnas - 2: compara indices
	 * @return retorna las diferencias dependiendo del parametro "type"
	 */
	@SuppressWarnings("unchecked")
	private String compareTheAttributesOfTheTable(String tableName, int type){
		String tmp = "";
		//conjuntos que almacenan la informacion de las columnas de la tabla actual
		HashSet<TuplesOfStrings> table1 = new HashSet<TuplesOfStrings>();
		HashSet<TuplesOfStrings> table2 = new HashSet<TuplesOfStrings>();
		// cargo los conjuntos con la informacion de la columna o indice
		if(type == 1){ // si estoy comparando la informacion de columnas
			table1 = Queries.getAttributes(metaDataFirstDB, firstDB.getSchema(), tableName);
			table2 = Queries.getAttributes(metaDataSecondDB, secondDB.getSchema(), tableName);
		}else{ //si estoy comparando la informacion de indices
			table1 = Queries.getIndexs(metaDataFirstDB, firstDB.getSchema(), tableName);
			table2 = Queries.getIndexs(metaDataSecondDB, secondDB.getSchema(), tableName);
		}
		// conjuntos auxiliares usados para la iteracion
		HashSet<TuplesOfStrings> aux = (HashSet<TuplesOfStrings>) table1.clone();
		HashSet<TuplesOfStrings> aux2 = (HashSet<TuplesOfStrings>) table2.clone();
		for (Iterator<TuplesOfStrings> iterator = aux.iterator(); iterator.hasNext();) {
			TuplesOfStrings tupleOfAttribute1 = (TuplesOfStrings) iterator.next();

			for (Iterator<TuplesOfStrings> iterator2 = aux2.iterator(); iterator2.hasNext();) {
				TuplesOfStrings tupleOfAttribute2 = (TuplesOfStrings)  iterator2.next();
				//comparo si los nombres son iguales
				if (tupleOfAttribute1.getIndex(0).compareTo(tupleOfAttribute2.getIndex(0)) == 0) {
					//comparo si tienen distinto tipo (columnas) o hacen referencia a columnas distintas (indices)
					if (tupleOfAttribute1.getIndex(1).compareTo(tupleOfAttribute2.getIndex(1)) != 0) {
						tmp += type == 1 ? "- La columna " : "- El indice ";
						tmp	+= tupleOfAttribute1.getIndex(0) + " en el esquema " + firstDB.getSchema();
						tmp += type == 1 ? " el tipo es " : " es sobre la columna "; 
						tmp += tupleOfAttribute1.getIndex(1) + " y en el esquema " + secondDB.getSchema(); 
						tmp += type == 1 ? " el tipo es " : " es sobre la columna ";
						tmp += tupleOfAttribute2.getIndex(1) + "\n";
					}
					//elimino los atributos de ambos conjuntos, porque ya estan comparados
					table1.remove(tupleOfAttribute1);
					table2.remove(tupleOfAttribute2);
				}
			}
		}
		// informa las columnas o indices adicionales en el primer esquema
		for (Iterator<TuplesOfStrings> iterator3 = table1.iterator(); iterator3.hasNext();) {
			TuplesOfStrings tuple1 = (TuplesOfStrings) iterator3.next();
			tmp += type == 1 ? "- La columna " : "- El indice ";
			tmp += tuple1.getIndex(0);
			tmp += type == 1 ? " de tipo " : " sobre la columna "; 
			tmp += tuple1.getIndex(1)	+ " solo se encuentra en la tabla " + tableName
					+ " del esquema " + firstDB.getSchema() + "\n";
		}
		// informa las columnas o indices adicionales en el segundo esquema
		for (Iterator<TuplesOfStrings> iterator4 = table2.iterator(); iterator4.hasNext();) {
			TuplesOfStrings tuple2 = (TuplesOfStrings) iterator4.next();
			tmp += type == 1 ? "- La columna " : "- El indice ";
			tmp += tuple2.getIndex(0);
			tmp += type == 1 ? " de tipo " : " sobre la columna "; 
			tmp += tuple2.getIndex(1)	+ " solo se encuentra en la tabla " + tableName
					+ " del esquema " + secondDB.getSchema() + "\n";
		}
		return tmp;
	}
	
	/**
	 * Compara las claves unicas o primarias de la tabla pasada por parametro
	 * @param tableName "String"
	 * @param keyType "primaria" para clave primaria - "unica" para clave unica
	 * @return String
	 */
	private String compareKey(String tableName, String keyType){
		String tmp = "";
		//conjuntos que almacenan la informacion de las claves
		HashSet<String> table1 = new HashSet<String>();
		HashSet<String> table2 = new HashSet<String>();
		// cargo los conjuntos con la informacion de la clave,dependiendo de cual sea
		if(keyType.compareTo("primaria") == 0){ // si estoy comparando claves primarias
			table1 = Queries.getPrimaryKeys(metaDataFirstDB, firstDB.getSchema(), tableName);
			table2 = Queries.getPrimaryKeys(metaDataSecondDB, secondDB.getSchema(), tableName);
		}else{ // si estoy comparando claves unicas
			table1 = Queries.getUniques(metaDataFirstDB, firstDB.getSchema(), tableName);
			table2 = Queries.getUniques(metaDataSecondDB, secondDB.getSchema(), tableName);
		}
		//comparo las claves primarias del primer esquema con las del segundo
		for (Iterator<String> iterator = table1.iterator(); iterator.hasNext();) {
			String key1 = (String) iterator.next();
			//si se encuentra en ambos esquemas la elimino del segundo
			if(table2.contains(key1)){
				table2.remove(key1);
			}else //si se encuetra solo en el primer esquema, lo informo
				tmp += "\t- La clave "+ keyType +" "+ key1 + " de la tabla " + tableName 
					+ " solo se encuentra en el esquema " + firstDB.getSchema() + "\n";
		}
		//informo las claves primarias que se encuentran unicamente en el segundo esquema
		for (Iterator<String> iterator = table2.iterator(); iterator.hasNext();) {
			String key2 = (String) iterator.next();
			tmp += "\t- La clave "+ keyType +" "+ key2 + " de la tabla " + tableName 
					+ " solo se encuentra en el esquema " + secondDB.getSchema() + "\n";
		}
		return tmp;
	}
	
	/**
	 * compara las claves foraneas de tablas pasada por parametro 
	 * @param tableName
	 * @return retorna las diferencias
	 */
	@SuppressWarnings("unchecked")
	public String compareForeignKeys(String tableName){
		String tmp = ""; //variable donde se setean los resultados
		//conjuntos que almacenan la informacion de las claves foraneas
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
	
	private String compareStoredProcedures(){
		String result = "";
		//conjuntos que almacenan la informacion de los procedimientos
		HashSet<String> proceduresNameFirstDB = Queries.getNamesOfStoredProcedures(this.metaDataFirstDB, this.firstDB.getSchema());
		HashSet<String> proceduresNameSecondDB = Queries.getNamesOfStoredProcedures(this.metaDataSecondDB, this.secondDB.getSchema());
		for (String string : proceduresNameFirstDB) {
			boolean flagDiff = false;
			String aux = "";
			if (proceduresNameSecondDB.contains(string)){
				//Entro solamente si se encuentra el mismo nombre de procedimiento en el otro esquema.
				HashSet<String> profile1 = Queries.getProfilesOfStoreProcedures(this.metaDataFirstDB,this.firstDB.getSchema(), string);
				HashSet<String> profile2 = Queries.getProfilesOfStoreProcedures(this.metaDataSecondDB,this.secondDB.getSchema(), string);
				//Verifico si hay diferencias en el perfil de la funcion.
				if (profile1.size() > profile2.size()){
					result+= "\t- En el esquema "+this.firstDB.getSchema()+" el procedimiento tiene mayor cantidad de parametros.\n";
					flagDiff = true;
				}
				if (profile1.size() < profile2.size()){
					result+= "\t- En el esquema "+this.secondDB.getSchema()+" el procedimiento tiene mayor cantidad de parametros.\n";
					flagDiff = true;
				}
				if (profile1.size() == profile2.size())
					if (!profile1.equals(profile2)){
						flagDiff = true;
						result+= "\t- El perfil del procedimiento almacenado tiene parametros distintos.\n";
					}
				if (!flagDiff)
					result+= "- El procedimiento almacenado "+string+" es igual en los dos esquemas.";
				else{
					aux+= "- El procedimiento almacenado "+string+" tiene las siguientes diferencias.";
					result = aux + result;
				}
			}
		}
		return result;
	}
	
	private String compareTriggers(){
		String tmp = "";
		//conjuntos que almacenan la informacion de los triggers
		HashSet<TuplesOfStrings> schema1 = new HashSet<TuplesOfStrings>();
		HashSet<TuplesOfStrings> schema2 = new HashSet<TuplesOfStrings>();
		// cargo los conjuntos con la informacion de los triggers
		schema1 = Queries.getTriggers(metaDataFirstDB, firstDB.getSchema());
		schema2 = Queries.getTriggers(metaDataSecondDB, secondDB.getSchema());
		//variables para notificar diferencias
		String differences = "";
		for (Iterator<TuplesOfStrings> iterator = schema1.iterator(); iterator.hasNext();) {
			TuplesOfStrings tuple1 = (TuplesOfStrings) iterator.next();
			for (Iterator<TuplesOfStrings> iterator2 = schema2.iterator(); iterator2.hasNext();) {
				TuplesOfStrings tuple2 = (TuplesOfStrings) iterator2.next();
				//comparo si los nombres de los trigers
				if (tuple1.getIndex(0).compareTo(tuple2.getIndex(0)) == 0) {
					//comparo si las claves son de distintos atributo 
					if (tuple1.getIndex(1).compareTo(tuple2.getIndex(1)) != 0)
						differences += "\t - En el esquema " + firstDB.getSchema()
										+ "al ejecucion es " + tuple1.getIndex(1)
										+ " y en el esquema " + secondDB.getSchema()
										+ "la ejecucion es " + tuple2.getIndex(1);
					if (tuple1.getIndex(2).compareTo(tuple2.getIndex(2)) != 0)
						differences += "\t - En el esquema " + firstDB.getSchema()
										+ " se realiza la operacion " + tuple1.getIndex(2)
										+ " y en el esquema " + secondDB.getSchema()
										+ "se realiza la operacion " + tuple2.getIndex(2);
					if (tuple1.getIndex(3).compareTo(tuple2.getIndex(3)) != 0)
						differences += "\t - En el esquema " + firstDB.getSchema()
										+ " se realiza la operacion sobre la tabla " + tuple1.getIndex(3)
										+ " y en el esquema " + secondDB.getSchema()
										+ " se realiza la operacion sobre la tabla " + tuple2.getIndex(3);
					if (tuple1.getIndex(4).compareTo(tuple2.getIndex(4)) != 0)
						differences += "\t - En el esquema " + firstDB.getSchema()
										+ " el trigger realiza la accion " + tuple1.getIndex(4)
										+ " y en el esquema " + secondDB.getSchema()
										+ " el trigger realiza la accion " + tuple2.getIndex(4);
					if(differences.compareTo("") != 0){
						tmp += "El trigger " + tuple1.getIndex(0) + " que se encuentra en ambos esquemas tiene de diferente: \n";
						tmp += differences;
						differences = "";
					}
					
				}
			}
		}
		return tmp;
	}
}

