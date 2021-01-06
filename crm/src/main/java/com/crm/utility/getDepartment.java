package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class getDepartment {
	public static String getDepartment(Connection con,int ll_deisg_cd) {
		
		String response = null;
		Statement stmt = null;
		ResultSet rs= null;
		try {
			String ls_query = "SELECT NAME FROM deprtment_mst WHERE DEPART_ID = "+ll_deisg_cd;		
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {
				response = rs.getString("NAME");
			}
		} catch(Exception e) {
			Utility.PrintMessage("Error in Get Department : "+e);
			response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
		}
		
		return response;
	}

}
