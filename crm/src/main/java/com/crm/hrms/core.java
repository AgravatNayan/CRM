package com.crm.hrms;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.hrms.branch.branchService;
import com.crm.hrms.company.companyService;
import com.crm.hrms.shift.shiftService;
import com.crm.utility.ConnectionDbB;
import com.crm.utility.getCategoryValue;
import com.crm.utility.getDesignation;
import com.crm.utility.getParaValue;

public class core {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException {
		Connection con= ConnectionDbB.getCon();		
		String ls_response = companyService.GetMaxBoradMemtID(con, "{\r\n" + 
				"  \"USERNAME\": \"MAULIK\",\r\n" + 
				"  \"REQUEST_IP\": \"127.0.0.0\",\r\n" + 
				"  \"VIEW_FLAG\": \"G\"\r\n" + 
				"}");
		System.out.println(ls_response.toString());
//		int ll_cnt = getParaValue.getMaxTranCd(con, "LOGIN_AUD_MST", "TRAN_CD");
//		System.out.println(ll_cnt);
		
	}
}