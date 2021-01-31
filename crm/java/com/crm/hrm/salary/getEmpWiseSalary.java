package com.crm.hrm.salary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getEmpWiseSalary {

	public static String getSalary(Connection con,String request) throws NumberFormatException, JSONException {
		Statement stmt = null;
		ResultSet rs = null;
		String ls_query = null;
		
		JSONObject input = new JSONObject(request);
		JSONObject output = null;
		JSONArray buffer = new JSONArray();
		JSONObject response = new JSONObject();
		int month = Integer.parseInt(input.getString("SALARY_MONTH"));
		int totDays = Integer.parseInt(input.getString("TOT_MONTH_DAYS"));
		int weekOffDay = Integer.parseInt(input.getString("WEEK_OFF_DAYS"));
		int paiedDay = Integer.parseInt(input.getString("TOTAL_PAID_DAYS"));
		int year = Integer.parseInt(input.getString("SALARY_YEAR"));
		
		try {
			ls_query = "SELECT A.COMP_CD,A.EMP_ID,A.FIRST_NM,A.LAST_NM,A.DESIGNATION_ID, \r\n" + 
					"A.LEAVE_CNT, \r\n" + 
					"(CASE WHEN A.LEAVE_CNT = 0 THEN A.SALARY_AMOUNT\r\n" + 
					"ELSE (A.SALARY_AMOUNT - ((A.SALARY_AMOUNT / "+paiedDay+") * A.LEAVE_CNT)) \r\n" + 
					"END) AS SALARY_AMOUNT\r\n" + 
					"FROM (SELECT COMP_CD,EMP_ID,FIRST_NM,LAST_NM,DESIGNATION_ID,\r\n" + 
					"(SELECT COUNT(*) FROM EMP_LEAVE_MST\r\n" + 
					"WHERE EMP_LEAVE_MST.COMP_CD = EMP_MST.COMP_CD\r\n" + 
					"AND EMP_LEAVE_MST.EMP_ID = EMP_MST.EMP_ID\r\n" + 
					"AND EMP_LEAVE_MST.IS_DELETE = 'N'\r\n" + 
					"AND EXTRACT(MONTH FROM EMP_LEAVE_MST.SANCTION_DATE) ="+month+" \r\n" + 
					"AND EMP_LEAVE_MST.STATUS = 'Y') AS LEAVE_CNT,\r\n" + 
					"EMP_MST.SALARY_AMOUNT\r\n" + 
					"FROM EMP_MST \r\n" + 
					"WHERE ACTIVE_STATUS = 'Y' AND IS_DELETE = 'N' AND EXTRACT(YEAR FROM SYSDATE()) = "+year+") A\r\n"
							+ "WHERE (SELECT COUNT(*) FROM EMP_PAYSLIP_HDR \r\n" + 
							"		WHERE EMP_PAYSLIP_HDR.EMP_ID = A.EMP_ID\r\n" + 
							"        AND EMP_PAYSLIP_HDR.SALARY_MONTH BETWEEN (date(now()) - interval day(now()) day + interval 1 day) AND LAST_DAY(CURDATE())) <= 0 \r\n" + 
							"\r\n" + 
							"";
						
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {
				output = new JSONObject();
				output.put("COMP_CD",rs.getString("COMP_CD"));
				output.put("EMP_ID",rs.getString("EMP_ID"));
				output.put("NAME",rs.getString("FIRST_NM")+" "+rs.getString("LAST_NM"));
				output.put("DESIGNATION_ID",rs.getString("DESIGNATION_ID"));
				output.put("LEAVE_CNT",rs.getString("LEAVE_CNT"));
				output.put("SALARY_AMOUNT",rs.getString("SALARY_AMOUNT"));
				
				buffer.put(output);
			}
			response.put("STATUS_CD", "0");
			response.put("RESPONSE", buffer);
		} catch(Exception e) {
			response.put("STATUS","99");
			response.put("MESSAGE", "Error while get Employee Salary");
			response.put("ERROR",e.getMessage());
		}		
		return response.toString();
	}
}
