package com.crm.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class leavelService {
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
	            String ls_query = "SELECT ID,NAME FROM USER_LEVEL_MST";
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
	            System.out.println("Get User Level Error : " + e);
	            e.printStackTrace();
	    }	       
	    return mainObject;
	  }
}
