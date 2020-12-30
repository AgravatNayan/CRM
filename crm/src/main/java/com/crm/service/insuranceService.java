package com.crm.service;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.crm.utility.NVL;
import com.crm.utility.Utility;
import com.crm.utility.getCategoryValue;

public class insuranceService {
	
	public static String GetMaxInsuID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(INSU_ID) AS INSU_ID FROM insurance_mst";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("INSU_ID");
            }
            maxID = maxID + 1;
            JSONArray jresponse = new JSONArray();
            JSONObject jobj = new JSONObject();
            jobj.put("USERNAME", jin.getString("USERNAME"));
            jobj.put("CONTRACT_ID", maxID);
            jresponse.put(jobj);
            jOut.put("STATUS_CD", "0");
            jOut.put("RESPONSE", jresponse);
            response = jOut.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in GetMax Insurance Id : " + e);
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
	
	public static String getInsuList(Connection con, String requestData) throws JSONException,
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
	            ls_query = "SELECT COMP_CD,INSU_ID,INSU_TYPE,INSU_NAME,INSU_START_DT,INSU_END_DT,INSU_STATUS,INSU_INACTIVE_DT,INSU_AGENCY,INSU_AGENCY_ID,INSU_CUST_NO,INSU_PREMIUM,INSU_DOC_POLICY FROM insurance_mst WHERE COMP_CD = '"+COMP_CD+"' AND INSU_STATUS = 'Y' AND IS_DELETE = 'N'";
		   } else {
			   ls_query = "SELECT COMP_CD,INSU_ID,INSU_TYPE,INSU_NAME,INSU_START_DT,INSU_END_DT,INSU_STATUS,INSU_INACTIVE_DT,INSU_AGENCY,INSU_AGENCY_ID,INSU_CUST_NO,INSU_PREMIUM,INSU_DOC_POLICY FROM insurance_mst WHERE COMP_CD = '"+COMP_CD+"' AND INSU_STATUS = 'Y'  AND ENTERED_BY= '"+ls_username+"' AND IS_DELETE = 'N'";			   
		   }		   
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {
	            
	            	jObject = new JSONObject();
	            	jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
	            	jObject.put("INSU_ID", NVL.StringNvl(rs.getString("INSU_ID")));
	            	jObject.put("INSU_TYPE", NVL.StringNvl(rs.getString("INSU_TYPE")));	            	
	            	jObject.put("ID", NVL.StringNvl(rs.getString("INSU_ID")));
	            	jObject.put("NAME", NVL.StringNvl(rs.getString("INSU_NAME")));	            	
	            	jObject.put("INSU_NAME", NVL.StringNvl(rs.getString("INSU_NAME")));
	            	jObject.put("INSU_START_DT", NVL.StringNvl(rs.getString("INSU_START_DT")));
	            	jObject.put("INSU_END_DT", NVL.StringNvl(rs.getString("INSU_END_DT")));
	            	jObject.put("INSU_STATUS", NVL.StringNvl(rs.getString("INSU_STATUS")));
	            	jObject.put("INSU_INACTIVE_DT", NVL.StringNvl(rs.getString("INSU_INACTIVE_DT")));
	            	jObject.put("INSU_AGENCY", NVL.StringNvl(rs.getString("INSU_AGENCY")));
	            	jObject.put("INSU_AGENCY_ID", NVL.StringNvl(rs.getString("INSU_AGENCY_ID")));
	            	jObject.put("INSU_CUST_NO", NVL.StringNvl(rs.getString("INSU_CUST_NO")));
	            	jObject.put("INSU_PREMIUM", NVL.StringNvl(rs.getString("INSU_PREMIUM")));
	            	jObject.put("INSU_DOC_POLICY", NVL.StringNvl(rs.getString("INSU_DOC_POLICY")));
	              jArray.put(jObject);
	            }
	            if (jArray.length() <= 0) {
	            	mainObject.put("STATUS_CD", "99");
	            	mainObject.put("MESSAGE", "Insurance Type not found");
	            } else {
	            	 mainObject.put("STATUS_CD", "0");
	 	            mainObject.put("RESPONSE", jArray);	            	
	            }	            	           
	            response = mainObject.toString();
	          } catch(Exception e) {	            
	        	  e.printStackTrace();
	        	  Utility.PrintMessage("Error in Get Insurance List : " + e);
		          response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	           
	          }	       
	    return response;
	  }
	
	 public static String GetInsuID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ll_cnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);
	            String ls_comp_cd  = null;
	    		String ls_branch_cd = null;
	    		String ID  = null;
	    		String ld_start_dt  = null;
	    		String ld_end_dt  = null;
	    		String ll_sal_temp_id  = null;
	    		String ls_con_status  = null;
	    		String ld_inactive_dt  = null;
	    		String ls_con_nm  = null;
	    		
	            String INSU_ID = NVL.StringNvl(jin.getString("INSURANCE_ID"));
	            String viewFlag = NVL.StringNvl(jin.getString("VIEW_FLAG"));
	            String userName = NVL.StringNvl(jin.getString("USERNAME"));
	            String COMP_CD = NVL.StringNvl(jin.getString("COMP_CD"));

	            JSONObject jOut = new JSONObject();
	            String sql = null;
	            if (viewFlag.equals("G")) {
	                sql = "SELECT COMP_CD,INSU_ID,INSU_TYPE,INSU_NAME,INSU_START_DT,INSU_END_DT,INSU_STATUS,INSU_INACTIVE_DT,INSU_AGENCY,INSU_AGENCY_ID,INSU_CUST_NO,INSU_PREMIUM,INSU_DOC_POLICY FROM insurance_mst WHERE COMP_CD = '"+COMP_CD+"' AND INSU_ID = "+INSU_ID+" AND IS_DELETE = 'N'";
	            } else {
	                sql = "SELECT COMP_CD,INSU_ID,INSU_TYPE,INSU_NAME,INSU_START_DT,INSU_END_DT,INSU_STATUS,INSU_INACTIVE_DT,INSU_AGENCY,INSU_AGENCY_ID,INSU_CUST_NO,INSU_PREMIUM,INSU_DOC_POLICY FROM insurance_mst WHERE COMP_CD = '"+COMP_CD+"' AND INSU_ID = "+INSU_ID+" AND IS_DELETE = 'N' AND ENTERED_BY = '"+userName+"'";

	            }
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                ll_cnt = ll_cnt + 1;

	      			jOut = new JSONObject();
	      			jOut.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
	      			jOut.put("INSU_ID", NVL.StringNvl(rs.getString("INSU_ID")));
	      			jOut.put("INSU_TYPE", NVL.StringNvl(rs.getString("INSU_TYPE")));
	      			jOut.put("INSU_NAME", NVL.StringNvl(rs.getString("INSU_NAME")));
	      			jOut.put("INSU_START_DT", NVL.StringNvl(rs.getString("INSU_START_DT")));
	      			jOut.put("INSU_END_DT", NVL.StringNvl(rs.getString("INSU_END_DT")));
	      			jOut.put("INSU_STATUS", NVL.StringNvl(rs.getString("INSU_STATUS")));
	      			jOut.put("INSU_INACTIVE_DT", NVL.StringNvl(rs.getString("INSU_INACTIVE_DT")));
	      			jOut.put("INSU_AGENCY", NVL.StringNvl(rs.getString("INSU_AGENCY")));
	      			jOut.put("INSU_AGENCY_ID", NVL.StringNvl(rs.getString("INSU_AGENCY_ID")));
	      			jOut.put("INSU_CUST_NO", NVL.StringNvl(rs.getString("INSU_CUST_NO")));
	      			jOut.put("INSU_PREMIUM", NVL.StringNvl(rs.getString("INSU_PREMIUM")));
	      			jOut.put("INSU_DOC_POLICY", NVL.StringNvl(rs.getString("INSU_DOC_POLICY")));              	                	                           
	            }

	            JSONArray jresponse = new JSONArray();
	            JSONObject jobj = new JSONObject();
	            jobj.put("STATUS_CD", "0");
	            if (jOut.length() <= 0) {
	                jobj.put("STATUS_CD", "1");
	                jobj.put("MESSAGE", "Insurance Type not found");
	            } else {
	                jresponse.put(jOut);
	                jobj.put("STATUS_CD", "0");
	                jobj.put("RESPONSE", jresponse);
	            }
	            response = jobj.toString();
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Get Insurance : " + e);
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
	
	    public static String CreateInsu(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        int ExistingCnt = 0;
	        try {

	            JSONObject jin = new JSONObject(ls_request);
	            System.out.println(jin.toString());

	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            
	            String COMP_CD = NVL.StringNvl(jReuqest.getString("COMP_CD"));
	            int INSU_ID =0;
	            if (jReuqest.getString("INSU_ID") == null || jReuqest.getString("INSU_ID").equals("")) {
	            	INSU_ID = 0;
	            } else {
	            	INSU_ID = jReuqest.getInt("INSU_ID");
	            }	
	            String INSU_TYPE = NVL.StringNvl(jReuqest.getString("INSU_TYPE"));
	            String INSU_NAME  = NVL.StringNvl(jReuqest.getString("INSU_NAME"));
	            Date INSU_START_DT = null;
	            if (jReuqest.getString("INSU_START_DT") == null || jReuqest.getString("INSU_START_DT").equals("")) {
	            	INSU_START_DT = null;
	            } else {
	            	INSU_START_DT = Date.valueOf(jReuqest.getString("INSU_START_DT")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            Date INSU_END_DT =null;
	            if (jReuqest.getString("INSU_END_DT") == null || jReuqest.getString("INSU_END_DT").equals("")) {
	            	INSU_START_DT = null;
	            } else {
	            	INSU_END_DT = Date.valueOf(jReuqest.getString("INSU_END_DT")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String INSU_STATUS =NVL.StringNvl(jReuqest.getString("INSU_STATUS"));
	            Date INSU_INACTIVE_DT =null;
	            if (jReuqest.getString("INSU_INACTIVE_DT") == null || jReuqest.getString("INSU_INACTIVE_DT").equals("")) {
	            	INSU_START_DT = null;
	            } else {
	            	INSU_END_DT = Date.valueOf(jReuqest.getString("INSU_INACTIVE_DT")); 
	            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String INSU_AGENCY =NVL.StringNvl(jReuqest.getString("INSU_AGENCY"));
	            double INSU_PREMIUM =0;
	            if (jReuqest.getString("INSU_PREMIUM") == null || jReuqest.getString("INSU_PREMIUM").equals("")) {
	            	INSU_PREMIUM = 0;
	            } else {
	            	INSU_PREMIUM = jReuqest.getInt("INSU_PREMIUM");
	            }	            
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
	            String INSU_AGENCY_ID = NVL.StringNvl(jReuqest.getString("INSU_AGENCY_ID"));
	            int INSU_CUST_NO = 0; 
	            if (jReuqest.getString("INSU_CUST_NO") == null || jReuqest.getString("INSU_CUST_NO").equals("")) {
	            	INSU_CUST_NO = 0;
	            } else {
	            	INSU_CUST_NO = jReuqest.getInt("INSU_CUST_NO");
	            }	
	            
	            String INSU_DOC_POLICY = NVL.StringNvl(jReuqest.getString("INSU_DOC_POLICY")); 	            
	         	byte[] INSU_DOC_POLICY_BYTE = INSU_DOC_POLICY.getBytes();
	        	Blob INSU_DOC_POLICY_BLOB = new SerialBlob(INSU_DOC_POLICY_BYTE);	        		        		                    	    
                JSONObject jOut = new JSONObject();
                String sql = "INSERT INTO insurance_mst (COMP_CD, INSU_ID, INSU_TYPE, INSU_NAME, INSU_START_DT, INSU_END_DT, INSU_STATUS, INSU_INACTIVE_DT, INSU_AGENCY, INSU_AGENCY_ID,INSU_CUST_NO,INSU_PREMIUM, INSU_DOC_POLICY, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_MODIFIED_BY, LAST_MODOFIED_IP, LAST_MODIFIED_DT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                              
                preparedStatement.setString(1,COMP_CD);
                preparedStatement.setInt(2,INSU_ID);
                preparedStatement.setString(3,INSU_TYPE);
                preparedStatement.setString(4,INSU_NAME);
                preparedStatement.setDate(5,INSU_START_DT);
                preparedStatement.setDate(6,INSU_END_DT);
                preparedStatement.setString(7,INSU_STATUS);
                preparedStatement.setDate(8,INSU_INACTIVE_DT);
                preparedStatement.setString(9,INSU_AGENCY);
                preparedStatement.setString(10,INSU_AGENCY_ID);
                preparedStatement.setInt(11,INSU_CUST_NO);
                preparedStatement.setDouble(12, INSU_PREMIUM);                
                preparedStatement.setBlob(13,INSU_DOC_POLICY_BLOB);
	            preparedStatement.setString(14,ENTERED_BY);
	            preparedStatement.setString(15,ENTERED_IP);
	            preparedStatement.setDate(16,ENTERED_DATE);
	            preparedStatement.setString(17,LAST_MODIFIED_BY);
	            preparedStatement.setString(18,LAST_MODOFIED_IP);
	            preparedStatement.setDate(19,LAST_MODIFIED_DT);

                int row = preparedStatement.executeUpdate();

                if (row == 0) {
                    con.rollback();
                } else {
                    con.commit();
                }

                JSONObject jOutPut = new JSONObject();

                jOutPut.put("STATUS_CD", "0");
                jOutPut.put("MESSAGE", "Insurance Type " + INSU_ID + " Sucessfully Created.");
                response = jOutPut.toString();
	            } catch (Exception e) {
	            Utility.PrintMessage("Error in Create Insutrance Type : " + e);
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
	  
	    
	    public static String deleteInsuID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);                                
	            JSONObject jOutPut = new JSONObject();
	            
	            String INSU_ID = jin.getString("INSU_ID");  
	            String comp_cd= jin.getString("COMP_CD");
	           	           
	            String ls_del_flag = jin.getString("DELETE_FLAG");
	            
	            if(ls_del_flag.equals("Y")) {
	            	 String extingEmp = "UPDATE insurance_mst SET IS_DELETE = 'Y' WHERE INSU_ID = " +INSU_ID+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
	                 stmt = con.createStatement();
	                 
	                 int row = stmt.executeUpdate(extingEmp);
	                 
	                 if (row > 0) {
	                 	con.commit();
	                 	 jOutPut.put("STATUS_CD", "0");	
	                 	jOutPut.put("MESSAGE", "Insurance ID " + INSU_ID + " Sucessfully Deleted.");
	                 } else {
	                 	con.rollback();
	                 	 jOutPut.put("STATUS_CD", "99");	
	                 	jOutPut.put("MESSAGE", "Insurance Id Already Deleted.");
	                 }	                 			                
	            } else {
	            	jOutPut.put("STATUS_CD", "99");	
	            	jOutPut.put("MESSAGE", "Delete Not Allow.");
	            }             	                                                        
	            response = jOutPut.toString();
	                                                                                                     
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Delete Insurace : " + e);
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
	    public static String updateInsuID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            JSONObject jOutPut = new JSONObject();
	            
	            int INSU_ID = jin.getInt("INSURANCE_ID"); 
	            String COMP_CD = jin.getString("COMP_CD");
	            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
	            String USERNAME = jin.getString("USERNAME");
	            String extingEmp = "SELECT COUNT(*) AS INSU_ID FROM insurance_mst WHERE COMP_CD = '"+COMP_CD+"' AND INSU_ID = '" +INSU_ID+"' AND IS_DELETE = 'N'";	        
	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);
	            		
	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("INSU_ID");
	            }
	            
	            if (ExistingCnt == 0) {            	
	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "Insurance Id " + INSU_ID + " not Exists. Kindly Create First");
	                 response = jOutPut.toString();
	            } else {	  	            
	            	
		            String INSU_TYPE = NVL.StringNvl(jReuqest.getString("INSU_TYPE"));
		            String INSU_NAME  = NVL.StringNvl(jReuqest.getString("INSU_NAME"));
		            Date INSU_START_DT = null;
		            if (jReuqest.getString("INSU_START_DT") == null || jReuqest.getString("INSU_START_DT").equals("")) {
		            	INSU_START_DT = null;
		            } else {
		            	INSU_START_DT = Date.valueOf(jReuqest.getString("INSU_START_DT")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            Date INSU_END_DT =null;
		            if (jReuqest.getString("INSU_END_DT") == null || jReuqest.getString("INSU_END_DT").equals("")) {
		            	INSU_START_DT = null;
		            } else {
		            	INSU_END_DT = Date.valueOf(jReuqest.getString("INSU_END_DT")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            String INSU_STATUS =NVL.StringNvl(jReuqest.getString("INSU_STATUS"));
		            Date INSU_INACTIVE_DT =null;
		            if (jReuqest.getString("INSU_INACTIVE_DT") == null || jReuqest.getString("INSU_INACTIVE_DT").equals("")) {
		            	INSU_START_DT = null;
		            } else {
		            	INSU_END_DT = Date.valueOf(jReuqest.getString("INSU_INACTIVE_DT")); 
		            		//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            String INSU_AGENCY =NVL.StringNvl(jReuqest.getString("INSU_AGENCY"));
		            String INSU_AGENCY_ID =NVL.StringNvl(jReuqest.getString("INSU_AGENCY_ID"));		            
		            int INSU_CUST_NO =0;
		            if (jReuqest.getString("INSU_CUST_NO") == null || jReuqest.getString("INSU_CUST_NO").equals("")) {
		            	INSU_CUST_NO = 0;
		            } else {
		            	INSU_CUST_NO = jReuqest.getInt("INSU_CUST_NO");
		            }		            		            
		            double INSU_PREMIUM =0;
		            if (jReuqest.getString("INSU_PREMIUM") == null || jReuqest.getString("INSU_PREMIUM").equals("")) {
		            	INSU_PREMIUM = 0;
		            } else {
		            	INSU_PREMIUM = jReuqest.getInt("INSU_PREMIUM");
		            }
		            
		            String INSU_DOC_POLICY = NVL.StringNvl(jReuqest.getString("INSU_DOC_POLICY")); 
		            
		         	byte[] INSU_DOC_POLICY_BYTE = INSU_DOC_POLICY.getBytes();
		        	Blob INSU_DOC_POLICY_BLOB = new SerialBlob(INSU_DOC_POLICY_BYTE);
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
	  	            	String updateEmployee = "UPDATE insurance_mst SET COMP_CD = ?,INSU_TYPE = ?, INSU_NAME = ?, INSU_START_DT = ?, INSU_END_DT = ?, INSU_STATUS = ?,INSU_INACTIVE_DT = ?, INSU_AGENCY = ?, INSU_AGENCY_ID = ?, INSU_CUST_NO = ?, INSU_PREMIUM = ?, INSU_DOC_POLICY = ?,LAST_MODIFIED_BY = ?, LAST_MODOFIED_IP = ?, LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND INSU_ID = ? AND IS_DELETE = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,COMP_CD);	                
		                preparedStatement.setString(2,INSU_TYPE );
		                preparedStatement.setString(3,INSU_NAME);	                
		                preparedStatement.setDate(4,INSU_START_DT);
		                preparedStatement.setDate(5,INSU_END_DT);	                
		                preparedStatement.setString(6,INSU_STATUS );
		                preparedStatement.setDate(7,INSU_INACTIVE_DT );
		                preparedStatement.setString(8,INSU_AGENCY );	                
		                preparedStatement.setString(9,INSU_AGENCY_ID );
		                preparedStatement.setInt(10,INSU_CUST_NO );
		                
		                preparedStatement.setDouble(11, INSU_PREMIUM);		                
		                preparedStatement.setBlob(12,INSU_DOC_POLICY_BLOB );
		                preparedStatement.setString(13,LAST_MODIFIED_BY );
		                preparedStatement.setString(14,LAST_MODOFIED_IP );
		                preparedStatement.setDate(15,LAST_MODIFIED_DT );
		                
		                		     		              
		                preparedStatement.setString(16,COMP_CD);
		                preparedStatement.setInt(17,INSU_ID);
		                preparedStatement.setString(18,"N");
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Insurance ID " + INSU_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            } else {
	  	            	String updateEmployee = "UPDATE insurance_mst SET COMP_CD = ?,INSU_TYPE = ?, INSU_NAME = ?, INSU_START_DT = ?, INSU_END_DT = ?, INSU_STATUS = ?,INSU_INACTIVE_DT = ?, INSU_AGENCY = ?, INSU_AGENCY_ID = ?, INSU_CUST_NO = ?, INSU_PREMIUM = ?, INSU_DOC_POLICY = ?,LAST_MODIFIED_BY = ?, LAST_MODOFIED_IP = ?, LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND INSU_ID = ? AND IS_DELETE = ? AND ENTERED_BY = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,COMP_CD);	                
		                preparedStatement.setString(2,INSU_TYPE );
		                preparedStatement.setString(3,INSU_NAME);	                
		                preparedStatement.setDate(4,INSU_START_DT);
		                preparedStatement.setDate(5,INSU_END_DT);	                
		                preparedStatement.setString(6,INSU_STATUS );
		                preparedStatement.setDate(7,INSU_INACTIVE_DT );
		                preparedStatement.setString(8,INSU_AGENCY );	                
		                preparedStatement.setString(9,INSU_AGENCY_ID );
		                preparedStatement.setInt(10,INSU_CUST_NO );		                
		                preparedStatement.setDouble(11, INSU_PREMIUM);		                
		                preparedStatement.setBlob(12,INSU_DOC_POLICY_BLOB );
		                preparedStatement.setString(13,LAST_MODIFIED_BY );
		                preparedStatement.setString(14,LAST_MODOFIED_IP );
		                preparedStatement.setDate(15,LAST_MODIFIED_DT );		                		                		     		             
		                preparedStatement.setString(16,COMP_CD);
		                preparedStatement.setInt(17,INSU_ID);
		                preparedStatement.setString(18,"N");
		                preparedStatement.setString(19,USERNAME);
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Insurance ID " + INSU_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            }	  	            	                        	               	               
	            }                                                                                              
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Update Insurance : " + e);
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
