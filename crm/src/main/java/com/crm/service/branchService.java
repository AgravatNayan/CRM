package com.crm.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	    String ls_req_comp_cd = "132";
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    
	          try {
	            String ls_query = "SELECT COMP_CD,BRANCH_CD,BRANCH_NM,BRANCH_OPENING_DT,BRANCH_MANAGER,BRANCH_CONTACT,BRANCH_EMAIL FROM BRANCH_MST WHERE COMP_CD='"+ls_req_comp_cd+"'";
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {
	            	ls_comp_cd = rs.getString("COMP_CD");
	         	    ls_branch_cd =  rs.getString("BRANCH_CD");;
	         	    ls_branch_nm =  rs.getString("BRANCH_NM");;
	         	    ls_branch_opening_dt =  rs.getString("BRANCH_OPENING_DT");;
	         	    ls_maneger =  rs.getString("BRANCH_MANAGER");;
	         	    ls_branch_contact =  rs.getString("BRANCH_CONTACT");;
	         	    ls_branch_email =  rs.getString("BRANCH_EMAIL");;

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

	            mainObject.put("STATUS_CD", "0");
	            mainObject.put("RESPONSE", jArray);
	          } catch(Exception e) {	            
	            System.out.println("Get Branch Error : " + e);
	            Utility.PrintMessage("Error in GetMax Employee : " + e);
	            
	            mainObject.put("STATUS_CD", "99");
	            mainObject.put("MESSAGE", "Something went to wrong,Please try after some time.");	            	            	           
	          }
	       
	    return mainObject;
	}	 
}
