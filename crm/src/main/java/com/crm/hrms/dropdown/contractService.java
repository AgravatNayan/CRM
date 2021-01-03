package com.crm.hrms.dropdown;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class contractService {
	public static JSONObject getdropDwon(Connection con, String requestData) throws JSONException,
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
	    String ls_sortname = null;	    
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
	            String ls_query = "SELECT COMP_CD,CONTARCT_ID,CON_START_DT,CON_END_DT,SALARY_TEMP_CD,CONTRACT_STATUS,CONTRACT_INACTIVE_DT,CONTRACT_NM\r\n" + 
	            		"FROM CONTRACT_TYPE_MST WHERE CONTRACT_STATUS = 'Y'";
	            	            
	            Statement stmt = null;
	            stmt = con.createStatement();
	            rs = stmt.executeQuery(ls_query);

	            while (rs.next()) {
	            	ls_comp_cd=rs.getString("COMP_CD");        			
        			ID=rs.getString("CONTARCT_ID");
        			ld_start_dt=rs.getString("CON_START_DT");
        			ld_end_dt=rs.getString("CON_END_DT");
        			ll_sal_temp_id=rs.getString("SALARY_TEMP_CD");
        			ls_con_status=rs.getString("CONTRACT_STATUS");
        			ld_inactive_dt=rs.getString("CONTRACT_INACTIVE_DT");
        			ls_con_nm=rs.getString("CONTRACT_NM");

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

	            mainObject.put("STATUS_CD", "0");
	            mainObject.put("RESPONSE", jArray);
	          } catch(Exception e) {	            
	            System.out.println("Get Grade Error : " + e);
	            e.printStackTrace();
	          }	       
	    return mainObject;
	  }
}
