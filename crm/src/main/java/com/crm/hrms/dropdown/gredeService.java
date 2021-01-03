package com.crm.hrms.dropdown;

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

public class gredeService {
	public static JSONObject getdropDwon(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {

	    int ll_id = 0;
	    String ls_sortname = null;
	    String name = null;
	    String ls_phcode = null;
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();
	   
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    
	   try {
	            String ls_query = "SELECT ID,NAME FROM GRADE_MST";
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {
	              ll_id = rs.getInt("ID");
	              name = rs.getString("NAME");

	              jObject = new JSONObject();
	              jObject.put("NAME", name);
	              jObject.put("ID", ll_id);

	              jArray.put(jObject);

	            }

	            mainObject.put("STATUS_CD", "0");
	            mainObject.put("RESPONSE", jArray);
	          } catch(Exception e) {
	            ll_id = 0;
	            System.out.println("Get Grade Error : " + e);
	            e.printStackTrace();
	          }	       
	    return mainObject;
	  }
	
	public static String GetMaxGradeID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(ID) AS ID FROM grade_mst";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("ID");
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
            Utility.PrintMessage("Error in GetMax Grade Id : " + e);
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
	
	 public static String getGradeList(Connection con, String requestData) throws JSONException,
     ClassNotFoundException,
     SQLException {

         int ll_id = 0;
         String ls_sortname = null;
         String name = null;
         String ls_phcode = null;
         JSONObject jObject = null;
         JSONArray jArray = new JSONArray();
         JSONObject mainObject = new JSONObject();
         ResultSet rs = null;
         JSONObject request = new JSONObject(requestData);
         JSONObject jError = new JSONObject();

         String ls_username = request.getString("USERNAME");
         String ls_req_ip = request.getString("REQUEST_IP");
         String ls_view = request.getString("VIEW_FLAG");
         String ls_comp_cd = request.getString("COMP_CD");
         String response = null;
         try {
             String ls_query = null;
             if (ls_view.equals("G")) {
                 ls_query = "SELECT ID,NAME,COMP_CD,GRADE_INACTIVE_DT, ACTIVE_STATUS FROM grade_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND IS_DELETE = 'N'";
             } else {
                 ls_query = "SELECT ID,NAME,COMP_CD,GRADE_INACTIVE_DT, ACTIVE_STATUS FROM grade_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND IS_DELETE = 'N' AND ENTERED_BY = '" + ls_username + "'";
             }
             System.out.println(ls_query);
             Statement stmt = null;
             stmt = con.createStatement();
             rs = stmt.executeQuery(ls_query);

             while (rs.next()) {

                 jObject = new JSONObject();
                 jObject.put("ID", NVL.StringNvl(rs.getString("ID")));
                 jObject.put("NAME", NVL.StringNvl(rs.getString("NAME")));
                 jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                 jObject.put("GRADE_INACTIVE_DT", NVL.StringNvl(rs.getString("GRADE_INACTIVE_DT")));
                 jObject.put("ACTIVE_STATUS", NVL.StringNvl(rs.getString("ACTIVE_STATUS")));                
                 jArray.put(jObject);

             }

             mainObject.put("STATUS_CD", "0");
             mainObject.put("RESPONSE", jArray);
         } catch (Exception e) {
             Utility.PrintMessage("Error in Grade List : " + e);
             response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
         }
         return mainObject.toString();
     }
	
	 
	 public static String getGradeId(Connection con, String requestData) throws JSONException,
     ClassNotFoundException,
     SQLException {

         int ll_id = 0;
         String ls_sortname = null;
         String name = null;
         String ls_phcode = null;
         JSONObject jObject = null;
         JSONArray jArray = new JSONArray();
         JSONObject mainObject = new JSONObject();
         ResultSet rs = null;
         JSONObject request = new JSONObject(requestData);
         JSONObject jError = new JSONObject();

         String ls_username = request.getString("USERNAME");
         String ls_req_ip = request.getString("REQUEST_IP");
         String ls_view = request.getString("VIEW_FLAG");
         String ls_comp_cd = request.getString("COMP_CD");
         int ll_desig_cd = request.getInt("GRADE_ID");

         String response = null;
         try {
             String ls_query = null;
             if (ls_view.equals("G")) {
                 ls_query = "SELECT ID,NAME,COMP_CD,GRADE_INACTIVE_DT, ACTIVE_STATUS FROM grade_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND ID = " + ll_desig_cd + " AND IS_DELETE = 'N'";
             } else {
                 ls_query = "SELECT ID,NAME,COMP_CD,GRADE_INACTIVE_DT, ACTIVE_STATUS FROM grade_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND ID = " + ll_desig_cd + " AND IS_DELETE = 'N' AND ENTERED_BY = '" + ls_username + "'";
             }
             System.out.println(ls_query);
             Statement stmt = null;
             stmt = con.createStatement();
             rs = stmt.executeQuery(ls_query);

             while (rs.next()) {

                 jObject = new JSONObject();
                 jObject.put("ID", NVL.StringNvl(rs.getString("ID")));
                 jObject.put("NAME", NVL.StringNvl(rs.getString("NAME")));
                 jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                 jObject.put("GRADE_INACTIVE_DT", NVL.StringNvl(rs.getString("GRADE_INACTIVE_DT")));
                 jObject.put("ACTIVE_STATUS", NVL.StringNvl(rs.getString("ACTIVE_STATUS")));   
                 jArray.put(jObject);

             }

             mainObject.put("STATUS_CD", "0");
             mainObject.put("RESPONSE", jArray);
         } catch (Exception e) {
             Utility.PrintMessage("Error in Grade Id : " + e);
             response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
         }
         return mainObject.toString();
     }

	 public static String CreateGrade(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        int ExistingCnt = 0;
	        try {

	            JSONObject jin = new JSONObject(ls_request);
	            System.out.println(jin.toString());

	            String CREATE_FLAG = jin.getString("CREATE_FLAG");
	            String USERNAME = jin.getString("USERNAME");
	            String REQUEST_IP = jin.getString("REQUEST_IP");

	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");


	            int ID = 0;
	            if (jReuqest.getString("ID") == null || jReuqest.getString("ID").equals("")) {
	            	ID = 0;
	            } else {
	            	ID = jReuqest.getInt("ID");
	            }	           
	            String NAME = NVL.StringNvl(jReuqest.getString("NAME"));
	            String COMP_CD = NVL.StringNvl(jReuqest.getString("COMP_CD"));
	            Date GRADE_INACTIVE_DT = null;
	            if (jReuqest.getString("GRADE_INACTIVE_DT") == null || jReuqest.getString("GRADE_INACTIVE_DT").equals("")) {
	            	GRADE_INACTIVE_DT = null;
	            } else {
	            	GRADE_INACTIVE_DT = Date.valueOf(jReuqest.getString("GRADE_INACTIVE_DT"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String ACTIVE_STATUS = NVL.StringNvl(jReuqest.getString("ACTIVE_STATUS"));
	            String ENTERED_BY = NVL.StringNvl(jReuqest.getString("ENTERED_BY"));
	            String ENTERED_IP = NVL.StringNvl(jReuqest.getString("ENTERED_IP"));
	            Date ENTERED_DATE = null;
	            if (jReuqest.getString("ENTERED_DATE") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
	                ENTERED_DATE = null;
	            } else {
	                ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String LAST_MODIFIED_BY = NVL.StringNvl(jReuqest.getString("LAST_MODIFIED_BY"));
	            String LAST_MODOFIED_IP = NVL.StringNvl(jReuqest.getString("LAST_MODOFIED_IP"));
	            Date LAST_MODIFIED_DT = null;
	            if (jReuqest.getString("LAST_MODIFIED_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
	            	LAST_MODIFIED_DT = null;
	            } else {
	            	LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }


	            JSONObject jOut = new JSONObject();
	            String sql = "INSERT INTO grade_mst (ID, NAME, COMP_CD, GRADE_INACTIVE_DT, ACTIVE_STATUS, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_MODIFIED_BY, LAST_MODOFIED_IP, LAST_MODIFIED_DT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	            if (CREATE_FLAG.equals("Y")) {
	                PreparedStatement preparedStatement = con.prepareStatement(sql);

	                preparedStatement.setInt(1, ID);
	                preparedStatement.setString(2, NAME);
	                preparedStatement.setString(3, COMP_CD);
	                preparedStatement.setDate(4, GRADE_INACTIVE_DT);
	                preparedStatement.setString(5, ACTIVE_STATUS);
	                preparedStatement.setString(6, ENTERED_BY);
	                preparedStatement.setString(7, ENTERED_IP);
	                preparedStatement.setDate(8, ENTERED_DATE);
	                preparedStatement.setString(9, LAST_MODIFIED_BY);
	                preparedStatement.setString(10, LAST_MODOFIED_IP);
	                preparedStatement.setDate(11, LAST_MODIFIED_DT);

	                int row = preparedStatement.executeUpdate();

	                if (row == 0) {
	                    con.rollback();
	                } else {
	                    con.commit();
	                }

	                JSONObject jOutPut = new JSONObject();

	                jOutPut.put("STATUS_CD", "0");
	                jOutPut.put("MESSAGE", "Grade ID : " + ID + " Sucessfully Created.");
	                response = jOutPut.toString();
	            } else {
	                JSONObject jOutPut = new JSONObject();

	                jOutPut.put("STATUS_CD", "99");
	                jOutPut.put("MESSAGE", "Create not allow.");
	                response = jOutPut.toString();
	            }


	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Create Grade ID : " + e);
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
	 public static String deleteGradeID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);                                
	            JSONObject jOutPut = new JSONObject();
	            
	            String GRADE_ID = jin.getString("GRADE_ID");  
	            String comp_cd= jin.getString("COMP_CD");
	           	           
	            String ls_del_flag = jin.getString("DELETE_FLAG");
	            
	            if(ls_del_flag.equals("Y")) {
	            	 String extingEmp = "UPDATE grade_mst SET IS_DELETE = 'Y' WHERE ID  = " +GRADE_ID+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
	                 stmt = con.createStatement();
	                 
	                 int row = stmt.executeUpdate(extingEmp);
	                 
	                 if (row > 0) {
	                 	con.commit();
	                 	 jOutPut.put("STATUS_CD", "0");	
	                 	jOutPut.put("MESSAGE", "Grade Id " + GRADE_ID + " Sucessfully Deleted.");
	                 } else {
	                 	con.rollback();
	                 	 jOutPut.put("STATUS_CD", "99");	
	                 	jOutPut.put("MESSAGE", "Grade Id Already Deleted.");
	                 }	                 			                
	            } else {
	            	jOutPut.put("STATUS_CD", "99");	
	            	jOutPut.put("MESSAGE", "Delete Not Allow.");
	            }             	                                                        
	            response = jOutPut.toString();
	                                                                                                     
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Delete Grade : " + e);
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
	 
	 public static String updateGrade(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            JSONObject jOutPut = new JSONObject();
	            
	            int ID = jin.getInt("GRADE_ID"); 
	            String COMP_CD = jin.getString("COMP_CD");
	            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
	            String USERNAME = jin.getString("USERNAME");
	            String extingEmp = "SELECT COUNT(*) AS ID FROM grade_mst WHERE COMP_CD = '"+COMP_CD+"' AND ID = '" +ID+"' AND IS_DELETE = 'N'";	        
	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);
	            		
	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("ID");
	            }
	            
	            if (ExistingCnt == 0) {            	
	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "Grade Id " + ID + " not Exists. Kindly Create First");
	                 response = jOutPut.toString();
	            } else {	  	            	            			           	            	       
		            String NAME = NVL.StringNvl(jReuqest.getString("NAME"));		            
		            Date GRADE_INACTIVE_DT = null;
		            if (jReuqest.getString("GRADE_INACTIVE_DT") == null || jReuqest.getString("GRADE_INACTIVE_DT").equals("")) {
		            	GRADE_INACTIVE_DT = null;
		            } else {
		            	GRADE_INACTIVE_DT = Date.valueOf(jReuqest.getString("GRADE_INACTIVE_DT"));
		                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            String ACTIVE_STATUS = NVL.StringNvl(jReuqest.getString("ACTIVE_STATUS"));		       
		            String LAST_MODIFIED_BY = NVL.StringNvl(jReuqest.getString("LAST_MODIFIED_BY"));
		            String LAST_MODOFIED_IP = NVL.StringNvl(jReuqest.getString("LAST_MODOFIED_IP"));
		            Date LAST_MODIFIED_DT = null;
		            if (jReuqest.getString("LAST_MODIFIED_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
		            	LAST_MODIFIED_DT = null;
		            } else {
		            	LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT"));
		                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }

	                 
	  	            if (UPDATE_FLAG.equals("Y")) {
	  	            	String updateEmployee = "UPDATE grade_mst SET NAME = ?, COMP_CD = ?, GRADE_INACTIVE_DT = ?, ACTIVE_STATUS = ?, LAST_MODIFIED_BY = ?, LAST_MODOFIED_IP = ?, LAST_MODIFIED_DT = ? WHERE ID = ? AND COMP_CD = ? AND IS_DELETE = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
	  	            	
	  	                preparedStatement.setString(1,NAME);
	  	                preparedStatement.setString(2,COMP_CD);
	  	                preparedStatement.setDate(3,GRADE_INACTIVE_DT);
	  	                preparedStatement.setString(4,ACTIVE_STATUS);
	  		            preparedStatement.setString(5,LAST_MODIFIED_BY);
	  		            preparedStatement.setString(6,LAST_MODOFIED_IP);
	  		            preparedStatement.setDate(7,LAST_MODIFIED_DT);
	  		              		            
	  		            preparedStatement.setInt(8,ID);  		       
	  		            preparedStatement.setString(9,COMP_CD);
	  		            preparedStatement.setString(10,"N");
	  		          
		                int row = preparedStatement.executeUpdate();
	                    
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Grade ID " + ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            } else {
	  	            	String updateEmployee = "UPDATE grade_mst SET NAME = ?, COMP_CD = ?, GRADE_INACTIVE_DT = ?, ACTIVE_STATUS = ?, LAST_MODIFIED_BY = ?, LAST_MODOFIED_IP = ?, LAST_MODIFIED_DT = ? WHERE ID = ? AND COMP_CD = ? AND IS_DELETE = ? AND ENTERED_BY = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,NAME);
	  	                preparedStatement.setString(2,COMP_CD);
	  	                preparedStatement.setDate(3,GRADE_INACTIVE_DT);
	  	                preparedStatement.setString(4,ACTIVE_STATUS);
	  		            preparedStatement.setString(5,LAST_MODIFIED_BY);
	  		            preparedStatement.setString(6,LAST_MODOFIED_IP);
	  		            preparedStatement.setDate(7,LAST_MODIFIED_DT);
	  		              		            
	  		            preparedStatement.setInt(8,ID);  		       
	  		            preparedStatement.setString(9,COMP_CD);
	  		            preparedStatement.setString(10,"N");
	  		            preparedStatement.setString(12,USERNAME);
	  		          
	  		          
	  		            
		                int row = preparedStatement.executeUpdate();
	                    
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "Grade ID " + ID + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            }	  	            	                        	               	               
	            }                                                                                              
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Update Grade : " + e);
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
