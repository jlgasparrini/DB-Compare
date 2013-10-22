package model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import utils.Querys;

public class Comparator {

	DBConnection firstDB;
	DBConnection secondDB;
	DatabaseMetaData metaDataFirstDB;
	DatabaseMetaData metaDataSecondDB;
	HashSet<String> tablesFirstDB;
	HashSet<String> tablesSecondDB;
	String result;

	
	/**
	 * @param db1
	 * @param db2
	 * 
	 * Class constructor.
	 * 		Take two databases and load its data in my structures.
	 */
	public Comparator(DBConnection db1, DBConnection db2) {
		this.firstDB = db1;
		this.secondDB = db2;
		this.result = "";
		try {
			this.metaDataFirstDB = db1.getConnection().getMetaData();
			this.metaDataSecondDB = db2.getConnection().getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.tablesFirstDB = new HashSet<String>();
		this.tablesSecondDB = new HashSet<String>();
		loadTables(this.firstDB, this.tablesFirstDB);
		loadTables(this.secondDB, this.tablesSecondDB);
	}

	public String runComparison() {
		compareWithDB(this.firstDB, this.tablesFirstDB, this.secondDB, this.tablesSecondDB);
		compareWithDB(this.secondDB, this.tablesSecondDB, this.firstDB, this.tablesFirstDB);
		return this.result;
	}

	@SuppressWarnings("unchecked")
	private void compareWithDB(DBConnection firstDB,
			HashSet<String> tablesFirstDB, DBConnection secondDB,
			HashSet<String> tablesSecondDB) {
		HashSet<String> aux1 = (HashSet<String>) tablesFirstDB.clone();
		HashSet<String> aux2 = (HashSet<String>) tablesSecondDB.clone();
		for (Iterator<String> iterator = aux1.iterator(); iterator
				.hasNext();) {
			String first = (String) iterator.next();
			boolean flag = false;
			for (Iterator<String> iterator2 = aux2.iterator(); iterator2
					.hasNext();) {
				String second = (String) iterator2.next();
				if (!flag)
					flag = (first.compareTo(second) == 0);
			}
			if (!flag) {
				this.result += "La base de datos \"" + firstDB.getBd()
						+ "\" posee la tabla \"" + first + "\"\n";
			} else {
				this.result += "Las bases de datos poseen la tabla \"" + first
						+ "\"\n";
				aux2.remove(first);
				this.result += loadDifferences(first);
			}
		}
	}

	private String loadDifferences(String table) {
		ResultSet res = DBConnection.preparateConsult(firstDB, Querys.attributesFromTableQuery(firstDB.getBd(), table));
		try {
			while (res.next()){
				//Aca hay que ir verificando los campos de las tablas!!
				//y mostrarlos en la ventanita!!
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadTables(DBConnection connection, HashSet<String> tables) {
		ResultSet result = DBConnection.preparateConsult(connection,
				Querys.tablaQuery());
		try {
			while (result.next()) {
				if (result.getString(2).compareTo(connection.getBd()) == 0) {
					tables.add(result.getString(3));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
