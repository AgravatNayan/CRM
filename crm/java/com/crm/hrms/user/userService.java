package com.crm.hrms.user;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import com.crm.utility.NVL;
import com.crm.utility.Utility;
import com.crm.utility.getCategoryValue;

public class userService {
	 public static String getUserList(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;	      
	        try {
	            JSONObject jin = new JSONObject(ls_request);
	            JSONObject jOut = null;
	            JSONArray jresponse = new JSONArray();

	            String viewType = NVL.StringNvl(jin.getString("VIEW_FLAG"));
	            String enteredUser = NVL.StringNvl(jin.getString("USERNAME"));
	            String sql = null;
	            if (viewType.equals("G")) {
	                sql = "SELECT COMP_CD,USER_ID,USER_LEVEL_CD,REF_ID,CONTACT1,EMAIL,ACTIVE_STATUS,BLOCK_STATUS,INACTIVE_DATE,BLOCKED_DATE,FAILED_CNT,USER_SIGNATURE,USER_PHOTO,USER_PWD,TEMP_PWD,FAV_QUE_CD FROM user_mst WHERE IS_DELETE = 'N'";
	            } else {
	                sql = "SELECT COMP_CD,USER_ID,USER_LEVEL_CD,REF_ID,CONTACT1,EMAIL,ACTIVE_STATUS,BLOCK_STATUS,INACTIVE_DATE,BLOCKED_DATE,FAILED_CNT,USER_SIGNATURE,USER_PHOTO,USER_PWD,TEMP_PWD,FAV_QUE_CD FROM user_mst WHERE ENTERED_BY = '" + enteredUser + "' AND IS_DELETE = 'N'";
	            }

	            stmt = con.createStatement();
	            rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                jOut = new JSONObject();	                	               	    	        		    	        	    	       
		            jOut.put("USER_PHOTO",NVL.StringNvl(rs.getString("USER_PHOTO")));	    	        
	    	        jOut.put("COMP_CD",NVL.StringNvl(rs.getString("COMP_CD")));
	    	        jOut.put("USER_ID", NVL.StringNvl(rs.getString("USER_ID")));
	    	        jOut.put("USER_LEVEL_CD",NVL.StringNvl(getCategoryValue.getPara(con, rs.getString("COMP_CD"),"USER_LEVEL",rs.getString("USER_LEVEL_CD"))));
	    	        jOut.put("REF_ID",NVL.StringNvl(rs.getString("REF_ID")));
	    	        jOut.put("CONTACT1",NVL.StringNvl(rs.getString("CONTACT1")));
	    	        jOut.put("EMAIL", NVL.StringNvl(rs.getString("EMAIL")));
	    	        jOut.put("ACTIVE_STATUS",NVL.StringNvl(rs.getString("ACTIVE_STATUS")));
	    	        jOut.put("BLOCK_STATUS",NVL.StringNvl(rs.getString("BLOCK_STATUS")));
	    	        jOut.put("INACTIVE_DATE",NVL.StringNvl(rs.getString("INACTIVE_DATE")));
	    	        jOut.put("BLOCKED_DATE",NVL.StringNvl(rs.getString("BLOCKED_DATE")));
	    	        jOut.put("FAILED_CNT",NVL.StringNvl(rs.getString("FAILED_CNT")));
	    	        jOut.put("USER_PWD",NVL.StringNvl(rs.getString("USER_PWD")));
	    	        jOut.put("TEMP_PWD",NVL.StringNvl(rs.getString("TEMP_PWD")));
	    	        jOut.put("FAV_QUE_CD",NVL.StringNvl(rs.getString("FAV_QUE_CD")));
	    	        	                	                	                	             
	                jresponse.put(jOut);
	                jOut = new JSONObject();
	            }

	            JSONObject jOutput = new JSONObject();

	            if (jresponse.length() <= 0) {
	                jOutput.put("STATUS_CD", "99");
	                jOutput.put("MESSAGE", "User not found");
	            } else {
	            	jOutput.put("STATUS_CD", "0");
	                jOutput.put("RESPONSE", jresponse);
	            }

	            response = jOutput.toString();
	        } catch (Exception e) {
	            e.printStackTrace();
	            Utility.PrintMessage("Error in Get User List : " + e);
	            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	        } finally {
	            try {
	                rs.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                con.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	        }

	        return response;
	    }
	 
	 public static String GetUserID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ll_cnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);

	            String ls_user_id = NVL.StringNvl(jin.getString("USER_ID"));
	            String viewFlag = NVL.StringNvl(jin.getString("VIEW_FLAG"));
	            String userName = NVL.StringNvl(jin.getString("USERNAME"));

	            JSONObject jOut = new JSONObject();
	            String sql = null;
	            if (viewFlag.equals("G")) {
	                sql = "SELECT COMP_CD,USER_ID,USER_LEVEL_CD,REF_ID,CONTACT1,EMAIL,ACTIVE_STATUS,BLOCK_STATUS,INACTIVE_DATE,BLOCKED_DATE,FAILED_CNT,USER_SIGNATURE,USER_PHOTO,USER_PWD,TEMP_PWD,FAV_QUE_CD FROM user_mst WHERE USER_ID = '"+ls_user_id+"'AND IS_DELETE = 'N'";
	            } else {
	                sql = "SELECT COMP_CD,USER_ID,USER_LEVEL_CD,REF_ID,CONTACT1,EMAIL,ACTIVE_STATUS,BLOCK_STATUS,INACTIVE_DATE,BLOCKED_DATE,FAILED_CNT,USER_SIGNATURE,USER_PHOTO,USER_PWD,TEMP_PWD,FAV_QUE_CD FROM user_mst WHERE ENTERED_BY = '" + userName + "' AND IS_DELETE = 'N'";

	            }

	            stmt = con.createStatement();
	            rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                ll_cnt = ll_cnt + 1;
	                jOut.put("COMP_CD",NVL.StringNvl(rs.getString("COMP_CD")));
	                jOut.put("USER_ID",NVL.StringNvl(rs.getString("USER_ID")));
	                jOut.put("USER_LEVEL_CD",NVL.StringNvl(rs.getString("USER_LEVEL_CD")));
	                jOut.put("REF_ID",NVL.StringNvl(rs.getString("REF_ID")));
	                jOut.put("CONTACT1",NVL.StringNvl(rs.getString("CONTACT1")));
	                jOut.put("EMAIL",NVL.StringNvl(rs.getString("EMAIL")));
	                jOut.put("ACTIVE_STATUS",NVL.StringNvl(rs.getString("ACTIVE_STATUS")));
	                jOut.put("BLOCK_STATUS",NVL.StringNvl(rs.getString("BLOCK_STATUS")));
	                jOut.put("INACTIVE_DATE",NVL.StringNvl(rs.getString("INACTIVE_DATE")));
	                jOut.put("BLOCKED_DATE",NVL.StringNvl(rs.getString("BLOCKED_DATE")));
	                jOut.put("FAILED_CNT",NVL.StringNvl(rs.getString("FAILED_CNT")));
	              //  jOut.put("USER_SIGNATURE",NVL.StringNvl(rs.getString("USER_SIGNATURE")));
	              //  jOut.put("USER_PHOTO",NVL.StringNvl(rs.getString("USER_PHOTO")));
	                jOut.put("USER_PWD",NVL.StringNvl(rs.getString("USER_PWD")));
	                jOut.put("TEMP_PWD",NVL.StringNvl(rs.getString("TEMP_PWD")));
	                jOut.put("FAV_QUE_CD",NVL.StringNvl(rs.getString("FAV_QUE_CD")));               
	            }

	            JSONArray jresponse = new JSONArray();
	            JSONObject jobj = new JSONObject();
	            jobj.put("STATUS_CD", "0");
	            if (jOut.length() <= 0) {
	                jobj.put("STATUS_CD", "1");
	                jobj.put("MESSAGE", "User not found");
	            } else {
	                jresponse.put(jOut);
	                jobj.put("STATUS_CD", "0");
	                jobj.put("RESPONSE", jresponse);
	            }
	            response = jobj.toString();
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Get User : " + e);
	            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	        } finally {
	            try {
	                rs.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                con.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	        }

	        return response;

	    }
	
	    public static String CreateUser(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        int ExistingCnt = 0;
	        try {

	            JSONObject jin = new JSONObject(ls_request);
	            System.out.println(jin.toString());

	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");

	            String COMP_CD = jReuqest.getString("COMP_CD");
	            int REF_ID = jReuqest.getInt("REF_ID");
	            Date INACTIVE_DATE = null;
	            if (jReuqest.getString("INACTIVE_DATE") == null || jReuqest.getString("INACTIVE_DATE").equals("")) {
	            	INACTIVE_DATE = null;
	            } else {
	            	INACTIVE_DATE = Date.valueOf(jReuqest.getString("INACTIVE_DATE")); 
	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }	         	           
	            String ACTIVE_STATUS = jReuqest.getString("ACTIVE_STATUS");	            
	            String EMAIL = jReuqest.getString("EMAIL");
	            int CONTACT1 = 0;
	            if (jReuqest.getString("CONTACT1") == null || jReuqest.getString("CONTACT1").equals("")) {
	            	CONTACT1 = 0;
	            } else {
	            	CONTACT1 = jReuqest.getInt("CONTACT1");
	            }	            
	            Date BLOCKED_DATE = null;
	            if (jReuqest.getString("BLOCKED_DATE") == null || jReuqest.getString("BLOCKED_DATE").equals("")) {
	            	BLOCKED_DATE = null;
	            } else {
	            	BLOCKED_DATE = Date.valueOf(jReuqest.getString("BLOCKED_DATE"));
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
	            }
	            Date ENTERED_DATE = null;
	            if (jReuqest.getString("ENTERED_DATE") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
	            	ENTERED_DATE = null;
	            } else {
	            	ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE"));
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
	            }
	            Date LAST_MODIFIED_DATE = null;
	            if (jReuqest.getString("LAST_MODIFIED_DATE") == null || jReuqest.getString("LAST_MODIFIED_DATE").equals("")) {
	            	LAST_MODIFIED_DATE = null;
	            } else {
	            	LAST_MODIFIED_DATE = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DATE"));
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
	            }
	            int FAILED_CNT = jReuqest.getInt("FAILED_CNT");
	            
	            	
	            String USER_ID = jReuqest.getString("USER_ID");
	            String BLOCK_STATUS = jReuqest.getString("BLOCK_STATUS");
	            String USER_LEVEL_CD = jReuqest.getString("USER_LEVEL_CD");
	            String FAV_QUE_CD = jReuqest.getString("FAV_QUE_CD");
	            String TEMP_PWD = jReuqest.getString("TEMP_PWD");	            	           
	            String USER_PWD = jReuqest.getString("USER_PWD");
	            String ENTERED_BY = jReuqest.getString("ENTERED_BY");
	            String ENTERED_IP_ADD = jReuqest.getString("ENTERED_IP_ADD");
	            String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");
	            String LAST_MODIFIED_IP_ADD = jReuqest.getString("LAST_MODIFIED_IP_ADD");
	            
	            String USER_SIGNATURE = jReuqest.getString("USER_SIGNATURE");
	        	String USER_PHOTO = jReuqest.getString("USER_PHOTO");
	        	
	        	
	        	byte[] USER_SIGNATURE_BYTE = USER_SIGNATURE.getBytes();
	        	byte[] USER_PHOTO_BYTE = USER_PHOTO.getBytes();
	        	
	        	
	        	Blob USER_SIGNATURE_BLOB = new SerialBlob(USER_SIGNATURE_BYTE);
	        	Blob USER_PHOTO_BLOB = new SerialBlob(USER_PHOTO_BYTE);

	            String extingEmp = "SELECT COUNT(*) AS EMP_CNT FROM USER_MST WHERE COMP_CD = '"+COMP_CD+"' AND USER_ID = '"+ USER_ID+"' AND IS_DELETE = 'N'";

	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);

	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("EMP_CNT");
	            }
	            System.out.println(ExistingCnt);
	            if (ExistingCnt > 0) {
	            	 JSONObject jOutPut = new JSONObject();

	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "User ID " + USER_ID + " Already Exists.");
	                 response = jOutPut.toString();
	            } else {
	                JSONObject jOut = new JSONObject();
	                String sql = "INSERT INTO user_mst (COMP_CD, USER_ID, USER_LEVEL_CD, REF_ID, CONTACT1, EMAIL, ACTIVE_STATUS, BLOCK_STATUS, INACTIVE_DATE, BLOCKED_DATE, FAILED_CNT, USER_SIGNATURE, USER_PHOTO, USER_PWD, TEMP_PWD, FAV_QUE_CD, ENTERED_DATE, ENTERED_BY, ENTERED_IP_ADD, LAST_MODIFIED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP_ADD) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	                PreparedStatement preparedStatement = con.prepareStatement(sql);
	                
	                preparedStatement.setString(1,COMP_CD);
	                preparedStatement.setString(2,USER_ID);
	                preparedStatement.setString(3,USER_LEVEL_CD);
	                preparedStatement.setInt(4,REF_ID);
	                preparedStatement.setInt(5,CONTACT1);
	                preparedStatement.setString(6,EMAIL);
	                preparedStatement.setString(7,ACTIVE_STATUS);
	                preparedStatement.setString(8,BLOCK_STATUS);
	                preparedStatement.setDate(9,INACTIVE_DATE);
	                preparedStatement.setDate(10,BLOCKED_DATE);
	                preparedStatement.setInt(11,FAILED_CNT);
	                preparedStatement.setBlob(12,USER_SIGNATURE_BLOB);
	                preparedStatement.setBlob(13,USER_PHOTO_BLOB);
	                preparedStatement.setString(14,USER_PWD);
	                preparedStatement.setString(15,TEMP_PWD);
	                preparedStatement.setString(16,FAV_QUE_CD);
	                preparedStatement.setDate(17,ENTERED_DATE);
	                preparedStatement.setString(18,ENTERED_BY);
	                preparedStatement.setString(19,ENTERED_IP_ADD);
	                preparedStatement.setDate(20,LAST_MODIFIED_DATE);
	                preparedStatement.setString(21,LAST_MODIFIED_BY);
	                preparedStatement.setString(22,LAST_MODIFIED_IP_ADD);

	                int row = preparedStatement.executeUpdate();

	                if (row == 0) {
	                    con.rollback();
	                } else {
	                    con.commit();
	                }

	                JSONObject jOutPut = new JSONObject();

	                jOutPut.put("STATUS_CD", "0");
	                jOutPut.put("MESSAGE", "User ID " + USER_ID + " Sucessfully Created.");
	                response = jOutPut.toString();
	            }
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Create User : " + e);
	            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	        } finally {
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                con.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	        }

	        return response;

	    }
	  
	    public static String deleteUserID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);                                
	            JSONObject jOutPut = new JSONObject();
	            
	            String USER_ID = jin.getString("USER_ID");  
	            String comp_cd= jin.getString("COMP_CD");
	            
	            String ls_del_flag = jin.getString("DELETE_FLAG");
	            
	            if(ls_del_flag.equals("Y")) {
	            	 String extingEmp = "UPDATE USER_MST SET IS_DELETE = 'Y' WHERE USER_ID = '" +USER_ID+"' AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";
	                 System.out.println(extingEmp);
	                 stmt = con.createStatement();
	                 
	                 int row = stmt.executeUpdate(extingEmp);
	                 
	                 if (row > 0) {
	                 	con.commit();
	                 	 jOutPut.put("STATUS_CD", "0");	
	                 	jOutPut.put("MESSAGE", "User ID " + USER_ID + " Sucessfully Deleted.");
	                 } else {
	                 	con.rollback();
	                 	 jOutPut.put("STATUS_CD", "99");	
	                 	jOutPut.put("MESSAGE", "User Id Already Delted.");
	                 }
	                 		
	                
	            } else {
	            	jOutPut.put("STATUS_CD", "99");	
	            	jOutPut.put("MESSAGE", "Delete Not Allow.");
	            }             	                                                        
	            response = jOutPut.toString();
	                                                                                                     
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Delete User : " + e);
	            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	        } finally {
	            try {
	                rs.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                con.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	        }
	        return response;

	    }
	    public static String updateUserID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            JSONObject jOutPut = new JSONObject();
	            
	            String USER_ID = jin.getString("USER_ID"); 
	            String COMP_CD = jin.getString("COMP_CD");
	            
	            String extingEmp = "SELECT COUNT(*) AS EMP_CNT FROM USER_MST WHERE COMP_CD = '"+COMP_CD+"' AND USER_ID = '" +USER_ID+"' AND IS_DELETE = 'N'";	        
	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);
	            		
	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("EMP_CNT");
	            }
	            
	            if (ExistingCnt == 0) {            	
	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "User ID " + USER_ID + " not Exists. Kindly Create First");
	                 response = jOutPut.toString();
	            } else {

	  	            String REF_ID = jReuqest.getString("REF_ID");
	  	            Date INACTIVE_DATE = null;
	  	            if (jReuqest.getString("INACTIVE_DATE") == null || jReuqest.getString("INACTIVE_DATE").equals("")) {
	  	            	INACTIVE_DATE = null;
	  	            } else {
	  	            	INACTIVE_DATE = Date.valueOf(jReuqest.getString("INACTIVE_DATE")); 
	  	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	  	            }	         	           
	  	            String ACTIVE_STATUS = jReuqest.getString("ACTIVE_STATUS");	  	            
	  	            String EMAIL = jReuqest.getString("EMAIL");
	  	            int CONTACT1 = 0;
	  	            if (jReuqest.getString("CONTACT1") == null || jReuqest.getString("CONTACT1").equals("")) {
	  	            	CONTACT1 = 0;
	  	            } else {
	  	            	CONTACT1 = jReuqest.getInt("CONTACT1");
	  	            }	            
	  	            Date BLOCKED_DATE = null;
	  	            if (jReuqest.getString("BLOCKED_DATE") == null || jReuqest.getString("BLOCKED_DATE").equals("")) {
	  	            	BLOCKED_DATE = null;
	  	            } else {
	  	            	BLOCKED_DATE = Date.valueOf(jReuqest.getString("BLOCKED_DATE"));
	  	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
	  	            }	  	           
	  	            Date LAST_MODIFIED_DATE = null;
	  	            if (jReuqest.getString("LAST_MODIFIED_DATE") == null || jReuqest.getString("LAST_MODIFIED_DATE").equals("")) {
	  	            	LAST_MODIFIED_DATE = null;
	  	            } else {
	  	            	LAST_MODIFIED_DATE = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DATE"));
	  	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
	  	            }
	  	            String FAILED_CNT = jReuqest.getString("FAILED_CNT");
	  	            String BLOCK_STATUS = jReuqest.getString("BLOCK_STATUS");
	  	            String USER_LEVEL_CD = jReuqest.getString("USER_LEVEL_CD");
	  	            String FAV_QUE_CD = jReuqest.getString("FAV_QUE_CD");
	  	            String TEMP_PWD = jReuqest.getString("TEMP_PWD");
	  	            
	  	            String USER_SIGNATURE = jReuqest.getString("USER_SIGNATURE");
	  	            String USER_PHOTO = jReuqest.getString("USER_PHOTO");
	  	            
	  	            byte[] USER_SIGNATURE_BYTE = USER_SIGNATURE.getBytes();
		        	byte[] USER_PHOTO_BYTE = USER_PHOTO.getBytes();
		        	
		        	
		        	Blob USER_SIGNATURE_BLOB = new SerialBlob(USER_SIGNATURE_BYTE);
		        	Blob USER_PHOTO_BLOB = new SerialBlob(USER_PHOTO_BYTE);
		        	
	  	            String USER_PWD = jReuqest.getString("USER_PWD");	  	            
	  	            String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");	  	          
	  	            String LAST_MODIFIED_IP_ADD = jReuqest.getString("LAST_MODIFIED_IP_ADD"); 
	  	            String PASSWORD_CHANGE_FLAG = jReuqest.getString("PASSWORD_CHANGE_FLAG");
	  	            
	  	            if (PASSWORD_CHANGE_FLAG.equals("Y")) {
	  	            	String updateEmployee = "UPDATE user_mst SET COMP_CD = ?,USER_LEVEL_CD = ?,REF_ID = ?,CONTACT1 = ?,EMAIL = ?,ACTIVE_STATUS = ?,\r\n" + 
		                		"BLOCK_STATUS = ?,INACTIVE_DATE = ?,BLOCKED_DATE = ?,USER_SIGNATURE = ?,USER_PHOTO = ?,USER_PWD = ?,TEMP_PWD = ?,FAV_QUE_CD = ?,LAST_MODIFIED_DATE = ?,LAST_MODIFIED_BY = ?,LAST_MODIFIED_IP_ADD = ?\r\n" + 
		                		"WHERE COMP_CD = ? AND USER_ID = ? AND IS_DELETE = 'N'";	
		                PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
		                preparedStatement.setString(1,COMP_CD);	                
		                preparedStatement.setString(2,USER_LEVEL_CD );
		                preparedStatement.setInt(3,Integer.parseInt(REF_ID));	                
		                preparedStatement.setInt(4,CONTACT1);
		                preparedStatement.setString(5,EMAIL);	                
		                preparedStatement.setString(6,ACTIVE_STATUS );
		                preparedStatement.setString(7,BLOCK_STATUS );
		                preparedStatement.setDate(8,INACTIVE_DATE );	                
		                preparedStatement.setDate(9,BLOCKED_DATE );
		                preparedStatement.setBlob(10,USER_SIGNATURE_BLOB );
		                preparedStatement.setBlob(11,USER_PHOTO_BLOB );
		                preparedStatement.setString(12,USER_PWD );
		                preparedStatement.setString(13,TEMP_PWD );
		                preparedStatement.setString(14,FAV_QUE_CD );
		                preparedStatement.setDate(15,LAST_MODIFIED_DATE );
		                preparedStatement.setString(16,LAST_MODIFIED_BY );
		                preparedStatement.setString(17,LAST_MODIFIED_IP_ADD);
		                
		                preparedStatement.setString(18,COMP_CD);
		                preparedStatement.setString(19,USER_ID);
		                
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Employee ID " + USER_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            } else {
	  	            	String updateEmployee = "UPDATE user_mst SET COMP_CD = ?,USER_LEVEL_CD = ?,REF_ID = ?,CONTACT1 = ?,EMAIL = ?,ACTIVE_STATUS = ?,\r\n" + 
		                		"BLOCK_STATUS = ?,INACTIVE_DATE = ?,BLOCKED_DATE = ?,USER_SIGNATURE = ?,USER_PHOTO = ?,FAV_QUE_CD = ?,LAST_MODIFIED_DATE = ?,LAST_MODIFIED_BY = ?,LAST_MODIFIED_IP_ADD = ?\r\n" + 
		                		"WHERE COMP_CD = ? AND USER_ID = ? AND IS_DELETE = 'N'";	
	  	            	
		                PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
		                preparedStatement.setString(1,COMP_CD);	                
		                preparedStatement.setString(2,USER_LEVEL_CD );
		                preparedStatement.setInt(3,Integer.parseInt(REF_ID));	                
		                preparedStatement.setInt(4,CONTACT1);
		                preparedStatement.setString(5,EMAIL);	                
		                preparedStatement.setString(6,ACTIVE_STATUS );
		                preparedStatement.setString(7,BLOCK_STATUS );
		                preparedStatement.setDate(8,INACTIVE_DATE );	                
		                preparedStatement.setDate(9,BLOCKED_DATE );
		                preparedStatement.setString(10,USER_SIGNATURE );
		                preparedStatement.setString(11,USER_PHOTO );
		                preparedStatement.setString(12,FAV_QUE_CD );
		                preparedStatement.setDate(13,LAST_MODIFIED_DATE );
		                preparedStatement.setString(14,LAST_MODIFIED_BY );
		                preparedStatement.setString(15,LAST_MODIFIED_IP_ADD);
		                
		                preparedStatement.setString(16,COMP_CD);
		                preparedStatement.setString(17,USER_ID);
		                
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Employee ID " + USER_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            }	  	            	                        	               	               
	            }                                                                                              
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Update Employee : " + e);
	            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	        } finally {
	            try {
	                rs.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                stmt.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	            try {
	                con.close();
	            } catch (Exception e2) {
	                // TODO: handle exception
	            }
	        }
	        return response;

	    }
	 
}
