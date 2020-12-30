package com.crm.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class getCategoryValue {
	public static String getPara(Connection con, String comp_cd,String category,String data_val) throws ClassNotFoundException,
	  SQLException {
	    String ls_para = null;
	    ResultSet rs = null;
	    try {
	      String ls_query = "SELECT DISPLAY_VALUE FROM P_MISC_MST WHERE COMP_CD = ? AND CATEGORY_CD = ? AND DATA_VALUE= ?";
	      PreparedStatement ps = null;
	      ps = con.prepareStatement(ls_query);
	      ps.setString(1, comp_cd);
	      ps.setString(2, category);
	      ps.setString(3, data_val);
	      rs = ps.executeQuery();

	      if (rs.next()) {
	        ls_para = rs.getString("DISPLAY_VALUE");
	      }
	    } catch(Exception e) {
	      ls_para = "";
	      System.out.println("Get P_MISC_MST Error : " + e);
	      e.printStackTrace();
	    }
	    return ls_para;
	  }

}
