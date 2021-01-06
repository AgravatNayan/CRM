package com.crm.hrms.branch;

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

public class branchService {
	public static JSONObject getBranchData(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {	    
	    String ls_comp_cd = null;
	    String ls_branch_cd = null;
	    String ls_branch_nm = null;
	    String ls_branch_opening_dt = null;
	    String ls_maneger = null;
	    String ls_branch_contact = null;
	    String ls_branch_email = null;
	    		    	    	   
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();

	    String ls_req_comp_cd = NVL.StringNvl(request.getString("COMP_CD"));
	    String ls_view_flag = "G";//NVL.StringNvl(request.getString("VIEW_FLAG")); 
	    String ls_username = NVL.StringNvl(request.getString("USERNAME"));
	    String ls_req_ip = NVL.StringNvl(request.getString("REQUEST_IP"));
	    
	          try {
	            String ls_query = null;
	            if (ls_view_flag.equals("G")) {
	            	ls_query = "SELECT COMP_CD,BRANCH_CD,BRANCH_NM,BRANCH_OPENING_DT,BRANCH_MANAGER,BRANCH_CONTACT,BRANCH_EMAIL FROM BRANCH_MST WHERE COMP_CD='"+ls_req_comp_cd+"'";
	            } else {
	            	ls_query = "SELECT COMP_CD,BRANCH_CD,BRANCH_NM,BRANCH_OPENING_DT,BRANCH_MANAGER,BRANCH_CONTACT,BRANCH_EMAIL FROM BRANCH_MST WHERE COMP_CD='"+ls_req_comp_cd+"' AND ENTERED_BY = '"+ls_username+"'";
	            }
	            System.out.println("Branch Data :"+ls_query);        
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {
	            	ls_comp_cd = NVL.StringNvl(rs.getString("COMP_CD"));
	         	    ls_branch_cd =  NVL.StringNvl(rs.getString("BRANCH_CD"));
	         	    ls_branch_nm =  NVL.StringNvl(rs.getString("BRANCH_NM"));
	         	    ls_branch_opening_dt =  NVL.StringNvl(rs.getString("BRANCH_OPENING_DT"));
	         	    ls_maneger = NVL.StringNvl(rs.getString("BRANCH_MANAGER"));
	         	    ls_branch_contact =  NVL.StringNvl(rs.getString("BRANCH_CONTACT"));
	         	    ls_branch_email = NVL.StringNvl(rs.getString("BRANCH_EMAIL"));

	              jObject = new JSONObject();	              
	              jObject.put("BRANCH_CD",ls_branch_cd);
	              jObject.put("BRANCH_NM", ls_branch_nm);
	              jObject.put("ID",ls_branch_cd);
	              jObject.put("NAME", ls_branch_nm);
	              jObject.put("BRANCH_OPENING_DT", ls_branch_opening_dt);
	              jObject.put("BRANCH_MANAGER",ls_maneger);
	              jObject.put("BRANCH_CONTACT", ls_branch_contact);
	              jObject.put("BRANCH_EMAIL", ls_branch_email);

	              jArray.put(jObject);

	            }
	            System.out.println("Branch Array Length :"+jArray.length()); 
	            if (jArray.length() > 0 ) {
	            	mainObject.put("STATUS_CD", "0");
		            mainObject.put("RESPONSE", jArray);
	            } else {
	            	mainObject.put("STATUS_CD", "99");
		            mainObject.put("RESPONSE", "Data Not Found");
	            }
	            
	          } catch(Exception e) {	            
	            System.out.println("Get Branch Error : " + e);
	            Utility.PrintMessage("Error in Get Branch Data : " + e);
	            
	            mainObject.put("STATUS_CD", "99");
	            mainObject.put("MESSAGE", "Something went to wrong,Please try after some time.");	            	            	           
	          }
	       
	    return mainObject;
	}	 
	
	public static JSONObject getMaxBranchList(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {	    
		
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();

	    String ls_req_comp_cd =  request.getString("COMP_CD");
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    String viewType = NVL.StringNvl(request.getString("VIEW_FLAG"));
	    String ls_query = null;
	          try {
	        	if (viewType.equals("G")) {
	        		ls_query = "SELECT COMP_CD, BRANCH_CD, BRANCH_NM, BRANCH_OPENING_DT, BRANCH_MANAGER, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, ACTIVE_STATUE, INACTIVE_DATE, LANDLINE_NO, CONTACT_PERSON, BRANCH_CONTACT, BRANCH_EMAIL, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP, LAST_ENTERED_DATE, ACTIVE_STATUS, INACTIVE_DT FROM branch_mst WHERE COMP_CD = '"+ls_req_comp_cd+"'";
	        	} else {
	        		ls_query = "SELECT COMP_CD, BRANCH_CD, BRANCH_NM, BRANCH_OPENING_DT, BRANCH_MANAGER, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, ACTIVE_STATUE, INACTIVE_DATE, LANDLINE_NO, CONTACT_PERSON, BRANCH_CONTACT, BRANCH_EMAIL, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP, LAST_ENTERED_DATE, ACTIVE_STATUS, INACTIVE_DT FROM branch_mst WHERE COMP_CD = '"+ls_req_comp_cd+"' AND ENTERED_BY = '"+ls_username+"'";
	        	}
	        	System.out.println("Branch List Query :"+ls_query);
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {	            	

	              jObject = new JSONObject();	              
	              jObject.put("COMP_CD",NVL.StringNvl(rs.getString("COMP_CD")));
	              jObject.put("BRANCH_CD",NVL.StringNvl(rs.getString("BRANCH_CD")));
	              jObject.put("BRANCH_NM",NVL.StringNvl(rs.getString("BRANCH_NM")));
	              jObject.put("BRANCH_OPENING_DT",NVL.StringNvl(rs.getString("BRANCH_OPENING_DT")));
	              jObject.put("BRANCH_MANAGER",NVL.StringNvl(rs.getString("BRANCH_MANAGER")));
	              jObject.put("ADDRESS_1",NVL.StringNvl(rs.getString("ADDRESS_1")));
	              jObject.put("ADDRESS_2",NVL.StringNvl(rs.getString("ADDRESS_2")));
	              jObject.put("ADDRESS_3",NVL.StringNvl(rs.getString("ADDRESS_3")));
	              jObject.put("CONTRAY_CD",NVL.StringNvl(rs.getString("CONTRAY_CD")));
	              jObject.put("STATE_CD",NVL.StringNvl(rs.getString("STATE_CD")));
	              jObject.put("CITY_CD",NVL.StringNvl(rs.getString("CITY_CD")));
	              jObject.put("PIN_CODE",NVL.StringNvl(rs.getString("PIN_CODE")));
	              jObject.put("ACTIVE_STATUE",NVL.StringNvl(rs.getString("ACTIVE_STATUE")));
	              jObject.put("INACTIVE_DATE",NVL.StringNvl(rs.getString("INACTIVE_DATE")));
	              jObject.put("LANDLINE_NO",NVL.StringNvl(rs.getString("LANDLINE_NO")));
	              jObject.put("CONTACT_PERSON",NVL.StringNvl(rs.getString("CONTACT_PERSON")));	            
	              jObject.put("BRANCH_CONTACT",NVL.StringNvl(rs.getString("BRANCH_CONTACT")));
	              jObject.put("BRANCH_EMAIL",NVL.StringNvl(rs.getString("BRANCH_EMAIL")));
	              jObject.put("ENTERED_BY",NVL.StringNvl(rs.getString("ENTERED_BY")));
	              jObject.put("ENTERED_IP",NVL.StringNvl(rs.getString("ENTERED_IP")));
	              jObject.put("ENTERED_DATE",NVL.StringNvl(rs.getString("ENTERED_DATE")));
	              jObject.put("LAST_ENTERED_BY",NVL.StringNvl(rs.getString("LAST_ENTERED_BY")));
	              jObject.put("LAST_ENTERED_IP",NVL.StringNvl(rs.getString("LAST_ENTERED_IP")));
	              jObject.put("LAST_ENTERED_DATE",NVL.StringNvl(rs.getString("LAST_ENTERED_DATE")));
	              jObject.put("ACTIVE_STATUS",NVL.StringNvl(rs.getString("ACTIVE_STATUS")));
	              jObject.put("INACTIVE_DT",NVL.StringNvl(rs.getString("INACTIVE_DT")));
	              jArray.put(jObject);

	            }
	            System.out.println("Branch List Array  :"+jArray.length());	            
	            if (jArray.length() <= 0) {
	            	mainObject.put("STATUS_CD", "99");
	 	            mainObject.put("MessageBox", "not rights");
	            } else {
	            	mainObject.put("STATUS_CD", "0");
	 	            mainObject.put("RESPONSE", jArray);
	            }	            	           
	          } catch(Exception e) {	            
	            System.out.println("Get Branch List Error : " + e);
	            Utility.PrintMessage("Error in Branch List : " + e);
	            
	            mainObject.put("STATUS_CD", "99");
	            mainObject.put("MESSAGE", "Something went to wrong,Please try after some time.");	            	            	           
	          }
	       
	    return mainObject;
	}	 
	
	public static JSONObject getMBranchId(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {	    
		
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();

	    String ls_req_comp_cd =  NVL.StringNvl(request.getString("COMP_CD"));
	    String ls_username = NVL.StringNvl(request.getString("USERNAME"));
	    String ls_req_ip = request.getString("REQUEST_IP");
	    String viewType = NVL.StringNvl(request.getString("VIEW_FLAG"));
	    String ls_branch = NVL.StringNvl(request.getString("BRANCH_CD"));
	    String ls_query = null;
	          try {
	        	if (viewType.equals("G")) {
	        		ls_query = "SELECT COMP_CD, BRANCH_CD, BRANCH_NM, BRANCH_OPENING_DT, BRANCH_MANAGER, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, ACTIVE_STATUE, INACTIVE_DATE, LANDLINE_NO, CONTACT_PERSON, BRANCH_CONTACT, BRANCH_EMAIL, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP, LAST_ENTERED_DATE, ACTIVE_STATUS, INACTIVE_DT FROM branch_mst WHERE COMP_CD = '"+ls_req_comp_cd+"' AND BRANCH_CD = '"+ls_branch+"' AND IS_DELETE = 'N'";
	        	} else {
	        		ls_query = "SELECT COMP_CD, BRANCH_CD, BRANCH_NM, BRANCH_OPENING_DT, BRANCH_MANAGER, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, ACTIVE_STATUE, INACTIVE_DATE, LANDLINE_NO, CONTACT_PERSON, BRANCH_CONTACT, BRANCH_EMAIL, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP, LAST_ENTERED_DATE, ACTIVE_STATUS, INACTIVE_DT FROM branch_mst WHERE COMP_CD = '"+ls_req_comp_cd+"' AND BRANCH_CD = '"+ls_branch+"' AND IS_DELETE = 'N' AND  ENTERED_BY = '"+ls_username+"'";
	        	}
	        	System.out.println("Branch Max Id Query:"+ls_query);
	            System.out.println(ls_query);
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {	            	

	              jObject = new JSONObject();	              
	              jObject.put("COMP_CD",NVL.StringNvl(rs.getString("COMP_CD")));
	              jObject.put("BRANCH_CD",NVL.StringNvl(rs.getString("BRANCH_CD")));
	              jObject.put("BRANCH_NM",NVL.StringNvl(rs.getString("BRANCH_NM")));
	              jObject.put("BRANCH_OPENING_DT",NVL.StringNvl(rs.getString("BRANCH_OPENING_DT")));
	              jObject.put("BRANCH_MANAGER",NVL.StringNvl(rs.getString("BRANCH_MANAGER")));
	              jObject.put("ADDRESS_1",NVL.StringNvl(rs.getString("ADDRESS_1")));
	              jObject.put("ADDRESS_2",NVL.StringNvl(rs.getString("ADDRESS_2")));
	              jObject.put("ADDRESS_3",NVL.StringNvl(rs.getString("ADDRESS_3")));
	              jObject.put("CONTRAY_CD",NVL.StringNvl(rs.getString("CONTRAY_CD")));
	              jObject.put("STATE_CD",NVL.StringNvl(rs.getString("STATE_CD")));
	              jObject.put("CITY_CD",NVL.StringNvl(rs.getString("CITY_CD")));
	              jObject.put("PIN_CODE",NVL.StringNvl(rs.getString("PIN_CODE")));
	              jObject.put("ACTIVE_STATUE",NVL.StringNvl(rs.getString("ACTIVE_STATUE")));
	              jObject.put("INACTIVE_DATE",NVL.StringNvl(rs.getString("INACTIVE_DATE")));
	              jObject.put("LANDLINE_NO",NVL.StringNvl(rs.getString("LANDLINE_NO")));
	              jObject.put("CONTACT_PERSON",NVL.StringNvl(rs.getString("CONTACT_PERSON")));	             
	              jObject.put("BRANCH_CONTACT",NVL.StringNvl(rs.getString("BRANCH_CONTACT")));
	              jObject.put("BRANCH_EMAIL",NVL.StringNvl(rs.getString("BRANCH_EMAIL")));
	              jObject.put("ENTERED_BY",NVL.StringNvl(rs.getString("ENTERED_BY")));
	              jObject.put("ENTERED_IP",NVL.StringNvl(rs.getString("ENTERED_IP")));
	              jObject.put("ENTERED_DATE",NVL.StringNvl(rs.getString("ENTERED_DATE")));
	              jObject.put("LAST_ENTERED_BY",NVL.StringNvl(rs.getString("LAST_ENTERED_BY")));
	              jObject.put("LAST_ENTERED_IP",NVL.StringNvl(rs.getString("LAST_ENTERED_IP")));
	              jObject.put("LAST_ENTERED_DATE",NVL.StringNvl(rs.getString("LAST_ENTERED_DATE")));
	              jObject.put("ACTIVE_STATUS",NVL.StringNvl(rs.getString("ACTIVE_STATUS")));
	              jObject.put("INACTIVE_DT",NVL.StringNvl(rs.getString("INACTIVE_DT")));
	              jArray.put(jObject);

	            }
	            System.out.println("Branch Max Id Array Size:"+jArray.length());
	            if (jArray.length() <= 0) {
	            	mainObject.put("STATUS_CD", "99");
	 	            mainObject.put("MessageBox", "not rights");
	            } else {
	            	mainObject.put("STATUS_CD", "0");
	 	            mainObject.put("RESPONSE", jArray);
	            }
	          } catch(Exception e) {	            
	            System.out.println("Get Branch Error : " + e);
	            Utility.PrintMessage("Error in Branch List : " + e);
	            
	            mainObject.put("STATUS_CD", "99");
	            mainObject.put("MESSAGE", "Something went to wrong,Please try after some time.");	            	            	           
	          }
	       
	    return mainObject;
	}	 
	
	 public static String deleteBranchID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);                                
	            JSONObject jOutPut = new JSONObject();
	            
	            String BRANCH_CD = jin.getString("BRANCH_CD");  
	            String comp_cd= jin.getString("COMP_CD");
	            String ls_username= jin.getString("USERNAME");
	            String ls_del_flag = jin.getString("DELETE_FLAG");
	            System.out.println("Branch Delete User Rights:"+ls_del_flag+" For User :"+ls_username);
	            if(ls_del_flag.equals("Y")) {
	            	 String extingEmp = "UPDATE BRANCH_MST SET IS_DELETE = 'Y' WHERE BRANCH_CD  = " +BRANCH_CD+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
	                 stmt = con.createStatement();
	                 System.out.println("Branch Delete Query :"+extingEmp);
	                 int row = stmt.executeUpdate(extingEmp);
	                 System.out.println("Branch Delete Row Count :"+row);
	                 if (row > 0) {
	                 	con.commit();
	                 	 jOutPut.put("STATUS_CD", "0");	
	                 	jOutPut.put("MESSAGE", "Branch Id " + BRANCH_CD + " Sucessfully Deleted.");
	                 } else {
	                 	con.rollback();
	                 	 jOutPut.put("STATUS_CD", "99");	
	                 	jOutPut.put("MESSAGE", "Branch Already Deleted.");
	                 }	                 			                
	            } else {
	            	jOutPut.put("STATUS_CD", "99");	
	            	jOutPut.put("MESSAGE", "Delete Not Allow.");
	            }             	                                                        
	            response = jOutPut.toString();
	                                                                                                     
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Delete Branch : " + e);
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
	 
	 public static String  leftPadding(String input, char ch, int L) 
	    { 
	  
	        String result = String.format("%" + L + "s", input).replace(' ', ch); 

	        return result; 
	    } 
	 
	  public static String GetMaxBranchtID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        try {
	            JSONObject jin = new JSONObject(ls_request);
	            JSONObject jOut = new JSONObject();
	            String sql = "SELECT MAX(BRANCH_CD) AS BRANCH_CD FROM branch_mst";
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(sql);
	            int maxID = 0;
	            if (rs.next()) {
	                maxID = rs.getInt("BRANCH_CD");
	            }
	            maxID = maxID + 1;
	            String id = String.valueOf(maxID);
	            JSONArray jresponse = new JSONArray();
	            JSONObject jobj = new JSONObject();
	            jobj.put("USERNAME", jin.getString("USERNAME"));
	            jobj.put("BRANCH_CD", leftPadding(id ,'0',3));
	            jresponse.put(jobj);
	            jOut.put("STATUS_CD", "0");
	            jOut.put("RESPONSE", jresponse);
	            response = jOut.toString();
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in GetMax Branch Id : " + e);
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
	  
	  public static String CreateBranch(Connection con, String ls_request) {
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
	            
	            String COMP_CD = NVL.StringNvl(jReuqest.getString("COMP_CD"));
	            String BRANCH_CD = NVL.StringNvl(jReuqest.getString("BRANCH_CD"));
	            String BRANCH_NM= NVL.StringNvl(jReuqest.getString("BRANCH_NM"));
	            Date BRANCH_OPENING_DT = null;
	            if (jReuqest.getString("BRANCH_OPENING_DT") == null || jReuqest.getString("BRANCH_OPENING_DT").equals("")) {
	            	BRANCH_OPENING_DT = null;
	            } else {
	            	BRANCH_OPENING_DT = Date.valueOf(jReuqest.getString("BRANCH_OPENING_DT"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            
	            String BRANCH_MANAGER= NVL.StringNvl(jReuqest.getString("BRANCH_MANAGER"));
	            String ADDRESS_1= NVL.StringNvl(jReuqest.getString("ADDRESS_1"));
	            String ADDRESS_2= NVL.StringNvl(jReuqest.getString("ADDRESS_2"));
	            String ADDRESS_3= NVL.StringNvl(jReuqest.getString("ADDRESS_3"));
	            int CONTRAY_CD = 0;
	            if (jReuqest.getString("CONTRAY_CD") == null || jReuqest.getString("CONTRAY_CD").equals("")) {
	            	CONTRAY_CD = 0;
	            } else {
	            	CONTRAY_CD = jReuqest.getInt("CONTRAY_CD");
	            }
	            int STATE_CD = 0;
	            if (jReuqest.getString("STATE_CD") == null || jReuqest.getString("STATE_CD").equals("")) {
	            	STATE_CD = 0;
	            } else {
	            	STATE_CD = jReuqest.getInt("STATE_CD");
	            }
	            int CITY_CD = 0;
	            if (jReuqest.getString("CITY_CD") == null || jReuqest.getString("CITY_CD").equals("")) {
	            	STATE_CD = 0;
	            } else {
	            	STATE_CD = jReuqest.getInt("CITY_CD");
	            }
	            		
	            String PIN_CODE =NVL.StringNvl(jReuqest.getString("PIN_CODE"));
	            String ACTIVE_STATUE =NVL.StringNvl(jReuqest.getString("ACTIVE_STATUS"));
	            Date INACTIVE_DATE = null;
	            if (jReuqest.getString("INACTIVE_DATE") == null || jReuqest.getString("INACTIVE_DATE").equals("")) {
	            	INACTIVE_DATE = null;
	            } else {
	            	INACTIVE_DATE = Date.valueOf(jReuqest.getString("INACTIVE_DATE"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            int LANDLINE_NO = 0;
	            if (jReuqest.getString("LANDLINE_NO") == null || jReuqest.getString("LANDLINE_NO").equals("")) {
	            	LANDLINE_NO = 0;
	            } else {
	            	LANDLINE_NO = jReuqest.getInt("LANDLINE_NO");
	            }
	            String CONTACT_PERSON = NVL.StringNvl(jReuqest.getString("CONTACT_PERSON"));
	            int BRANCH_CONTACT = 0;
	            if (jReuqest.getString("BRANCH_CONTACT") == null || jReuqest.getString("BRANCH_CONTACT").equals("")) {
	            	BRANCH_CONTACT = 0;
	            } else {
	            	BRANCH_CONTACT = jReuqest.getInt("BRANCH_CONTACT");
	            }
	            String BRANCH_EMAIL= NVL.StringNvl(jReuqest.getString("BRANCH_EMAIL"));
	            String ENTERED_BY= NVL.StringNvl(jReuqest.getString("ENTERED_BY"));
	            String ENTERED_IP = NVL.StringNvl(jReuqest.getString("ENTERED_IP"));
	            Date ENTERED_DATE = null;
	            if (jReuqest.getString("ENTERED_DATE") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
	            	ENTERED_DATE = null;
	            } else {
	            	ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String LAST_ENTERED_BY  = NVL.StringNvl(jReuqest.getString("LAST_ENTERED_BY"));
	            String LAST_ENTERED_IP = NVL.StringNvl(jReuqest.getString("LAST_ENTERED_IP"));
	            Date LAST_ENTERED_DATE = null;
	            if (jReuqest.getString("LAST_ENTERED_DATE") == null || jReuqest.getString("LAST_ENTERED_DATE").equals("")) {
	            	LAST_ENTERED_DATE = null;
	            } else {
	            	LAST_ENTERED_DATE = Date.valueOf(jReuqest.getString("LAST_ENTERED_DATE"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            String ACTIVE_STATUS= NVL.StringNvl(jReuqest.getString("ACTIVE_STATUS"));
	            Date INACTIVE_DT = null;
	            if (jReuqest.getString("INACTIVE_DT") == null || jReuqest.getString("INACTIVE_DT").equals("")) {
	            	INACTIVE_DT = null;
	            } else {
	            	INACTIVE_DT = Date.valueOf(jReuqest.getString("INACTIVE_DT"));
	                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
	            
	            JSONObject jOut = new JSONObject();
	            String sql = "INSERT INTO branch_mst (COMP_CD, BRANCH_CD, BRANCH_NM, BRANCH_OPENING_DT, BRANCH_MANAGER, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, ACTIVE_STATUE, INACTIVE_DATE, LANDLINE_NO, CONTACT_PERSON, BRANCH_CONTACT, BRANCH_EMAIL, ENTERED_BY, ENTERED_IP, ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP, LAST_ENTERED_DATE, ACTIVE_STATUS, INACTIVE_DT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            System.out.println("Create Branch Query :"+sql);
	            if (CREATE_FLAG.equals("Y")) {
	                PreparedStatement preparedStatement = con.prepareStatement(sql);
	                		preparedStatement.setString(1,COMP_CD);
	                		preparedStatement.setString(2,BRANCH_CD);
	                		preparedStatement.setString(3,BRANCH_NM);
	                		preparedStatement.setDate(4,BRANCH_OPENING_DT);
	                		preparedStatement.setString(5,BRANCH_MANAGER);
	                		preparedStatement.setString(6,ADDRESS_1);
	                		preparedStatement.setString(7,ADDRESS_2);
	                		preparedStatement.setString(8,ADDRESS_3);
	                		preparedStatement.setInt(9,CONTRAY_CD);
	                		preparedStatement.setInt(10,STATE_CD);
	                		preparedStatement.setInt(11,CITY_CD);
	                		preparedStatement.setString(12,PIN_CODE);
	                		preparedStatement.setString(13,ACTIVE_STATUE);
	                		preparedStatement.setDate(14,INACTIVE_DATE);
	                		preparedStatement.setInt(15,LANDLINE_NO);
	                		preparedStatement.setString(16,CONTACT_PERSON);	                		
	                		preparedStatement.setInt(17,BRANCH_CONTACT);
	                		preparedStatement.setString(18,BRANCH_EMAIL);
	                		preparedStatement.setString(19,ENTERED_BY);
	                		preparedStatement.setString(20,ENTERED_IP);
	                		preparedStatement.setDate(21,ENTERED_DATE);
	                		preparedStatement.setString(22,LAST_ENTERED_BY);
	                		preparedStatement.setString(23,LAST_ENTERED_IP);
	                		preparedStatement.setDate(24,LAST_ENTERED_DATE);
	                		preparedStatement.setString(25,ACTIVE_STATUS);
	                		preparedStatement.setDate(26,INACTIVE_DT);

	                int row = preparedStatement.executeUpdate();

	                if (row == 0) {
	                    con.rollback();
	                } else {
	                    con.commit();
	                }

	                JSONObject jOutPut = new JSONObject();

	                jOutPut.put("STATUS_CD", "0");
	                jOutPut.put("MESSAGE", "Branch ID " + BRANCH_CD + " Sucessfully Created.");
	                response = jOutPut.toString();
	            } else {
	                JSONObject jOutPut = new JSONObject();

	                jOutPut.put("STATUS_CD", "99");
	                jOutPut.put("MESSAGE", "Create not allow.");
	                response = jOutPut.toString();
	            }


	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Create Branch ID : " + e);
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
	  
	  public static String updateBranchID(Connection con, String ls_request) {
	        String response = "";
	        Statement stmt = null;
	        ResultSet rs = null;
	        int ExistingCnt = 0;
	        try {
	            JSONObject jin = new JSONObject(ls_request);            
	            JSONObject jReuqest = new JSONObject();
	            jReuqest = jin.getJSONObject("REQUEST_DATA");
	            JSONObject jOutPut = new JSONObject();
	            
	            String BRANCH_CD = jin.getString("BRANCH_CD"); 
	            String COMP_CD = jin.getString("COMP_CD");
	            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
	            String USERNAME = jin.getString("USERNAME");
	            String extingEmp = "SELECT COUNT(*) AS BRANCH_CD FROM branch_mst WHERE COMP_CD = '"+COMP_CD+"' AND BRANCH_CD = '" +BRANCH_CD+"' AND IS_DELETE = 'N'";	        
	            stmt = con.createStatement();
	            ResultSet empResultSet = stmt.executeQuery(extingEmp);
	            		
	            while (empResultSet.next()) {
	                ExistingCnt = empResultSet.getInt("BRANCH_CD");
	            }
	            
	            if (ExistingCnt == 0) {            	
	                 jOutPut.put("STATUS_CD", "99");
	                 jOutPut.put("MESSAGE", "Branch Id " + BRANCH_CD + " not Exists. Kindly Create First");
	                 response = jOutPut.toString();
	            } else {	  	            
	            	
	            	String BRANCH_NM= NVL.StringNvl(jReuqest.getString("BRANCH_NM"));
		            Date BRANCH_OPENING_DT = null;
		            if (jReuqest.getString("BRANCH_OPENING_DT") == null || jReuqest.getString("BRANCH_OPENING_DT").equals("")) {
		            	BRANCH_OPENING_DT = null;
		            } else {
		            	BRANCH_OPENING_DT = Date.valueOf(jReuqest.getString("BRANCH_OPENING_DT"));
		                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            String BRANCH_MANAGER= NVL.StringNvl(jReuqest.getString("BRANCH_MANAGER"));
		            String ADDRESS_1= NVL.StringNvl(jReuqest.getString("ADDRESS_1"));
		            String ADDRESS_2= NVL.StringNvl(jReuqest.getString("ADDRESS_2"));
		            String ADDRESS_3= NVL.StringNvl(jReuqest.getString("ADDRESS_3"));
		            int CONTRAY_CD = 0;
		            if (jReuqest.getString("CONTRAY_CD") == null || jReuqest.getString("CONTRAY_CD").equals("")) {
		            	CONTRAY_CD = 0;
		            } else {
		            	CONTRAY_CD = jReuqest.getInt("CONTRAY_CD");
		            }
		            int STATE_CD = 0;
		            if (jReuqest.getString("STATE_CD") == null || jReuqest.getString("STATE_CD").equals("")) {
		            	STATE_CD = 0;
		            } else {
		            	STATE_CD = jReuqest.getInt("STATE_CD");
		            }
		            int CITY_CD = 0;
		            if (jReuqest.getString("CITY_CD") == null || jReuqest.getString("CITY_CD").equals("")) {
		            	STATE_CD = 0;
		            } else {
		            	STATE_CD = jReuqest.getInt("CITY_CD");
		            }
		            		
		            String PIN_CODE =NVL.StringNvl(jReuqest.getString("PIN_CODE"));
		            String ACTIVE_STATUE =NVL.StringNvl(jReuqest.getString("ACTIVE_STATUE"));
		            Date INACTIVE_DATE = null;
		            if (jReuqest.getString("INACTIVE_DATE") == null || jReuqest.getString("INACTIVE_DATE").equals("")) {
		            	INACTIVE_DATE = null;
		            } else {
		            	INACTIVE_DATE = Date.valueOf(jReuqest.getString("INACTIVE_DATE"));
		                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            int LANDLINE_NO = 0;
		            if (jReuqest.getString("LANDLINE_NO") == null || jReuqest.getString("LANDLINE_NO").equals("")) {
		            	LANDLINE_NO = 0;
		            } else {
		            	LANDLINE_NO = jReuqest.getInt("LANDLINE_NO");
		            }
		            String CONTACT_PERSON = NVL.StringNvl(jReuqest.getString("CONTACT_PERSON"));
		            int BRANCH_CONTACT = 0;
		            if (jReuqest.getString("BRANCH_CONTACT") == null || jReuqest.getString("BRANCH_CONTACT").equals("")) {
		            	BRANCH_CONTACT = 0;
		            } else {
		            	BRANCH_CONTACT = jReuqest.getInt("BRANCH_CONTACT");
		            }
		            String BRANCH_EMAIL= NVL.StringNvl(jReuqest.getString("BRANCH_EMAIL"));
		          
		            String LAST_ENTERED_BY  = NVL.StringNvl(jReuqest.getString("LAST_ENTERED_BY"));
		            String LAST_ENTERED_IP = NVL.StringNvl(jReuqest.getString("LAST_ENTERED_IP"));
		            Date LAST_ENTERED_DATE = null;
		            if (jReuqest.getString("LAST_ENTERED_DATE") == null || jReuqest.getString("LAST_ENTERED_DATE").equals("")) {
		            	LAST_ENTERED_DATE = null;
		            } else {
		            	LAST_ENTERED_DATE = Date.valueOf(jReuqest.getString("LAST_ENTERED_DATE"));
		                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            String ACTIVE_STATUS= NVL.StringNvl(jReuqest.getString("ACTIVE_STATUS"));
		            Date INACTIVE_DT = null;
		            if (jReuqest.getString("INACTIVE_DT") == null || jReuqest.getString("INACTIVE_DT").equals("")) {
		            	INACTIVE_DT = null;
		            } else {
		            	INACTIVE_DT = Date.valueOf(jReuqest.getString("INACTIVE_DT"));
		                //	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
		            }
		            
		            
	  	            if (UPDATE_FLAG.equals("Y")) {
	  	            	String updateEmployee = "UPDATE branch_mst SET COMP_CD = ?, BRANCH_NM = ?, BRANCH_OPENING_DT = ?, ADDRESS_1 = ?, ADDRESS_2 = ?, ADDRESS_3 = ?, CONTRAY_CD = ?, STATE_CD = ?, CITY_CD = ?, PIN_CODE = ?, ACTIVE_STATUE = ?, INACTIVE_DATE = ?, LANDLINE_NO = ?, CONTACT_PERSON = ?, BRANCH_CONTACT = ?, BRANCH_EMAIL = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ?, LAST_ENTERED_DATE = ?, ACTIVE_STATUS = ?, INACTIVE_DT = ? WHERE COMP_CD = ? AND BRANCH_CD = ? AND IS_DELETE = ?";	
	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               

	  	            	preparedStatement.setString(1,COMP_CD);
	  	                preparedStatement.setString(2,BRANCH_NM);
	  	                preparedStatement.setDate(3,BRANCH_OPENING_DT);
	  	                preparedStatement.setString(4,ADDRESS_1);
	  	                preparedStatement.setString(5,ADDRESS_2);
	  	                preparedStatement.setString(6,ADDRESS_3);
	  		            preparedStatement.setInt(7,CONTRAY_CD);
	  		            preparedStatement.setInt(8,STATE_CD);
	  		            preparedStatement.setInt(9,CITY_CD);
	  		            preparedStatement.setString(10,PIN_CODE);
	  	                preparedStatement.setString(11,ACTIVE_STATUE);
	  	                preparedStatement.setDate(12,INACTIVE_DATE);
	  	                preparedStatement.setInt(13,LANDLINE_NO);
	  	                preparedStatement.setString(14,CONTACT_PERSON);
	  	                preparedStatement.setInt(15,BRANCH_CONTACT);
	  	                preparedStatement.setString(16,BRANCH_EMAIL);
	  		            preparedStatement.setString(17,LAST_ENTERED_BY);
	  		            preparedStatement.setString(18,LAST_ENTERED_IP);
	  		            preparedStatement.setDate(19,LAST_ENTERED_DATE);
	  		            preparedStatement.setString(20,ACTIVE_STATUS);
	  		            preparedStatement.setDate(21,INACTIVE_DT);
	  		            
	  		            preparedStatement.setString(22,COMP_CD);
	  		            preparedStatement.setString(23,BRANCH_CD);
	  		            preparedStatement.setString(24,"N");
	  		            
	  		            
		                int row = preparedStatement.executeUpdate();
                      
		                if (row == 0) {
		                	con.rollback();
		                } else {
		                	con.commit();	                	                	                
		                }            
		                
		                jOutPut.put("STATUS_CD", "0");
		                jOutPut.put("MESSAGE", "BRANCH " + BRANCH_CD + " Sucessfully Updated.");
		                response = jOutPut.toString();
	  	            } else {
//	  	            	String updateEmployee = "UPDATE branch_mst SET COMP_CD = ?, BRANCH_NM = ?, BRANCH_OPENING_DT = ?, BRANCH_MANAGER = ?, ADDRESS_1 = ?, ADDRESS_2 = ?, ADDRESS_3 = ?, CONTRAY_CD = ?, STATE_CD = ?, CITY_CD = ?, PIN_CODE = ?, ACTIVE_STATUE = ?, INACTIVE_DATE = ?, LANDLINE_NO = ?, CONTACT_PERSON = ?, BRANCH_CONTACT = ?, BRANCH_EMAIL = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ?, LAST_ENTERED_DATE = ?, ACTIVE_STATUS = ?, INACTIVE_DT = ? WHERE COMP_CD = ? AND BRANCH_CD = ? AND IS_DELETE = ? AND ENTERED_BY = ?";	
//	  	            	PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);	               
//	  	            	preparedStatement.setString(1,COMP_CD);
//	  	                preparedStatement.setString(2,BRANCH_NM);
//	  	                preparedStatement.setDate(3,BRANCH_OPENING_DT);
//	  	                preparedStatement.setDate(4,BRANCH_OPENING_DT);
//	  	                preparedStatement.setString(5,ADDRESS_1);
//	  	                preparedStatement.setString(6,ADDRESS_2);
//	  	                preparedStatement.setString(7,ADDRESS_3);
//	  		            preparedStatement.setInt(8,CONTRAY_CD);
//	  		            preparedStatement.setInt(9,STATE_CD);
//	  		            preparedStatement.setInt(10,CITY_CD);
//	  		            preparedStatement.setString(11,PIN_CODE);
//	  	                preparedStatement.setString(12,ACTIVE_STATUE);
//	  	                preparedStatement.setDate(13,INACTIVE_DATE);
//	  	                preparedStatement.setInt(14,LANDLINE_NO);
//	  	                preparedStatement.setString(15,CONTACT_PERSON);
//	  	                preparedStatement.setInt(16,BRANCH_CONTACT);
//	  	                preparedStatement.setString(17,BRANCH_EMAIL);
//	  		            preparedStatement.setString(18,LAST_ENTERED_BY);
//	  		            preparedStatement.setString(19,LAST_ENTERED_IP);
//	  		            preparedStatement.setDate(20,LAST_ENTERED_DATE);
//	  		            preparedStatement.setString(21,ACTIVE_STATUS);
//	  		            preparedStatement.setDate(22,INACTIVE_DT);
//	  		            
//	  		            preparedStatement.setString(21,COMP_CD);
//	  		            preparedStatement.setString(22,BRANCH_CD);
//	  		            preparedStatement.setString(23,"N");
//	  		            preparedStatement.setString(24,USERNAME);
//	  		          	  		        
//		                int row = preparedStatement.executeUpdate();
//                      
//		                if (row == 0) {
//		                	con.rollback();
//		                } else {
//		                	con.commit();	                	                	                
//		                }   
	  	            	
		                jOutPut.put("STATUS_CD", "99");
		                jOutPut.put("MESSAGE", "Update not allow.");		                
		                response = jOutPut.toString();
	  	            }	  	            	                        	               	               
	            }                                                                                              
	        } catch (Exception e) {
	            Utility.PrintMessage("Error in Update Branch : " + e);
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
