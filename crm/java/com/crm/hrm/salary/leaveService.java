package com.crm.hrm.salary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.common.createJsonData;
import com.crm.common.createJsonList;
import com.crm.common.getMaxIdentityCd;
import com.crm.utility.Utility;

public class leaveService {
	
	public static String getLeaveDtl(Connection con,String request,String flag) throws JSONException {		
		String where				= null;
		JSONObject inputJson 		= new JSONObject(request);
		JSONArray response 			= new JSONArray();
		JSONObject finalResponse 	= new JSONObject();		
		try {
			if (flag.equalsIgnoreCase("L")) {
				String viewFlag 	= inputJson.getString("VIEW_FLAG");
				String compCD 		= inputJson.getString("COMP_CD");
				String enterdBy 	= inputJson.getString("USERNAME");
				if (viewFlag.equalsIgnoreCase("G")) {
					where = "WHERE COMP_CD = '"+compCD+"'";
				} else {
					where = "WHERE COMP_CD = '"+compCD+"' ENTERED_BY = '"+enterdBy+"'";
				}
			} else {
				String viewFlag 	= inputJson.getString("VIEW_FLAG");
				String compCD 		= inputJson.getString("COMP_CD");
				String enterdBy 	= inputJson.getString("USERNAME");
				String tranCD 		= inputJson.getString("TRAN_CD");
				String empID		= inputJson.getString("EMP_ID");
				if (viewFlag.equalsIgnoreCase("G")) {
					where = "WHERE COMP_CD = '"+compCD+"' AND TRAN_CD = "+tranCD+" AND EMP_ID = "+empID+"";
				} else {
					where = "WHERE COMP_CD = '"+compCD+"' AND ENTERED_BY = '"+enterdBy+"' AND TRAN_CD = "+tranCD+" AND EMP_ID = "+empID+"";
				}
			}						
			response = createJsonList.createJsonList(con, "crm","emp_leave_mst", where);
			finalResponse.put("STATUS_CD", "0");
			finalResponse.put("RESPONSE",response);
		} catch (Exception e) {
			finalResponse.put("STATUS","99");
			finalResponse.put("MESSAGE", "Error while createJsonList of table : emp_leave_mst");
			finalResponse.put("ERROR",e.getMessage());
		}	
		String res = finalResponse.toString(); 
		return res;
	}
	
	public static String getMaxCd(Connection con,String request) throws JSONException {
		
		JSONObject input = new JSONObject(request);
		JSONObject output = new JSONObject();
		JSONObject finalResponse = new JSONObject();
		JSONArray finalJson = new JSONArray();
		String userName = input.getString("USERNAME");
		String compCD = input.getString("COMP_CD");
		String ViewFlag = input.getString("VIEW_FLAG");
		String where = "WHERE COMP_CD = "+compCD+"";
		try {
			output = getMaxIdentityCd.getMaxidentityCode(con, "TRAN_CD", "emp_leave_mst", where, userName);
			finalResponse.put("STATUS_CD", "0");
			finalJson.put(output);
			finalResponse.put("RESPONSE",finalJson);
		} catch(Exception e) {
			finalResponse.put("STATUS","99");
			finalResponse.put("MESSAGE", "Error while get Max Tran CD of table : emp_leave_mst");
			finalResponse.put("ERROR",e.getMessage());
		}	
		
		return finalResponse.toString();
	}
	
	public static String createLeave(Connection con,String request) throws JSONException {		
		JSONObject input = new JSONObject(request);
		JSONObject requestData = new JSONObject();
		requestData = input.getJSONObject("REQUEST_DATA");
		JSONObject response = new JSONObject();
		String userName = input.getString("USERNAME");
		String createFlag = input.getString("CREATE_FLAG");
		try {
			if (createFlag.equalsIgnoreCase("Y")) {
				response = createJsonData.createData(con, requestData.toString(), "emp_leave_mst");
			} else {
				response.put("STATUS_CD", "99");
				response.put("MESSAGE","User :"+userName+" have not Rights to insert leave entry.");				
			}			
		} catch(Exception e) {
			response.put("STATUS","99");
			response.put("MESSAGE", "Error while create Leave Entey.");
			response.put("ERROR",e.getMessage());
		}		
		return response.toString();
	}
	
	public static String deleteLeaveID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        try {
            JSONObject jin = new JSONObject(ls_request);                                
            JSONObject jOutPut = new JSONObject();
            
            String TRAN_CD = jin.getString("TRAN_CD");  
            String comp_cd= jin.getString("COMP_CD");
           	String empId = jin.getString("EMP_ID");           
            String ls_del_flag = jin.getString("DELETE_FLAG");
            
            if(ls_del_flag.equals("Y")) {
            	 String extingEmp = "UPDATE emp_leave_mst SET IS_DELETE = 'Y' WHERE TRAN_CD = " +TRAN_CD+" AND EMP_ID = "+empId+" AND IS_DELETE = 'N' AND COMP_CD = '"+comp_cd+"'";	            	
                 stmt = con.createStatement();
                 
                 int row = stmt.executeUpdate(extingEmp);
                 
                 if (row > 0) {
                 	con.commit();
                 	 jOutPut.put("STATUS_CD", "0");	
                 	jOutPut.put("MESSAGE", "Leave Sucessfully Deleted.");
                 } else {
                 	con.rollback();
                 	 jOutPut.put("STATUS_CD", "99");	
                 	jOutPut.put("MESSAGE", "Leave Already Deleted.");
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
	
	public static String getEmpLeaveDtl(Connection con,String request) throws JSONException {		
		String where				= null;
		JSONObject inputJson 		= new JSONObject(request);
		JSONObject response 			= new JSONObject();
		JSONObject finalResponse 	= new JSONObject();		
		try {			
				String viewFlag 	= inputJson.getString("VIEW_FLAG");
				String compCD 		= inputJson.getString("COMP_CD");
				String enterdBy 	= inputJson.getString("USERNAME");				
				String empID		= inputJson.getString("EMP_ID");
					
				finalResponse = createJsonList.createJsonColumnWiseList(con,"crm","emp_mst","WHERE COMP_CD = '"+compCD+"' AND EMP_ID = "+empID+" ","EMP_ID,FIRST_NM,MIDDEL_NM,LAST_NM,DEPARTMENT_ID,DESIGNATION_ID,BRANCH_CD");
//			finalResponse.put("STATUS_CD", "0");
//			finalResponse.put("RESPONSE",response);
		} catch (Exception e) {
			finalResponse.put("STATUS","99");
			finalResponse.put("MESSAGE", "Error while createJsonList of table : emp_leave_mst");
			finalResponse.put("ERROR",e.getMessage());
		}	
		String res = finalResponse.toString(); 
		return res;
	}
}
