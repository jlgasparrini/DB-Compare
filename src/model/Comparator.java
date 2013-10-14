package model;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import utils.Querys;

public class Comparator {

	DBConnection firstDB;
	DBConnection secondDB;
	DatabaseMetaData metaDataFirstDB;
	DatabaseMetaData metaDataSecondDB;
	HashSet<String> tablesFirstDB;
	HashSet<String> tablesSecondDB;

	public Comparator(DBConnection db1, DBConnection db2) {
		this.firstDB = db1;
		this.secondDB = db2;
		try {
			this.metaDataFirstDB = db1.getConnection().getMetaData();
			this.metaDataSecondDB = db2.getConnection().getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tablesFirstDB = new HashSet<String>();
		tablesSecondDB = new HashSet<String>();
	}

	public String runComparison() {
		loadTables(firstDB, tablesFirstDB);
		//loadTables(secondDB, tablesSecondDB);
		return "";
	}

	private void loadTables(DBConnection connection, HashSet<String> tables) {
		PreparedStatement preparateConsult;
		try {
			preparateConsult = connection.getConnection().prepareCall(
					Querys.tablaQuery());
			ResultSet result = preparateConsult.executeQuery();
			while (result.next()) {
				if (result.getString(2).compareTo(connection.getBd()) == 0) {
					tables.add(result.getString(3));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(tables.toString());
	}

//	private String tableCompare(HashSet<String> tablesDB1,
//			HashSet<String> tablesDB2) {
//		String aux = "";
//		if (table1.compareTo(table2) == 0)
//
//			return aux;
//	}

}
