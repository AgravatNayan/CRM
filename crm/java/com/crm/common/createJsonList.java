package com.crm.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.crm.utility.Utility;
import com.crm.utility.getBranch;
import com.crm.utility.getDepartment;
import com.crm.utility.getDesignation;
import com.crm.utility.getEmployeeName;

public class createJsonList {
	
	public static JSONArray createJsonList(Connection con,String schemaName, String tableName,String whereClaus) throws JSONException {
		Statement stmt 			= null;
		ResultSet rs 			= null;
		String ls_query 		= null;
		String ls_column_nm		= "";
		ArrayList<String> mylist = new ArrayList<String>();
		String ls_main_query	= null;
		JSONObject response		= null;
		int		i				= 0;
		JSONObject inputJson	= null;
		JSONArray mainJson	= new JSONArray();
		String empName = null;
		
		try {
			ls_query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"+schemaName+"' AND TABLE_NAME = '"+tableName+"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {
				if (ls_column_nm.equals("")) {
					ls_column_nm = rs.getString("COLUMN_NAME")+",";
				} else {
					ls_column_nm = ls_column_nm + rs.getString("COLUMN_NAME")+",";
				}	
				i = i + 1;
				mylist.add(rs.getString("COLUMN_NAME"));
			}
			
			ls_column_nm = ls_column_nm.substring(0, ls_column_nm.length()-1);
								
			if (whereClaus.length() <= 0 || whereClaus.equals(""))
			{
				ls_main_query = "SELECT "+ls_column_nm+" FROM "+tableName;
			} else {
				ls_main_query = "SELECT "+ls_column_nm+" FROM "+tableName+" "+ whereClaus;
			}
			
			ResultSet buffer = null;
			buffer = stmt.executeQuery(ls_main_query);			
			while (buffer.next()) {		
				inputJson = new JSONObject();
				for(int j=0;j<mylist.size();j++) {
					if (mylist.get(j).equals("EMP_ID")) {
						System.out.println(Integer.parseInt(buffer.getString(mylist.get(j))));
						empName = getEmployeeName.getEmpName(con,Integer.parseInt(buffer.getString(mylist.get(j))));
					}
					inputJson.put(mylist.get(j),buffer.getString(mylist.get(j)));					
				}
				inputJson.put("EMP_NM",empName);
				mainJson.put(inputJson);
				empName = "";
			}						
		} catch (Exception e) {
			Utility.PrintMessage("Error Whilw createJsonList Of table :"+tableName);
			response = new JSONObject();
			response.put("STATUS","99");
			response.put("MESSAGE", "Error while createJsonList of table :"+tableName);
			response.put("ERROR",e.getMessage());
			mainJson.put(response);		
		}		
		return mainJson;
	}
	
	public static JSONObject createJsonColumnWiseList(Connection con,String schemaName, String tableName,String whereClaus,String columnName) throws JSONException {
		Statement stmt 			= null;
		ResultSet rs 			= null;
		String ls_query 		= null;
		String ls_column_nm		= "";
		ArrayList<String> mylist = new ArrayList<String>();
		String ls_main_query	= null;
		JSONObject response		= null;
		int		i				= 0;
		JSONObject inputJson	= null;
		JSONArray mainJson	= new JSONArray();
		JSONObject temp = new JSONObject();
		try {
								
			if (whereClaus.length() <= 0 || whereClaus.equals(""))
			{
				ls_main_query = "SELECT "+columnName+" FROM "+tableName;
			} else {
				ls_main_query = "SELECT "+columnName+" FROM "+tableName+" "+ whereClaus;
			}
			System.out.println(ls_main_query);
			ResultSet buffer = null;
			stmt = con.createStatement();
			buffer = stmt.executeQuery(ls_main_query);			
			while (buffer.next()) {		
				inputJson = new JSONObject();
				inputJson.put("EMP_ID", buffer.getString("EMP_ID"));
				inputJson.put("FIRST_NM",buffer.getString("FIRST_NM"));
			    inputJson.put("MIDDEL_NM",buffer.getString("MIDDEL_NM"));
			    inputJson.put("LAST_NM",buffer.getString("LAST_NM"));			    
			    inputJson.put("DEPARTMENT_ID",getDepartment.getDepartment(con, Integer.parseInt(buffer.getString("DEPARTMENT_ID"))));
			    inputJson.put("DESIGNATION_ID",getDesignation.getDesig(con, Integer.parseInt(buffer.getString("DESIGNATION_ID"))));
			    inputJson.put("BRANCH_CD",getBranch.getBranch(con, buffer.getString("BRANCH_CD")));
			    	
//				for(int j=0;j<mylist.size();j++) {
//					inputJson.put(mylist.get(j),buffer.getString(mylist.get(j)));					
//				}			    
				mainJson.put(inputJson);				
			}
			temp.put("STATUS_CD", "0");
			temp.put("RESPONSE", mainJson);
		} catch (Exception e) {
			e.printStackTrace();
			Utility.PrintMessage("Error Whilw createJsonList Of table :"+tableName);
			response = new JSONObject();
			temp.put("STATUS","99");
			temp.put("MESSAGE", "Error while createJsonList of table :"+tableName);
			temp.put("ERROR",e.getMessage());
			//mainJson.put(response);		
		}		
		
		return temp;
	}

}
