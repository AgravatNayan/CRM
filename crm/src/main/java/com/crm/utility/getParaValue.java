package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class getParaValue {
	
	public static String getPara(Connection con,String ls_comp_cd,String ls_branch_cd,int ll_para_cd) throws ClassNotFoundException, SQLException {					
		String ls_para = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String ls_query = "SELECT PARA_VAL FROM SYS_PARA_MST WHERE COMP_CD='"+ls_comp_cd+"' AND BRANCH_CD = '"+ls_branch_cd+"' AND PARA_CD= "+ll_para_cd+"";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			while(rs.next()) {
				ls_para = rs.getString("PARA_VAL");
			}	
			
		} catch(Exception e) {
			e.printStackTrace();
		}	
		return ls_para;
	}

}
