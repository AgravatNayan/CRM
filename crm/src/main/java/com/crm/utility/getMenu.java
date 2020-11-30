package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getMenu {
	public static JSONArray getMenu(String menuInputStirng) throws JSONException, ClassNotFoundException, SQLException {
		String[] mTrancd = menuInputStirng.split(",");					
		JSONArray jArray = new JSONArray();	
		int cnt = 0;					 
		Statement stmt = null;
		ResultSet rs = null;		 
		Connection conMain = ConnectionDbB.getCon();
					
		for (int i=0;i<mTrancd.length;i++) {
			JSONObject jtest = new JSONObject();
			
			String mainMenu = "SELECT TRAN_CD,MENU_NM FROM MENU_MST WHERE TRAN_CD = "+mTrancd[i];						
			stmt = conMain.createStatement();
			rs = stmt.executeQuery(mainMenu);
			String menuName = null;
			int tran_cd = 0;
			while (rs.next()) {
				menuName = rs.getString("MENU_NM");
				tran_cd = rs.getInt("TRAN_CD");				
				jtest.put(mTrancd[i],menuName);															 							
				JSONArray subJson = getSubMenu(tran_cd);
				jtest.put("SubMenu",subJson);
			}					
			jArray.put(jtest);
		}								
		return jArray;
	}
	
	public static JSONArray getSubMenu(int tran_cd) throws SQLException, JSONException, ClassNotFoundException {
		String mainMenu = "SELECT SR_CD,MENU_NM FROM MENU_DTL WHERE TRAN_CD = "+tran_cd;
		
		JSONArray jSubmenu = new JSONArray();		
		Statement stmt = null;
		ResultSet rs = null;
		 
		Connection conSub = ConnectionDbB.getCon();			
		stmt = conSub.createStatement();
		rs = stmt.executeQuery(mainMenu);
		String subMenuCode = null;
		String subMenuName = null;
		JSONObject temp = new JSONObject();
		while(rs.next()) {
			subMenuCode = rs.getString("SR_CD");
			subMenuName = rs.getString("MENU_NM");												
			temp.put(subMenuCode, subMenuName);				
		}
		jSubmenu.put(temp);		
		return jSubmenu;
	}
}
