package com.crm.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.ConnectionDbB;
import com.crm.utility.getDesignation;

public class core {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException {
		Connection con= ConnectionDbB.getCon();		
		JSONObject ls_response = contractService.getdropDwon(con, "{\r\n" + 
				"  \"USERNAME\": \"\",\r\n" + 
				"  \"REQUEST_IP\": \"\"\r\n" + 
				"}");
		System.out.println(ls_response.toString());
	}
}
