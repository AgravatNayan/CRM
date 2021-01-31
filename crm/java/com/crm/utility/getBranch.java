package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class getBranch {
	public static String getBranch(Connection con,String ls_branch_cd) {
		
		String response = null;
		Statement stmt = null;
		ResultSet rs= null;
		try {
			String ls_query = "SELECT BRANCH_NM FROM BRANCH_MST WHERE BRANCH_CD = '"+ls_branch_cd+"'";		
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {
				response = rs.getString("BRANCH_NM");
			}
		} catch(Exception e) {
			Utility.PrintMessage("Error in Get Department : "+e);
			response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
		}
		
		return response;
	}

}
