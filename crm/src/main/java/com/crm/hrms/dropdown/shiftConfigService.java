package com.crm.hrms.dropdown;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class shiftConfigService {
	public static JSONObject getdropDwon(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {

	    
		int ll_shift_id  = 0;
		String ls_shift_nm = null;
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();
	   
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    String ls_comp_cd = request.getString("COMP_CD");
	    
	    if (ls_comp_cd.equals(null) || ls_comp_cd.equals("")) {            
            mainObject.put("STATUS_CD", "00");
            mainObject.put("RESPONSE", "Kindly check your request data.");
	    } else {
	    	  try {
		            String ls_query = "SELECT SHIFT_NAME,SHIFT_ID FROM SHIFT_CONFIG_MST WHERE COMP_CD = '"+ls_comp_cd+"' AND SHIFT_STATUS = 'Y'";
		           System.out.println(ls_query); 	            
		            Statement stmt = null;
		            stmt = con.createStatement();
		            rs = stmt.executeQuery(ls_query);

		            while (rs.next()) {
		            	ll_shift_id=rs.getInt("SHIFT_ID");
		            	ls_shift_nm=rs.getString("SHIFT_NAME");        			

		              jObject = new JSONObject();
		              jObject.put("ID", ll_shift_id);
		              jObject.put("NAME", ls_shift_nm);	             
		              jArray.put(jObject);

		            }

		            mainObject.put("STATUS_CD", "0");
		            mainObject.put("RESPONSE", jArray);
		          } catch(Exception e) {	            
		            System.out.println("Get Shift Type Drop Down Error : " + e);
		            e.printStackTrace();
		          }	       
	    }	    
	    return mainObject;
	  }
}
