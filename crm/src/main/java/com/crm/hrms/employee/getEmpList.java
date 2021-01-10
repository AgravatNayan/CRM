package com.crm.hrms.employee;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.sql.rowset.serial.SerialBlob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.NVL;
import com.crm.utility.Utility;
import com.crm.utility.getDesignation;

public class getEmpList {

    public static String GetMaxEmployeeID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(EMP_ID) AS EMP_ID FROM EMP_MST";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("EMP_ID");
            }
            maxID = maxID + 1;
            jOut.put("STATUS_CD", "0");
            JSONArray jresponse = new JSONArray();
            JSONObject jobj = new JSONObject();
            jobj.put("USERNAME", jin.getString("USERNAME"));
            jobj.put("EMP_ID", maxID);
            jresponse.put(jobj);
            jOut.put("STATUS_CD", "0");
            jOut.put("RESPONSE", jresponse);
            response = jOut.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in GetMax Employee : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
        } finally {
            try {
                rs.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                con.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return response;

    }
    public static String getEMployeeList(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;

        String empID = null;
        String empNM = null;
        String primaryContact = null;
        String birthDate = null;
        String gender = null;
        String designation = null;
        String Status = null;
        String photoPath = null;

        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = null;
            JSONArray jresponse = new JSONArray();

            String viewType = NVL.StringNvl(jin.getString("VIEW_FLAG"));
            String enteredUser = NVL.StringNvl(jin.getString("USERNAME"));
            String sql = null;
            if (viewType.equals("G")) {
                sql = "SELECT EMP_ID,CONCAT(FIrst_NM,' ',MIDDEL_NM,' ',LAST_NM) AS EMP_NM,primary_CONTACT,BIRTH_DT,GENDER,DESIGNATION_ID,ACTIVE_STATUS,PROFILE_IMG FROM EMP_MST WHERE IS_DELETE = 'N'";
            } else {
                sql = "SELECT EMP_ID,CONCAT(FIrst_NM,' ',MIDDEL_NM,' ',LAST_NM) AS EMP_NM,primary_CONTACT,BIRTH_DT,GENDER,DESIGNATION_ID,ACTIVE_STATUS,PROFILE_IMG FROM EMP_MST WHERE ENTERED_BY = '" + enteredUser + "' AND IS_DELETE = 'Y'";
            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                jOut = new JSONObject();
                empID = NVL.StringNvl(rs.getString("EMP_ID"));
                empNM = NVL.StringNvl(rs.getString("EMP_NM"));
                primaryContact = NVL.StringNvl(rs.getString("primary_CONTACT"));
                birthDate = NVL.StringNvl(rs.getString("BIRTH_DT"));
                gender = NVL.StringNvl(rs.getString("GENDER"));
                
                if (gender.equals("M")) {
                	gender = "Male";
                } else if (gender.equals("F")) {
                	gender = "Female";
                } else if (gender.equals("O")) {
                	gender = "Other";
                } else {
                	gender = "NAN";
                }               
                
                designation = NVL.StringNvl(rs.getString("DESIGNATION_ID"));                                
                designation = getDesignation.getDesig(con, Integer.parseInt(designation));
                
                Status = NVL.StringNvl(rs.getString("ACTIVE_STATUS"));
                photoPath = NVL.StringNvl(rs.getString("PROFILE_IMG"));

                jOut.put("EMP_ID", empID);
                jOut.put("NAME", empNM);
                jOut.put("MOBILE_NO", primaryContact);
                jOut.put("BIRTH_DT", birthDate);
                jOut.put("GENDER", gender);
                jOut.put("DESIGNATION", designation);
                jOut.put("STATUS", Status);
                jOut.put("PHOTO_PATH", photoPath);

                jresponse.put(jOut);
                jOut = new JSONObject();
            }

            JSONObject jOutput = new JSONObject();

            if (jresponse.length() <= 0) {
                jOutput.put("STATUS_CD", "1");
                jOutput.put("MESSAGE", "Employee not found");
            } else {
            	jOutput.put("STATUS_CD", "0");
                jOutput.put("RESPONSE", jresponse);
            }

            response = jOutput.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.PrintMessage("Error in Get Employee List : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
        } finally {
            try {
                rs.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                con.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return response;
    }
    public static String GetEmployeeID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ll_cnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);

            String ls_emp_id = NVL.StringNvl(jin.getString("EMP_ID"));
            String viewFlag = NVL.StringNvl(jin.getString("VIEW_FLAG"));
            String userName = NVL.StringNvl(jin.getString("USERNAME"));

            JSONObject jOut = new JSONObject();
            String sql = null;
            if (viewFlag.equals("G")) {
                sql = "SELECT COMP_CD,BRANCH_CD,EMP_ID,ACTIVE_STATUS,ENTRY_DT,FIRST_NM,MIDDEL_NM,LAST_NM,PROFILE_IMG,EMAIL_ID,INACTIVE_DT,BIRTH_DT,RELIGION_CD,GENDER,BLOOD_GRP,MARRITAL_STATUS,HIGHEST_EDU,UNIQUE_ID,\r\n" + "PAN_NO,REF_EMP_ID,REF_REMARKS,REMARKS,PRIMARY_CONTACT,SECONDARY_CONTACT,RESUME,CONFIRMATION_DT,JOINIG_DT,RETIREMENT_DT,LEAVING_DT,GRADE_CD,DEPARTMENT_ID,DESIGNATION_ID,\r\n" + "SALARY_AMOUNT,LEAVING_REASON,COMP_REMARKS,CONTRACT_APPLICABLE,CONTRACT_TYPE,CONTRACT_SIGNED_DATE,PER_ADD_LINE_1,PER_ADD_LINE_2,PER_ADD_LINE_3,PER_COUNTRY_CD,\r\n" + "PER_STATE_CD,PER_PIN_CODE,TEMP_ADD_LIINE_1,TEMP_ADD_LIINE_2,TEMP_ADD_LIINE_3,TEMP_COUNTRY_CD,TEMP_STATE_CD,TEMP_CITY_CD,TEMP_PIN_CODE,ACTIVE,PER_CITY_CD,SHIFT_ID FROM EMP_MST WHERE EMP_ID = " + Integer.parseInt(ls_emp_id) +" AND IS_DELETE = 'N'";
            } else {
                sql = "SELECT COMP_CD,BRANCH_CD,EMP_ID,ACTIVE_STATUS,ENTRY_DT,FIRST_NM,MIDDEL_NM,LAST_NM,PROFILE_IMG,EMAIL_ID,INACTIVE_DT,BIRTH_DT,RELIGION_CD,GENDER,BLOOD_GRP,MARRITAL_STATUS,HIGHEST_EDU,UNIQUE_ID,\r\n" + "PAN_NO,REF_EMP_ID,REF_REMARKS,REMARKS,PRIMARY_CONTACT,SECONDARY_CONTACT,RESUME,CONFIRMATION_DT,JOINIG_DT,RETIREMENT_DT,LEAVING_DT,GRADE_CD,DEPARTMENT_ID,DESIGNATION_ID,\r\n" + "SALARY_AMOUNT,LEAVING_REASON,COMP_REMARKS,CONTRACT_APPLICABLE,CONTRACT_TYPE,CONTRACT_SIGNED_DATE,PER_ADD_LINE_1,PER_ADD_LINE_2,PER_ADD_LINE_3,PER_COUNTRY_CD,\r\n" + "PER_STATE_CD,PER_PIN_CODE,TEMP_ADD_LIINE_1,TEMP_ADD_LIINE_2,TEMP_ADD_LIINE_3,TEMP_COUNTRY_CD,TEMP_STATE_CD,TEMP_CITY_CD,TEMP_PIN_CODE,ACTIVE,PER_CITY_CD,SHIFT_ID FROM EMP_MST WHERE EMP_ID = " + Integer.parseInt(ls_emp_id) + " AND ENTERED_BY = '" + userName + "' AND IS_DELETE = 'N'";

            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ll_cnt = ll_cnt + 1;
                jOut.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                jOut.put("BRANCH_CD", NVL.StringNvl(rs.getString("BRANCH_CD")));
                jOut.put("EMP_ID", NVL.StringNvl(rs.getString("EMP_ID")));
                jOut.put("ACTIVE_STATUS", NVL.StringNvl(rs.getString("ACTIVE_STATUS")));
                jOut.put("ENTRY_DT", NVL.StringNvl(rs.getString("ENTRY_DT")));
                jOut.put("FIRST_NM", NVL.StringNvl(rs.getString("FIRST_NM")));
                jOut.put("MIDDLE_NM", NVL.StringNvl(rs.getString("MIDDEL_NM")));
                jOut.put("LAST_NM", NVL.StringNvl(rs.getString("LAST_NM")));
                jOut.put("PROFILE_IMG", NVL.StringNvl(rs.getString("PROFILE_IMG")));
                jOut.put("EMIAL_ID", NVL.StringNvl(rs.getString("EMAIL_ID")));
                jOut.put("INACTIVE_DT", NVL.StringNvl(rs.getString("INACTIVE_DT")));
                jOut.put("BIRTH_DT", NVL.StringNvl(rs.getString("BIRTH_DT")));
                jOut.put("RELIGION_CD", NVL.StringNvl(rs.getString("RELIGION_CD")));
                jOut.put("GENDER", NVL.StringNvl(rs.getString("GENDER")));
                jOut.put("BLOOD_GROUP", NVL.StringNvl(rs.getString("BLOOD_GRP")));
                jOut.put("MARRITAL_STATUS", NVL.StringNvl(rs.getString("MARRITAL_STATUS")));
                jOut.put("HIGHEST_EDU", NVL.StringNvl(rs.getString("HIGHEST_EDU")));
                jOut.put("UNIQUE_ID", NVL.StringNvl(rs.getString("UNIQUE_ID")));
                jOut.put("PAN_NO", NVL.StringNvl(rs.getString("PAN_NO")));
                jOut.put("REF_EMP_ID", NVL.StringNvl(rs.getString("REF_EMP_ID")));
                jOut.put("REF_REMARKS", NVL.StringNvl(rs.getString("REF_REMARKS")));
                jOut.put("REMARKS", NVL.StringNvl(rs.getString("REMARKS")));
                jOut.put("PRIMARY_CONTACT", NVL.StringNvl(rs.getString("PRIMARY_CONTACT")));
                jOut.put("SECONDARY_CONTACT", NVL.StringNvl(rs.getString("SECONDARY_CONTACT")));
                jOut.put("RESUME", NVL.StringNvl(rs.getString("RESUME")));
                jOut.put("CONFIRMATION_DT", NVL.StringNvl(rs.getString("CONFIRMATION_DT")));
                jOut.put("JOINIG_DT", NVL.StringNvl(rs.getString("JOINIG_DT")));
                jOut.put("RETIREMENT_DT", NVL.StringNvl(rs.getString("RETIREMENT_DT")));
                jOut.put("LEAVING_DT", NVL.StringNvl(rs.getString("LEAVING_DT")));
                jOut.put("GRADE_CD", NVL.StringNvl(rs.getString("GRADE_CD")));
                jOut.put("DEPARTMENT_ID", NVL.StringNvl(rs.getString("DEPARTMENT_ID")));
                jOut.put("DESIGNATION_ID", NVL.StringNvl(rs.getString("DESIGNATION_ID")));
                jOut.put("SALARY_AMOUNT", NVL.StringNvl(rs.getString("SALARY_AMOUNT")));
                jOut.put("LEAVING_REASON", NVL.StringNvl(rs.getString("LEAVING_REASON")));
                jOut.put("COMP_REMARKS", NVL.StringNvl(rs.getString("COMP_REMARKS")));
                jOut.put("CONTRACT_APPLICABLE", NVL.StringNvl(rs.getString("CONTRACT_APPLICABLE")));
                jOut.put("CONTRACT_TYPE", NVL.StringNvl(rs.getString("CONTRACT_TYPE")));
                jOut.put("CONTRACT_SIGNED_DATE", NVL.StringNvl(rs.getString("CONTRACT_SIGNED_DATE")));
                jOut.put("PER_ADD_LINE_1", NVL.StringNvl(rs.getString("PER_ADD_LINE_1")));
                jOut.put("PER_ADD_LINE_2", NVL.StringNvl(rs.getString("PER_ADD_LINE_2")));
                jOut.put("PER_ADD_LINE_3", NVL.StringNvl(rs.getString("PER_ADD_LINE_3")));
                jOut.put("PER_COUNTRY_CD", NVL.StringNvl(rs.getString("PER_COUNTRY_CD")));
                jOut.put("PER_STATE_CD", NVL.StringNvl(rs.getString("PER_STATE_CD")));
                jOut.put("PER_PIN_CODE", NVL.StringNvl(rs.getString("PER_PIN_CODE")));
                jOut.put("TEMP_ADD_LINE_1", NVL.StringNvl(rs.getString("TEMP_ADD_LIINE_1")));
                jOut.put("TEMP_ADD_LINE_2", NVL.StringNvl(rs.getString("TEMP_ADD_LIINE_2")));
                jOut.put("TEMP_ADD_LINE_3", NVL.StringNvl(rs.getString("TEMP_ADD_LIINE_3")));
                jOut.put("TEMP_COUNTRY_CD", NVL.StringNvl(rs.getString("TEMP_COUNTRY_CD")));
                jOut.put("TEMP_STATE_CD", NVL.StringNvl(rs.getString("TEMP_STATE_CD")));
                jOut.put("TEMP_CITY_CD", NVL.StringNvl(rs.getString("TEMP_CITY_CD"))); 
                jOut.put("TEMP_PIN_CODE", NVL.StringNvl(rs.getString("TEMP_PIN_CODE")));
                jOut.put("ACTIVE", NVL.StringNvl(rs.getString("ACTIVE")));
                jOut.put("PER_CITY_CD", NVL.StringNvl(rs.getString("PER_CITY_CD")));
                jOut.put("SHIFT_ID", NVL.StringNvl(rs.getString("SHIFT_ID")));
                
                JSONArray insuDtl  = new JSONArray();
                insuDtl = getInsurance(con,Integer.parseInt(ls_emp_id));
                System.out.println(insuDtl);
                jOut.put("INSU_DTL",insuDtl);
                
            }

            JSONArray jresponse = new JSONArray();
            JSONObject jobj = new JSONObject();
            jobj.put("STATUS_CD", "0");
            if (jOut.length() <= 0) {
                jobj.put("STATUS_CD", "1");
                jobj.put("MESSAGE", "Employee not found");
            } else {
                jresponse.put(jOut);
                jobj.put("STATUS_CD", "0");
                jobj.put("RESPONSE", jresponse);
            }
            response = jobj.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in Get Employee : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
        } finally {
            try {
                rs.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                con.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return response;

    }
    
    public static JSONArray getInsurance(Connection con,int Emp_id) throws SQLException, JSONException {
    	
    	Statement stmt = null;
    	ResultSet rs = null;
    	JSONObject response = null;
    	JSONArray jArray = new JSONArray();
    	String sql = "SELECT COMP_CD, EMP_ID, INSU_THROUGH, INSU_ID, INSU_TYPE, INSU_REG_NO, INSU_BOOK_NO, INSU_START_DT, INSU_END_DT, INSU_STATUS, INSU_INACTIVE_DT, INSU_AGENCY, INSU_AGENCY_ID, INSU_CUST_NO, INSU_PREMIUM, INSU_DOC_POLICY, IS_DELETE FROM emp_insurance_mst WHERE EMP_ID = "+Emp_id;    	
    	stmt = con.createStatement();
    	rs = stmt.executeQuery(sql);
    	
    	while (rs.next()) {
    		response = new JSONObject();
    		
    		response.put("COMP_CD",rs.getString("COMP_CD"));
    		response.put("EMP_ID",rs.getString("EMP_ID"));
    		response.put("INSU_THROUGH",rs.getString("INSU_THROUGH"));
    		response.put("INSU_ID",rs.getString("INSU_ID"));
    		response.put("INSU_TYPE",rs.getString("INSU_TYPE"));
    		response.put("INSU_REG_NO",rs.getString("INSU_REG_NO"));
    		response.put("INSU_BOOK_NO",rs.getString("INSU_BOOK_NO"));
    		response.put("INSU_START_DT",rs.getString("INSU_START_DT"));
    		response.put("INSU_END_DT",rs.getString("INSU_END_DT"));
    		response.put("INSU_STATUS",rs.getString("INSU_STATUS"));
    		response.put("INSU_INACTIVE_DT",rs.getString("INSU_INACTIVE_DT"));
    		response.put("INSU_AGENCY",rs.getString("INSU_AGENCY"));
    		response.put("INSU_AGENCY_ID",rs.getString("INSU_AGENCY_ID"));
    		response.put("INSU_CUST_NO",rs.getString("INSU_CUST_NO"));
    		response.put("INSU_PREMIUM",rs.getString("INSU_PREMIUM"));
    		response.put("INSU_DOC_POLICY",rs.getString("INSU_DOC_POLICY"));
    		response.put("IS_DELETE",rs.getString("IS_DELETE"));    		
    		jArray.put(response);
		}
    	
    	if(jArray.length() < 0 ) {
    		return null;
    	} else {
    		return jArray;
    	}    	
    }
    

    public static String CreateEmployee(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        int ExistingCnt = 0;
        try {

            JSONObject jin = new JSONObject(ls_request);
            System.out.println(jin.toString());

            JSONObject jReuqest = new JSONObject();
            jReuqest = jin.getJSONObject("REQUEST_DATA");

            String COMP_CD = jReuqest.getString("COMP_CD");
            String BRANCH_CD = jReuqest.getString("BRANCH_CD");
            int EMP_ID = jReuqest.getInt("EMP_ID");
            Date ENTRY_DT = null;
            if (jReuqest.getString("ENTRY_DT") == null || jReuqest.getString("ENTRY_DT").equals("")) {
                ENTRY_DT = null;
            } else {
                ENTRY_DT = Date.valueOf(jReuqest.getString("ENTRY_DT")); 
                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
            }
            System.out.println(ENTRY_DT);

            String ACTIVE_STATUS = jReuqest.getString("ACTIVE_STATUS");
            String FIRST_NM = jReuqest.getString("FIRST_NM");
            String MIDDEL_NM = jReuqest.getString("MIDDEL_NM");
            String LAST_NM = jReuqest.getString("LAST_NM");
            String PROFILE_IMG = jReuqest.getString("PROFILE_IMG");
            String EMAIL_ID = jReuqest.getString("EMAIL_ID");
            Date INACTIVE_DT = null;
            if (jReuqest.getString("INACTIVE_DT") == null || jReuqest.getString("INACTIVE_DT").equals("")) {
                INACTIVE_DT = null;
            } else {
                INACTIVE_DT = Date.valueOf(jReuqest.getString("INACTIVE_DT"));
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
            }

            Date BIRTH_DT = null;
            if (jReuqest.getString("BIRTH_DT") == null || jReuqest.getString("BIRTH_DT").equals("")) {
                BIRTH_DT = null;
            } else {
                BIRTH_DT = Date.valueOf(jReuqest.getString("BIRTH_DT"));
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("BIRTH_DT"));
            }
            int RELIGION_CD = jReuqest.getInt("RELIGION_CD");
            String GENDER = jReuqest.getString("GENDER");
            String BLOOD_GRP = jReuqest.getString("BLOOD_GRP");
            System.out.println(BLOOD_GRP);
            String MARRITAL_STATUS = jReuqest.getString("MARRITAL_STATUS");
            String HIGHEST_EDU = jReuqest.getString("HIGHEST_EDU");
            String UNIQUE_ID = jReuqest.getString("UNIQUE_ID");
            String PAN_NO = jReuqest.getString("PAN_NO");
            int REF_EMP_ID = 0;
            System.out.println(jReuqest.getString("REF_EMP_ID"));
            if (jReuqest.getString("REF_EMP_ID") == null || jReuqest.getString("REF_EMP_ID").equals("")) {
                REF_EMP_ID = 0;
            } else {
                REF_EMP_ID = jReuqest.getInt("REF_EMP_ID");
            }

            String REF_REMARKS = jReuqest.getString("REF_REMARKS");
            String REMARKS = jReuqest.getString("REMARKS");
            int PRIMARY_CONTACT = jReuqest.getInt("PRIMARY_CONTACT");

            int SECONDARY_CONTACT = 0;
            if (jReuqest.getString("SECONDARY_CONTACT") == null || jReuqest.getString("SECONDARY_CONTACT").equals("")) {
                SECONDARY_CONTACT = 0;
            } else {
                SECONDARY_CONTACT = jReuqest.getInt("SECONDARY_CONTACT");
            }
            
            int SHIFT_ID = 0;
            if (jReuqest.getString("SHIFT_ID") == null || jReuqest.getString("SHIFT_ID").equals("")) {
            	SHIFT_ID = 0;
            } else {
            	SHIFT_ID = jReuqest.getInt("SHIFT_ID");
            }
            
            
            
            String RESUME = jReuqest.getString("RESUME");
            Date CONFIRMATION_DT = null;
            if (jReuqest.getString("CONFIRMATION_DT") == null || jReuqest.getString("CONFIRMATION_DT").equals("")) {
                CONFIRMATION_DT = null;
            } else {
                CONFIRMATION_DT =  Date.valueOf(jReuqest.getString("CONFIRMATION_DT"));
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONFIRMATION_DT"));
            }
            Date JOINIG_DT = null;
            if (jReuqest.getString("JOINIG_DT") == null || jReuqest.getString("JOINIG_DT").equals("")) {
                JOINIG_DT = null;
            } else {
                JOINIG_DT =  Date.valueOf(jReuqest.getString("JOINIG_DT")); 
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            String ACTIVE = jReuqest.getString("ACTIVE");
            Date RETIREMENT_DT = null;
            if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("RETIREMENT_DT").equals("")) {
                RETIREMENT_DT = null;
            } else {
                RETIREMENT_DT = Date.valueOf(jReuqest.getString("RETIREMENT_DT")); 
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("RETIREMENT_DT"));
            }
            Date LEAVING_DT = null;
            if (jReuqest.getString("LEAVING_DT") == null || jReuqest.getString("LEAVING_DT").equals("")) {
                LEAVING_DT = null;
            } else {
                LEAVING_DT = Date.valueOf(jReuqest.getString("LEAVING_DT")); 
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LEAVING_DT"));
            }
            int GRADE_CD = 0;
            if (jReuqest.getString("GRADE_CD") == null || jReuqest.getString("GRADE_CD").equals("")) {
                GRADE_CD = 0;
            } else {
                GRADE_CD = jReuqest.getInt("GRADE_CD");
            }
            int DEPARTMENT_ID = 0;
            if (jReuqest.getString("DEPARTMENT_ID") == null || jReuqest.getString("DEPARTMENT_ID").equals("")) {
                DEPARTMENT_ID = 0;
            } else {
                DEPARTMENT_ID = jReuqest.getInt("DEPARTMENT_ID");
            }
            int DESIGNATION_ID = 0;
            if (jReuqest.getString("DESIGNATION_ID") == null || jReuqest.getString("DESIGNATION_ID").equals("")) {
                DEPARTMENT_ID = 0;
            } else {
                DEPARTMENT_ID = jReuqest.getInt("DESIGNATION_ID");
            }
            int SALARY_AMOUNT = 0;
            if (jReuqest.getString("SALARY_AMOUNT") == null || jReuqest.getString("SALARY_AMOUNT").equals("")) {
                SALARY_AMOUNT = 0;
            } else {
                SALARY_AMOUNT = jReuqest.getInt("SALARY_AMOUNT");
            }
            String LEAVING_REASON = jReuqest.getString("LEAVING_REASON");
            String COMP_REMARKS = jReuqest.getString("COMP_REMARKS");
            String CONTRACT_APPLICABLE = "";
            if (jReuqest.getString("CONTRACT_APPLICABLE") == null || jReuqest.getString("CONTRACT_APPLICABLE").equals("")) {
                CONTRACT_APPLICABLE = "";
            } else {
                CONTRACT_APPLICABLE = jReuqest.getString("CONTRACT_APPLICABLE");
            }
            int CONTRACT_TYPE = 0;
            if (jReuqest.getString("CONTRACT_TYPE") == null || jReuqest.getString("CONTRACT_TYPE").equals("")) {
                CONTRACT_TYPE = 0;
            } else {
                CONTRACT_TYPE = jReuqest.getInt("CONTRACT_TYPE");
            }
            Date CONTRACT_SIGNED_DATE = null;
            if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("RETIREMENT_DT").equals("")) {
                CONTRACT_SIGNED_DATE = null;
            } else {
                CONTRACT_SIGNED_DATE = Date.valueOf(jReuqest.getString("CONTRACT_SIGNED_DATE"));  
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONTRACT_SIGNED_DATE"));
            }
            String PER_ADD_LINE_1 = jReuqest.getString("PER_ADD_LINE_1");
            String PER_ADD_LINE_2 = jReuqest.getString("PER_ADD_LINE_2");
            String PER_ADD_LINE_3 = jReuqest.getString("PER_ADD_LINE_3");
            int PER_COUNTRY_CD = 0;
            if (jReuqest.getString("PER_COUNTRY_CD") == null || jReuqest.getString("PER_COUNTRY_CD").equals("")) {
                PER_COUNTRY_CD = 0;
            } else {
                PER_COUNTRY_CD = jReuqest.getInt("PER_COUNTRY_CD");
            }
            int PER_STATE_CD = 0;
            if (jReuqest.getString("PER_STATE_CD") == null || jReuqest.getString("PER_STATE_CD").equals("")) {
                PER_STATE_CD = 0;
            } else {
                PER_STATE_CD = jReuqest.getInt("PER_STATE_CD");
            }
            int PER_CITY_CD = 0;
            if (jReuqest.getString("PER_CITY_CD") == null || jReuqest.getString("PER_CITY_CD").equals("")) {
                PER_CITY_CD = 0;
            } else {
                PER_CITY_CD = jReuqest.getInt("PER_CITY_CD");
            }
            String PER_PIN_CODE = jReuqest.getString("PER_PIN_CODE");
            String TEMP_ADD_LIINE_1 = jReuqest.getString("TEMP_ADD_LINE_1");
            String TEMP_ADD_LIINE_2 = jReuqest.getString("TEMP_ADD_LINE_2");
            String TEMP_ADD_LIINE_3 = jReuqest.getString("TEMP_ADD_LINE_3");
            int TEMP_COUNTRY_CD = 0;
            if (jReuqest.getString("TEMP_COUNTRY_CD") == null || jReuqest.getString("TEMP_COUNTRY_CD").equals("")) {
                TEMP_COUNTRY_CD = 0;
            } else {
                TEMP_COUNTRY_CD = jReuqest.getInt("TEMP_COUNTRY_CD");
            }
            int TEMP_STATE_CD = 0;
            if (jReuqest.getString("TEMP_STATE_CD") == null || jReuqest.getString("TEMP_STATE_CD").equals("")) {
                TEMP_STATE_CD = 0;
            } else {
                TEMP_STATE_CD = jReuqest.getInt("TEMP_STATE_CD");
            }
            int TEMP_CITY_CD = 0;
            if (jReuqest.getString("TEMP_CITY_CD") == null || jReuqest.getString("TEMP_CITY_CD").equals("")) {
                TEMP_CITY_CD = 0;
            } else {
                TEMP_CITY_CD = jReuqest.getInt("TEMP_CITY_CD");
            }
            String TEMP_PIN_CODE = jReuqest.getString("TEMP_PIN_CODE");
            String ENTERED_BY = jReuqest.getString("ENTERED_BY");
            String ENTERED_IP = jReuqest.getString("ENTERED_IP");
            Date ENTERED_DATE = null;
            if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
                ENTERED_DATE = null;
            } else {
                ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE"));  
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
            }
            String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");
            String LAST_MODOFIED_IP = jReuqest.getString("LAST_MODOFIED_IP");
            Date LAST_MODIFIED_DT = null;
            if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
                LAST_MODIFIED_DT = null;
            } else {
                LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT"));  
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
            }
                        
            JSONArray insuranceDelete = new JSONArray(jReuqest.getString("INSURANCELISTDELETE"));
            int i = 0;
            try {
                if (insuranceDelete.length() > 0) {
                    i = deleteInsu(con, insuranceDelete, COMP_CD);
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Delete Insurance Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
            
            
            JSONArray insuranceInsert = new JSONArray(jReuqest.getString("INSURANCELISTINSERT"));
            int j = 0;
            try {
                if (insuranceInsert.length() > 0) {
                    j = insertInsu(con, insuranceInsert, COMP_CD, EMP_ID);
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Insert Board Member Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
              
            JSONArray insuranceUpdate = new JSONArray(jReuqest.getString("INSURANCELISTUPDATE"));
            int k = 0;
            try {
                if (insuranceUpdate.length() > 0) {
                    k = updateInsu(con, insuranceUpdate, COMP_CD,EMP_ID);
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Update Board Member Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
            
            
            
            String extingEmp = "SELECT COUNT(*) AS EMP_CNT FROM EMP_MST WHERE EMP_ID = " + EMP_ID+" AND IS_DELETE = 'N'";

            stmt = con.createStatement();
            ResultSet empResultSet = stmt.executeQuery(extingEmp);

            while (empResultSet.next()) {
                ExistingCnt = empResultSet.getInt("EMP_CNT");
            }
            System.out.println(ExistingCnt);
            if (ExistingCnt > 0) {
            	 JSONObject jOutPut = new JSONObject();

                 jOutPut.put("STATUS_CD", "99");
                 jOutPut.put("MESSAGE", "Employee ID " + EMP_ID + " Already Exists.");
                 response = jOutPut.toString();
            } else {
                JSONObject jOut = new JSONObject();
                String sql = "INSERT INTO crm.emp_mst(COMP_CD,BRANCH_CD,EMP_ID,ENTRY_DT,ACTIVE_STATUS,FIRST_NM,\r\n" + "MIDDEL_NM,LAST_NM,PROFILE_IMG,EMAIL_ID,INACTIVE_DT,BIRTH_DT,RELIGION_CD,\r\n" + "GENDER,BLOOD_GRP,MARRITAL_STATUS,HIGHEST_EDU,UNIQUE_ID,PAN_NO,REF_EMP_ID,REF_REMARKS,\r\n" + "REMARKS,PRIMARY_CONTACT,SECONDARY_CONTACT,RESUME,CONFIRMATION_DT,JOINIG_DT,\r\n" + "ACTIVE,RETIREMENT_DT,LEAVING_DT,GRADE_CD,DEPARTMENT_ID,DESIGNATION_ID,SALARY_AMOUNT,\r\n" + "LEAVING_REASON,COMP_REMARKS,CONTRACT_APPLICABLE,CONTRACT_TYPE,CONTRACT_SIGNED_DATE,PER_ADD_LINE_1,PER_ADD_LINE_2,PER_ADD_LINE_3,PER_COUNTRY_CD,PER_STATE_CD,PER_CITY_CD,PER_PIN_CODE,TEMP_ADD_LIINE_1,TEMP_ADD_LIINE_2,TEMP_ADD_LIINE_3,TEMP_COUNTRY_CD,TEMP_STATE_CD,TEMP_CITY_CD,TEMP_PIN_CODE,ENTERED_BY,ENTERED_IP,ENTERED_DATE,LAST_MODIFIED_BY,LAST_MODOFIED_IP,LAST_MODIFIED_DT,SHIFT_ID)\r\n" + "VALUES\r\n" + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, COMP_CD);
                preparedStatement.setString(2, BRANCH_CD);
                preparedStatement.setInt(3, EMP_ID);
                preparedStatement.setDate(4, ENTRY_DT);
                preparedStatement.setString(5, ACTIVE_STATUS);
                preparedStatement.setString(6, FIRST_NM);
                preparedStatement.setString(7, MIDDEL_NM);
                preparedStatement.setString(8, LAST_NM);
                preparedStatement.setString(9, PROFILE_IMG);
                preparedStatement.setString(10, EMAIL_ID);
                preparedStatement.setDate(11, INACTIVE_DT);
                preparedStatement.setDate(12, BIRTH_DT);
                preparedStatement.setInt(13, RELIGION_CD);
                preparedStatement.setString(14, GENDER);
                preparedStatement.setString(15, BLOOD_GRP);
                preparedStatement.setString(16, MARRITAL_STATUS);
                preparedStatement.setString(17, HIGHEST_EDU);
                preparedStatement.setString(18, UNIQUE_ID);
                preparedStatement.setString(19, PAN_NO);
                preparedStatement.setInt(20, REF_EMP_ID);
                preparedStatement.setString(21, REF_REMARKS);
                preparedStatement.setString(22, REMARKS);
                preparedStatement.setInt(23, PRIMARY_CONTACT);
                preparedStatement.setInt(24, SECONDARY_CONTACT);
                preparedStatement.setString(25, RESUME);
                preparedStatement.setDate(26, CONFIRMATION_DT);
                preparedStatement.setDate(27, JOINIG_DT);
                preparedStatement.setString(28, ACTIVE);
                preparedStatement.setDate(29, RETIREMENT_DT);
                preparedStatement.setDate(30, LEAVING_DT);
                preparedStatement.setInt(31, GRADE_CD);
                preparedStatement.setInt(32, DEPARTMENT_ID);
                preparedStatement.setInt(33, DESIGNATION_ID);
                preparedStatement.setInt(34, SALARY_AMOUNT);
                preparedStatement.setString(35, LEAVING_REASON);
                preparedStatement.setString(36, COMP_REMARKS);
                preparedStatement.setString(37, CONTRACT_APPLICABLE);
                preparedStatement.setInt(38, CONTRACT_TYPE);
                preparedStatement.setDate(39, CONTRACT_SIGNED_DATE);
                preparedStatement.setString(40, PER_ADD_LINE_1);
                preparedStatement.setString(41, PER_ADD_LINE_2);
                preparedStatement.setString(42, PER_ADD_LINE_3);
                preparedStatement.setInt(43, PER_COUNTRY_CD);
                preparedStatement.setInt(44, PER_STATE_CD);
                preparedStatement.setInt(45, PER_CITY_CD);
                preparedStatement.setString(46, PER_PIN_CODE);
                preparedStatement.setString(47, TEMP_ADD_LIINE_1);
                preparedStatement.setString(48, TEMP_ADD_LIINE_2);
                preparedStatement.setString(49, TEMP_ADD_LIINE_3);
                preparedStatement.setInt(50, TEMP_COUNTRY_CD);
                preparedStatement.setInt(51, TEMP_STATE_CD);
                preparedStatement.setInt(52, TEMP_CITY_CD);
                preparedStatement.setString(53, TEMP_PIN_CODE);
                preparedStatement.setString(54, ENTERED_BY);
                preparedStatement.setString(55, ENTERED_IP);
                preparedStatement.setDate(56, ENTERED_DATE);
                preparedStatement.setString(57, LAST_MODIFIED_BY);
                preparedStatement.setString(58, LAST_MODOFIED_IP);
                preparedStatement.setDate(59, LAST_MODIFIED_DT);
                preparedStatement.setInt(60,SHIFT_ID);
                
                int row = preparedStatement.executeUpdate();

                if (row == 0) {
                    con.rollback();
                } else {
                    con.commit();
                }

                JSONObject jOutPut = new JSONObject();

                jOutPut.put("STATUS_CD", "0");
                jOutPut.put("MESSAGE", "Employee ID " + EMP_ID + " Sucessfully Created.");
                response = jOutPut.toString();
            }
        } catch (Exception e) {
            Utility.PrintMessage("Error in Create Employee : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
        } finally {
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                con.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        return response;

    }
    public static String updateEmployeeID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);            
            JSONObject jReuqest = new JSONObject();
            jReuqest = jin.getJSONObject("REQUEST_DATA");
            JSONObject jOutPut = new JSONObject();
            
            int EMP_ID = jin.getInt("EMP_ID");            
            String extingEmp = "SELECT COUNT(*) AS EMP_CNT FROM EMP_MST WHERE EMP_ID = " +EMP_ID+" AND IS_DELETE = 'N'";
            System.out.println(extingEmp);
            stmt = con.createStatement();
            ResultSet empResultSet = stmt.executeQuery(extingEmp);
            		
            while (empResultSet.next()) {
                ExistingCnt = empResultSet.getInt("EMP_CNT");
            }
            
            if (ExistingCnt == 0) {            	
                 jOutPut.put("STATUS_CD", "99");
                 jOutPut.put("MESSAGE", "Employee ID " + EMP_ID + " not Exists. Kindly Create First");
                 response = jOutPut.toString();
            } else {
                String COMP_CD = jReuqest.getString("COMP_CD");
                String BRANCH_CD = jReuqest.getString("BRANCH_CD");           
                Date ENTRY_DT = null;
                if (jReuqest.getString("ENTRY_DT") == null || jReuqest.getString("ENTRY_DT").equals("")) {
                    ENTRY_DT = null;
                } else {
                    ENTRY_DT = Date.valueOf(jReuqest.getString("ENTRY_DT")); 
                    		//(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
                }
                System.out.println(ENTRY_DT);

                String ACTIVE_STATUS = jReuqest.getString("ACTIVE_STATUS");
                String FIRST_NM = jReuqest.getString("FIRST_NM");
                String MIDDEL_NM = jReuqest.getString("MIDDEL_NM");
                String LAST_NM = jReuqest.getString("LAST_NM");
                String PROFILE_IMG = jReuqest.getString("PROFILE_IMG");
                String EMAIL_ID = jReuqest.getString("EMAIL_ID");
                Date INACTIVE_DT = null;
                if (jReuqest.getString("INACTIVE_DT") == null || jReuqest.getString("INACTIVE_DT").equals("")) {
                    INACTIVE_DT = null;
                } else {
                    INACTIVE_DT = Date.valueOf(jReuqest.getString("INACTIVE_DT"));  
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
                }
                
                int SHIFT_ID = 0;
                if (jReuqest.getString("SHIFT_ID") == null || jReuqest.getString("SHIFT_ID").equals("")) {
                	SHIFT_ID = 0;
                } else {
                	SHIFT_ID = jReuqest.getInt("SHIFT_ID");
                }
                
                Date BIRTH_DT = null;
                if (jReuqest.getString("BIRTH_DT") == null || jReuqest.getString("BIRTH_DT").equals("")) {
                    BIRTH_DT = null;
                } else {
                    BIRTH_DT = Date.valueOf(jReuqest.getString("BIRTH_DT"));  
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("BIRTH_DT"));
                }
                int RELIGION_CD = jReuqest.getInt("RELIGION_CD");
                String GENDER = jReuqest.getString("GENDER");
                String BLOOD_GRP = jReuqest.getString("BLOOD_GRP");
                System.out.println(BLOOD_GRP);
                String MARRITAL_STATUS = jReuqest.getString("MARRITAL_STATUS");
                String HIGHEST_EDU = jReuqest.getString("HIGHEST_EDU");
                String UNIQUE_ID = jReuqest.getString("UNIQUE_ID");
                String PAN_NO = jReuqest.getString("PAN_NO");
                int REF_EMP_ID = 0;
                System.out.println(jReuqest.getString("REF_EMP_ID"));
                if (jReuqest.getString("REF_EMP_ID") == null || jReuqest.getString("REF_EMP_ID").equals("")) {
                    REF_EMP_ID = 0;
                } else {
                    REF_EMP_ID = jReuqest.getInt("REF_EMP_ID");
                }

                String REF_REMARKS = jReuqest.getString("REF_REMARKS");
                String REMARKS = jReuqest.getString("REMARKS");
                int PRIMARY_CONTACT = jReuqest.getInt("PRIMARY_CONTACT");

                int SECONDARY_CONTACT = 0;
                if (jReuqest.getString("SECONDARY_CONTACT") == null || jReuqest.getString("SECONDARY_CONTACT").equals("")) {
                    SECONDARY_CONTACT = 0;
                } else {
                    SECONDARY_CONTACT = jReuqest.getInt("SECONDARY_CONTACT");
                }
                String RESUME = jReuqest.getString("RESUME");
                Date CONFIRMATION_DT = null;
                if (jReuqest.getString("CONFIRMATION_DT") == null || jReuqest.getString("CONFIRMATION_DT").equals("")) {
                    CONFIRMATION_DT = null;
                } else {
                    CONFIRMATION_DT = Date.valueOf(jReuqest.getString("CONFIRMATION_DT"));
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONFIRMATION_DT"));
                }
                Date JOINIG_DT = null;
                if (jReuqest.getString("JOINIG_DT") == null || jReuqest.getString("JOINIG_DT").equals("")) {
                    JOINIG_DT = null;
                } else {
                    JOINIG_DT =  Date.valueOf(jReuqest.getString("JOINIG_DT")); 
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
                }
                String ACTIVE = jReuqest.getString("ACTIVE");
                Date RETIREMENT_DT = null;
                if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("RETIREMENT_DT").equals("")) {
                    RETIREMENT_DT = null;
                } else {
                    RETIREMENT_DT =  Date.valueOf(jReuqest.getString("RETIREMENT_DT")); 
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("RETIREMENT_DT"));
                }
                Date LEAVING_DT = null;
                if (jReuqest.getString("LEAVING_DT") == null || jReuqest.getString("LEAVING_DT").equals("")) {
                    LEAVING_DT = null;
                } else {
                    LEAVING_DT = Date.valueOf(jReuqest.getString("LEAVING_DT"));  
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LEAVING_DT"));
                }
                int GRADE_CD = 0;
                if (jReuqest.getString("GRADE_CD") == null || jReuqest.getString("GRADE_CD").equals("")) {
                    GRADE_CD = 0;
                } else {
                    GRADE_CD = jReuqest.getInt("GRADE_CD");
                }
                int DEPARTMENT_ID = 0;
                if (jReuqest.getString("DEPARTMENT_ID") == null || jReuqest.getString("DEPARTMENT_ID").equals("")) {
                    DEPARTMENT_ID = 0;
                } else {
                    DEPARTMENT_ID = jReuqest.getInt("DEPARTMENT_ID");
                }
                int DESIGNATION_ID = 0;
                if (jReuqest.getString("DESIGNATION_ID") == null || jReuqest.getString("DESIGNATION_ID").equals("")) {
                	DESIGNATION_ID = 0;
                } else {
                	DESIGNATION_ID = jReuqest.getInt("DESIGNATION_ID");
                }
                int SALARY_AMOUNT = 0;
                if (jReuqest.getString("SALARY_AMOUNT") == null || jReuqest.getString("SALARY_AMOUNT").equals("")) {
                    SALARY_AMOUNT = 0;
                } else {
                    SALARY_AMOUNT = jReuqest.getInt("SALARY_AMOUNT");
                }
                String LEAVING_REASON = jReuqest.getString("LEAVING_REASON");
                String COMP_REMARKS = jReuqest.getString("COMP_REMARKS");
                String CONTRACT_APPLICABLE = null;
                if (jReuqest.getString("CONTRACT_APPLICABLE") == null || jReuqest.getString("CONTRACT_APPLICABLE").equals("")) {
                    CONTRACT_APPLICABLE = null;
                } else {
                    CONTRACT_APPLICABLE = jReuqest.getString("CONTRACT_APPLICABLE");
                }
                String CONTRACT_TYPE =null;
                if (jReuqest.getString("CONTRACT_TYPE") == null || jReuqest.getString("CONTRACT_TYPE").equals("")) {
                    CONTRACT_TYPE = null;
                } else {
                    CONTRACT_TYPE = jReuqest.getString("CONTRACT_TYPE");
                }
                Date CONTRACT_SIGNED_DATE = null;
                if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("RETIREMENT_DT").equals("")) {
                    CONTRACT_SIGNED_DATE = null;
                } else {
                    CONTRACT_SIGNED_DATE = Date.valueOf(jReuqest.getString("CONTRACT_SIGNED_DATE"));  
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONTRACT_SIGNED_DATE"));
                }
                String PER_ADD_LINE_1 = jReuqest.getString("PER_ADD_LINE_1");
                String PER_ADD_LINE_2 = jReuqest.getString("PER_ADD_LINE_2");
                String PER_ADD_LINE_3 = jReuqest.getString("PER_ADD_LINE_3");
                int PER_COUNTRY_CD = 0;
                if (jReuqest.getString("PER_COUNTRY_CD") == null || jReuqest.getString("PER_COUNTRY_CD").equals("")) {
                    PER_COUNTRY_CD = 0;
                } else {
                    PER_COUNTRY_CD = jReuqest.getInt("PER_COUNTRY_CD");
                }
                int PER_STATE_CD = 0;
                if (jReuqest.getString("PER_STATE_CD") == null || jReuqest.getString("PER_STATE_CD").equals("")) {
                    PER_STATE_CD = 0;
                } else {
                    PER_STATE_CD = jReuqest.getInt("PER_STATE_CD");
                }
                int PER_CITY_CD = 0;
                if (jReuqest.getString("PER_CITY_CD") == null || jReuqest.getString("PER_CITY_CD").equals("")) {
                    PER_CITY_CD = 0;
                } else {
                    PER_CITY_CD = jReuqest.getInt("PER_CITY_CD");
                }
                String PER_PIN_CODE = jReuqest.getString("PER_PIN_CODE");
                String TEMP_ADD_LIINE_1 = jReuqest.getString("TEMP_ADD_LIINE_1");
                String TEMP_ADD_LIINE_2 = jReuqest.getString("TEMP_ADD_LIINE_2");
                String TEMP_ADD_LIINE_3 = jReuqest.getString("TEMP_ADD_LIINE_3");
                int TEMP_COUNTRY_CD = 0;
                if (jReuqest.getString("TEMP_COUNTRY_CD") == null || jReuqest.getString("TEMP_COUNTRY_CD").equals("")) {
                    TEMP_COUNTRY_CD = 0;
                } else {
                    TEMP_COUNTRY_CD = jReuqest.getInt("TEMP_COUNTRY_CD");
                }
                int TEMP_STATE_CD = 0;
                if (jReuqest.getString("TEMP_STATE_CD") == null || jReuqest.getString("TEMP_STATE_CD").equals("")) {
                    TEMP_STATE_CD = 0;
                } else {
                    TEMP_STATE_CD = jReuqest.getInt("TEMP_STATE_CD");
                }
                int TEMP_CITY_CD = 0;
                if (jReuqest.getString("TEMP_CITY_CD") == null || jReuqest.getString("TEMP_CITY_CD").equals("")) {
                    TEMP_CITY_CD = 0;
                } else {
                    TEMP_CITY_CD = jReuqest.getInt("TEMP_CITY_CD");
                }
                String TEMP_PIN_CODE = jReuqest.getString("TEMP_PIN_CODE");
                String ENTERED_BY = jReuqest.getString("ENTERED_BY");
                String ENTERED_IP = jReuqest.getString("ENTERED_IP");
                Date ENTERED_DATE = null;
                if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
                    ENTERED_DATE = null;
                } else {
                    ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE")); 
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
                }
                String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");
                String LAST_MODOFIED_IP = jReuqest.getString("LAST_MODOFIED_IP");
                Date LAST_MODIFIED_DT = null;
                if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
                    LAST_MODIFIED_DT = null;
                } else {
                    LAST_MODIFIED_DT = Date.valueOf(jReuqest.getString("LAST_MODIFIED_DT")); 
                    		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
                }            
                
                JSONArray insuranceDelete = new JSONArray(jReuqest.getString("INSURANCELISTDELETE"));
                int i = 0;
                try {
                    if (insuranceDelete.length() > 0) {
                        i = deleteInsu(con, insuranceDelete, COMP_CD);
                    }
                } catch (Exception e) {
                    Utility.PrintMessage("Error in Delete Insurance Details : " + e);
                    return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                }
                
                
                JSONArray insuranceInsert = new JSONArray(jReuqest.getString("INSURANCELISTINSERT"));
                int j = 0;
                System.out.println(insuranceInsert);
                try {
                    if (insuranceInsert.length() > 0) {
                        j = insertInsu(con, insuranceInsert, COMP_CD, EMP_ID);
                    }
                    
                    if (j == 99) {
                    	//Utility.PrintMessage("Error in Update Board Member Details : " + e);
                        return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Insurance Id Already Inserted.\"}";
                    }
                } catch (Exception e) {
                    Utility.PrintMessage("Error in Insert Board Member Details : " + e);
                    return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                }
                  
                JSONArray insuranceUpdate = new JSONArray(jReuqest.getString("INSURANCELISTUPDATE"));
                int k = 0;
                try {
                    if (insuranceUpdate.length() > 0) {
                        k = updateInsu(con, insuranceUpdate, COMP_CD,EMP_ID);
                    }
                } catch (Exception e) {
                    Utility.PrintMessage("Error in Update Board Member Details : " + e);
                   return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                }
                
                
                
                String updateEmployee = "UPDATE crm.emp_mst\r\n" + 
                		"SET\r\n" + 
                		"COMP_CD = ?,BRANCH_CD = ?,ACTIVE_STATUS = ?,FIRST_NM = ?,MIDDEL_NM = ?,LAST_NM = ?,PROFILE_IMG = ?,\r\n" + 
                		"EMAIL_ID = ?,INACTIVE_DT = ?,BIRTH_DT = ?,RELIGION_CD = ?,GENDER = ?,BLOOD_GRP = ?,MARRITAL_STATUS = ?,\r\n" + 
                		"HIGHEST_EDU = ?,UNIQUE_ID = ?,PAN_NO = ?,REF_EMP_ID = ?,REF_REMARKS = ?,REMARKS = ?,PRIMARY_CONTACT = ?,\r\n" + 
                		"SECONDARY_CONTACT = ?,RESUME = ?,CONFIRMATION_DT = ?,JOINIG_DT = ?,ACTIVE = ?,RETIREMENT_DT = ?,\r\n" + 
                		"LEAVING_DT = ?,GRADE_CD = ?,DEPARTMENT_ID = ?,DESIGNATION_ID = ?,SALARY_AMOUNT = ?,LEAVING_REASON = ?,COMP_REMARKS = ?,\r\n" + 
                		"CONTRACT_APPLICABLE = ?,CONTRACT_TYPE = ?,CONTRACT_SIGNED_DATE = ?,PER_ADD_LINE_1 = ?,PER_ADD_LINE_2 = ?,\r\n" + 
                		"PER_ADD_LINE_3 = ?,PER_COUNTRY_CD = ?,PER_STATE_CD = ?,PER_CITY_CD = ?,PER_PIN_CODE = ?,TEMP_ADD_LIINE_1 = ?,\r\n" + 
                		"TEMP_ADD_LIINE_2 = ?,TEMP_ADD_LIINE_3 = ?,TEMP_COUNTRY_CD = ?,TEMP_STATE_CD = ?,TEMP_CITY_CD = ?,\r\n" + 
                		"TEMP_PIN_CODE = ?,LAST_MODIFIED_BY = ?,LAST_MODOFIED_IP = ?,\r\n" + 
                		"LAST_MODIFIED_DT = ?,SHIFT_ID=? WHERE COMP_CD = ? AND EMP_ID = ?\r\n" + 
                		"";
                PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);
                System.out.println(DESIGNATION_ID);
                preparedStatement.setString(1, COMP_CD);
                preparedStatement.setString(2, BRANCH_CD);                
                preparedStatement.setString(3, ACTIVE_STATUS);
                preparedStatement.setString(4, FIRST_NM);
                preparedStatement.setString(5, MIDDEL_NM);
                preparedStatement.setString(6, LAST_NM);
                preparedStatement.setString(7, PROFILE_IMG);
                preparedStatement.setString(8, EMAIL_ID);
                preparedStatement.setDate(9, INACTIVE_DT);
                preparedStatement.setDate(10, BIRTH_DT);
                preparedStatement.setInt(11, RELIGION_CD);
                preparedStatement.setString(12, GENDER);
                preparedStatement.setString(13, BLOOD_GRP);
                preparedStatement.setString(14, MARRITAL_STATUS);
                preparedStatement.setString(15, HIGHEST_EDU);
                preparedStatement.setString(16, UNIQUE_ID);
                preparedStatement.setString(17, PAN_NO);
                preparedStatement.setInt(18, REF_EMP_ID);
                preparedStatement.setString(19, REF_REMARKS);
                preparedStatement.setString(20, REMARKS);
                preparedStatement.setInt(21, PRIMARY_CONTACT);
                preparedStatement.setInt(22, SECONDARY_CONTACT);
                preparedStatement.setString(23, RESUME);
                preparedStatement.setDate(24, CONFIRMATION_DT);
                preparedStatement.setDate(25, JOINIG_DT);
                preparedStatement.setString(26, ACTIVE);
                preparedStatement.setDate(27, RETIREMENT_DT);
                preparedStatement.setDate(28, LEAVING_DT);
                preparedStatement.setInt(29, GRADE_CD);
                preparedStatement.setInt(30, DEPARTMENT_ID);
                preparedStatement.setInt(31, DESIGNATION_ID);
                preparedStatement.setInt(32, SALARY_AMOUNT);
                preparedStatement.setString(33, LEAVING_REASON);
                preparedStatement.setString(34, COMP_REMARKS);
                preparedStatement.setString(35, CONTRACT_APPLICABLE);
                preparedStatement.setString(36, CONTRACT_TYPE);
                preparedStatement.setDate(37, CONTRACT_SIGNED_DATE);
                preparedStatement.setString(38, PER_ADD_LINE_1);
                preparedStatement.setString(39, PER_ADD_LINE_2);
                preparedStatement.setString(40, PER_ADD_LINE_3);
                preparedStatement.setInt(41, PER_COUNTRY_CD);
                preparedStatement.setInt(42, PER_STATE_CD);
                preparedStatement.setInt(43, PER_CITY_CD);
                preparedStatement.setString(44, PER_PIN_CODE);
                preparedStatement.setString(45, TEMP_ADD_LIINE_1);
                preparedStatement.setString(46, TEMP_ADD_LIINE_2);
                preparedStatement.setString(47, TEMP_ADD_LIINE_3);
                preparedStatement.setInt(48, TEMP_COUNTRY_CD);
                preparedStatement.setInt(49, TEMP_STATE_CD);
                preparedStatement.setInt(50, TEMP_CITY_CD);
                preparedStatement.setString(51, TEMP_PIN_CODE);                
                preparedStatement.setString(52, LAST_MODIFIED_BY);
                preparedStatement.setString(53, LAST_MODOFIED_IP);
                preparedStatement.setDate(54, LAST_MODIFIED_DT);
                preparedStatement.setInt(55, SHIFT_ID);	
                preparedStatement.setString(56, COMP_CD);
                preparedStatement.setInt(57, EMP_ID);                                
                int row = preparedStatement.executeUpdate();
                
                if (row == 0) {
                	con.rollback();
                } else {
                	con.commit();
                	                	                	
                }            
                
                jOutPut.put("STATUS_CD", "0");
                jOutPut.put("MESSAGE", "Employee ID " + EMP_ID + " Sucessfully Updated.");
                response = jOutPut.toString();
            }                                                                                              
        } catch (Exception e) {
            Utility.PrintMessage("Error in Update Employee : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
        } finally {
            try {
                rs.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                con.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return response;

    }
    
    public static String deleteEmployeeID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);                                
            JSONObject jOutPut = new JSONObject();
            
            int CON_ID = jin.getInt("EMP_ID");  
            String comp_cd= jin.getString("COMP_CD");
            
            String ls_del_flag = jin.getString("DELETE_FLAG");
            
            if(ls_del_flag.equals("Y")) {
            	 String extingEmp = "UPDATE EMP_MST SET IS_DELETE = 'Y' WHERE EMP_ID = " +CON_ID+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";
                 System.out.println(extingEmp);
                 stmt = con.createStatement();                 
                 int row = stmt.executeUpdate(extingEmp);
                 
                 if (row > 0) {
                 	con.commit();
                 	 jOutPut.put("STATUS_CD", "0");	
                 	jOutPut.put("MESSAGE", "Employee ID " + CON_ID + " Sucessfully Deleted.");
                 } else {
                 	con.rollback();
                 	 jOutPut.put("STATUS_CD", "99");	
                 	jOutPut.put("MESSAGE", "Employee ID Already Delted.");
                 }
                 		
                
            } else {
            	jOutPut.put("STATUS_CD", "99");	
            	jOutPut.put("MESSAGE", "Delete Not Allow.");
            }             	                                                        
            response = jOutPut.toString();
                                                                                                     
        } catch (Exception e) {
            Utility.PrintMessage("Error in Delete Contract : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
        } finally {
            try {
                rs.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                stmt.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
            try {
                con.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return response;

    }
    
    public static int deleteInsu(Connection con, JSONArray insuData, String comp_cd) throws JSONException, SQLException {
        Statement stmt = null;
        int row = 0;

        for (int i = 0; i < insuData.length(); i++) {
            JSONObject deleteInsu = new JSONObject();
            deleteInsu = (JSONObject) insuData.get(i);

            int id = deleteInsu.getInt("INSU_ID");
//            String ls_flag = deleteInsu.getString("DELETE_FLAG");
            String sql = null;
//            if (ls_flag.equals("Y")) {
                sql = "UPDATE emp_insurance_mst SET IS_DELETE = 'Y' WHERE COMP_CD = '" + comp_cd + "' AND INSU_ID =" + id;
                stmt = con.createStatement();
                row = stmt.executeUpdate(sql);
                row = row + 1;
//            }
        }
        return row;
    }
    
    public static int insertInsu(Connection con, JSONArray insuData, String ls_comp_cd,int emp_id) throws SQLException, JSONException {
        int row = 0;
        for (int i = 0; i < insuData.length(); i++) {
            JSONObject insertMem = new JSONObject();
            insertMem = (JSONObject) insuData.get(i);


            //String INSERT_FLAG = NVL.StringNvl(insertMem.getString("INSERT_FLAG"));
            
            int ID = 0;
            if (insertMem.getString("SRCD") == null || insertMem.getString("SRCD").equals("")) {
                ID = 0;
            } else {
                ID = insertMem.getInt("SRCD");
            }                       
            String INSURANCEAGENCY = NVL.StringNvl(insertMem.getString("INSURANCEAGENCY"));
            String INSURANCEREGNO = NVL.StringNvl(insertMem.getString("INSURANCEREGNO"));
            Double INSURANCEAMOUNT = 0.00;
            if (insertMem.getString("INSURANCEAMOUNT") == null || insertMem.getString("INSURANCEAMOUNT").equals("")) {
            	INSURANCEAMOUNT = 0.00;
            } else {
            	INSURANCEAMOUNT = insertMem.getDouble("INSURANCEAMOUNT");            			
            }
            String INSURANCETYPE = NVL.StringNvl(insertMem.getString("INSURANCETYPE"));
            Date INSURANCEINACTIVEDT = null;
            if (insertMem.getString("INSURANCEINACTIVEDT") == null || insertMem.getString("INSURANCEINACTIVEDT").equals("")) {
            	INSURANCEINACTIVEDT = null;
            } else {
            	INSURANCEINACTIVEDT = Date.valueOf(insertMem.getString("INSURANCEINACTIVEDT"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            Date INSURANCEENDDATE = null;
            if (insertMem.getString("INSURANCEENDDATE") == null || insertMem.getString("INSURANCEENDDATE").equals("")) {
            	INSURANCEINACTIVEDT = null;
            } else {
            	INSURANCEINACTIVEDT = Date.valueOf(insertMem.getString("INSURANCEENDDATE"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            String INSURANCETHROUGH = NVL.StringNvl(insertMem.getString("INSURANCETHROUGH"));
            Date INSURANCESTARTDATE = null;
            if (insertMem.getString("INSURANCESTARTDATE") == null || insertMem.getString("INSURANCESTARTDATE").equals("")) {
            	INSURANCEINACTIVEDT = null;
            } else {
            	INSURANCEINACTIVEDT = Date.valueOf(insertMem.getString("INSURANCESTARTDATE"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            String INSURANESTATUS = NVL.StringNvl(insertMem.getString("INSURANESTATUS"));
//            int INSU_AGENCY = 0;
//            if (insertMem.getString("INSU_AGENCY") == null || insertMem.getString("INSU_AGENCY").equals("")) {
//            	INSU_AGENCY = 0;
//            } else {
//            	INSU_AGENCY = insertMem.getInt("INSU_AGENCY");
//            } 
//            int INSU_CUST_NO = 0;
//            if (insertMem.getString("INSU_CUST_NO") == null || insertMem.getString("INSU_CUST_NO").equals("")) {
//            	INSU_CUST_NO = 0;
//            } else {
//            	INSU_CUST_NO = insertMem.getInt("INSU_CUST_NO");
//            } 
//            
//            String POLICY = NVL.StringNvl(insertMem.getString("POLICY"));
//            byte[] POLICY_BUTE = POLICY.getBytes();
//        	
//        	
//        	Blob POLICY_BLOB = new SerialBlob(POLICY_BUTE);
     	
            
            
            Statement stmt = null;
            ResultSet rs = null;

            String ls_sql = "SELECT COUNT(*) AS CNT FROM emp_insurance_mst WHERE INSU_ID =" + ID + " AND IS_DELETE = 'N'";
            System.out.println(ls_sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(ls_sql);

            int cnt = 0;
            while (rs.next()) {
                cnt = rs.getInt("CNT");
            }

            if (cnt <= 0) {
               // if (INSERT_FLAG.equals("Y")) {
                    JSONObject jOut = new JSONObject();
                    String sql = "INSERT INTO emp_insurance_mst (COMP_CD, EMP_ID, INSU_THROUGH, INSU_ID, INSU_TYPE, INSU_REG_NO, INSU_START_DT, INSU_END_DT, INSU_STATUS, INSU_INACTIVE_DT, INSU_AGENCY_ID, INSU_PREMIUM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = null;

                    try {
                        preparedStatement = con.prepareStatement(sql);                                              
                        preparedStatement.setString(1, ls_comp_cd);
                        preparedStatement.setInt(2, emp_id);
                        preparedStatement.setString(3, INSURANCETHROUGH);
                        preparedStatement.setInt(4, ID);
                        preparedStatement.setString(5, INSURANCETYPE);
                        preparedStatement.setString(6, INSURANCEREGNO);
                        preparedStatement.setDate(7, INSURANCESTARTDATE);
                        preparedStatement.setDate(8, INSURANCEENDDATE);                       
                        preparedStatement.setString(9, INSURANESTATUS);
                        preparedStatement.setDate(10, INSURANCEINACTIVEDT);
                        preparedStatement.setString(11, INSURANCEAGENCY);
//                        preparedStatement.setInt(12, INSU_AGENCY);
//                        preparedStatement.setInt(12, INSU_CUST_NO);
                        preparedStatement.setDouble(12, INSURANCEAMOUNT);
//                        preparedStatement.setBlob(14, POLICY_BLOB);
                        
                       
                        row = preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        row = 0;
                        Utility.PrintMessage("Error in Create Insurance: " + e);
                        // response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                    }
//                } else {
//                    row = 0;
//                }
            } else {
            	row = 99;
            }
            	
        }
        return row;
    }
    
    public static int updateInsu(Connection con, JSONArray insuData, String ls_comp_cd,int emp_id) throws JSONException {
        int row = 0;
        for (int i = 0; i < insuData.length(); i++) {
            JSONObject updateMem = new JSONObject();
            updateMem = (JSONObject) insuData.get(i);

            int id = updateMem.getInt("INSU_ID");
//            String UPDATE_FLAG = NVL.StringNvl(updateMem.getString("UPDATE_FLAG"));
            JSONObject updateData = new JSONObject(updateMem.getString("DATA"));
            
            String INSURANCEAGENCY = NVL.StringNvl(updateData.getString("INSURANCEAGENCY"));
            String INSURANCEREGNO = NVL.StringNvl(updateData.getString("INSURANCEREGNO"));
            Double INSURANCEAMOUNT = 0.00;
            if (updateData.getString("INSURANCEAMOUNT") == null || updateData.getString("INSURANCEAMOUNT").equals("")) {
            	INSURANCEAMOUNT = 0.00;
            } else {
            	INSURANCEAMOUNT = updateData.getDouble("INSURANCEAMOUNT");            			
            }
            String INSURANCETYPE = NVL.StringNvl(updateData.getString("INSURANCETYPE"));
            Date INSURANCEINACTIVEDT = null;
            if (updateData.getString("INSURANCEINACTIVEDT") == null || updateData.getString("INSURANCEINACTIVEDT").equals("")) {
            	INSURANCEINACTIVEDT = null;
            } else {
            	INSURANCEINACTIVEDT = Date.valueOf(updateData.getString("INSURANCEINACTIVEDT"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            Date INSURANCEENDDATE = null;
            if (updateData.getString("INSURANCEENDDATE") == null || updateData.getString("INSURANCEENDDATE").equals("")) {
            	INSURANCEINACTIVEDT = null;
            } else {
            	INSURANCEINACTIVEDT = Date.valueOf(updateData.getString("INSURANCEENDDATE"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            String INSURANCETHROUGH = NVL.StringNvl(updateData.getString("INSURANCETHROUGH"));
            Date INSURANCESTARTDATE = null;
            if (updateData.getString("INSURANCESTARTDATE") == null || updateData.getString("INSURANCESTARTDATE").equals("")) {
            	INSURANCEINACTIVEDT = null;
            } else {
            	INSURANCEINACTIVEDT = Date.valueOf(updateData.getString("INSURANCESTARTDATE"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            String INSURANESTATUS = NVL.StringNvl(updateData.getString("INSURANESTATUS"));
//            int INSU_AGENCY = 0;
//            if (updateData.getString("INSU_AGENCY") == null || updateData.getString("INSU_AGENCY").equals("")) {
//            	INSU_AGENCY = 0;
//            } else {
//            	INSU_AGENCY = updateData.getInt("INSU_AGENCY");
//            } 
//            int INSU_CUST_NO = 0;
//            if (updateData.getString("INSU_CUST_NO") == null || updateData.getString("INSU_CUST_NO").equals("")) {
//            	INSU_CUST_NO = 0;
//            } else {
//            	INSU_CUST_NO = updateData.getInt("INSU_CUST_NO");
//            } 


//            if (UPDATE_FLAG.equals("Y")) {
                JSONObject jOut = new JSONObject();
                String sql = "UPDATE emp_insurance_mst SET COMP_CD =? , EMP_ID =?, INSU_THROUGH = ?, INSU_ID = ?, INSU_TYPE = ?, INSU_REG_NO = ?, INSU_BOOK_NO = ?, INSU_START_DT = ?, INSU_END_DT = ?, INSU_STATUS = ?, INSU_INACTIVE_DT = ?, INSU_AGENCY_ID = ?, INSU_PREMIUM = ?, INSU_DOC_POLICY = ? WHERE COMP_CD = ? AND BRANCH_CD = ? AND INSU_ID = ? EMP_ID = ?";
                PreparedStatement preparedStatement = null;

                try {
                	preparedStatement = con.prepareStatement(sql);                                              
                    preparedStatement.setString(1, ls_comp_cd);
                    preparedStatement.setInt(2, emp_id);
                    preparedStatement.setString(3, INSURANCETHROUGH);                   
                    preparedStatement.setString(4, INSURANCETYPE);
                    preparedStatement.setString(5, INSURANCEREGNO);
                    preparedStatement.setDate(6, INSURANCESTARTDATE);
                    preparedStatement.setDate(7, INSURANCEENDDATE);                       
                    preparedStatement .setString(9, INSURANESTATUS);
                    preparedStatement.setDate(9, INSURANCEINACTIVEDT);
                    preparedStatement.setString(10, INSURANCEAGENCY);
//                    preparedStatement.setInt(11, INSU_AGENCY);
//                    preparedStatement.setInt(11, INSU_CUST_NO);
                    preparedStatement.setDouble(11, INSURANCEAMOUNT);

                    row = preparedStatement.executeUpdate();
                } catch (Exception e) {
                    row = 0;
                    Utility.PrintMessage("Error in Update Insurance Master : " + e);
                    // response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                }
//            } else {
//                row = 0;
//            }
        }
        return 0;
    }

}