package com.crm.service;

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

public class departService {
    public static String GetMaxDepartID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(DEPART_ID) AS DEPART_ID FROM deprtment_mst";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("DEPART_ID");
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
            Utility.PrintMessage("Error in GetMax Department Id : " + e);
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


    public static String getDepartList(Connection con, String requestData) throws JSONException,
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
                    ls_query = "SELECT DEPART_ID,NAME,COMP_CD,DEPT_INACTIVE_DT,ACTIVE_STATUS FROM deprtment_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND IS_DELETE = 'N'";
                } else {
                    ls_query = "SELECT DEPART_ID,NAME,COMP_CD,DEPT_INACTIVE_DT,ACTIVE_STATUS FROM deprtment_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND IS_DELETE = 'N' AND ENTERED_BY = '" + ls_username + "'";
                }
                System.out.println(ls_query);
                Statement stmt = null;
                stmt = con.createStatement();
                rs = stmt.executeQuery(ls_query);

                while (rs.next()) {                   
                    jObject = new JSONObject();
                    jObject.put("DEPART_ID", NVL.StringNvl(rs.getString("DEPART_ID")));                    
                    jObject.put("NAME", NVL.StringNvl(rs.getString("NAME")));
                    jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));                    
                    jObject.put("DEPT_INACTIVE_DT", NVL.StringNvl(rs.getString("DEPT_INACTIVE_DT")));
                    jObject.put("ACTIVE_STATUS", NVL.StringNvl(rs.getString("ACTIVE_STATUS")));

                    jArray.put(jObject);

                }

                mainObject.put("STATUS_CD", "0");
                mainObject.put("RESPONSE", jArray);
            } catch (Exception e) {
                Utility.PrintMessage("Error in GetMax Department Id : " + e);
                response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
            return mainObject.toString();
        }

    public static String getDepartId(Connection con, String requestData) throws JSONException,
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
            int ll_desig_cd = request.getInt("DEPART_ID");

            String response = null;
            try {
                String ls_query = null;
                if (ls_view.equals("G")) {
                    ls_query = "SELECT DEPART_ID,NAME,COMP_CD,DEPT_INACTIVE_DT,ACTIVE_STATUS FROM deprtment_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND DEPART_ID = " + ll_desig_cd + " AND IS_DELETE = 'N'";
                } else {
                    ls_query = "SELECT DEPART_ID,NAME,COMP_CD,DEPT_INACTIVE_DT,ACTIVE_STATUS FROM deprtment_mst WHERE COMP_CD = '" + ls_comp_cd + "' AND DEPART_ID = " + ll_desig_cd + " AND IS_DELETE = 'N' AND ENTERED_BY = '" + ls_username + "'";
                }
                System.out.println(ls_query);
                Statement stmt = null;
                stmt = con.createStatement();
                rs = stmt.executeQuery(ls_query);

                while (rs.next()) {                 
                    jObject = new JSONObject();
                    jObject.put("DEPART_ID", NVL.StringNvl(rs.getString("DEPART_ID")));                    
                    jObject.put("NAME", NVL.StringNvl(rs.getString("NAME")));
                    jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                    jObject.put("DEPT_INACTIVE_DT", NVL.StringNvl(rs.getString("DEPT_INACTIVE_DT")));
                    jObject.put("ACTIVE_STATUS", NVL.StringNvl(rs.getString("ACTIVE_STATUS")));

                    jArray.put(jObject);

                }

                mainObject.put("STATUS_CD", "0");
                mainObject.put("RESPONSE", jArray);
            } catch (Exception e) {
                Utility.PrintMessage("Error in GetMax Designation Id : " + e);
                response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
            return mainObject.toString();
        }


    public static String CreateDepartment(Connection con, String ls_request) {
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


            int DEPART_ID = 0;
            if (jReuqest.getString("DEPART_ID") == null || jReuqest.getString("DEPART_ID").equals("")) {
                DEPART_ID = 0;
            } else {
                DEPART_ID = jReuqest.getInt("DEPART_ID");
            }           
            String NAME = NVL.StringNvl(jReuqest.getString("NAME"));
            String COMP_CD = NVL.StringNvl(jReuqest.getString("COMP_CD"));
            Date DEPT_INACTIVE_DT = null;
            if (jReuqest.getString("DEPT_INACTIVE_DT") == null || jReuqest.getString("DEPT_INACTIVE_DT").equals("")) {
            	DEPT_INACTIVE_DT = null;
            } else {
            	DEPT_INACTIVE_DT = Date.valueOf(jReuqest.getString("DEPT_INACTIVE_DT"));
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
            String sql = "INSERT INTO deprtment_mst (DEPART_ID, NAME, COMP_CD, DEPT_INACTIVE_DT, ACTIVE_STATUS, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP, LAST_ENTERED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            if (CREATE_FLAG.equals("Y")) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);

                preparedStatement.setInt(1, DEPART_ID);
                preparedStatement.setString(2, NAME);
                preparedStatement.setString(3, COMP_CD);
                preparedStatement.setDate(4, DEPT_INACTIVE_DT);
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
                jOutPut.put("MESSAGE", "Department Type " + DEPART_ID + " Sucessfully Created.");
                response = jOutPut.toString();
            } else {
                JSONObject jOutPut = new JSONObject();

                jOutPut.put("STATUS_CD", "99");
                jOutPut.put("MESSAGE", "Create not allow.");
                response = jOutPut.toString();
            }


        } catch (Exception e) {
            Utility.PrintMessage("Error in Create Department Type : " + e);
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
    
    public static String deleteDepartID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);                                
            JSONObject jOutPut = new JSONObject();
            
            String DESIG_ID = jin.getString("DEPART_ID");  
            String comp_cd= jin.getString("COMP_CD");
           	           
            String ls_del_flag = jin.getString("DELETE_FLAG");
            
            if(ls_del_flag.equals("Y")) {
            	 String extingEmp = "UPDATE deprtment_mst SET IS_DELETE = 'Y' WHERE DEPART_ID  = " +DESIG_ID+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
                 stmt = con.createStatement();
                 
                 int row = stmt.executeUpdate(extingEmp);
                 
                 if (row > 0) {
                 	con.commit();
                 	 jOutPut.put("STATUS_CD", "0");	
                 	jOutPut.put("MESSAGE", "Department Type " + DESIG_ID + " Sucessfully Deleted.");
                 } else {
                 	con.rollback();
                 	 jOutPut.put("STATUS_CD", "99");	
                 	jOutPut.put("MESSAGE", "Department Type Already Deleted.");
                 }	                 			                
            } else {
            	jOutPut.put("STATUS_CD", "99");	
            	jOutPut.put("MESSAGE", "Delete Not Allow.");
            }             	                                                        
            response = jOutPut.toString();
                                                                                                     
        } catch (Exception e) {
            Utility.PrintMessage("Error in Delete Department : " + e);
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
    
    public static String updateDepartment(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);            
            JSONObject jReuqest = new JSONObject();
            jReuqest = jin.getJSONObject("REQUEST_DATA");
            JSONObject jOutPut = new JSONObject();
            
            int DESIG_ID = jin.getInt("DEPART_ID"); 
            String COMP_CD = jin.getString("COMP_CD");
            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
            String USERNAME = jin.getString("USERNAME");
            String extingEmp = "SELECT COUNT(*) AS DEPART_ID FROM deprtment_mst WHERE COMP_CD = '"+COMP_CD+"' AND DEPART_ID = '" +DESIG_ID+"' AND IS_DELETE = 'N'";	
            
            System.out.println(extingEmp);
            
            stmt = con.createStatement();
            ResultSet empResultSet = stmt.executeQuery(extingEmp);
            		
            while (empResultSet.next()) {
                ExistingCnt = empResultSet.getInt("DEPART_ID");
            }
            
            if (ExistingCnt == 0) {            	
                 jOutPut.put("STATUS_CD", "99");
                 jOutPut.put("MESSAGE", "Department Id " + DESIG_ID + " not Exists. Kindly Create First");
                 response = jOutPut.toString();
            } else {	  	                        		                       	
                 String NAME = NVL.StringNvl(jReuqest.getString("NAME"));                 
                 Date INACTIVE_DT = null;
                 if (jReuqest.getString("INACTIVE_DT") == null || jReuqest.getString("INACTIVE_DT").equals("")) {
                     INACTIVE_DT = null;
                 } else {
                     INACTIVE_DT = Date.valueOf(jReuqest.getString("INACTIVE_DT"));
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
  	            	String updateEmployee = "UPDATE deprtment_mst SET NAME = ?, COMP_CD = ?, DEPT_INACTIVE_DT = ?, ACTIVE_STATUS = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ?, LAST_ENTERED_DATE = ? WHERE DEPART_ID = ? AND COMP_CD = ? AND IS_DELETE = ?";	
  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
  	            	
  	                preparedStatement.setString(1,NAME);
  	                preparedStatement.setString(2,COMP_CD);
  	                preparedStatement.setDate(3,INACTIVE_DT);
  	                preparedStatement.setString(4,ACTIVE_STATUS);
  		            preparedStatement.setString(5,LAST_MODIFIED_BY);
  		            preparedStatement.setString(6,LAST_MODOFIED_IP);
  		            preparedStatement.setDate(7,LAST_MODIFIED_DT);
  		              		            
  		            preparedStatement.setInt(8,DESIG_ID);  		       
  		            preparedStatement.setString(9,COMP_CD);
  		            preparedStatement.setString(10,"N");
  		          
	                int row = preparedStatement.executeUpdate();
                    
	                if (row == 0) {
	                	con.rollback();
	                } else {
	                	con.commit();	                	                	                
	                }            
	                
	                jOutPut.put("STATUS_CD", "0");
	                jOutPut.put("MESSAGE", "Department ID " + DESIG_ID + " Sucessfully Updated.");
	                response = jOutPut.toString();
  	            } else {
  	            	String updateEmployee = "UPDATE designation_mst SET DEPART_ID = ?, NAME = ?, COMP_CD = ?, DEPT_INACTIVE_DT = ?, ACTIVE_STATUS = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ?, LAST_ENTERED_DATE = ? WHERE DESIG_ID = ? AND COMP_CD = ? AND IS_DELETE = ? AND ENTERED_BY = ?";	
  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
  	
  	                preparedStatement.setString(1,NAME);
  	                preparedStatement.setString(2,COMP_CD);
  	                preparedStatement.setDate(3,INACTIVE_DT);
  	                preparedStatement.setString(4,ACTIVE_STATUS);
  		            preparedStatement.setString(5,LAST_MODIFIED_BY);
  		            preparedStatement.setString(6,LAST_MODOFIED_IP);
  		            preparedStatement.setDate(7,LAST_MODIFIED_DT);
  		            
  		            preparedStatement.setInt(8,DESIG_ID);
  		            preparedStatement.setString(9,COMP_CD);
  		            preparedStatement.setString(10,"N");
  		            preparedStatement.setString(11,USERNAME);
  		          
  		          
  		            
	                int row = preparedStatement.executeUpdate();
                    
	                if (row == 0) {
	                	con.rollback();
	                } else {
	                	con.commit();	                	                	                
	                }            
	                
	                jOutPut.put("STATUS_CD", "0");
	                jOutPut.put("MESSAGE", "Department ID " + DESIG_ID + " Sucessfully Updated.");
	                response = jOutPut.toString();
  	            }	  	            	                        	               	               
            }                                                                                              
        } catch (Exception e) {
            Utility.PrintMessage("Error in Update Department : " + e);
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