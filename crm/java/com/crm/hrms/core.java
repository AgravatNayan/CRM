package com.crm.hrms;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.common.createJsonData;
import com.crm.common.createJsonList;
import com.crm.common.getMaxIdentityCd;
import com.crm.hrm.salary.getEmpWiseSalary;
import com.crm.hrm.salary.leaveService;
import com.crm.hrm.salary.salaryService;
import com.crm.hrms.branch.branchService;
import com.crm.hrms.company.companyService;
import com.crm.hrms.department.departService;
import com.crm.hrms.designation.designService;
import com.crm.hrms.dropdown.gredeService;
import com.crm.hrms.employee.getEmpList;
import com.crm.hrms.shift.shiftService;
import com.crm.utility.ConnectionDbB;
import com.crm.utility.getCategoryValue;
import com.crm.utility.getDesignation;
import com.crm.utility.getEmployeeName;
import com.crm.utility.getMaxTranCd;
import com.crm.utility.getParaValue;

public class core {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException {
		Connection con= ConnectionDbB.getCon();		
		
		String test = getEmpWiseSalary.getSalary(con, "{\r\n" + 
				"  \"SALARY_MONTH\": \"3\",\r\n" + 
				"  \"SALARY_YEAR\":\"2021\",\r\n" + 
				"  \"TOT_MONTH_DAYS\": \"31\",\r\n" + 
				"  \"WEEK_OFF_DAYS\": \"5\",\r\n" + 
				"  \"TOTAL_PAID_DAYS\": \"26\"\r\n" + 
				"}");
//		JSONObject test = new JSONObject();
//		test=createJsonList.createJsonColumnWiseList(con,"crm","emp_mst","WHERE COMP_CD = '132' AND EMP_ID = 1 AND IS_DELETE = 'N'","EMP_ID,FIRST_NM,MIDDEL_NM,LAST_NM,DEPARTMENT_ID,DESIGNATION_ID,BRANCH_CD");
//		String test = getEmployeeName.getEmpName(con,1);
		
//		int tran_cd = getMaxTranCd.getMaxTranCD(con, "EMP_PAYSLIP_HDR");
		System.out.println(test);
	}
}
