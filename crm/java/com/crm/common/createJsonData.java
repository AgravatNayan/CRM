package com.crm.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class createJsonData {
	public static JSONObject createData(Connection con, String jsonData, String tableName)
			throws JSONException, SQLException {
		JSONObject response = new JSONObject();
		try {
			JSONObject inputJson = new JSONObject(jsonData);
			ArrayList<String> dataTypeList = new ArrayList<String>();
			ArrayList<String> columnNameList = new ArrayList<String>();

			columnNameList = getColumnName(con, tableName, "COLUMN_NAME");
			dataTypeList = getColumnName(con, tableName, "DATA_TYPE");

			String columnList = null;
			String ls_val = null;
			for (int i = 0; i < columnNameList.size(); i++) {
				if (columnList == null) {
					columnList = columnNameList.get(i) + ",";
					ls_val = "?,";
				} else {
					columnList = columnList + columnNameList.get(i) + ",";
					ls_val = ls_val + "?,";
				}
			}
			columnList = columnList.substring(0, columnList.length() - 1);
			ls_val = ls_val.substring(0, ls_val.length() - 1);

			String insertQuery = "INSERT INTO " + tableName + " (" + columnList + ") VALUES(" + ls_val + ")";
			PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
			System.out.println(insertQuery);
			for (int j = 0; j < columnNameList.size(); j++) {

				if (dataTypeList.get(j).toUpperCase().equals("VARCHAR")) {
					preparedStatement.setString(j + 1, inputJson.getString(columnNameList.get(j)));
				} else if (dataTypeList.get(j).toUpperCase().equals("INT")) {
					preparedStatement.setString(j + 1, inputJson.getString(columnNameList.get(j)));
				} else if (dataTypeList.get(j).toUpperCase().equals("DATE")) {
					preparedStatement.setString(j + 1, inputJson.getString(columnNameList.get(j)));
				}
			}

			int row = preparedStatement.executeUpdate();
			int transactionStatus = 0;
			if (row == 0) {
				con.rollback();
				System.out.println("Rollback");
			} else {
				con.commit();
				transactionStatus = 1;
			}
			
			if (transactionStatus == 1 ) {
				response.put("STATUS","0");
				response.put("MESSAGE", "Record Sucessfully Inserted");
			} 
			
		} catch (Exception e) {
			response = new JSONObject();
			response.put("STATUS","99");
			response.put("MESSAGE", "Error while createJsonList of table :"+tableName);
			response.put("ERROR",e.getMessage());
		}
		return response;
	}

	public static ArrayList getColumnName(Connection con, String tableName, String getValue) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		String ls_query = null;
		String ls_column_nm = "";
		ArrayList<String> columnList = new ArrayList<String>();
		String ls_main_query = null;
		JSONObject response = null;
		int i = 0;
		JSONObject inputJson = null;
		JSONArray mainJson = new JSONArray();

		ls_query = "SELECT " + getValue
				+ " AS DB_VAL FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'crm' AND TABLE_NAME = '" + tableName
				+ "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(ls_query);
		System.out.println(ls_query);
		while (rs.next()) {
			columnList.add(rs.getString("DB_VAL"));
		}
		return columnList;
	}

}
