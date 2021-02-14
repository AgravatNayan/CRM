package com.crm.hrm.salary;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.NVL;
import com.crm.utility.getDesignation;
import com.crm.utility.getMaxTranCd;

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
			ls_query = "SELECT \r\n" + 
					"    A.COMP_CD,\r\n" + 
					"    A.EMP_ID,\r\n" + 
					"    A.FIRST_NM,\r\n" + 
					"    A.LAST_NM,\r\n" + 
					"    A.DESIGNATION_ID,\r\n" + 
					"    A.LEAVE_CNT AS LEAVE_CNT,\r\n" + 
					"    (CASE\r\n" + 
					"        WHEN A.LEAVE_CNT = 0 THEN A.SALARY_AMOUNT\r\n" + 
					"        ELSE (A.SALARY_AMOUNT - ((A.SALARY_AMOUNT / "+paiedDay+") * A.LEAVE_CNT))\r\n" + 
					"    END) AS SALARY_AMOUNT,\r\n" + 
					"    A.SALARY_TEMP_CD\r\n" + 
					"FROM\r\n" + 
					"    (SELECT \r\n" + 
					"        COMP_CD,\r\n" + 
					"            EMP_ID,\r\n" + 
					"            FIRST_NM,\r\n" + 
					"            LAST_NM,\r\n" + 
					"            DESIGNATION_ID,\r\n" + 
					"            IFNULL((SELECT \r\n" + 
					"                    SANCTION_TO_DT - SANCTION_FROM_DT\r\n" + 
					"                FROM\r\n" + 
					"                    EMP_LEAVE_MST\r\n" + 
					"                WHERE\r\n" + 
					"                    EMP_LEAVE_MST.COMP_CD = EMP_MST.COMP_CD\r\n" + 
					"                        AND EMP_LEAVE_MST.EMP_ID = EMP_MST.EMP_ID\r\n" + 
					"                        AND EMP_LEAVE_MST.IS_DELETE = 'N'\r\n" + 
					"                        AND EXTRACT(MONTH FROM EMP_LEAVE_MST.SANCTION_DATE) = "+month+"\r\n" + 
					"                        AND EMP_LEAVE_MST.STATUS = 'Y'),0) AS LEAVE_CNT,\r\n" + 
					"            EMP_MST.SALARY_AMOUNT,\r\n" + 
					"            (CASE\r\n" + 
					"                WHEN\r\n" + 
					"                    EMP_MST.CONTRACT_TYPE = 'Y'\r\n" + 
					"                THEN\r\n" + 
					"                    (SELECT \r\n" + 
					"                            SALARY_TEMP_CD\r\n" + 
					"                        FROM\r\n" + 
					"                            CONTRACT_TYPE_MST\r\n" + 
					"                        WHERE\r\n" + 
					"                            CONTRACT_ID = EMP_MST.CONTRACT_APPLICABLE)\r\n" + 
					"                ELSE EMP_MST.SALARY_HEAD_CD\r\n" + 
					"            END) AS SALARY_TEMP_CD\r\n" + 
					"    FROM\r\n" + 
					"        EMP_MST\r\n" + 
					"    WHERE\r\n" + 
					"        ACTIVE_STATUS = 'Y' AND IS_DELETE = 'N'\r\n" + 
					"            AND EXTRACT(YEAR FROM SYSDATE()) = "+year+") A\r\n" + 
					"WHERE\r\n" + 
					"    (SELECT \r\n" + 
					"            COUNT(*)\r\n" + 
					"        FROM\r\n" + 
					"            EMP_PAYSLIP_HDR\r\n" + 
					"        WHERE\r\n" + 
					"            EMP_PAYSLIP_HDR.EMP_ID = A.EMP_ID\r\n" + 
					"                AND MONTH(EMP_PAYSLIP_HDR.SALARY_MONTH) = "+month+") <= 0\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"							   \r\n" + 
					"							\r\n" + 
					"							";
						
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			System.out.println(ls_query);
			while(rs.next()) {
				
				output = new JSONObject();
				output.put("COMP_CD",rs.getString("COMP_CD"));
				output.put("EMP_ID",rs.getString("EMP_ID"));
				output.put("NAME",rs.getString("FIRST_NM")+" "+rs.getString("LAST_NM"));
				output.put("DESIGNATION_ID",NVL.StringNvl(getDesignation.getDesig(con, Integer.parseInt(rs.getString("DESIGNATION_ID")))));
				output.put("LEAVE_CNT",NVL.StringNvl(rs.getString("LEAVE_CNT")));
				output.put("SALARY_AMOUNT",NVL.StringNvl(rs.getString("SALARY_AMOUNT")));
				output.put("SALARY_TEMP_CD",rs.getString("SALARY_TEMP_CD"));
				
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
	
	
	
	public static String getEmpSalaryAmtDtl(Connection con,String request) throws JSONException {
		
		Statement stmt = null;
		ResultSet rs = null;
		JSONObject input = new JSONObject(request);
		JSONObject output = null;
		JSONArray response = new JSONArray();
		JSONArray compContri = new JSONArray();
		JSONObject summary = new JSONObject();
		JSONObject finalResponse = new JSONObject();		
		String userName = input.getString("USERNAME");
		String empCD = input.getString("EMP_CD");
		String salaryTempCd = input.getString("SALARY_TEMP_CD");
		String SalaryAmt = input.getString("SALARY_AMT");
		double amt = 0.00;
		double salaryAmt = Double.parseDouble(SalaryAmt);  
		double deductionAmt = 0.00;
		double compContr = 0.00;
		try {
			String ls_query = "SELECT HEAD_CD,HEAD_NM,ED_FLAG,AMT FROM SALARY_HEAD_DTL WHERE TRAN_CD = "+salaryTempCd+"";
			stmt = con.createStatement();
			rs = stmt.executeQuery(ls_query);
			
			while(rs.next()) {							
				output = new JSONObject();
				
				if (!rs.getString("HEAD_CD").equals("CF") && !rs.getString("HEAD_CD").equals("CE")) {
					amt = rs.getDouble("AMT");
					salaryAmt = salaryAmt - amt;				
					deductionAmt = deductionAmt + amt;
					output.put("HEAD_NM", rs.getString("HEAD_NM"));
					output.put("HEAD_CD", rs.getString("HEAD_CD"));
					output.put("ED_FLAG", rs.getString("ED_FLAG"));
					output.put("AMOUNT", rs.getString("AMT"));
					response.put(output);
				} else {
					JSONObject compContribution =new JSONObject();					
					compContribution.put("HEAD_NM", rs.getString("HEAD_NM"));
					compContribution.put("HEAD_CD", rs.getString("HEAD_CD"));
					compContribution.put("ED_FLAG", rs.getString("ED_FLAG"));
					compContribution.put("AMOUNT", rs.getString("AMT"));
					compContr = compContr + Double.parseDouble(rs.getString("AMT"));
					compContri.put(compContribution);					
				}
				
				
			}
			output = new JSONObject();
			output.put("HEAD_NM", "Salary");
			output.put("HEAD_CD", "SL");
			output.put("ED_FLAG","E");
			output.put("AMOUNT", salaryAmt);
			response.put(output);
			
			output = new JSONObject();			
			summary.put("TOTAL_EMP_EARNIG", salaryAmt);
			summary.put("TOTAL_EMP_DEDUCTION", deductionAmt);
			summary.put("TOTAL_CMP_DEDUCTION", compContr);
			summary.put("TOTAL_EMP_GROSS", salaryAmt + deductionAmt);
			summary.put("TOTAL_EMP_CTC", salaryAmt + deductionAmt + compContr);
								
			finalResponse.put("STATUS_CD","0");
			finalResponse.put("RESPONSE",response);
			finalResponse.put("SUMMARY",summary);
			finalResponse.put("COMP_CONTRI",compContri);
			
		} catch(Exception e) {
			e.printStackTrace();
			finalResponse.put("STATUS","99");
			finalResponse.put("MESSAGE", "Error while get Employee Salary");
			finalResponse.put("ERROR",e.getMessage());
		}
		return finalResponse.toString();
	}	
	
	
	public static String insertEmpSalary(Connection con,String request) throws JSONException {
	
		JSONObject input = new JSONObject(request);
		JSONArray salaryData = new JSONArray();
		JSONObject output = new JSONObject();
		 String COMP_CD = null;
		 String TOTAL_PAID_DAYS = null;
		 String REQUEST_IP = null;
		 String WEEK_OFF_DAYS = null;
		 String SALARY_MONTH_YEAR = null;
		 String USERNAME = null;
		 String SALARY_YEAR = null;
		 String SALARY_MONTH = null;
		 String TOT_MONTH_DAYS = null;
		 int tran_cd = 0;
		 tran_cd = getMaxTranCd.getMaxTranCD(con, "EMP_PAYSLIP_HDR");
		 
		 COMP_CD = input.getString("COMP_CD");
		 TOTAL_PAID_DAYS = input.getString("TOTAL_PAID_DAYS");
		 REQUEST_IP = input.getString("REQUEST_IP");
		 WEEK_OFF_DAYS = input.getString("WEEK_OFF_DAYS");
		 SALARY_MONTH_YEAR = input.getString("SALARY_MONTH_YEAR");
		 USERNAME = input.getString("USERNAME");
		 SALARY_YEAR = input.getString("SALARY_YEAR");
		 SALARY_MONTH = input.getString("SALARY_MONTH");
		 TOT_MONTH_DAYS = input.getString("TOT_MONTH_DAYS");
		 		 		
		salaryData = input.getJSONArray("SALARY_DATA"); 
		try {
			int cnt = salaryData.length();
			for(int i=0;i<cnt;i++) {
				JSONObject buffer = new JSONObject();
				buffer = salaryData.getJSONObject(i);
//				int TRAN_CD = 0;
//				if (input.getString("TRAN_CD") == null || input.getString("TRAN_CD").equals("")) {
//		            TRAN_CD = 0;
//		        } else {
//		        	 TRAN_CD = input.getInt("TRAN_CD");
//		        }		   		            				 				
				int EMP_ID = 0;
				if (buffer.getString("EMP_ID") == null || buffer.getString("EMP_ID").equals("")) {
					EMP_ID = 0;
		        } else {
		        	EMP_ID = buffer.getInt("EMP_ID");
		        }
				System.out.println(EMP_ID);
				Date SALARY_MONTH1 = null; 
				if (buffer.getString("SALARY_MONTH") == null || buffer.getString("SALARY_MONTH").equals("")) {
					SALARY_MONTH1 = null;
	            } else {
	            	SALARY_MONTH1 = Date.valueOf(buffer.getString("SALARY_MONTH")); 
	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }
				int TOT_MONTH_DAYS1 = 0;
				if (buffer.getString("TOT_MONTH_DAYS") == null || buffer.getString("TOT_MONTH_DAYS").equals("")) {
					TOT_MONTH_DAYS1 = 0;
		        } else {
		        	TOT_MONTH_DAYS1 = buffer.getInt("TOT_MONTH_DAYS");
		        }
				int TOT_WEEKOFF_DAYS = 0;
				if (buffer.getString("TOT_WEEKOFF_DAYS") == null || buffer.getString("TOT_WEEKOFF_DAYS").equals("")) {
					TOT_WEEKOFF_DAYS = 0;
		        } else {
		        	TOT_WEEKOFF_DAYS = buffer.getInt("TOT_WEEKOFF_DAYS");
		        }														
				int EMP_LEAVE_DAYS = 0; 
				if (buffer.getString("EMP_LEAVE_DAYS") == null || buffer.getString("EMP_LEAVE_DAYS").equals("")) {
					EMP_LEAVE_DAYS = 0;
		        } else {
		        	EMP_LEAVE_DAYS = buffer.getInt("EMP_LEAVE_DAYS");
		        }
//				int EMP_HIST_REF = 0;
//				if (buffer.getString("EMP_HIST_REF") == null || buffer.getString("EMP_HIST_REF").equals("")) {
//					EMP_HIST_REF = 0;
//		        } else {
//		        	EMP_HIST_REF = buffer.getInt("EMP_HIST_REF");
//		        }				
				String ENTERED_BY =  NVL.StringNvl(buffer.getString("ENTERED_BY")); 
				Date ENTERED_DATE = null;
				if (buffer.getString("ENTERED_DATE") == null || buffer.getString("ENTERED_DATE").equals("")) {
					ENTERED_DATE = null;
	            } else {
	            	ENTERED_DATE = Date.valueOf(buffer.getString("ENTERED_DATE")); 
	                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
	            }								
				String ENTERED_IP = NVL.StringNvl(buffer.getString("ENTERED_IP")); 
								
				String ls_insert = "INSERT INTO emp_payslip_hdr (COMP_CD, TRAN_CD, EMP_ID, SALARY_MONTH, TOT_MONTH_DAYS, TOT_WEEKOFF_DAYS,EMP_LEAVE_DAYS, ENTERED_BY, ENTERED_DATE, ENTERED_IP) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedstmt = con.prepareStatement(ls_insert);
				preparedstmt.setString(1, COMP_CD);				
				preparedstmt.setInt(2, tran_cd);
				preparedstmt.setInt(3, EMP_ID);			
				preparedstmt.setDate(4,SALARY_MONTH1); 
				preparedstmt.setInt(5,TOT_MONTH_DAYS1);
				preparedstmt.setInt(6,TOT_WEEKOFF_DAYS);
				preparedstmt.setInt(7,EMP_LEAVE_DAYS);
//				preparedstmt.setInt(8,EMP_HIST_REF);
				preparedstmt.setString(8,ENTERED_BY);
				preparedstmt.setDate(9,ENTERED_DATE); 
				preparedstmt.setString(10,ENTERED_IP);
				
				int row = preparedstmt.executeUpdate();

	            if (row == 0) {
	                con.rollback();
	                output.put("STATUS_CD","99");
	                output.put("MESSAGE","Error while processed empployee Salary. Kindly Contact to admin.");
	            } else {
	                con.commit();
	                output.put("STATUS_CD","0");
	                output.put("MESSAGE","Employee Salary Sucessfully Processed.");
	            }				
			}
		} catch(Exception e) {
			e.printStackTrace();
			output.put("STATUS_CD","99");
            output.put("MESSAGE","Error in salary process : "+e.getMessage());
		}
		
		return output.toString();
	}
}
