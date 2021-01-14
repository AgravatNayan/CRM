package com.crm.hrms;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.hrm.salary.salaryService;
import com.crm.hrms.branch.branchService;
import com.crm.hrms.company.companyService;
import com.crm.hrms.designation.designService;
import com.crm.hrms.dropdown.gredeService;
import com.crm.hrms.employee.getEmpList;
import com.crm.hrms.shift.shiftService;
import com.crm.utility.ConnectionDbB;
import com.crm.utility.getCategoryValue;
import com.crm.utility.getDesignation;
import com.crm.utility.getParaValue;

public class core {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException {
		Connection con= ConnectionDbB.getCon();		
		//JSONArray test = new JSONArray();
		String test = salaryService.UpdateSalaryHead(con,"{\r\n" + 
				"  \"UPDATE_FLAG\": \"Y\",\r\n" + 
				"  \"REQUEST_IP\": \"127.0.0.0\",\r\n" + 
				"  \"USERNAME\": \"MAULIK\",\r\n" + 
				"  \"TRAN_CD\": \"1\",\r\n" + 
				"  \"COMP_CD\": \"132\",\r\n" + 
				"  \"REQUEST_DATA\": {\r\n" + 
				"    \"TEMP_NAME\": \"teteteyvytvtyv\",\r\n" + 
				"    \"APP_FROM_DT\": \"2020-12-12\",\r\n" + 
				"    \"APP_TO_DT\": \"1997-12-12\",\r\n" + 
				"    \"LAST_ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"    \"LAST_ENTERED_BY\": \"sys\",\r\n" + 
				"    \"LAST_ENTERED_IP\": \"9.9.9.9\",\r\n" + 
				"    \"DELETE_SALARY_HEAD\": [\r\n" + 
				"      {\r\n" + 
				"        \"TRAN_CD\": \"1\",\r\n" + 
				"        \"SR_CD\": \"14\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"TRAN_CD\": \"1\",\r\n" + 
				"        \"SR_CD\": \"15\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"INSERT_SALARY_HEAD\": [\r\n" + 
				"      {\r\n" + 
				"        \"COMP_CD\": \"132\",\r\n" + 
				"        \"TRAN_CD\": \"1\",\r\n" + 
				"        \"SR_CD\": \"16\",\r\n" + 
				"        \"HEAD_CD\": \"AA\",\r\n" + 
				"        \"HEAD_NM\": \"adadadewe\",\r\n" + 
				"        \"ED_FLAG\": \"S\",\r\n" + 
				"        \"AMT\": \"10000\",\r\n" + 
				"        \"ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"        \"ENTERED_BY\": \"admin\",\r\n" + 
				"        \"ENTERED_IP\": \"9.9.9.9\",\r\n" + 
				"        \"LAST_ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"        \"LAST_ENTERED_BY\": \"sys\",\r\n" + 
				"        \"LAST_ENTERED_IP\": \"9.9.9.9\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"COMP_CD\": \"132\",\r\n" + 
				"        \"TRAN_CD\": \"1\",\r\n" + 
				"        \"SR_CD\": \"17\",\r\n" + 
				"        \"HEAD_CD\": \"ee\",\r\n" + 
				"        \"HEAD_NM\": \"asasd\",\r\n" + 
				"        \"ED_FLAG\": \"Z\",\r\n" + 
				"        \"AMT\": \"200\",\r\n" + 
				"        \"ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"        \"ENTERED_BY\": \"sys\",\r\n" + 
				"        \"ENTERED_IP\": \"9.9.9.9\",\r\n" + 
				"        \"LAST_ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"        \"LAST_ENTERED_BY\": \"sys\",\r\n" + 
				"        \"LAST_ENTERED_IP\": \"9.9.9.9\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"UPDATE_SALARY_HEAD\": [\r\n" + 
				"      {\r\n" + 
				"        \"COMP_CD\": \"132\",\r\n" + 
				"        \"TRAN_CD\": \"1\",\r\n" + 
				"        \"SR_CD\": \"13\",\r\n" + 
				"        \"HEAD_CD\": \"fe\",\r\n" + 
				"        \"HEAD_NM\": \"asdfsdsdsasd\",\r\n" + 
				"        \"ED_FLAG\": \"D\",\r\n" + 
				"        \"AMT\": \"2000\",\r\n" + 
				"        \"ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"        \"ENTERED_BY\": \"sys\",\r\n" + 
				"        \"ENTERED_IP\": \"9.9.9.9\",\r\n" + 
				"        \"LAST_ENTERED_DATE\": \"2020-12-12\",\r\n" + 
				"        \"LAST_ENTERED_BY\": \"sys\",\r\n" + 
				"        \"LAST_ENTERED_IP\": \"9.9.9.9\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  }\r\n" + 
				"}");
		System.out.println(test);
//		int ll_cnt = getParaValue.getMaxTranCd(con, "LOGIN_AUD_MST", "TRAN_CD");
//		System.out.println(ll_cnt);
		
	}
}
