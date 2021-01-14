package com.crm.hrms.dropdown;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class cityService {
	public static JSONObject getdropDwon(Connection con, String requestData) throws JSONException,
	  ClassNotFoundException,
	  SQLException {

	    int ll_id = 0;	  
	    String name = null;	  
	    JSONObject jObject = null;
	    JSONArray jArray = new JSONArray();
	    JSONObject mainObject = new JSONObject();
	    ResultSet rs = null;
	    JSONObject request = new JSONObject(requestData);
	    JSONObject jError = new JSONObject();

	   // String ls_dropdown = request.getString("DROP_DOWN");
	    String ls_username = request.getString("USERNAME");
	    String ls_req_ip = request.getString("REQUEST_IP");
	    int ll_state = request.getInt("STATE_ID");

	          try {
	            String ls_query = "SELECT ID,NAME FROM CITIES WHERE STATE_ID = " + ll_state;
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
	            System.out.println("Get Country Error : " + e);
	            e.printStackTrace();
	          }	     	   
	    return mainObject;
	  }
}
