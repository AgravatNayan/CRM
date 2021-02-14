package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class getMaxTranCd {
	public static int getMaxTranCD(Connection con,String tableName) {
		
		Statement stmt = null;
		ResultSet rs = null;
		int tran_cd = 0;
		try {
			
			String ls_sql = "SELECT MAX(TRAN_CD) AS TRAN_CD FROM "+tableName+"";
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_sql);
			
			while(rs.next()) {
				tran_cd = rs.getInt("TRAN_CD");
			}
			
			if (tran_cd == 0 ) {
				tran_cd = tran_cd + 1;
			} else {
				tran_cd = tran_cd + 1; 
			}
		} catch(Exception e) {
			tran_cd = 1;
		}
		return tran_cd;
	}

}
