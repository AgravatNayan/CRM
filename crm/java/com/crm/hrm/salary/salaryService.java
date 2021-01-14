package com.crm.hrm.salary;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.NVL;
import com.crm.utility.Utility;

public class salaryService {
    public static String GetMaxSalaryTranCD(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(TRAN_CD) AS ID FROM SALARY_HEAD_MST";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("ID");
            }
            maxID = maxID + 1;
            JSONArray jresponse = new JSONArray();
            JSONObject jobj = new JSONObject();
            jobj.put("USERNAME", jin.getString("USERNAME"));
            jobj.put("TRAN_CD", maxID);
            jresponse.put(jobj);
            jOut.put("STATUS_CD", "0");
            jOut.put("RESPONSE", jresponse);
            response = jOut.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in GetMax Salary Tran CD : " + e);
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

    public static String GetMaxSalaryHeadCD(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();

            int TRAN_CD = jin.getInt("TRAN_CD");
            String sql = "SELECT MAX(TRAN_CD) AS ID FROM SALARY_HEAD_DTL WHERE TRAN_CD = " + TRAN_CD;
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int maxID = 0;
            if (rs.next()) {
                maxID = rs.getInt("ID");
            }
            maxID = maxID + 1;
            JSONArray jresponse = new JSONArray();
            JSONObject jobj = new JSONObject();
            jobj.put("USERNAME", jin.getString("USERNAME"));
            jobj.put("HEAD_CD", maxID);
            jresponse.put(jobj);
            jOut.put("STATUS_CD", "0");
            jOut.put("RESPONSE", jresponse);
            response = jOut.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in GetMax Salary Head CD : " + e);
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

    public static String getSalaryHeadList(Connection con, String requestData) throws JSONException,
        ClassNotFoundException,
        SQLException {

            JSONObject jObject = null;
            JSONArray jArray = new JSONArray();
            JSONObject mainObject = new JSONObject();
            ResultSet rs = null;
            JSONObject request = new JSONObject(requestData);
            JSONObject jError = new JSONObject();
            String response = "";
            String ls_username = request.getString("USERNAME");
            String ls_req_ip = request.getString("REQUEST_IP");
            String viewType = NVL.StringNvl(request.getString("VIEW_FLAG"));
            String compCD = NVL.StringNvl(request.getString("COMP_CD"));
            String ls_query = null;
            try {
                if (viewType.equals("G")) {
                    ls_query = "SELECT COMP_CD, TRAN_CD, TEMP_NAME, APP_FROM_DT, APP_TO_DT, ENTERED_DATE, ENTERED_BY, ENTERED_IP, LAST_ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP FROM salary_head_mst WHERE COMP_CD = '" + compCD + "' AND IS_DELETE = 'N'";
                } else {
                    ls_query = "SELECT COMP_CD, TRAN_CD, TEMP_NAME, APP_FROM_DT, APP_TO_DT, ENTERED_DATE, ENTERED_BY, ENTERED_IP, LAST_ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP FROM salary_head_mst WHERE COMP_CD = '" + compCD + "' AND ENTERED_DATE = '" + ls_username + "' AND IS_DELETE = 'N'";
                }
             
                Statement stmt = null;
                stmt = con.createStatement();
                rs = stmt.executeQuery(ls_query);

                while (rs.next()) {
                    jObject = new JSONObject();
                    jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                    jObject.put("TRAN_CD", NVL.StringNvl(rs.getString("TRAN_CD")));
                    jObject.put("TEMP_NAME", NVL.StringNvl(rs.getString("TEMP_NAME")));
                    jObject.put("ID", NVL.StringNvl(rs.getString("TRAN_CD")));
                    jObject.put("NAME", NVL.StringNvl(rs.getString("TEMP_NAME")));
                    jObject.put("APP_FROM_DT", NVL.StringNvl(rs.getString("APP_FROM_DT")));
                    jObject.put("APP_TO_DT", NVL.StringNvl(rs.getString("APP_TO_DT")));
                    jObject.put("ENTERED_DATE", NVL.StringNvl(rs.getString("ENTERED_DATE")));
                    jObject.put("ENTERED_BY", NVL.StringNvl(rs.getString("ENTERED_BY")));
                    jObject.put("ENTERED_IP", NVL.StringNvl(rs.getString("ENTERED_IP")));
                    jObject.put("LAST_ENTERED_DATE", NVL.StringNvl(rs.getString("LAST_ENTERED_DATE")));
                    jObject.put("LAST_ENTERED_BY", NVL.StringNvl(rs.getString("LAST_ENTERED_BY")));
                    jObject.put("LAST_ENTERED_IP", NVL.StringNvl(rs.getString("LAST_ENTERED_IP")));

                    JSONArray jSubDetail = new JSONArray();
                    jSubDetail = jheadSubDetail(con, Integer.parseInt(NVL.StringNvl(rs.getString("TRAN_CD"))), viewType, NVL.StringNvl(rs.getString("COMP_CD")), ls_username);
                    jObject.put("HEAD_SUB_DTL", jSubDetail);
                    jArray.put(jObject);
                }
                if (jArray.length() <= 0) {
                    mainObject.put("STATUS_CD", "99");
                    mainObject.put("MESSAGE", "Salary Head not found");
                } else {
                    mainObject.put("STATUS_CD", "0");
                    mainObject.put("RESPONSE", jArray);
                }


                response = mainObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Utility.PrintMessage("Error in Get User List : " + e);
                response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";

            }
            return response;
        }

    public static JSONArray jheadSubDetail(Connection con, int head_cd, String ls_view_flag, String ls_comp_cd, String ls_username) throws JSONException {
        JSONObject jObject = null;
        JSONArray jArray = new JSONArray();
        JSONObject mainObject = new JSONObject();
        ResultSet rs = null;
        JSONObject jError = new JSONObject();
        String response = "";
        String ls_query = null;

        try {
            if (ls_view_flag.equals("G")) {
                ls_query = "SELECT COMP_CD, TRAN_CD, SR_CD, HEAD_CD, HEAD_NM, ED_FLAG, AMT, ENTERED_DATE, ENTERED_BY, ENTERED_IP, LAST_ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP FROM salary_head_dtl WHERE COMP_CD ='" + ls_comp_cd + "' AND TRAN_CD = " + head_cd + "";
            } else {
                ls_query = "SELECT COMP_CD, TRAN_CD, SR_CD, HEAD_CD, HEAD_NM, ED_FLAG, AMT, ENTERED_DATE, ENTERED_BY, ENTERED_IP, LAST_ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP FROM salary_head_dtl COMP_CD ='" + ls_comp_cd + "' AND TRAN_CD = " + head_cd + " AND ENTERED_BY = '" + ls_username + "'";
            }
           
            Statement stmt = null;
            stmt = con.createStatement();
            rs = stmt.executeQuery(ls_query);

            while (rs.next()) {
                jObject = new JSONObject();
                jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                jObject.put("TRAN_CD", NVL.StringNvl(rs.getString("TRAN_CD")));
                jObject.put("SR_CD", NVL.StringNvl(rs.getString("SR_CD")));
                jObject.put("HEAD_CD", NVL.StringNvl(rs.getString("HEAD_CD")));
                jObject.put("HEAD_NM", NVL.StringNvl(rs.getString("HEAD_NM")));
                jObject.put("ED_FLAG", NVL.StringNvl(rs.getString("ED_FLAG")));
                jObject.put("AMT", NVL.StringNvl(rs.getString("AMT")));
                jArray.put(jObject);
            }
            if (jArray.length() <= 0) {
                jObject = new JSONObject();
                jObject.put("STATUS_CD", "99");
                jObject.put("MESSAGE", "Salary Head Sub Details Not Found");
                jArray.put(jObject);
            }
        } catch (Exception e) {
            Utility.PrintMessage("Error in Get User List : " + e);
            JSONObject jErr = new JSONObject();
            jErr.put("STATUS_CD", "99");
            jErr.put("ERROR", e.getMessage());
            jErr.put("MESSAGE", "Salary Head Sub Details Not Found");
            jArray.put(jObject);
        }

        return jArray;
    }

    public static String deleteSalaryHead(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOutPut = new JSONObject();

            String HEAD_ID = jin.getString("TRAN_CD");
            String comp_cd = jin.getString("COMP_CD");

            String ls_del_flag = jin.getString("DELETE_FLAG");

            if (ls_del_flag.equals("Y")) {
                String extingEmp = "UPDATE salary_head_mst SET IS_DELETE = 'Y' WHERE TRAN_CD = " + HEAD_ID + " AND IS_DELETE = 'N' AND COMP_CD = '" + comp_cd + "'";
                stmt = con.createStatement();

                int row = stmt.executeUpdate(extingEmp);

                if (row > 0) {
                    con.commit();
                    jOutPut.put("STATUS_CD", "0");
                    jOutPut.put("MESSAGE", "Salary Head " + HEAD_ID + " Sucessfully Deleted.");
                } else {
                    con.rollback();
                    jOutPut.put("STATUS_CD", "99");
                    jOutPut.put("MESSAGE", "Salary Head " + HEAD_ID + " Already Deleted.");
                }


            } else {
                jOutPut.put("STATUS_CD", "99");
                jOutPut.put("MESSAGE", "Delete Not Allow.");
            }
            response = jOutPut.toString();

        } catch (Exception e) {
            Utility.PrintMessage("Error in Delete Salary Head : " + e);
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
    
    public static int checkHead(Connection con,String ls_comp_cd,int Tran_cd) throws SQLException {
    	
    	Statement stmt = null;
    	ResultSet rs = null;
    	String ls_sql = "SELECT COUNT(*) AS ID FROM SALARY_HEAD_MST WHERE COMP_CD = '"+ls_comp_cd+"' AND TRAN_CD = "+Tran_cd;
    	int cnt = 0;
    	
    	try {
    		stmt = con.createStatement();
        	rs =stmt.executeQuery(ls_sql);
        	while (rs.next()) {
        		cnt = rs.getInt("ID");
        	}        	
        	
    	} catch(Exception e) {
    		cnt = 99;
    	}
    	
    	return cnt;
    }
    
   
    public static String CreateSalaryHead(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        int ExistingCnt = 0;
        try {

            JSONObject jin = new JSONObject(ls_request);           
            
            String CREATE_FLAG = jin.getString("CREATE_FLAG");
            String REQUEST_IP = jin.getString("REQUEST_IP");
            String USERNAME = jin.getString("USERNAME");
            
            JSONObject jReuqest = new JSONObject();
            jReuqest = jin.getJSONObject("REQUEST_DATA");

            String COMP_CD = NVL.StringNvl(jReuqest.getString("COMP_CD"));	                        
            int TRAN_CD =0;
            if (jReuqest.getString("TRAN_CD") == null || jReuqest.getString("TRAN_CD").equals("")) {
            	TRAN_CD = 0;
            } else {
            	TRAN_CD = jReuqest.getInt("TRAN_CD");
            }		   
            
            int cnt = checkHead(con,COMP_CD,TRAN_CD);
           
            if(cnt > 0 ) {
            	JSONObject Headcnt = new JSONObject();
            	Headcnt.put("STATUS_CD", "99");
            	Headcnt.put("MESSAGE", "Salary Head " + TRAN_CD + " Already Exists.");
                response = Headcnt.toString();
                return response;
            }
            
            String TEMP_NAME = NVL.StringNvl(jReuqest.getString("TEMP_NAME"));
            Date APP_FROM_DT = null;
            if (jReuqest.getString("APP_FROM_DT") == null || jReuqest.getString("APP_FROM_DT").equals("")) {
            	APP_FROM_DT = null;
            } else {
            	APP_FROM_DT = Date.valueOf(jReuqest.getString("APP_FROM_DT")); 
                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
            }	  	            
            Date APP_TO_DT = null;
            if (jReuqest.getString("APP_TO_DT") == null || jReuqest.getString("APP_TO_DT").equals("")) {
            	APP_TO_DT = null;
            } else {
            	APP_TO_DT = Date.valueOf(jReuqest.getString("APP_TO_DT")); 
                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
            }                             
            String ENTERED_BY = jReuqest.getString("ENTERED_BY");
            String ENTERED_IP = jReuqest.getString("ENTERED_IP");
            Date ENTERED_DATE = null;
            if (jReuqest.getString("ENTERED_DATE") == null || jReuqest.getString("ENTERED_DATE").equals("")) {
                ENTERED_DATE = null;
            } else {
                ENTERED_DATE = Date.valueOf(jReuqest.getString("ENTERED_DATE"));  
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
            }
            String LAST_ENTERED_BY = jReuqest.getString("LAST_ENTERED_BY");
            String LAST_ENTERED_IP = jReuqest.getString("LAST_ENTERED_IP");
            Date LAST_ENTERED_DATE = null;
            if (jReuqest.getString("LAST_ENTERED_DATE") == null || jReuqest.getString("LAST_ENTERED_DATE").equals("")) {
            	LAST_ENTERED_DATE = null;
            } else {
            	LAST_ENTERED_DATE = Date.valueOf(jReuqest.getString("LAST_ENTERED_DATE"));  
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
            }
            
            JSONArray SalaryHeadSubDelete = new JSONArray(jReuqest.getString("DELETE_SALARY_HEAD"));
            String i = null;
            try {
                if (SalaryHeadSubDelete.length() > 0) {
                    i = deleteSalaryHeadSub(con, SalaryHeadSubDelete, COMP_CD);
                } else { i = "N";}
                
                if (!i.equals("Y") && !i.equals("N")) {
                	return i;
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Delete Salary Head Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
    
            JSONArray SalaryHeadSubInsert = new JSONArray(jReuqest.getString("INSERT_SALARY_HEAD"));
            String j = null;
            try {
                if (SalaryHeadSubInsert.length() > 0) {
                    j = insertSalaryHeadSub(con, SalaryHeadSubInsert);
                } else { j = "N";}                
                if (!j.equals("Y") && !j.equals("N")) {
                	return j;
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Insert Salary Head Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
           
            JSONArray SalaryHeadSubUpdate = new JSONArray(jReuqest.getString("UPDATE_SALARY_HEAD"));
            String k = null;
            try {
                if (SalaryHeadSubUpdate.length() > 0) {
                    k = updateSalaryHeadSub(con, SalaryHeadSubUpdate);
                } else { k = "N";}
                if (!k.equals("Y") && !k.equals("N")) {
                	return k;
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Update Salary Head Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
            
            JSONObject jOut = new JSONObject();
            String sql = "INSERT INTO salary_head_mst (COMP_CD, TRAN_CD, TEMP_NAME, APP_FROM_DT, APP_TO_DT, ENTERED_DATE, ENTERED_BY, ENTERED_IP, LAST_ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);                         
            preparedStatement.setString(1,COMP_CD);
            preparedStatement.setInt(2,TRAN_CD);
            preparedStatement.setString(3,TEMP_NAME);
            preparedStatement.setDate(4,APP_FROM_DT);
            preparedStatement.setDate(5,APP_TO_DT);
            preparedStatement.setDate(6,ENTERED_DATE);
            preparedStatement.setString(7,ENTERED_BY);
            preparedStatement.setString(8,ENTERED_IP);
            preparedStatement.setDate(9,LAST_ENTERED_DATE);
            preparedStatement.setString(10,LAST_ENTERED_BY);
            preparedStatement.setString(11,LAST_ENTERED_IP);

            int row = preparedStatement.executeUpdate();

            if (row == 0) {
                con.rollback();
            } else {
                con.commit();
            }

            JSONObject jOutPut = new JSONObject();

            jOutPut.put("STATUS_CD", "0");
            jOutPut.put("MESSAGE", "Salary Head " + TRAN_CD + " Sucessfully Created.");
            response = jOutPut.toString();
            } catch (Exception e) {
            Utility.PrintMessage("Error in Create Salary Head : " + e);
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
    
    public static String UpdateSalaryHead(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        int ExistingCnt = 0;
        try {

            JSONObject jin = new JSONObject(ls_request);           
            
            String CREATE_FLAG = NVL.StringNvl(jin.getString("UPDATE_FLAG"));
            String REQUEST_IP = NVL.StringNvl(jin.getString("REQUEST_IP"));
            String USERNAME = NVL.StringNvl(jin.getString("USERNAME"));
            String COMP_CD = NVL.StringNvl(jin.getString("COMP_CD"));	
            int TRAN_CD = jin.getInt("TRAN_CD");
            
            JSONObject jReuqest = new JSONObject();
            jReuqest = jin.getJSONObject("REQUEST_DATA");                                   
            int cnt = checkHead(con,COMP_CD,TRAN_CD);
            
            if(cnt < 0 ) {
            	JSONObject Headcnt = new JSONObject();
            	Headcnt.put("STATUS_CD", "99");
            	Headcnt.put("MESSAGE", "Salary Head " + TRAN_CD + " Not Found For Update");
                response = Headcnt.toString();
                return response;
            }

            String TEMP_NAME = NVL.StringNvl(jReuqest.getString("TEMP_NAME"));
            Date APP_FROM_DT = null;
            if (jReuqest.getString("APP_FROM_DT") == null || jReuqest.getString("APP_FROM_DT").equals("")) {
            	APP_FROM_DT = null;
            } else {
            	APP_FROM_DT = Date.valueOf(jReuqest.getString("APP_FROM_DT")); 
                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
            }	  	            
            Date APP_TO_DT = null;
            if (jReuqest.getString("APP_TO_DT") == null || jReuqest.getString("APP_TO_DT").equals("")) {
            	APP_TO_DT = null;
            } else {
            	APP_TO_DT = Date.valueOf(jReuqest.getString("APP_TO_DT")); 
                	//	(Date) new SimpleDateFormat("DD-MM-YYYY").parse(jReuqest.getString("ENTRY_DT"));
            }                             
            
            String LAST_ENTERED_BY = jReuqest.getString("LAST_ENTERED_BY");
            String LAST_ENTERED_IP = jReuqest.getString("LAST_ENTERED_IP");
            Date LAST_ENTERED_DATE = null;
            if (jReuqest.getString("LAST_ENTERED_DATE") == null || jReuqest.getString("LAST_ENTERED_DATE").equals("")) {
            	LAST_ENTERED_DATE = null;
            } else {
            	LAST_ENTERED_DATE = Date.valueOf(jReuqest.getString("LAST_ENTERED_DATE"));  
                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
            }
            
            JSONArray SalaryHeadSubDelete = new JSONArray(jReuqest.getString("DELETE_SALARY_HEAD"));
            String i = null;
            try {
                if (SalaryHeadSubDelete.length() > 0) {
                    i = deleteSalaryHeadSub(con, SalaryHeadSubDelete, COMP_CD);
                } else { i = "N";}
                
                if (!i.equals("Y") && !i.equals("N")) {
                	return i;
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Delete Salary Head Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
    
            JSONArray SalaryHeadSubInsert = new JSONArray(jReuqest.getString("INSERT_SALARY_HEAD"));           
            String j = null;
            try {
                if (SalaryHeadSubInsert.length() > 0) {
                    j = insertSalaryHeadSub(con, SalaryHeadSubInsert);
                } else { j = "N";}                
                if (!j.equals("Y") && !j.equals("N")) {
                	return j;
                }
            } catch (Exception e) {
                Utility.PrintMessage("Error in Insert Salary Head Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
           
            JSONArray SalaryHeadSubUpdate = new JSONArray(jReuqest.getString("UPDATE_SALARY_HEAD"));
            String k = null;          
            try {
                if (SalaryHeadSubUpdate.length() > 0) {
                    k = updateSalaryHeadSub(con, SalaryHeadSubUpdate);
                } else { k = "N";}               
                if (!k.equals("Y") && !k.equals("N")) {
                	return k;
                }
            } catch (Exception e) {
            	e.printStackTrace();
                Utility.PrintMessage("Error in Update Salary Head Details : " + e);
                return response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
            }
           
            
            
            JSONObject jOut = new JSONObject();
            String sql = "UPDATE salary_head_mst SET TEMP_NAME = ?, APP_FROM_DT = ?, APP_TO_DT = ?, LAST_ENTERED_DATE = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ? WHERE COMP_CD = ? AND TRAN_CD = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);                         
            
            preparedStatement.setString(1,TEMP_NAME);
            preparedStatement.setDate(2,APP_FROM_DT);
            preparedStatement.setDate(3,APP_TO_DT);            
            preparedStatement.setDate(4,LAST_ENTERED_DATE);
            preparedStatement.setString(5,LAST_ENTERED_BY);
            preparedStatement.setString(6,LAST_ENTERED_IP);

            preparedStatement.setString(7,COMP_CD);
            preparedStatement.setInt(8,TRAN_CD);
                        
            int row = preparedStatement.executeUpdate();

            if (row == 0) {
                con.rollback();
            } else {
                con.commit();
            }

            JSONObject jOutPut = new JSONObject();

            jOutPut.put("STATUS_CD", "0");
            jOutPut.put("MESSAGE", "Salary Head " + TRAN_CD + " Sucessfully Updated.");
            response = jOutPut.toString();
            } catch (Exception e) {
            Utility.PrintMessage("Error in Update Salary Head : " + e);
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
    
	private static String deleteSalaryHeadSub(Connection con, JSONArray salaryHeadSubDelete, String cOMP_CD) throws JSONException {
		int row = 0;
		String response = null;
		Statement stmt = null;
		try {
			for (int i = 0; i < salaryHeadSubDelete.length(); i++) {
	            JSONObject insertHeadDetail = new JSONObject();
	            insertHeadDetail = (JSONObject) salaryHeadSubDelete.get(i);
	            
	            int TRAN_CD = insertHeadDetail.getInt("TRAN_CD");
	            int SR_CD = insertHeadDetail.getInt("SR_CD");
	            
	            String sql = null;            
	            sql = "UPDATE salary_head_dtl SET IS_DELETE = 'Y' WHERE COMP_CD = '" + cOMP_CD + "' AND TRAN_CD =" + TRAN_CD +" AND SR_CD = "+SR_CD;
	            stmt = con.createStatement();
	            row = stmt.executeUpdate(sql);
	            row = row + 1;                  
			}
			if (row > 0 ) {
				response = "Y";
			} 
		} catch(Exception e) {
			response = "{\r\n" + 
					"  \"STATUS_CD\":\"99\",\r\n" + 
					"  \"MESSAGE\":\" Delete Salary Head Sub Detail :"+e.getLocalizedMessage()+"\"\r\n" + 
					"}";
		}
		return response;
	}
	
	private static String insertSalaryHeadSub(Connection con, JSONArray salaryHeadSubDelete) throws JSONException {
		int row = 0;
		String response = null;
		Statement stmt = null;
		try {
			for (int i = 0; i < salaryHeadSubDelete.length(); i++) {
	            JSONObject insertHeadDetail = new JSONObject();
	            insertHeadDetail = (JSONObject) salaryHeadSubDelete.get(i);
	            
	            String COMP_CD = NVL.StringNvl(insertHeadDetail.getString("COMP_CD"));
	            int TRAN_CD = insertHeadDetail.getInt("TRAN_CD");
	            if (insertHeadDetail.getString("TRAN_CD") == null || insertHeadDetail.getString("TRAN_CD").equals("")) {
	            	TRAN_CD = 0;
	            } else {
	            	TRAN_CD = insertHeadDetail.getInt("TRAN_CD");
	            }	 
	            
	            int SR_CD = insertHeadDetail.getInt("SR_CD");
	            if (insertHeadDetail.getString("SR_CD") == null || insertHeadDetail.getString("SR_CD").equals("")) {
	            	SR_CD = 0;
	            } else {
	            	SR_CD = insertHeadDetail.getInt("SR_CD");
	            }
	            String HEAD_CD = NVL.StringNvl(insertHeadDetail.getString("HEAD_CD"));
	            String HEAD_NM = NVL.StringNvl(insertHeadDetail.getString("HEAD_NM"));
	            String ED_FLAG = NVL.StringNvl(insertHeadDetail.getString("ED_FLAG"));
	            double AMT = 0.00;
	            if (insertHeadDetail.getString("AMT") == null || insertHeadDetail.getString("AMT").equals("")) {
	            	AMT = 0;
	            } else {
	            	AMT = insertHeadDetail.getDouble("AMT");	            			
	            }
	            
	            String ENTERED_BY = insertHeadDetail.getString("ENTERED_BY");
	            String ENTERED_IP = insertHeadDetail.getString("ENTERED_IP");
	            Date ENTERED_DATE = null;
	            if (insertHeadDetail.getString("ENTERED_DATE") == null || insertHeadDetail.getString("ENTERED_DATE").equals("")) {
	                ENTERED_DATE = null;
	            } else {
	                ENTERED_DATE = Date.valueOf(insertHeadDetail.getString("ENTERED_DATE"));  
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
	            }
	            String LAST_ENTERED_BY = insertHeadDetail.getString("LAST_ENTERED_BY");
	            String LAST_ENTERED_IP = insertHeadDetail.getString("LAST_ENTERED_IP");
	            Date LAST_ENTERED_DATE = null;
	            if (insertHeadDetail.getString("LAST_ENTERED_DATE") == null || insertHeadDetail.getString("LAST_ENTERED_DATE").equals("")) {
	            	LAST_ENTERED_DATE = null;
	            } else {
	            	LAST_ENTERED_DATE = Date.valueOf(insertHeadDetail.getString("LAST_ENTERED_DATE"));  
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
	            }
	            
	            ResultSet rs_check = null;

	            String  ls_sql= "SELECT COUNT(*) AS CNT FROM SALARY_HEAD_DTL WHERE COMP_CD ='" + COMP_CD + "' AND TRAN_CD = "+TRAN_CD+" AND SR_CD = "+SR_CD;
	            
	            stmt = con.createStatement();
	            rs_check = stmt.executeQuery(ls_sql);
	                  	          
	            int cnt = 0;
	            while (rs_check.next()) {
	                cnt = rs_check.getInt("CNT");
	                
	            }
	            		            
	            if (cnt <= 0) {
	            	String sql = null;            
		            sql = "INSERT INTO salary_head_dtl (COMP_CD, TRAN_CD, SR_CD, HEAD_CD, HEAD_NM, ED_FLAG, AMT, ENTERED_DATE, ENTERED_BY, ENTERED_IP, LAST_ENTERED_DATE, LAST_ENTERED_BY, LAST_ENTERED_IP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		            PreparedStatement preparedStatement = null;
		            try {
		            	preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, COMP_CD);
                        preparedStatement.setInt(2, TRAN_CD);
                        preparedStatement.setInt(3, SR_CD);
                        preparedStatement.setString(4, HEAD_CD);
                        preparedStatement.setString(5, HEAD_NM);
                        preparedStatement.setString(6, ED_FLAG);
                        preparedStatement.setDouble(7, AMT);                        
                        preparedStatement.setDate(8,ENTERED_DATE);
                        preparedStatement.setString(9,ENTERED_BY);
                        preparedStatement.setString(10,ENTERED_IP);
                        preparedStatement.setDate(11,LAST_ENTERED_DATE);
                        preparedStatement.setString(12,LAST_ENTERED_BY);
                        preparedStatement.setString(13,LAST_ENTERED_IP);

                        row = preparedStatement.executeUpdate();
                        if (row > 0 ) {
                        	response = "Y";
                        }
		            }catch(Exception e) {
		           
		            	response = "{\r\n" + 
		    					"  \"STATUS_CD\":\"99\",\r\n" + 
		    					"  \"MESSAGE\":\" Insert Salary Head Sub Detail :"+e.getMessage()+"\"\r\n" + 
		    					"}";
		            }
	            } else {
	            	response = "{\r\n" + 
	    					"  \"STATUS_CD\":\"99\",\r\n" + 
	    					"  \"MESSAGE\":\" Salary Head Sub Detail Id : "+SR_CD+" Already Inserted.\"\r\n" + 
	    					"}";
	            }           
			}
			if (row > 0 ) {
				response = "Y";
			} 
		} catch(Exception e) {
			e.printStackTrace();
			response = "{\r\n" + 
					"  \"STATUS_CD\":\"99\",\r\n" + 
					"  \"MESSAGE\":\" Insert Salary Head Sub Detail :"+e.getMessage()+"\"\r\n" + 
					"}";
		}
		return response;
	}
	
	private static String updateSalaryHeadSub(Connection con, JSONArray salaryHeadSubDelete) throws JSONException {
		int row = 0;
		String response = null;
		Statement stmt = null;
		try {
			for (int i = 0; i < salaryHeadSubDelete.length(); i++) {
	            JSONObject updateHeadDetail = new JSONObject();
	            updateHeadDetail = (JSONObject) salaryHeadSubDelete.get(i);
	            
	            String COMP_CD = NVL.StringNvl(updateHeadDetail.getString("COMP_CD"));
	            int TRAN_CD = updateHeadDetail.getInt("TRAN_CD");
	            if (updateHeadDetail.getString("TRAN_CD") == null || updateHeadDetail.getString("TRAN_CD").equals("")) {
	            	TRAN_CD = 0;
	            } else {
	            	TRAN_CD = updateHeadDetail.getInt("TRAN_CD");
	            }	 
	            
	            int SR_CD = updateHeadDetail.getInt("SR_CD");
	            if (updateHeadDetail.getString("SR_CD") == null || updateHeadDetail.getString("SR_CD").equals("")) {
	            	SR_CD = 0;
	            } else {
	            	SR_CD = updateHeadDetail.getInt("SR_CD");
	            }
	            String HEAD_CD = NVL.StringNvl(updateHeadDetail.getString("HEAD_CD"));
	            String HEAD_NM = NVL.StringNvl(updateHeadDetail.getString("HEAD_NM"));
	            String ED_FLAG = NVL.StringNvl(updateHeadDetail.getString("ED_FLAG"));
	            double AMT = 0.00;
	            if (updateHeadDetail.getString("AMT") == null || updateHeadDetail.getString("AMT").equals("")) {
	            	AMT = 0;
	            } else {
	            	AMT = updateHeadDetail.getDouble("AMT");	            			
	            }
	            String ENTERED_BY = updateHeadDetail.getString("ENTERED_BY");
	            String ENTERED_IP = updateHeadDetail.getString("ENTERED_IP");
	            Date ENTERED_DATE = null;
	            if (updateHeadDetail.getString("ENTERED_DATE") == null || updateHeadDetail.getString("ENTERED_DATE").equals("")) {
	                ENTERED_DATE = null;
	            } else {
	                ENTERED_DATE = Date.valueOf(updateHeadDetail.getString("ENTERED_DATE"));  
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
	            }
	            String LAST_ENTERED_BY = updateHeadDetail.getString("LAST_ENTERED_BY");
	            String LAST_ENTERED_IP = updateHeadDetail.getString("LAST_ENTERED_IP");
	            Date LAST_ENTERED_DATE = null;
	            if (updateHeadDetail.getString("LAST_ENTERED_DATE") == null || updateHeadDetail.getString("LAST_ENTERED_DATE").equals("")) {
	            	LAST_ENTERED_DATE = null;
	            } else {
	            	LAST_ENTERED_DATE = Date.valueOf(updateHeadDetail.getString("LAST_ENTERED_DATE"));  
	                		//(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("LAST_MODIFIED_DT"));
	            }
	            int cnt = 0; 	           
	            ResultSet rs_temp = null;
	            String ls_query = "SELECT COUNT(*) AS ID FROM SALARY_HEAD_DTL WHERE COMP_CD ='" + COMP_CD + "' AND TRAN_CD = "+TRAN_CD+" AND SR_CD = "+SR_CD+" AND IS_DELETE = 'N'";
	            
	            stmt = con.createStatement();
	            rs_temp = stmt.executeQuery(ls_query);
	          
	            while (rs_temp.next()) {
	                cnt = rs_temp.getInt("ID");	                
	            }
	            cnt = 1;	      
	            if (cnt > 0) {
	            	String sql = null;            
		            sql = "UPDATE salary_head_dtl SET HEAD_CD = ?, HEAD_NM = ?, ED_FLAG = ?, AMT = ?, LAST_ENTERED_DATE = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ? WHERE COMP_CD = ? AND TRAN_CD = ? AND SR_CD = ?";
		            PreparedStatement preparedStatement = null;
		            try {
		            	preparedStatement = con.prepareStatement(sql);

                        preparedStatement.setString(1, HEAD_CD);
                        preparedStatement.setString(2, HEAD_NM);
                        preparedStatement.setString(3, ED_FLAG);
                        preparedStatement.setDouble(4, AMT);                                                
                        preparedStatement.setDate(5,LAST_ENTERED_DATE);
                        preparedStatement.setString(6,LAST_ENTERED_BY);
                        preparedStatement.setString(7,LAST_ENTERED_IP);
                        
                        preparedStatement.setString(8, COMP_CD);
                        preparedStatement.setInt(9, TRAN_CD);
                        preparedStatement.setInt(10, SR_CD);
                        
                        row = preparedStatement.executeUpdate();
                        if (row > 0 ) {
                        	response = "Y";
                        }
		            }catch(Exception e) {
		            	response = "{\r\n" + 
		    					"  \"STATUS_CD\":\"99\",\r\n" + 
		    					"  \"MESSAGE\":\" Update Salary Head Sub Detail :"+e.getLocalizedMessage()+"\"\r\n" + 
		    					"}";
		            }
	            } else {
	            	response = "{\r\n" + 
	    					"  \"STATUS_CD\":\"99\",\r\n" + 
	    					"  \"MESSAGE\":\" Salary Head Sub Detail Id : "+SR_CD+" Not Found.\"\r\n" + 
	    					"}";
	            }           
			}
			if (row > 0 ) {
				response = "Y";
			} 
		} catch(Exception e) {
			response = "{\r\n" + 
					"  \"STATUS_CD\":\"99\",\r\n" + 
					"  \"MESSAGE\":\" Update Salary Head Sub Detail :"+e.getLocalizedMessage()+"\"\r\n" + 
					"}";
		}
		return response;
	}	
}