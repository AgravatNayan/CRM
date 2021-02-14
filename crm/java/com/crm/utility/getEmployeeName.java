package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class getEmployeeName {
	public static String getEmpName(Connection con,int ll_emp_cd) {
		
		String response = null;
		Statement stmt = null;
		ResultSet rs= null;
		String ls_first_nm = null;
		String ls_middle_nm = null;
		String ls_last_nm = null;
		
		try {
			String ls_query = "SELECT FIRST_NM,MIDDEL_NM,LAST_NM FROM EMP_MST WHERE EMP_ID = "+ll_emp_cd;		
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {				
				ls_first_nm = rs.getString("FIRST_NM");
				ls_middle_nm = rs.getString("MIDDEL_NM");
				ls_last_nm = rs.getString("LAST_NM");							
			}
		} catch(Exception e) {
			Utility.PrintMessage("Error in Get Department : "+e);
			response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
		}
		
		return ls_first_nm+" "+ls_middle_nm+" "+ls_last_nm;
	}

}
