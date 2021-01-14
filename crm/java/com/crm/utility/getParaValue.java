package com.crm.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class getParaValue {

  public static String getPara(Connection con, int ll_para_cd) throws ClassNotFoundException,
  SQLException {
    String ls_para = null;
    ResultSet rs = null;
    try {
      String ls_query = "SELECT PARA_VAL FROM SYS_PARA_MST WHERE PARA_CD= ?";
      PreparedStatement ps = null;
      ps = con.prepareStatement(ls_query);
      ps.setInt(1, ll_para_cd);
      rs = ps.executeQuery();

      if (rs.next()) {
        ls_para = rs.getString("PARA_VAL");
      }
    } catch(Exception e) {
      ls_para = "";
      System.out.println("Get Para Error : " + e);
      e.printStackTrace();
    } finally {
    	rs.close();
    }    
    return ls_para;
  }
  public static int getMaxTranCd(Connection con,String tableName,String columnName) throws SQLException {
	  String ls_para = null;
	  int maxTranCd = 0;
	  ResultSet rs = null;
	  try {
	      String ls_query = "SELECT MAX("+columnName+") AS MAXCD FROM "+tableName;
	      PreparedStatement ps = null;
	      ps = con.prepareStatement(ls_query);	      
	      rs = ps.executeQuery();

	      if (rs.next()) {
	    	  maxTranCd = rs.getInt("MAXCD");
	      }
	    } catch(Exception e) {
	      ls_para = "";
	      System.out.println("Get Para Error : " + e);
	      e.printStackTrace();
	    } finally {
	    	rs.close();
	    }    
	  if (maxTranCd > 0) {
		  maxTranCd = maxTranCd +1;
	  } else {
		  maxTranCd = 1;
	  }
	  return maxTranCd;
  }
}