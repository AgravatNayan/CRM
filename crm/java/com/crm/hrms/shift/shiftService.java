package com.crm.hrms.shift;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.crm.utility.NVL;
import com.crm.utility.Utility;

public class shiftService {
	
	public static String GetMaxShiftID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(SHIFT_ID) AS SHIFT_ID FROM shift_config_mst";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("SHIFT_ID");
            }
            maxID = maxID + 1;
            JSONArray jresponse = new JSONArray();
            JSONObject jobj = new JSONObject();
            jobj.put("USERNAME", jin.getString("USERNAME"));
            jobj.put("SHIFT_ID", maxID);
            jresponse.put(jobj);
            jOut.put("STATUS_CD", "0");
            jOut.put("RESPONSE", jresponse);
            response = jOut.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in GetMax Shift Id : " + e);
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
	
	public static String getShiftList(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {	   
		
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();
	    String response = "";
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    String COMP_CD = request.getString("COMP_CD");
	    String viewType = NVL.StringNvl(request.getString("VIEW_FLAG"));
	    String ls_query = null;
	   try {	
		   if (viewType.equals("G")) {
	            ls_query = "SELECT COMP_CD,SHIFT_ID,SHIFT_NAME,SHIFT_STATUS,SHIFT_INACTIVE_DT,SHIFT_START_TIME,SHIFT_END_TIME,REMARKS FROM shift_config_mst WHERE COMP_CD = '"+COMP_CD+"' AND IS_DELETE = 'N'";
		   } else {
			   ls_query = "SELECT COMP_CD,SHIFT_ID,SHIFT_NAME,SHIFT_STATUS,SHIFT_INACTIVE_DTSHIFT_START_TIME,SHIFT_END_TIME,REMARKS FROM shift_config_mst WHERE COMP_CD = '"+COMP_CD+"' AND IS_DELETE = 'N' AND  ENTERED_BY = '"+ls_username+"'";			   
		   }	
		   		System.out.println(ls_query);
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {	            
	            	jObject = new JSONObject();
	            	jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
	            	jObject.put("SHIFT_ID", NVL.StringNvl(rs.getString("SHIFT_ID")));
	            	jObject.put("SHIFT_NAME", NVL.StringNvl(rs.getString("SHIFT_NAME")));
	            	jObject.put("SHIFT_STATUS", NVL.StringNvl(rs.getString("SHIFT_STATUS")));
	            	jObject.put("SHIFT_INACTIVE_DT", NVL.StringNvl(rs.getString("SHIFT_INACTIVE_DT")));
	            	jObject.put("SHIFT_START_TIME", NVL.StringNvl(rs.getString("SHIFT_START_TIME")));
	            	jObject.put("SHIFT_END_TIME", NVL.StringNvl(rs.getString("SHIFT_END_TIME")));
	            	jObject.put("REMARKS", NVL.StringNvl(rs.getString("REMARKS")));	            
	            	jArray.put(jObject);
	            }
	            if (jArray.length() <= 0) {
	            	mainObject.put("STATUS_CD", "99");
	            	mainObject.put("MESSAGE", "Shift Type not found");
	            } else {
	            	 mainObject.put("STATUS_CD", "0");
	 	            mainObject.put("RESPONSE", jArray);	            	
	            }	            	           
	            response = mainObject.toString();
	          } catch(Exception e) {	            
	        	  e.printStackTrace();
	        	  Utility.PrintMessage("Error in Get Shift List : " + e);
		          response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	           
	          }	       
	    return response;
	  }
		
	 public static String GetShiftID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ll_cnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);
	            
	    		
	            String SHIFT_ID = NVL.StringNvl(jin.getString("SHIFT_ID"));
	            String viewFlag = NVL.StringNvl(jin.getString("VIEW_FLAG"));
	            String userName = NVL.StringNvl(jin.getString("USERNAME"));
	            String COMP_CD = NVL.StringNvl(jin.getString("COMP_CD"));

	            JSONObject jOut = new JSONObject();
	            String sql = null;
	            if (viewFlag.equals("G")) {
	                sql = "SELECT COMP_CD,SHIFT_ID,SHIFT_NAME,SHIFT_STATUS,SHIFT_INACTIVE_DT,SHIFT_START_TIME,SHIFT_END_TIME,REMARKS FROM shift_config_mst WHERE COMP_CD = '"+COMP_CD+"' AND IS_DELETE = 'N' AND SHIFT_ID = "+SHIFT_ID+"";
	            } else {
	                sql = "SELECT COMP_CD,SHIFT_ID,SHIFT_NAME,SHIFT_STATUS,SHIFT_INACTIVE_DTSHIFT_START_TIME,SHIFT_END_TIME,REMARKS FROM shift_config_mst WHERE COMP_CD = '"+COMP_CD+"' AND IS_DELETE = 'N' AND  ENTERED_BY = '"+userName+"' AND SHIFT_ID = "+SHIFT_ID+"";

	            }
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                ll_cnt = ll_cnt + 1;
	      			jOut = new JSONObject();
	      			jOut.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
	      			jOut.put("SHIFT_ID", NVL.StringNvl(rs.getString("SHIFT_ID")));
	      			jOut.put("SHIFT_NAME", NVL.StringNvl(rs.getString("SHIFT_NAME")));
	      			jOut.put("SHIFT_STATUS", NVL.StringNvl(rs.getString("SHIFT_STATUS")));
	      			jOut.put("SHIFT_INACTIVE_DT", NVL.StringNvl(rs.getString("SHIFT_INACTIVE_DT")));
	      			jOut.put("SHIFT_START_TIME", NVL.StringNvl(rs.getString("SHIFT_START_TIME")));
	            	jOut.put("SHIFT_END_TIME", NVL.StringNvl(rs.getString("SHIFT_END_TIME")));
	            	jOut.put("REMARKS", NVL.StringNvl(rs.getString("REMARKS")));	              	                	                           
	            }
	            JSONArray jresponse = new JSONArray();
	            JSONObject jobj = new JSONObject();
	            jobj.put("STATUS_CD", "0");
	            if (jOut.length() <= 0) {
	                jobj.put("STATUS_CD", "1");
	                jobj.put("MESSAGE", "Shift Type not found");
	            } else {
	                jresponse.put(jOut);
	                jobj.put("STATUS_CD", "0");
	                jobj.put("RESPONSE", jresponse);
	            }
	            response = jobj.toString();
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Get Shift : " + e);
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
	
	    public static String CreateShift(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        int ExistingCnt = 0;
	        try {

	            JSONObject jin = new JSONObject(ls_request);
	            System.out.println(jin.toString());

	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            
	            String COMP_CD = NVL.StringNvl(jReuqest.getString("COMP_CD"));
	            int SHIFT_ID =0;
	            if (jReuqest.getString("SHIFT_ID") == null || jReuqest.getString("SHIFT_ID").equals("")) {
	            	SHIFT_ID = 0;
	            } else {
	            	SHIFT_ID = jReuqest.getInt("SHIFT_ID");
	            }		            	            
	            String SHIFT_NAME = NVL.StringNvl(jReuqest.getString("SHIFT_NAME"));	            
	            String SHIFT_STATUS  = NVL.StringNvl(jReuqest.getString("SHIFT_STATUS"));	            	            
	            Date SHIFT_INACTIVE_DT = null;
	            if (jReuqest.getString("SHIFT_INACTIVE_DT") == null || jReuqest.getString("SHIFT_INACTIVE_DT").equals("")) {
	            	SHIFT_INACTIVE_DT = null;
	            } else {
	            	SHIFT_INACTIVE_DT = Date.valueOf(jReuqest.getString("SHIFT_INACTIVE_DT")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            Time SHIFT_START_TIME = null;
	            if (jReuqest.getString("SHIFT_START_TIME") == null || jReuqest.getString("SHIFT_START_TIME").equals("")) {
	            	SHIFT_START_TIME = null;
	            } else {
	            	SHIFT_START_TIME = Time.valueOf(jReuqest.getString("SHIFT_START_TIME")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            Time SHIFT_END_TIME = null;
	            if (jReuqest.getString("SHIFT_END_TIME") == null || jReuqest.getString("SHIFT_END_TIME").equals("")) {
	            	SHIFT_END_TIME = null;
	            } else {
	            	SHIFT_END_TIME = Time.valueOf(jReuqest.getString("SHIFT_END_TIME")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String REMARKS  = NVL.StringNvl(jReuqest.getString("REMARKS"));	            
	            String ENTERED_BY =NVL.StringNvl(jReuqest.getString("ENTERED_BY"));
	            String ENTERED_IP =NVL.StringNvl(jReuqest.getString("ENTERED_IP"));
	            Date ENTERED_DATE =null;
	            if (jReuqest.getString("ENTERED_DATE") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
	            	ENTERED_DATE = null;
	            } else {
	            	ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String LAST_MODIFIED_BY =NVL.StringNvl(jReuqest.getString("LAST_MODIFIED_BY"));
	            String LAST_MODOFIED_IP =NVL.StringNvl(jReuqest.getString("LAST_MODOFIED_IP"));
	            Date LAST_MODIFIED_DT =null;
	            if (jReuqest.getString("LAST_MODIFIED_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
	            	LAST_MODIFIED_DT = null;
	            } else {
	            	LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }	          
	            	                   		        		                    	   
                JSONObject jOut = new JSONObject();
                String sql = "INSERT INTO shift_config_mst(COMP_CD,SHIFT_ID,SHIFT_NAME,SHIFT_STATUS,SHIFT_INACTIVE_DT,SHIFT_START_TIME,SHIFT_END_TIME,REMARKS,ENTERED_BY,ENTERED_IP,ENTERED_DATE,LAST_MODIFIED_BY,LAST_MODOFIED_IP,LAST_MODIFIED_DT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                              
                preparedStatement.setString(1,COMP_CD);
                preparedStatement.setInt(2,SHIFT_ID);
                preparedStatement.setString(3,SHIFT_NAME);
                preparedStatement.setString(4,SHIFT_STATUS);
                preparedStatement.setDate(5,SHIFT_INACTIVE_DT);
                preparedStatement.setTime(6,SHIFT_START_TIME);
                preparedStatement.setTime(7,SHIFT_END_TIME);
                preparedStatement.setString(8,REMARKS);
	            preparedStatement.setString(9,ENTERED_BY);
	            preparedStatement.setString(10,ENTERED_IP);
	            preparedStatement.setDate(11,ENTERED_DATE);
	            preparedStatement.setString(12,LAST_MODIFIED_BY);
	            preparedStatement.setString(13,LAST_MODOFIED_IP);
	            preparedStatement.setDate(14,LAST_MODIFIED_DT);

                int row = preparedStatement.executeUpdate();

                if (row == 0) {
                    con.rollback();
                } else {
                    con.commit();
                }

                JSONObject jOutPut = new JSONObject();

                jOutPut.put("STATUS_CD", "0");
                jOutPut.put("MESSAGE", "Shift Type " + SHIFT_ID + " Sucessfully Created.");
                response = jOutPut.toString();
	            } catch (Exception e) {
	            e.printStackTrace();
	            Utility.PrintMessage("Error in Create Shift Type : " + e.getMessage());
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
	  
	    
	    public static String deleteShiftID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);                                
	            JSONObject jOutPut = new JSONObject();
	            
	            String SHIFT_ID = jin.getString("SHIFT_ID");  
	            String comp_cd= jin.getString("COMP_CD");
	           	           
	            String ls_del_flag = jin.getString("DELETE_FLAG");
	            
	            if(ls_del_flag.equals("Y")) {
	            	 String extingEmp = "UPDATE shift_config_mst SET IS_DELETE = 'Y' WHERE SHIFT_ID = " +SHIFT_ID+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
	                 stmt = con.createStatement();
	                 
	                 int row = stmt.executeUpdate(extingEmp);
	                 
	                 if (row > 0) {
	                 	con.commit();
	                 	 jOutPut.put("STATUS_CD", "0");	
	                 	jOutPut.put("MESSAGE", "Shift ID " + SHIFT_ID + " Sucessfully Deleted.");
	                 } else {
	                 	con.rollback();
	                 	 jOutPut.put("STATUS_CD", "99");	
	                 	jOutPut.put("MESSAGE", "Shift Type Already Deleted.");
	                 }	                 			                
	            } else {
	            	jOutPut.put("STATUS_CD", "99");	
	            	jOutPut.put("MESSAGE", "Delete Not Allow.");
	            }             	                                                        
	            response = jOutPut.toString();
	                                                                                                     
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Delete Shift : " + e);
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
	    public static String updateShiftID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            JSONObject jOutPut = new JSONObject();
	            
	            int SHIFT_ID = jin.getInt("SHIFT_ID"); 
	            String COMP_CD = jin.getString("COMP_CD");
	            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
	            String USERNAME = jin.getString("USERNAME");
	            String extingEmp = "SELECT COUNT(*) AS INSU_ID FROM shift_config_mst WHERE COMP_CD = '"+COMP_CD+"' AND SHIFT_ID = '" +SHIFT_ID+"' AND IS_DELETE = 'N'";	        
	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);
	            		
	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("INSU_ID");
	            }
	            
	            if (ExistingCnt == 0) {            	
	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "Shift Id " + SHIFT_ID + " not Exists. Kindly Create First");
	                 response = jOutPut.toString();
	            } else {	  	            
	            	
		           
		            String SHIFT_NAME = NVL.StringNvl(jReuqest.getString("SHIFT_NAME"));	            
		            String SHIFT_STATUS  = NVL.StringNvl(jReuqest.getString("SHIFT_STATUS"));	            	            
		            Date SHIFT_INACTIVE_DT = null;
		            if (jReuqest.getString("SHIFT_INACTIVE_DT") == null || jReuqest.getString("SHIFT_INACTIVE_DT").equals("")) {
		            	SHIFT_INACTIVE_DT = null;
		            } else {
		            	SHIFT_INACTIVE_DT = Date.valueOf(jReuqest.getString("SHIFT_INACTIVE_DT")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            Time SHIFT_START_TIME = null;
		            if (jReuqest.getString("SHIFT_START_TIME") == null || jReuqest.getString("SHIFT_START_TIME").equals("")) {
		            	SHIFT_START_TIME = null;
		            } else {
		            	SHIFT_START_TIME = Time.valueOf(jReuqest.getString("SHIFT_START_TIME")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            Time SHIFT_END_TIME = null;
		            if (jReuqest.getString("SHIFT_END_TIME") == null || jReuqest.getString("SHIFT_END_TIME").equals("")) {
		            	SHIFT_END_TIME = null;
		            } else {
		            	SHIFT_END_TIME = Time.valueOf(jReuqest.getString("SHIFT_END_TIME")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            String REMARKS  = NVL.StringNvl(jReuqest.getString("REMARKS"));	            		            
		            String LAST_MODIFIED_BY =NVL.StringNvl(jReuqest.getString("LAST_MODIFIED_BY"));
		            String LAST_MODOFIED_IP =NVL.StringNvl(jReuqest.getString("LAST_MODOFIED_IP"));
		            Date LAST_MODIFIED_DT =null;
		            if (jReuqest.getString("LAST_MODIFIED_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
		            	LAST_MODIFIED_DT = null;
		            } else {
		            	LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }		            		            	  	            	  	       
	  	            if (UPDATE_FLAG.equals("Y")) {
	  	            	String updateEmployee = "UPDATE shift_config_mst SET COMP_CD = ?,SHIFT_NAME = ?,SHIFT_STATUS = ?,SHIFT_INACTIVE_DT = ?,SHIFT_START_TIME =?,SHIFT_END_TIME = ?,REMARKS = ?,LAST_MODIFIED_BY = ?,LAST_MODOFIED_IP = ?,LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND SHIFT_ID = ? AND IS_DELETE = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,COMP_CD);
	  	                preparedStatement.setString(2,SHIFT_NAME);
	  	                preparedStatement.setString(3,SHIFT_STATUS);
	  	                preparedStatement.setDate(4,SHIFT_INACTIVE_DT);
	  	                preparedStatement.setTime(5,SHIFT_START_TIME);
	  	                preparedStatement.setTime(6,SHIFT_END_TIME);
	  	                preparedStatement.setString(7,REMARKS);
	  		            preparedStatement.setString(8,LAST_MODIFIED_BY);
	  		            preparedStatement.setString(9,LAST_MODOFIED_IP);
	  		            preparedStatement.setDate(10,LAST_MODIFIED_DT);
	  		            
	  		            preparedStatement.setString(11,COMP_CD);
	  		            preparedStatement.setInt(12,SHIFT_ID);
	  		            preparedStatement.setString(13,"N");
	  		            
	  		            
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Shift ID " + SHIFT_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            } else {
	  	            	String updateEmployee = "UPDATE shift_config_mst SET COMP_CD = ?,SHIFT_NAME = ?,SHIFT_STATUS = ?,SHIFT_INACTIVE_DT = ?,SHIFT_START_TIME =?,SHIFT_END_TIME = ?,REMARKS = ?,LAST_MODIFIED_BY = ?,LAST_MODOFIED_IP = ?,LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND SHIFT_ID = ? AND IS_DELETE = ? AND ENTERED_BY = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,COMP_CD);
	  	                preparedStatement.setString(2,SHIFT_NAME);
	  	                preparedStatement.setString(3,SHIFT_STATUS);
	  	                preparedStatement.setDate(4,SHIFT_INACTIVE_DT);
	  	                preparedStatement.setTime(5,SHIFT_START_TIME);
	  	                preparedStatement.setTime(6,SHIFT_END_TIME);
	  	                preparedStatement.setString(7,REMARKS);
	  		            preparedStatement.setString(8,LAST_MODIFIED_BY);
	  		            preparedStatement.setString(9,LAST_MODOFIED_IP);
	  		            preparedStatement.setDate(10,LAST_MODIFIED_DT);
	  		            
	  		            preparedStatement.setString(11,COMP_CD);
	  		            preparedStatement.setInt(12,SHIFT_ID);
	  		            preparedStatement.setString(13,"N");
	  		            preparedStatement.setString(14,USERNAME);

		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Shift ID " + SHIFT_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            }	  	            	                        	               	               
	            }                                                                                              
	        } catch (Exception e) {
	        	e.printStackTrace();
	            Utility.PrintMessage("Error in Update Shift : " + e);
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
