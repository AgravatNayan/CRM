package com.crm.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.ConnectionDbB;
import com.crm.utility.getCategoryValue;
import com.crm.utility.getDesignation;
import com.crm.utility.getParaValue;

public class core {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException {
		Connection con= ConnectionDbB.getCon();		
		String ls_response = departService.updateDepartment(con, "{\r\n" + 
				"  \"UPDATE_FLAG\": \"Y\",\r\n" + 
				"  \"REQUEST_IP\": \"127.0.0.0\",\r\n" + 
				"  \"USERNAME\": \"MAULIK\",\r\n" + 
				"  \"DEPART_ID\": \"2\",\r\n" + 
				"  \"COMP_CD\": \"132\",\r\n" + 
				"  \"REQUEST_DATA\": {\r\n" + 
				"    \"NAME\": \"sdfsfsdf\",\r\n" + 
				"    \"COMP_CD\": \"132\",\r\n" + 
				"    \"INACTIVE_DT\": \"\",\r\n" + 
				"    \"ACTIVE_STATUS\": \"Y\",\r\n" + 
				"    \"LAST_MODIFIED_BY\": \"admin\",\r\n" + 
				"    \"LAST_MODOFIED_IP\": \"127.9.0.0\",\r\n" + 
				"    \"LAST_MODIFIED_DT\": \"2020-12-12\"\r\n" + 
				"  }\r\n" + 
				"}");
		System.out.println(ls_response.toString());
//		int ll_cnt = getParaValue.getMaxTranCd(con, "LOGIN_AUD_MST", "TRAN_CD");
//		System.out.println(ll_cnt);
		
		 
	}
}
