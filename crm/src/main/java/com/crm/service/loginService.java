package com.crm.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.ConnectionDbB;
import com.crm.utility.getMenu;
import com.crm.utility.getParaValue;

public class loginService {
	public String login(String input) throws JSONException, ClassNotFoundException, SQLException {			
		JSONObject jObject = new JSONObject(input);
		String username = jObject.getString("username");
		String password = jObject.getString("password");
		JSONObject jOutput = new JSONObject();
		JSONArray menuGrant =new JSONArray();
		Connection con= null;	
		con = ConnectionDbB.getCon();		
		if (username.length() <= 0) {
			jOutput.put("RESPONSE","VALIDATION_FAILE");
			jOutput.put("STATUS_CD","999");
			jOutput.put("ERROR_MSG","Please enter your username.");
		}else {
			if(password.length() <= 0) {
				jOutput.put("RESPONSE","VALIDATION_FAILE");
				jOutput.put("STATUS_CD","999");
				jOutput.put("ERROR_MSG","Please enter your password.");
			} else {								
				Statement stmt = null;
				ResultSet rs = null;
				
				stmt = con.createStatement();
				String ls_query = "SELECT USER_ID,MENU_RIGHRS,ACTIVE_STATUS,BLOCK_STATUS,USER_PWD FROM USER_MST WHERE USER_ID = '"+username+"'";
				
				rs=stmt.executeQuery(ls_query);
				String ls_userid = null;
				String ls_menu = null;
				String activeStatus = null;
				String blockStatus = null;
				String user_pwd = null;
				try {
					while(rs.next()) {					
						ls_userid = rs.getString("USER_ID");					
						ls_menu = rs.getString("MENU_RIGHRS");
						activeStatus = rs.getString("ACTIVE_STATUS");
						blockStatus = rs.getString("BLOCK_STATUS");
						user_pwd = rs.getString("USER_PWD");
					}
					
					if (!password.equals(user_pwd) & !user_pwd.equals(null)) {
						String ls_para = null;
						try {
							ls_para = getParaValue.getPara(con, "999","999",1);	
						} catch(Exception e) {
							e.printStackTrace();
						}
												
						int block_cnt = Integer.parseInt(ls_para);
						
						Statement failed_stmt = null;
						ResultSet failed_rs = null;						
						String failed_query = "SELECT FAILED_CNT FROM USER_MST WHERE USER_ID='"+username+"'";										
						failed_stmt = con.createStatement();
						failed_rs = failed_stmt.executeQuery(failed_query);
																
						int count = 0;
						while(failed_rs.next()) {
							count = failed_rs.getInt("FAILED_CNT");
						}
												
						count = count + 1;
						if(block_cnt <= count) {
							try {
								String updateCnt = "UPDATE USER_MST SET BLOCK_STATUS = 'Y' ,FAILED_CNT = "+count+" WHERE USER_ID = '"+username+"'";							
								stmt.executeUpdate(updateCnt);		
								con.commit();
							} catch(Exception e) {
								e.printStackTrace();
							}	
							jOutput.put("RESPONSE","BLOCK");
							jOutput.put("STATUS_CD","999");
							jOutput.put("ERROR_MSG","UserID is block Kindly contact to your admin");
							String ls_output = jOutput.toString();			
							return ls_output;
						} else {
							try {
								String updateCnt = "UPDATE USER_MST SET BLOCK_STATUS = 'N' ,FAILED_CNT = "+count+" WHERE USER_ID = '"+username+"'";							
								stmt.executeUpdate(updateCnt);		
								con.commit();
							} catch(Exception e) {
								e.printStackTrace();
							}							
						}											
												
						jOutput.put("RESPONSE","INVALID_PASSWORD");
						jOutput.put("STATUS_CD","999");
						jOutput.put("ERROR_MSG","Password Wrong."+(block_cnt - count)+" attempt pending..");
						String ls_output = jOutput.toString();			
						return ls_output;	
					}
					
				} catch(Exception e) {
					jOutput.put("RESPONSE","NOT_FOUND");
					jOutput.put("STATUS_CD","999");
					jOutput.put("ERROR_MSG","UserID not found");
					String ls_output = jOutput.toString();			
					return ls_output;
				}
				
										
				if (activeStatus.equals("N")) {
					jOutput.put("RESPONSE","INACTIVATED");
					jOutput.put("STATUS_CD","999");
					jOutput.put("ERROR_MSG","UserID Inactived Kindly contact to your admin");
				} else {
					if(blockStatus.equals("Y")) {
						jOutput.put("RESPONSE","BLOCK");
						jOutput.put("STATUS_CD","999");
						jOutput.put("ERROR_MSG","UserID is block Kindly contact to your admin");
					} else {
						menuGrant = getMenu.getMenu(ls_menu);	
						jOutput.put("RESPONSE","SUCESS");
						jOutput.put("STATUS_CD","200");
						jOutput.put("USER_ID",ls_userid);
						jOutput.put("STATUS","Grant");
						jOutput.put("MENU",menuGrant);					
					}
				}																											
			}
		}		
		String ls_output = jOutput.toString();			
		return ls_output;
				
	}	
}
