package com.crm.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONException;
import org.json.JSONObject;

public class getMaxIdentityCd {
	public static JSONObject getMaxidentityCode(Connection Con,String columnName,String tableName, String whereCluses,String userName) throws JSONException {
		ResultSet rs = null;
		Statement stmt = null;
		int i = 0;
		JSONObject response = new JSONObject();		
		try {
			String ls_query = "SELECT MAX("+columnName+") AS ID FROM "+ tableName+" "+whereCluses; 
			
			stmt = Con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {
				i = rs.getInt("ID");
			}			
			response.put("USERNAME", userName);
			response.put("MAX_ID", i+1);			
		} catch(Exception e) {
			response.put("STATUS","99");
			response.put("MESSAGE", "Error while get Max Code of table :"+tableName);
			response.put("ERROR",e.getMessage());
		}
		return response;
	}
}
