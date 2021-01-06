package com.crm.hrms.contract;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.crm.utility.NVL;
import com.crm.utility.Utility;

public class contractTypeService {
	
	public static String GetMaxContractID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(CONTRACT_ID) AS CON_ID FROM contract_type_mst";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("CON_ID");
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
            Utility.PrintMessage("Error in GetMax Contract : " + e);
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
	
	public static String getContractList(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {	   
		String ls_comp_cd  = null;
		String ls_branch_cd = null;
		String ID  = null;
		String ld_start_dt  = null;
		String ld_end_dt  = null;
		String ll_sal_temp_id  = null;
		String ls_con_status  = null;
		String ld_inactive_dt  = null;
		String ls_con_nm  = null;
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();
	    String response = "";
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    String viewType = NVL.StringNvl(request.getString("VIEW_FLAG"));
	    String ls_query = null;
	   try {	
		   if (viewType.equals("G")) {
	            ls_query = "SELECT COMP_CD,CONTRACT_ID,CON_START_DT,CON_END_DT,SALARY_TEMP_CD,CONTRACT_STATUS,CONTRACT_INACTIVE_DT,CONTRACT_NM\r\n" + 
	            		"FROM CONTRACT_TYPE_MST WHERE CONTRACT_STATUS = 'Y' AND IS_DELETE = 'N'";
		   } else {
			   ls_query = "SELECT COMP_CD,CONTRACT_ID,CON_START_DT,CON_END_DT,SALARY_TEMP_CD,CONTRACT_STATUS,CONTRACT_INACTIVE_DT,CONTRACT_NM\r\n" + 
	            		"FROM CONTRACT_TYPE_MST WHERE ENTERED_BY = '"+ls_username+"' AND CONTRACT_STATUS = 'Y' AND IS_DELETE = 'N'";			   
		   }
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {
	            ls_comp_cd=NVL.StringNvl(rs.getString("COMP_CD"));        			
      			ID=NVL.StringNvl(rs.getString("CONTRACT_ID"));
      			ld_start_dt=NVL.StringNvl(rs.getString("CON_START_DT"));
      			ld_end_dt=NVL.StringNvl(rs.getString("CON_END_DT"));
      			ll_sal_temp_id=NVL.StringNvl(rs.getString("SALARY_TEMP_CD"));
      			ls_con_status=NVL.StringNvl(rs.getString("CONTRACT_STATUS"));
      			ld_inactive_dt=NVL.StringNvl(rs.getString("CONTRACT_INACTIVE_DT"));
      			ls_con_nm=NVL.StringNvl(rs.getString("CONTRACT_NM"));

	              jObject = new JSONObject();
	              jObject.put("COMP_CD", ls_comp_cd);
	              jObject.put("BRANCH_CD", ls_branch_cd);
	              jObject.put("ID", ID);
	              jObject.put("CON_START_DT", ld_start_dt);
	              jObject.put("CON_END_DT", ld_end_dt);
	              jObject.put("SALARY_TEMP_CD", ll_sal_temp_id);
	              jObject.put("CONTRACT_STATUS", ls_con_status);
	              jObject.put("CONTRACT_INACTIVE_DT", ld_inactive_dt);
	              jObject.put("NAME", ls_con_nm);

	              jArray.put(jObject);
	            }
	            if (jArray.length() <= 0) {
	            	mainObject.put("STATUS_CD", "99");
	            	mainObject.put("MESSAGE", "Contract Type not found");
	            } else {
	            	 mainObject.put("STATUS_CD", "0");
	 	            mainObject.put("RESPONSE", jArray);	            	
	            }
	            
	           
	            response = mainObject.toString();
	          } catch(Exception e) {	            
	        	  e.printStackTrace();
	        	  Utility.PrintMessage("Error in Get User List : " + e);
		          response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
	           
	          }	       
	    return response;
	  }
		
	 public static String GetContactID(Connection con, String ls_request) {
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
	    		
	            String ls_contract_id = NVL.StringNvl(jin.getString("CONTRACT_ID"));
	            String viewFlag = NVL.StringNvl(jin.getString("VIEW_FLAG"));
	            String userName = NVL.StringNvl(jin.getString("USERNAME"));

	            JSONObject jOut = new JSONObject();
	            String sql = null;
	            if (viewFlag.equals("G")) {
	                sql = "SELECT COMP_CD,CONTRACT_ID,CON_START_DT,CON_END_DT,SALARY_TEMP_CD,CONTRACT_STATUS,CONTRACT_INACTIVE_DT,CONTRACT_NM FROM CONTRACT_TYPE_MST WHERE CONTRACT_STATUS = 'Y' AND CONTRACT_ID = "+ls_contract_id+" AND IS_DELETE = 'N'";
	            } else {
	                sql = "SELECT COMP_CD,CONTRACT_ID,CON_START_DT,CON_END_DT,SALARY_TEMP_CD,CONTRACT_STATUS,CONTRACT_INACTIVE_DT,CONTRACT_NM FROM CONTRACT_TYPE_MST WHERE ENTERED_BY = '" + userName + "' AND CONTRACT_STATUS = 'Y' AND CONTRACT_ID = "+ls_contract_id+" AND IS_DELETE = 'N'";

	            }

	            stmt = con.createStatement();
	            rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                ll_cnt = ll_cnt + 1;
	                
	                
	                ls_comp_cd=NVL.StringNvl(rs.getString("COMP_CD"));        			
	      			ID=NVL.StringNvl(rs.getString("CONTRACT_ID"));
	      			ld_start_dt=NVL.StringNvl(rs.getString("CON_START_DT"));
	      			ld_end_dt=NVL.StringNvl(rs.getString("CON_END_DT"));
	      			ll_sal_temp_id=NVL.StringNvl(rs.getString("SALARY_TEMP_CD"));
	      			ls_con_status=NVL.StringNvl(rs.getString("CONTRACT_STATUS"));
	      			ld_inactive_dt=NVL.StringNvl(rs.getString("CONTRACT_INACTIVE_DT"));
	      			ls_con_nm=NVL.StringNvl(rs.getString("CONTRACT_NM"));

	      			jOut = new JSONObject();
	      			jOut.put("COMP_CD", ls_comp_cd);
	      			jOut.put("BRANCH_CD", ls_branch_cd);
	      			jOut.put("ID", ID);
	      			jOut.put("CON_START_DT", ld_start_dt);
	      			jOut.put("CON_END_DT", ld_end_dt);
	      			jOut.put("SALARY_TEMP_CD", ll_sal_temp_id);
	      			jOut.put("CONTRACT_STATUS", ls_con_status);
	      			jOut.put("CONTRACT_INACTIVE_DT", ld_inactive_dt);
	      			jOut.put("NAME", ls_con_nm);	              	                	                           
	            }

	            JSONArray jresponse = new JSONArray();
	            JSONObject jobj = new JSONObject();
	            jobj.put("STATUS_CD", "0");
	            if (jOut.length() <= 0) {
	                jobj.put("STATUS_CD", "1");
	                jobj.put("MESSAGE", "Contact not found");
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
	
	    public static String CreateContract(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        int ExistingCnt = 0;
	        try {

	            JSONObject jin = new JSONObject(ls_request);
	            System.out.println(jin.toString());	
	            
	            String ls_insert_flag = jin.getString("INSERT_FALG");
	            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");

	            String COMP_CD = jReuqest.getString("COMP_CD");	            	           
	            int CON_ID =0;
	            if (jReuqest.getString("CONTRACT_ID") == null || jReuqest.getString("CONTRACT_ID").equals("")) {
	            	CON_ID = 0;
	            } else {
	            	CON_ID = jReuqest.getInt("CONTRACT_ID");
	            }		    	       
	            Date CON_START_DT = null;
	            if (jReuqest.getString("CON_START_DT") == null || jReuqest.getString("CON_START_DT").equals("")) {
	            	CON_START_DT = null;
	            } else {
	            	CON_START_DT = Date.valueOf(jReuqest.getString("CON_START_DT")); 
	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }	  	            
	            Date CON_END_DT = null;
	            if (jReuqest.getString("CON_END_DT") == null || jReuqest.getString("CON_END_DT").equals("")) {
	            	CON_END_DT = null;
	            } else {
	            	CON_END_DT = Date.valueOf(jReuqest.getString("CON_END_DT")); 
	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }	            
	            int SALARY_TEMP_CD = jReuqest.getInt("SALARY_TEMP_CD");
	            if (jReuqest.getString("SALARY_TEMP_CD") == null || jReuqest.getString("SALARY_TEMP_CD").equals("")) {
	            	SALARY_TEMP_CD = 0;
	            } else {
	            	SALARY_TEMP_CD = jReuqest.getInt("SALARY_TEMP_CD");
	            }
	            String CONTRACT_STATUS = jReuqest.getString("CONTRACT_STATUS");	 
	            Date CONTRACT_INACTIVE_DT = null;
	            if (jReuqest.getString("CONTRACT_INACTIVE_DT") == null || jReuqest.getString("CONTRACT_INACTIVE_DT").equals("")) {
	            	CONTRACT_INACTIVE_DT = null;
	            } else {
	            	CONTRACT_INACTIVE_DT = Date.valueOf(jReuqest.getString("CON_END_DT")); 
	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	           }
	           String CONTRACT_NM = jReuqest.getString("CONTRACT_NM");
	           
	           String ENTERED_BY = jReuqest.getString("ENTERED_BY");
	            String ENTERED_IP = jReuqest.getString("ENTERED_IP");
	            Date ENTERED_DATE = null;
	            if (jReuqest.getString("ENTERED_DATE") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
	                ENTERED_DATE = null;
	            } else {
	                ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE"));  
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
	            }
	            String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");
	            String LAST_MODOFIED_IP = jReuqest.getString("LAST_MODOFIED_IP");
	            Date LAST_MODIFIED_DT = null;
	            if (jReuqest.getString("LAST_MODIFIED_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
	                LAST_MODIFIED_DT = null;
	            } else {
	                LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT"));  
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
	            }
	            
	            System.out.println(CON_ID);
                JSONObject jOut = new JSONObject();
                String sql = "INSERT INTO contract_type_mst(COMP_CD,CONTRACT_ID,CON_START_DT,CON_END_DT,SALARY_TEMP_CD,CONTRACT_STATUS,CONTRACT_INACTIVE_DT,CONTRACT_NM,ENTERED_BY,ENTERED_IP,ENTERED_DATE,LAST_MODIFIED_BY,LAST_MODOFIED_IP,LAST_MODIFIED_DT)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                              
                preparedStatement.setString(1,COMP_CD);
                preparedStatement.setInt(2,CON_ID);
                preparedStatement.setDate(3,CON_START_DT);
                preparedStatement.setDate(4,CON_END_DT);
                preparedStatement.setInt(5,SALARY_TEMP_CD);
                preparedStatement.setString(6,CONTRACT_STATUS);
                preparedStatement.setDate(7,CONTRACT_INACTIVE_DT);
                preparedStatement.setString(8,CONTRACT_NM);
                preparedStatement.setDate(9,ENTERED_DATE);
                preparedStatement.setString(10,ENTERED_BY);
                preparedStatement.setString(11,ENTERED_IP);
                preparedStatement.setDate(12,LAST_MODIFIED_DT);
                preparedStatement.setString(13,LAST_MODIFIED_BY);
	            preparedStatement.setString(14,LAST_MODOFIED_IP);

                int row = preparedStatement.executeUpdate();

                if (row == 0) {
                    con.rollback();
                } else {
                    con.commit();
                }

                JSONObject jOutPut = new JSONObject();

                jOutPut.put("STATUS_CD", "0");
                jOutPut.put("MESSAGE", "Contract Type " + CON_ID + " Sucessfully Created.");
                response = jOutPut.toString();
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
	            
	            String CONTRACT_ID = jin.getString("CONTRACT_ID");  
	            String comp_cd= jin.getString("COMP_CD");
	           	           
	            String ls_del_flag = jin.getString("DELETE_FLAG");
	            
	            if(ls_del_flag.equals("Y")) {
	            	 String extingEmp = "UPDATE contract_type_mst SET IS_DELETE = 'Y' WHERE CONTRACT_ID = " +CONTRACT_ID+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
	                 stmt = con.createStatement();
	                 
	                 int row = stmt.executeUpdate(extingEmp);
	                 
	                 if (row > 0) {
	                 	con.commit();
	                 	 jOutPut.put("STATUS_CD", "0");	
	                 	jOutPut.put("MESSAGE", "Contract ID " + CONTRACT_ID + " Sucessfully Deleted.");
	                 } else {
	                 	con.rollback();
	                 	 jOutPut.put("STATUS_CD", "99");	
	                 	jOutPut.put("MESSAGE", "Contract Id Already Deleted.");
	                 }
	                 		
	                
	            } else {
	            	jOutPut.put("STATUS_CD", "99");	
	            	jOutPut.put("MESSAGE", "Delete Not Allow.");
	            }             	                                                        
	            response = jOutPut.toString();
	                                                                                                     
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Delete Contract : " + e);
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
	    public static String updateContractID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            JSONObject jOutPut = new JSONObject();
	            
	            int CON_ID = jin.getInt("CONTRACT_ID"); 
	            String COMP_CD = jin.getString("COMP_CD");
	            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
	            String USERNAME = jin.getString("USERNAME");
	            String extingEmp = "SELECT COUNT(*) AS CON_ID FROM contract_type_mst WHERE COMP_CD = '"+COMP_CD+"' AND CONTRACT_ID = '" +CON_ID+"' AND IS_DELETE = 'N'";	        
	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);
	            		
	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("CON_ID");
	            }
	            
	            if (ExistingCnt == 0) {            	
	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "Contract Type " + CON_ID + " not Exists. Kindly Create First");
	                 response = jOutPut.toString();
	            } else {	  	            

	  	            Date CON_START_DT = null;
		            if (jReuqest.getString("CON_START_DT") == null || jReuqest.getString("CON_START_DT").equals("")) {
		            	CON_START_DT = null;
		            } else {
		            	CON_START_DT = Date.valueOf(jReuqest.getString("CON_START_DT"));  
		                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
		            }		            
		            Date CON_END_DT = null;
		            if (jReuqest.getString("CON_END_DT") == null || jReuqest.getString("CON_END_DT").equals("")) {
		            	CON_END_DT = null;
		            } else {
		            	CON_END_DT = Date.valueOf(jReuqest.getString("CON_END_DT"));  
		                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
		            }	            		            	  	           
	  	            int SALARY_TEMP_CD = jReuqest.getInt("SALARY_TEMP_CD");
	  	            String CONTRACT_STATUS = jReuqest.getString("CONTRACT_STATUS");	  	            	  	           	  	            
	  	            Date CONTRACT_INACTIVE_DT = null;
		            if (jReuqest.getString("CONTRACT_INACTIVE_DT") == null || jReuqest.getString("CONTRACT_INACTIVE_DT").equals("")) {
		            	CONTRACT_INACTIVE_DT = null;
		            } else {
		            	CONTRACT_INACTIVE_DT = Date.valueOf(jReuqest.getString("CONTRACT_INACTIVE_DT"));  
		                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
		            }		            		           
	  	            String CONTRACT_NM = jReuqest.getString("CONTRACT_NM");	  	            
	  	            String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");	  	          
	  	            String LAST_MODOFIED_IP = jReuqest.getString("LAST_MODOFIED_IP"); 	  	            
	  	            Date LAST_MODIFIED_DT = null;
		            if (jReuqest.getString("LAST_MODIFIED_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
		            	LAST_MODIFIED_DT = null;
		            } else {
		            	LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT"));  
		                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
		            }
	  	            	  	        
	  	            if (UPDATE_FLAG.equals("Y")) {
	  	            	String updateEmployee = "UPDATE contract_type_mst SET COMP_CD = ?,CON_START_DT = ?,CON_END_DT = ?,SALARY_TEMP_CD = ?,CONTRACT_STATUS = ?,CONTRACT_INACTIVE_DT = ?,CONTRACT_NM = ?,LAST_MODIFIED_BY = ?,LAST_MODOFIED_IP = ?,LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND CONTRACT_ID = ? AND IS_DELETE = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,COMP_CD);	                
		                preparedStatement.setDate(2,CON_START_DT );
		                preparedStatement.setDate(3,CON_END_DT);	                
		                preparedStatement.setInt(4,SALARY_TEMP_CD);
		                preparedStatement.setString(5,CONTRACT_STATUS);	                
		                preparedStatement.setDate(6,CONTRACT_INACTIVE_DT );
		                preparedStatement.setString(7,CONTRACT_NM );
		                preparedStatement.setString(8,LAST_MODIFIED_BY );	                
		                preparedStatement.setString(9,LAST_MODOFIED_IP );
		                preparedStatement.setDate(10,LAST_MODIFIED_DT );
		                		     		              
		                preparedStatement.setString(11,COMP_CD);
		                preparedStatement.setInt(12,CON_ID);
		                preparedStatement.setString(13,"N");
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Contract ID " + CON_ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            } else {
	  	            	String updateEmployee = "UPDATE contract_type_mst SET COMP_CD = ?,CON_START_DT = ?,CON_END_DT = ?,SALARY_TEMP_CD = ?,CONTRACT_STATUS = ?,CONTRACT_INACTIVE_DT = ?,CONTRACT_NM = ?,LAST_MODIFIED_BY = ?,LAST_MODOFIED_IP = ?,LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND CONTRACT_ID = ? AND IS_DELETE = ? AND ENTERED_BY = ?";
	  	         
		                PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
		                preparedStatement.setString(1,COMP_CD);	                
		                preparedStatement.setDate(2,CON_START_DT );
		                preparedStatement.setDate(3,CON_END_DT);	                
		                preparedStatement.setInt(4,SALARY_TEMP_CD);
		                preparedStatement.setString(5,CONTRACT_STATUS);	                
		                preparedStatement.setDate(6,CONTRACT_INACTIVE_DT );
		                preparedStatement.setString(7,CONTRACT_NM );
		                preparedStatement.setString(8,LAST_MODIFIED_BY );	                
		                preparedStatement.setString(9,LAST_MODOFIED_IP );
		                preparedStatement.setDate(10,LAST_MODIFIED_DT );
		                		     		              
		                preparedStatement.setString(11,COMP_CD);
		                preparedStatement.setInt(12,CON_ID);
		                preparedStatement.setString(13,"N");
		                preparedStatement.setString(14,USERNAME);
		                		                
		                int row = preparedStatement.executeUpdate();
                        
		                if (row == 0) {
		                	con.rollback();
		                	 jOutPut.put("STATUS_CD", "99");
				             jOutPut.put("MESSAGE", "You have not rights for update");
		                } else {
		                	con.commit();	                	                	                
		                	jOutPut.put("STATUS_CD", "0");
				            jOutPut.put("MESSAGE", "Contract ID " + CON_ID + " Sucessfully Updated.");
		                }            
		                
		               
		                response = jOutPut.toString();
	  	            }	  	            	                        	               	               
	            }                                                                                              
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Update Contract : " + e);
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
