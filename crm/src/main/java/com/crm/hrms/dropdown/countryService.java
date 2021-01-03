package com.crm.hrms.dropdown;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class countryService {

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
            String ls_query = "SELECT ID,SORTNAME,NAME,PHONECODE FROM COUNTRIES";
            Statement stmt = null;
            stmt = con.createStatement();
            rs = stmt.executeQuery(ls_query);

            while (rs.next()) {
              ll_id = rs.getInt("ID");
              ls_sortname = rs.getString("SORTNAME");
              name = rs.getString("NAME");
              ls_phcode = rs.getString("PHONECODE");

              jObject = new JSONObject();
              jObject.put("NAME", name);
              jObject.put("ID", ll_id);
              jObject.put("SHORT_NM", ls_sortname);
              jObject.put("PH_CD", ls_phcode);

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