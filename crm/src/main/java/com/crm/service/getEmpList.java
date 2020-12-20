package com.crm.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crm.utility.Utility;

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

            String viewType = jin.getString("VIEW_FLAG");
            String enteredUser = jin.getString("USERNAME");
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
                empID = rs.getString("EMP_ID");
                empNM = rs.getString("EMP_NM");
                primaryContact = rs.getString("primary_CONTACT");
                birthDate = rs.getString("BIRTH_DT");
                gender = rs.getString("GENDER");
                designation = rs.getString("DESIGNATION_ID");
                Status = rs.getString("ACTIVE_STATUS");
                photoPath = rs.getString("PROFILE_IMG");

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

            String ls_emp_id = jin.getString("EMP_ID");
            String viewFlag = jin.getString("VIEW_FLAG");
            String userName = jin.getString("USERNAME");

            JSONObject jOut = new JSONObject();
            String sql = null;
            if (viewFlag.equals("G")) {
                sql = "SELECT COMP_CD,BRANCH_CD,EMP_ID,ACTIVE_STATUS,ENTRY_DT,FIRST_NM,MIDDEL_NM,LAST_NM,PROFILE_IMG,EMAIL_ID,INACTIVE_DT,BIRTH_DT,RELIGION_CD,GENDER,BLOOD_GRP,MARRITAL_STATUS,HIGHEST_EDU,UNIQUE_ID,\r\n" + "PAN_NO,REF_EMP_ID,REF_REMARKS,REMARKS,PRIMARY_CONTACT,SECONDARY_CONTACT,RESUME,CONFIRMATION_DT,JOINIG_DT,RETIREMENT_DT,LEAVING_DT,GRADE_CD,DEPARTMENT_ID,DESIGNATION_ID,\r\n" + "SALARY_AMOUNT,LEAVING_REASON,COMP_REMARKS,CONTRACT_APPLICABLE,CONTRACT_TYPE,CONTRACT_SIGNED_DATE,PER_ADD_LINE_1,PER_ADD_LINE_2,PER_ADD_LINE_3,PER_COUNTRY_CD,\r\n" + "PER_STATE_CD,PER_PIN_CODE,TEMP_ADD_LIINE_1,TEMP_ADD_LIINE_2,TEMP_ADD_LIINE_3,TEMP_COUNTRY_CD,TEMP_STATE_CD,TEMP_CITY_CD,TEMP_PIN_CODE FROM EMP_MST WHERE EMP_ID = " + Integer.parseInt(ls_emp_id) +" AND IS_DELETE = 'N'";
            } else {
                sql = "SELECT COMP_CD,BRANCH_CD,EMP_ID,ACTIVE_STATUS,ENTRY_DT,FIRST_NM,MIDDEL_NM,LAST_NM,PROFILE_IMG,EMAIL_ID,INACTIVE_DT,BIRTH_DT,RELIGION_CD,GENDER,BLOOD_GRP,MARRITAL_STATUS,HIGHEST_EDU,UNIQUE_ID,\r\n" + "PAN_NO,REF_EMP_ID,REF_REMARKS,REMARKS,PRIMARY_CONTACT,SECONDARY_CONTACT,RESUME,CONFIRMATION_DT,JOINIG_DT,RETIREMENT_DT,LEAVING_DT,GRADE_CD,DEPARTMENT_ID,DESIGNATION_ID,\r\n" + "SALARY_AMOUNT,LEAVING_REASON,COMP_REMARKS,CONTRACT_APPLICABLE,CONTRACT_TYPE,CONTRACT_SIGNED_DATE,PER_ADD_LINE_1,PER_ADD_LINE_2,PER_ADD_LINE_3,PER_COUNTRY_CD,\r\n" + "PER_STATE_CD,PER_PIN_CODE,TEMP_ADD_LIINE_1,TEMP_ADD_LIINE_2,TEMP_ADD_LIINE_3,TEMP_COUNTRY_CD,TEMP_STATE_CD,TEMP_CITY_CD,TEMP_PIN_CODE FROM EMP_MST WHERE EMP_ID = " + Integer.parseInt(ls_emp_id) + " AND ENTERED_BY = '" + userName + "' AND IS_DELETE = 'N'";

            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ll_cnt = ll_cnt + 1;
                jOut.put("COMP_CD", rs.getString("COMP_CD"));
                jOut.put("BRANCH_CD", rs.getString("BRANCH_CD"));
                jOut.put("EMP_ID", rs.getString("EMP_ID"));
                jOut.put("ACTIVE_STATUS", rs.getString("ACTIVE_STATUS"));
                jOut.put("ENTRY_DT", rs.getString("ENTRY_DT"));
                jOut.put("FIRST_NM", rs.getString("FIRST_NM"));
                jOut.put("MIDDLE_NM", rs.getString("MIDDEL_NM"));
                jOut.put("LAST_NM", rs.getString("LAST_NM"));
                jOut.put("PROFILE_IMG", rs.getString("PROFILE_IMG"));
                jOut.put("EMIAL_ID", rs.getString("EMAIL_ID"));
                jOut.put("INACTIVE_DT", rs.getString("INACTIVE_DT"));
                jOut.put("BIRTH_DT", rs.getString("BIRTH_DT"));
                jOut.put("RELIGION_CD", rs.getString("RELIGION_CD"));
                jOut.put("GENDER", rs.getString("GENDER"));
                jOut.put("BLOOD_GROUP", rs.getString("BLOOD_GRP"));
                jOut.put("MARRITAL_STATUS", rs.getString("MARRITAL_STATUS"));
                jOut.put("HIGHEST_EDU", rs.getString("HIGHEST_EDU"));
                jOut.put("UNIQUE_ID", rs.getString("UNIQUE_ID"));
                jOut.put("PAN_NO", rs.getString("PAN_NO"));
                jOut.put("REF_EMP_ID", rs.getString("REF_EMP_ID"));
                jOut.put("REF_REMARKS", rs.getString("REF_REMARKS"));
                jOut.put("REMARKS", rs.getString("REMARKS"));
                jOut.put("PRIMARY_CONTACT", rs.getString("PRIMARY_CONTACT"));
                jOut.put("SECONDARY_CONTACT", rs.getString("SECONDARY_CONTACT"));
                jOut.put("RESUME", rs.getString("RESUME"));
                jOut.put("CONFIRMATION_DT", rs.getString("CONFIRMATION_DT"));
                jOut.put("JOINIG_DT", rs.getString("JOINIG_DT"));
                jOut.put("RETIREMENT_DT", rs.getString("RETIREMENT_DT"));
                jOut.put("LEAVING_DT", rs.getString("LEAVING_DT"));
                jOut.put("GRADE_CD", rs.getString("GRADE_CD"));
                jOut.put("DEPARTMENT_ID", rs.getString("DEPARTMENT_ID"));
                jOut.put("DESIGNATION_ID", rs.getString("DESIGNATION_ID"));
                jOut.put("SALARY_AMOUNT", rs.getString("SALARY_AMOUNT"));
                jOut.put("LEAVING_REASON", rs.getString("LEAVING_REASON"));
                jOut.put("COMP_REMARKS", rs.getString("COMP_REMARKS"));
                jOut.put("CONTRACT_APPLICABLE", rs.getString("CONTRACT_APPLICABLE"));
                jOut.put("CONTRACT_TYPE", rs.getString("CONTRACT_TYPE"));
                jOut.put("CONTRACT_SIGNED_DATE", rs.getString("CONTRACT_SIGNED_DATE"));
                jOut.put("PER_ADD_LINE_1", rs.getString("PER_ADD_LINE_1"));
                jOut.put("PER_ADD_LINE_2", rs.getString("PER_ADD_LINE_2"));
                jOut.put("PER_ADD_LINE_3", rs.getString("PER_ADD_LINE_3"));
                jOut.put("PER_COUNTRY_CD", rs.getString("PER_COUNTRY_CD"));
                jOut.put("PER_STATE_CD", rs.getString("PER_STATE_CD"));
                jOut.put("PER_PIN_CODE", rs.getString("PER_PIN_CODE"));
                jOut.put("TEMP_ADD_LINE_1", rs.getString("TEMP_ADD_LIINE_1"));
                jOut.put("TEMP_ADD_LINE_2", rs.getString("TEMP_ADD_LIINE_2"));
                jOut.put("TEMP_ADD_LINE_3", rs.getString("TEMP_ADD_LIINE_3"));
                jOut.put("TEMP_COUNTRY_CD", rs.getString("TEMP_COUNTRY_CD"));
                jOut.put("TEMP_STATE_CD", rs.getString("TEMP_STATE_CD"));
                jOut.put("TEMP_CITY_CD", rs.getString("TEMP_CITY_CD"));
                jOut.put("TEMP_PIN_CODE", rs.getString("TEMP_PIN_CODE"));
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
                ENTRY_DT = (Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
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
                INACTIVE_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
            }

            Date BIRTH_DT = null;
            if (jReuqest.getString("BIRTH_DT") == null || jReuqest.getString("BIRTH_DT").equals("")) {
                BIRTH_DT = null;
            } else {
                BIRTH_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("BIRTH_DT"));
            }
            int RELIGION_CD = jReuqest.getInt("RELIGION_CD");
            String GENDER = jReuqest.getString("GENDER");
            String BLOOD_GRP = jReuqest.getString("BLOOD_GRP");
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
                CONFIRMATION_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONFIRMATION_DT"));
            }
            Date JOINIG_DT = null;
            if (jReuqest.getString("JOINIG_DT") == null || jReuqest.getString("JOINIG_DT").equals("")) {
                JOINIG_DT = null;
            } else {
                JOINIG_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            String ACTIVE = jReuqest.getString("ACTIVE");
            Date RETIREMENT_DT = null;
            if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("RETIREMENT_DT").equals("")) {
                RETIREMENT_DT = null;
            } else {
                RETIREMENT_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("RETIREMENT_DT"));
            }
            Date LEAVING_DT = null;
            if (jReuqest.getString("LEAVING_DT") == null || jReuqest.getString("LEAVING_DT").equals("")) {
                LEAVING_DT = null;
            } else {
                LEAVING_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LEAVING_DT"));
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
            int CONTRACT_APPLICABLE = 0;
            if (jReuqest.getString("CONTRACT_APPLICABLE") == null || jReuqest.getString("CONTRACT_APPLICABLE").equals("")) {
                CONTRACT_APPLICABLE = 0;
            } else {
                CONTRACT_APPLICABLE = jReuqest.getInt("CONTRACT_APPLICABLE");
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
                CONTRACT_SIGNED_DATE = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONTRACT_SIGNED_DATE"));
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
                ENTERED_DATE = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
            }
            String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");
            String LAST_MODOFIED_IP = jReuqest.getString("LAST_MODOFIED_IP");
            Date LAST_MODIFIED_DT = null;
            if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
                LAST_MODIFIED_DT = null;
            } else {
                LAST_MODIFIED_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
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
                String sql = "INSERT INTO crm.emp_mst(COMP_CD,BRANCH_CD,EMP_ID,ENTRY_DT,ACTIVE_STATUS,FIRST_NM,\r\n" + "MIDDEL_NM,LAST_NM,PROFILE_IMG,EMAIL_ID,INACTIVE_DT,BIRTH_DT,RELIGION_CD,\r\n" + "GENDER,BLOOD_GRP,MARRITAL_STATUS,HIGHEST_EDU,UNIQUE_ID,PAN_NO,REF_EMP_ID,REF_REMARKS,\r\n" + "REMARKS,PRIMARY_CONTACT,SECONDARY_CONTACT,RESUME,CONFIRMATION_DT,JOINIG_DT,\r\n" + "ACTIVE,RETIREMENT_DT,LEAVING_DT,GRADE_CD,DEPARTMENT_ID,DESIGNATION_ID,SALARY_AMOUNT,\r\n" + "LEAVING_REASON,COMP_REMARKS,CONTRACT_APPLICABLE,CONTRACT_TYPE,CONTRACT_SIGNED_DATE,PER_ADD_LINE_1,PER_ADD_LINE_2,PER_ADD_LINE_3,PER_COUNTRY_CD,PER_STATE_CD,PER_CITY_CD,PER_PIN_CODE,TEMP_ADD_LIINE_1,TEMP_ADD_LIINE_2,TEMP_ADD_LIINE_3,TEMP_COUNTRY_CD,TEMP_STATE_CD,TEMP_CITY_CD,TEMP_PIN_CODE,ENTERED_BY,ENTERED_IP,ENTERED_DATE,LAST_MODIFIED_BY,LAST_MODOFIED_IP,LAST_MODIFIED_DT)\r\n" + "VALUES\r\n" + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                preparedStatement.setInt(37, CONTRACT_APPLICABLE);
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
                    ENTRY_DT = (Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
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
                    INACTIVE_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("INACTIVE_DT"));
                }

                Date BIRTH_DT = null;
                if (jReuqest.getString("BIRTH_DT") == null || jReuqest.getString("BIRTH_DT").equals("")) {
                    BIRTH_DT = null;
                } else {
                    BIRTH_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("BIRTH_DT"));
                }
                int RELIGION_CD = jReuqest.getInt("RELIGION_CD");
                String GENDER = jReuqest.getString("GENDER");
                String BLOOD_GRP = jReuqest.getString("BLOOD_GRP");
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
                    CONFIRMATION_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONFIRMATION_DT"));
                }
                Date JOINIG_DT = null;
                if (jReuqest.getString("JOINIG_DT") == null || jReuqest.getString("JOINIG_DT").equals("")) {
                    JOINIG_DT = null;
                } else {
                    JOINIG_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
                }
                String ACTIVE = jReuqest.getString("ACTIVE");
                Date RETIREMENT_DT = null;
                if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("RETIREMENT_DT").equals("")) {
                    RETIREMENT_DT = null;
                } else {
                    RETIREMENT_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("RETIREMENT_DT"));
                }
                Date LEAVING_DT = null;
                if (jReuqest.getString("LEAVING_DT") == null || jReuqest.getString("LEAVING_DT").equals("")) {
                    LEAVING_DT = null;
                } else {
                    LEAVING_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LEAVING_DT"));
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
                int CONTRACT_APPLICABLE = 0;
                if (jReuqest.getString("CONTRACT_APPLICABLE") == null || jReuqest.getString("CONTRACT_APPLICABLE").equals("")) {
                    CONTRACT_APPLICABLE = 0;
                } else {
                    CONTRACT_APPLICABLE = jReuqest.getInt("CONTRACT_APPLICABLE");
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
                    CONTRACT_SIGNED_DATE = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("CONTRACT_SIGNED_DATE"));
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
                    ENTERED_DATE = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
                }
                String LAST_MODIFIED_BY = jReuqest.getString("LAST_MODIFIED_BY");
                String LAST_MODOFIED_IP = jReuqest.getString("LAST_MODOFIED_IP");
                Date LAST_MODIFIED_DT = null;
                if (jReuqest.getString("RETIREMENT_DT") == null || jReuqest.getString("LAST_MODIFIED_DT").equals("")) {
                    LAST_MODIFIED_DT = null;
                } else {
                    LAST_MODIFIED_DT = (Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
                }            
                
                
                String updateEmployee = "UPDATE crm.emp_mst\r\n" + 
                		"SET\r\n" + 
                		"COMP_CD = ?,BRANCH_CD = ?,ENTRY_DT = ?,ACTIVE_STATUS = ?,FIRST_NM = ?,MIDDEL_NM = ?,LAST_NM = ?,PROFILE_IMG = ?,\r\n" + 
                		"EMAIL_ID = ?,INACTIVE_DT = ?,BIRTH_DT = ?,RELIGION_CD = ?,GENDER = ?,BLOOD_GRP = ?,MARRITAL_STATUS = ?,\r\n" + 
                		"HIGHEST_EDU = ?,UNIQUE_ID = ?,PAN_NO = ?,REF_EMP_ID = ?,REF_REMARKS = ?,REMARKS = ?,PRIMARY_CONTACT = ?,\r\n" + 
                		"SECONDARY_CONTACT = ?,RESUME = ?,CONFIRMATION_DT = ?,JOINIG_DT = ?,ACTIVE = ?,RETIREMENT_DT = ?,\r\n" + 
                		"LEAVING_DT = ?,GRADE_CD = ?,DEPARTMENT_ID = ?,DESIGNATION_ID = ?,SALARY_AMOUNT = ?,LEAVING_REASON = ?,COMP_REMARKS = ?,\r\n" + 
                		"CONTRACT_APPLICABLE = ?,CONTRACT_TYPE = ?,CONTRACT_SIGNED_DATE = ?,PER_ADD_LINE_1 = ?,PER_ADD_LINE_2 = ?,\r\n" + 
                		"PER_ADD_LINE_3 = ?,PER_COUNTRY_CD = ?,PER_STATE_CD = ?,PER_CITY_CD = ?,PER_PIN_CODE = ?,TEMP_ADD_LIINE_1 = ?,\r\n" + 
                		"TEMP_ADD_LIINE_2 = ?,TEMP_ADD_LIINE_3 = ?,TEMP_COUNTRY_CD = ?,TEMP_STATE_CD = ?,TEMP_CITY_CD = ?,\r\n" + 
                		"TEMP_PIN_CODE = ?,ENTERED_BY = ?,ENTERED_IP = ?,ENTERED_DATE = ?,LAST_MODIFIED_BY = ?,LAST_MODOFIED_IP = ?,\r\n" + 
                		"LAST_MODIFIED_DT = ? WHERE COMP_CD = ? AND EMP_ID = ?\r\n" + 
                		"";
                PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);
                
                preparedStatement.setString(1, COMP_CD);
                preparedStatement.setString(2, BRANCH_CD);                
                preparedStatement.setDate(3, ENTRY_DT);
                preparedStatement.setString(4, ACTIVE_STATUS);
                preparedStatement.setString(5, FIRST_NM);
                preparedStatement.setString(6, MIDDEL_NM);
                preparedStatement.setString(7, LAST_NM);
                preparedStatement.setString(8, PROFILE_IMG);
                preparedStatement.setString(9, EMAIL_ID);
                preparedStatement.setDate(10, INACTIVE_DT);
                preparedStatement.setDate(11, BIRTH_DT);
                preparedStatement.setInt(12, RELIGION_CD);
                preparedStatement.setString(13, GENDER);
                preparedStatement.setString(14, BLOOD_GRP);
                preparedStatement.setString(15, MARRITAL_STATUS);
                preparedStatement.setString(16, HIGHEST_EDU);
                preparedStatement.setString(17, UNIQUE_ID);
                preparedStatement.setString(18, PAN_NO);
                preparedStatement.setInt(19, REF_EMP_ID);
                preparedStatement.setString(20, REF_REMARKS);
                preparedStatement.setString(21, REMARKS);
                preparedStatement.setInt(22, PRIMARY_CONTACT);
                preparedStatement.setInt(23, SECONDARY_CONTACT);
                preparedStatement.setString(24, RESUME);
                preparedStatement.setDate(25, CONFIRMATION_DT);
                preparedStatement.setDate(26, JOINIG_DT);
                preparedStatement.setString(27, ACTIVE);
                preparedStatement.setDate(28, RETIREMENT_DT);
                preparedStatement.setDate(29, LEAVING_DT);
                preparedStatement.setInt(30, GRADE_CD);
                preparedStatement.setInt(31, DEPARTMENT_ID);
                preparedStatement.setInt(32, DESIGNATION_ID);
                preparedStatement.setInt(33, SALARY_AMOUNT);
                preparedStatement.setString(34, LEAVING_REASON);
                preparedStatement.setString(35, COMP_REMARKS);
                preparedStatement.setInt(36, CONTRACT_APPLICABLE);
                preparedStatement.setInt(37, CONTRACT_TYPE);
                preparedStatement.setDate(38, CONTRACT_SIGNED_DATE);
                preparedStatement.setString(39, PER_ADD_LINE_1);
                preparedStatement.setString(40, PER_ADD_LINE_2);
                preparedStatement.setString(41, PER_ADD_LINE_3);
                preparedStatement.setInt(42, PER_COUNTRY_CD);
                preparedStatement.setInt(43, PER_STATE_CD);
                preparedStatement.setInt(44, PER_CITY_CD);
                preparedStatement.setString(45, PER_PIN_CODE);
                preparedStatement.setString(46, TEMP_ADD_LIINE_1);
                preparedStatement.setString(47, TEMP_ADD_LIINE_2);
                preparedStatement.setString(48, TEMP_ADD_LIINE_3);
                preparedStatement.setInt(49, TEMP_COUNTRY_CD);
                preparedStatement.setInt(50, TEMP_STATE_CD);
                preparedStatement.setInt(51, TEMP_CITY_CD);
                preparedStatement.setString(52, TEMP_PIN_CODE);
                preparedStatement.setString(53, ENTERED_BY);
                preparedStatement.setString(54, ENTERED_IP);
                preparedStatement.setDate(55, ENTERED_DATE);
                preparedStatement.setString(56, LAST_MODIFIED_BY);
                preparedStatement.setString(57, LAST_MODOFIED_IP);
                preparedStatement.setDate(58, LAST_MODIFIED_DT);
                	
                preparedStatement.setString(59, COMP_CD);
                preparedStatement.setInt(60, EMP_ID);                                
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

}