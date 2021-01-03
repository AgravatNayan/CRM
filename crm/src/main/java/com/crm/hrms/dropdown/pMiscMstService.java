package com.crm.hrms.dropdown;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class pMiscMstService {
	public static JSONObject getdropDwon(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {

	    
		String ls_category  = null;
		String ls_data_val = null;
		String ls_display_val = null;
		
		
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();
	   
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    String ls_cat_cd = request.getString("CATEGORY_CD");
	    String ls_comp_cd = request.getString("COMP_CD");
	    
	    if (ls_cat_cd.equals(null) || ls_cat_cd.equals("")) {            
            mainObject.put("STATUS_CD", "999");
            mainObject.put("MESSAGE", "Kindly check your request data.");
	    } else {
	    	  try {
		            String ls_query = "SELECT CATEGORY_CD,DATA_VALUE,DISPLAY_VALUE FROM P_MISC_MST WHERE COMP_CD = '"+ls_comp_cd+"' AND CATEGORY_CD = '"+ls_cat_cd+"'";
		           System.out.println(ls_query); 	            
		            Statement stmt = null;
		            stmt = con.createStatement();
		            rs = stmt.executeQuery(ls_query);

		            while (rs.next()) {
		            	ls_category=rs.getString("CATEGORY_CD");
		            	ls_data_val=rs.getString("DATA_VALUE");        			
		            	ls_display_val = rs.getString("DISPLAY_VALUE");
		            	
		              jObject = new JSONObject();
		              jObject.put("CATEGORY", ls_category);
		              jObject.put("ID", ls_data_val);
		              jObject.put("NAME", ls_display_val);	             
		              jArray.put(jObject);

		            }

		            mainObject.put("STATUS_CD", "0");
		            mainObject.put("RESPONSE", jArray);
		          } catch(Exception e) {	            
		            System.out.println("Get P MISC MST "+ ls_cat_cd +" Drop Down Error : " + e);
		            e.printStackTrace();
		          }	       
	    }	    
	    return mainObject;
	  }
}
