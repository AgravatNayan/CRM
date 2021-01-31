	package com.crm.hrms.company;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.serial.SerialBlob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.NVL;
import com.crm.utility.Utility;

public class companyService {
    public static String GetMaxBoradMemtID(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jOut = new JSONObject();
            String sql = "SELECT MAX(ID) AS ID FROM comp_dtl";
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
            jobj.put("MEM_ID", maxID);
            jresponse.put(jobj);
            jOut.put("STATUS_CD", "0");
            jOut.put("RESPONSE", jresponse);
            response = jOut.toString();
        } catch (Exception e) {
            Utility.PrintMessage("Error in GetMax Borad Member Id : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time (Error :"+e.getMessage().replace("\"", "")+").\"}";
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


    public static String getCompanyDetails(Connection con, String requestData) throws JSONException,
        ClassNotFoundException,
        SQLException {

            int ll_id = 0;
            String ls_sortname = null;
            String name = null;
            String ls_phcode = null;
            JSONObject jObject = null;
            JSONArray jArray = new JSONArray();
            JSONObject mainObject = new JSONObject();
            ResultSet rs = null;
            JSONObject request = new JSONObject(requestData);
            JSONObject jError = new JSONObject();

            String ls_username = request.getString("USERNAME");
            String ls_req_ip = request.getString("REQUEST_IP");
            String ls_view = request.getString("VIEW_FLAG");
            String ls_comp_cd = request.getString("COMP_CD");
            String response = null;
            try {
                String ls_query = null;
                if (ls_view.equals("G")) {
                    ls_query = "SELECT COMP_CD, COMP_NM, TRADING_NM, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, GSTIN, PAN_NO, TYPE_OF_BUSINESS, COMP_LOGO, INCEPTION_DT, EMAIL_ID, CONTACT_NO, LANDLINE_NO, PERMANENT_ADD, TAX_DEDUC_AC_MO, WEBSITE, START_DT FROM crm.comp_mst";
                } else {
                    ls_query = "SELECT COMP_CD, COMP_NM, TRADING_NM, ADDRESS_1, ADDRESS_2, ADDRESS_3, CONTRAY_CD, STATE_CD, CITY_CD, PIN_CODE, GSTIN, PAN_NO, TYPE_OF_BUSINESS, COMP_LOGO, INCEPTION_DT, EMAIL_ID, CONTACT_NO, LANDLINE_NO, PERMANENT_ADD, TAX_DEDUC_AC_MO, WEBSITE, START_DT FROM crm.comp_mst WHERE ENTERED_BY = '" + ls_username + "'";
                }
                System.out.println(ls_query);
                Statement stmt = null;
                stmt = con.createStatement();
                rs = stmt.executeQuery(ls_query);

                while (rs.next()) {
                    jObject = new JSONObject();

                    jObject.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                    jObject.put("COMP_NM", NVL.StringNvl(rs.getString("COMP_NM")));
                    jObject.put("TRADING_NM", NVL.StringNvl(rs.getString("TRADING_NM")));
                    jObject.put("ADDRESS_1", NVL.StringNvl(rs.getString("ADDRESS_1")));
                    jObject.put("ADDRESS_2", NVL.StringNvl(rs.getString("ADDRESS_2")));
                    jObject.put("ADDRESS_3", NVL.StringNvl(rs.getString("ADDRESS_3")));
                    jObject.put("CONTRAY_CD", NVL.StringNvl(rs.getString("CONTRAY_CD")));
                    jObject.put("STATE_CD", NVL.StringNvl(rs.getString("STATE_CD")));
                    jObject.put("CITY_CD", NVL.StringNvl(rs.getString("CITY_CD")));
                    jObject.put("PIN_CODE", NVL.StringNvl(rs.getString("PIN_CODE")));
                    jObject.put("GSTIN", NVL.StringNvl(rs.getString("GSTIN")));
                    jObject.put("PAN_NO", NVL.StringNvl(rs.getString("PAN_NO")));
                    jObject.put("TYPE_OF_BUSINESS", NVL.StringNvl(rs.getString("TYPE_OF_BUSINESS")));
                    jObject.put("COMP_LOGO", NVL.StringNvl(rs.getString("COMP_LOGO")));
                    jObject.put("INCEPTION_DT", NVL.StringNvl(rs.getString("INCEPTION_DT")));
                    jObject.put("EMAIL_ID", NVL.StringNvl(rs.getString("EMAIL_ID")));
                    jObject.put("CONTACT_NO", NVL.StringNvl(rs.getString("CONTACT_NO")));
                    jObject.put("LANDLINE_NO", NVL.StringNvl(rs.getString("LANDLINE_NO")));
                    jObject.put("PERMANENT_ADD", NVL.StringNvl(rs.getString("PERMANENT_ADD")));
                    jObject.put("TAX_DEDUC_AC_MO", NVL.StringNvl(rs.getString("TAX_DEDUC_AC_MO")));
                    jObject.put("WEBSITE", NVL.StringNvl(rs.getString("WEBSITE")));
                    jObject.put("START_DT", NVL.StringNvl(rs.getString("START_DT")));

                    JSONArray boardMember = getBoardMem(con, ls_comp_cd, ls_username, ls_view);
                    jObject.put("BOARD_MEM", boardMember);

                    jArray.put(jObject);

                }

                mainObject.put("STATUS_CD", "0");
                mainObject.put("RESPONSE", jArray);
            } catch (Exception e) {
                Utility.PrintMessage("Error in Get Company Details: " + e);
                response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time (Error :"+e.getMessage().replace("\"", "")+").\"}";
            }
            return mainObject.toString();
        }

    public static JSONArray getBoardMem(Connection con, String ls_comp_cd, String ls_enterd_by, String ls_view_flag) throws SQLException, JSONException {

        Statement stmt = null;
        ResultSet rs = null;
        JSONObject response = null;
        JSONArray jArray = new JSONArray();
        String sql = null;

        if (ls_view_flag.equals("G")) {
            sql = "SELECT COMP_CD, ID, PARTNE_NM, ROLE, NATURE_OF_ROLE, JOINING_DT, CONTACT, REMARKS FROM crm.comp_dtl WHERE COMP_CD = '" + ls_comp_cd + "' AND IS_DELETE = 'N'";

            try {
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    response = new JSONObject();
                    response.put("COMP_CD", NVL.StringNvl(rs.getString("COMP_CD")));
                    response.put("ID", NVL.StringNvl(rs.getString("ID")));
                    response.put("PARTNE_NM", NVL.StringNvl(rs.getString("PARTNE_NM")));
                    response.put("ROLE", NVL.StringNvl(rs.getString("ROLE")));
                    response.put("NATURE_OF_ROLE", NVL.StringNvl(rs.getString("NATURE_OF_ROLE")));
                    response.put("JOINING_DT", NVL.StringNvl(rs.getString("JOINING_DT")));
                    response.put("CONTACT", NVL.StringNvl(rs.getString("CONTACT")));
                    response.put("REMARKS", NVL.StringNvl(rs.getString("REMARKS")));

                    jArray.put(response);

                }

                if (response == null) {
                    response = new JSONObject();
                    response.put("STATUS_CD", "0");
                    response.put("MESSAGE", "Not Data Found.");
                    jArray.put(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = new JSONObject();
                Utility.PrintMessage("Error in Get Board Member Details: " + e);
                response.put("STATUS_CD", "99");
                response.put("MESSAGE", "Something went to wrong,Please try after some time "+e.getMessage().replace("\"", "")+".");
                jArray.put(response);
            }
        } else {
            response = new JSONObject();
            response.put("STATUS_CD", "99");
            response.put("MESSAGE", "Your Have Not Rights For View Borad Menber.");
            jArray.put(response);
        }
        return jArray;
    }

    public static String updateCompayDetail(Connection con, String ls_request) {
        String response = "";
        Statement stmt = null;
        ResultSet rs = null;
        int ExistingCnt = 0;
        
        String deleteMsg = null;
        String updateMsg = null;
        String insertMeg = null;
        
        try {
            JSONObject jin = new JSONObject(ls_request);
            JSONObject jReuqest = new JSONObject();
            jReuqest = jin.getJSONObject("REQUEST_DATA");
            JSONObject jOutPut = new JSONObject();

            String COMP_CD = jin.getString("COMP_CD");
            String UPDATE_FLAG = jin.getString("UPDATE_FLAG");
            String USERNAME = jin.getString("USERNAME");
            String extingEmp = "SELECT COUNT(*) AS CON_ID FROM COMP_MST WHERE COMP_CD = '" + COMP_CD + "'";
            stmt = con.createStatement();
            ResultSet empResultSet = stmt.executeQuery(extingEmp);

            while (empResultSet.next()) {
                ExistingCnt = empResultSet.getInt("CON_ID");
            }

            if (ExistingCnt == 0) {
                jOutPut.put("STATUS_CD", "99");
                jOutPut.put("MESSAGE", "Company Id " + COMP_CD + " not Exists. Kindly Create First");
                response = jOutPut.toString();
            } else {

                String COMP_NM = NVL.StringNvl(jReuqest.getString("COMP_NM"));
                String TRADING_NM = NVL.StringNvl(jReuqest.getString("TRADING_NM"));
                String ADDRESS_1 = NVL.StringNvl(jReuqest.getString("ADDRESS_1"));
                String ADDRESS_2 = NVL.StringNvl(jReuqest.getString("ADDRESS_2"));
                String ADDRESS_3 = NVL.StringNvl(jReuqest.getString("ADDRESS_3"));
                int CONTRAY_CD = 0;
                if (jReuqest.getString("CONTRAY_CD") == null || jReuqest.getString("CONTRAY_CD").equals("")) {
                    CONTRAY_CD = 0;
                } else {
                    CONTRAY_CD = jReuqest.getInt("CONTRAY_CD");
                }
                int STATE_CD = 0;
                if (jReuqest.getString("STATE_CD") == null || jReuqest.getString("STATE_CD").equals("")) {
                    STATE_CD = 0;
                } else {
                    STATE_CD = jReuqest.getInt("STATE_CD");
                }
                int CITY_CD = 0;
                if (jReuqest.getString("CITY_CD") == null || jReuqest.getString("CITY_CD").equals("")) {
                    CITY_CD = 0;
                } else {
                    CITY_CD = jReuqest.getInt("CITY_CD");
                }
                String PIN_CODE = NVL.StringNvl(jReuqest.getString("PIN_CODE"));
                String GSTIN = NVL.StringNvl(jReuqest.getString("GSTIN"));
                String PAN_NO = NVL.StringNvl(jReuqest.getString("PAN_NO"));
                String TYPE_OF_BUSINESS = NVL.StringNvl(jReuqest.getString("TYPE_OF_BUSINESS"));
                String ls_comp_str = NVL.StringNvl(jReuqest.getString("COMP_LOGO"));
                byte[] COMP_LOGO_BYTE = ls_comp_str.getBytes();
                Blob COMP_LOGO = new SerialBlob(COMP_LOGO_BYTE);
                Date INCEPTION_DT = null;
                if (jReuqest.getString("INCEPTION_DT") == null || jReuqest.getString("INCEPTION_DT").equals("")) {
                    INCEPTION_DT = null;
                } else {
                    INCEPTION_DT = Date.valueOf(jReuqest.getString("INCEPTION_DT"));
                    //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
                }
                String EMAIL_ID = NVL.StringNvl(jReuqest.getString("EMAIL_ID"));
                int CONTACT_NO = 0;
                if (jReuqest.getString("CONTACT_NO") == null || jReuqest.getString("CONTACT_NO").equals("")) {
                    CONTACT_NO = 0;
                } else {
                    CONTACT_NO = jReuqest.getInt("CONTACT_NO");
                }
                int LANDLINE_NO = 0;
                if (jReuqest.getString("LANDLINE_NO") == null || jReuqest.getString("LANDLINE_NO").equals("")) {
                    LANDLINE_NO = 0;
                } else {
                    LANDLINE_NO = jReuqest.getInt("LANDLINE_NO");
                }
                String PERMANENT_ADD = NVL.StringNvl(jReuqest.getString("PERMANENT_ADD"));
                String TAX_DEDUC_AC_MO = NVL.StringNvl(jReuqest.getString("TAX_DEDUC_AC_MO"));
                String WEBSITE = NVL.StringNvl(jReuqest.getString("WEBSITE"));
                String LAST_ENTERED_BY = NVL.StringNvl(jReuqest.getString("LAST_ENTERED_BY"));
                String LAST_ENTERED_IP = NVL.StringNvl(jReuqest.getString("LAST_ENTERED_IP"));
                Date LAST_ENTERED_DATE = null;
                if (jReuqest.getString("LAST_ENTERED_DATE") == null || jReuqest.getString("LAST_ENTERED_DATE").equals("")) {
                    LAST_ENTERED_DATE = null;
                } else {
                    LAST_ENTERED_DATE = Date.valueOf(jReuqest.getString("LAST_ENTERED_DATE"));
                    //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
                }
                Date START_DT = null;
                if (jReuqest.getString("START_DT") == null || jReuqest.getString("START_DT").equals("")) {
                    START_DT = null;
                } else {
                    START_DT = Date.valueOf(jReuqest.getString("START_DT"));
                    //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("ENTERED_DATE"));
                }

                JSONArray deleteMem = new JSONArray(NVL.StringNvl(jReuqest.getString("DELETE_BOARD_MEM")));
                int i = 0;
                try {
                    if (deleteMem.length() > 0) {
                        i = deleteMem(con, deleteMem, COMP_CD);
                    }
                } catch (Exception e) {
                    Utility.PrintMessage("Error in Delete Board Member Details : " + e);
                    response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time (Error :"+e.getMessage().replace("\"","")+").\"}";
                }

                JSONArray UpdateMem = new JSONArray(NVL.StringNvl(jReuqest.getString("UPDATE_BOARD_MEM")));
                int k = 0;
                try {
                		
                    if (UpdateMem.length() > 0) {
                        k = updateMem(con, UpdateMem, COMP_CD);
                    }
                } catch (Exception e) {
                    Utility.PrintMessage("Error in Update Board Member Details : " + e);
                    response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time (Error :"+e.getMessage().replace("\"","")+").\"}";
                }
                
                JSONArray insertMem = new JSONArray(NVL.StringNvl(jReuqest.getString("INSERT_BOARD_MEM")));
                int j = 0;
                try {
                    if (insertMem.length() > 0) {
                        j = insertMem(con, insertMem, COMP_CD);
                    }
                } catch (Exception e) {
                    Utility.PrintMessage("Error in Insert Board Member Details : " + e);
                    response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time (Error : "+e.getMessage().replace("\"", "")+").\"}";
                }
                                                
                if (i > 0) {
                	deleteMsg = "Board Member Sucessfully Deleted";                   
                } else {
                	deleteMsg = "Erro While Deleteting Board Member"; 
                }

                if (j > 0) {                	
                    insertMeg = "Board Member Sucessfully Updated";
                } else {
                	insertMeg = "Error While Updateing Board Member"; 
                }

                if (k > 0) {
                	 updateMsg = "Board Member Sucessfully Inserted";
                } else {
                	updateMsg = "Error while Updateing Board Member";
                }
                
                System.out.println("delete :"+i);
                System.out.println("insert :"+j);
                System.out.println("update :"+k);
                
                

                if (UPDATE_FLAG.equals("Y")) {
                    String updateEmployee = "UPDATE comp_mst SET COMP_NM = ?, TRADING_NM = ?, ADDRESS_1 = ?, ADDRESS_2 = ?, ADDRESS_3 = ?, CONTRAY_CD = ?, STATE_CD = ?, CITY_CD = ?, PIN_CODE = ?, GSTIN = ?, PAN_NO = ?, TYPE_OF_BUSINESS = ?, COMP_LOGO = ?, INCEPTION_DT = ?, EMAIL_ID = ?, CONTACT_NO = ?, LANDLINE_NO = ?, PERMANENT_ADD = ?, TAX_DEDUC_AC_MO = ?, WEBSITE = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ?, LAST_ENTERED_DATE = ?, START_DT = ? WHERE COMP_CD = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);

                    preparedStatement.setString(1, COMP_NM);
                    preparedStatement.setString(2, TRADING_NM);
                    preparedStatement.setString(3, ADDRESS_1);
                    preparedStatement.setString(4, ADDRESS_2);
                    preparedStatement.setString(5, ADDRESS_3);
                    preparedStatement.setInt(6, CONTRAY_CD);
                    preparedStatement.setInt(7, STATE_CD);
                    preparedStatement.setInt(8, CITY_CD);
                    preparedStatement.setString(9, PIN_CODE);
                    preparedStatement.setString(10, GSTIN);
                    preparedStatement.setString(11, PAN_NO);
                    preparedStatement.setString(12, TYPE_OF_BUSINESS);
                    preparedStatement.setBlob(13, COMP_LOGO);
                    preparedStatement.setDate(14, INCEPTION_DT);
                    preparedStatement.setString(15, EMAIL_ID);
                    preparedStatement.setInt(16, CONTACT_NO);
                    preparedStatement.setInt(17, LANDLINE_NO);
                    preparedStatement.setString(18, PERMANENT_ADD);
                    preparedStatement.setString(19, TAX_DEDUC_AC_MO);
                    preparedStatement.setString(20, WEBSITE);
                    preparedStatement.setString(21, LAST_ENTERED_BY);
                    preparedStatement.setString(22, LAST_ENTERED_IP);
                    preparedStatement.setDate(23, LAST_ENTERED_DATE);
                    preparedStatement.setDate(24, START_DT);

                    preparedStatement.setString(25, COMP_CD);

                    int row = preparedStatement.executeUpdate();

                    if (row == 0) {
                        con.rollback();
                        jOutPut.put("STATUS_CD", "99");
                        jOutPut.put("MESSAGE", "You have not rights for update");
                    } else {
                        con.commit();
                        jOutPut.put("STATUS_CD", "0");
                        jOutPut.put("MESSAGE", "Company Detail " + COMP_CD + " Sucessfully Updated.");                                                
                    }                   
                    
                    response = jOutPut.toString();
                } else {
//                    String updateEmployee = "UPDATE comp_mst SET COMP_NM = ?, TRADING_NM = ?, ADDRESS_1 = ?, ADDRESS_2 = ?, ADDRESS_3 = ?, CONTRAY_CD = ?, STATE_CD = ?, CITY_CD = ?, PIN_CODE = ?, GSTIN = ?, PAN_NO = ?, TYPE_OF_BUSINESS = ?, COMP_LOGO = ?, INCEPTION_DT = ?, EMAIL_ID = ?, CONTACT_NO = ?, LANDLINE_NO = ?, PERMANENT_ADD = ?, TAX_DEDUC_AC_MO = ?, WEBSITE = ?, LAST_ENTERED_BY = ?, LAST_ENTERED_IP = ?, LAST_ENTERED_DATE = ?, START_DT = ? WHERE COMP_CD = ?";
//
//                    PreparedStatement preparedStatement = con.prepareStatement(updateEmployee);
//                    preparedStatement.setString(1, COMP_NM);
//                    preparedStatement.setString(2, TRADING_NM);
//                    preparedStatement.setString(3, ADDRESS_1);
//                    preparedStatement.setString(4, ADDRESS_2);
//                    preparedStatement.setString(5, ADDRESS_3);
//                    preparedStatement.setInt(6, CONTRAY_CD);
//                    preparedStatement.setInt(7, STATE_CD);
//                    preparedStatement.setInt(8, CITY_CD);
//                    preparedStatement.setString(9, PIN_CODE);
//                    preparedStatement.setString(10, GSTIN);
//                    preparedStatement.setString(11, PAN_NO);
//                    preparedStatement.setString(12, TYPE_OF_BUSINESS);
//                    preparedStatement.setBlob(13, COMP_LOGO);
//                    preparedStatement.setDate(14, INCEPTION_DT);
//                    preparedStatement.setString(15, EMAIL_ID);
//                    preparedStatement.setInt(16, CONTACT_NO);
//                    preparedStatement.setInt(17, LANDLINE_NO);
//                    preparedStatement.setString(18, PERMANENT_ADD);
//                    preparedStatement.setString(19, TAX_DEDUC_AC_MO);
//                    preparedStatement.setString(20, WEBSITE);
//                    preparedStatement.setString(21, LAST_ENTERED_BY);
//                    preparedStatement.setString(22, LAST_ENTERED_IP);
//                    preparedStatement.setDate(23, LAST_ENTERED_DATE);
//                    preparedStatement.setDate(24, START_DT);
//
//                    preparedStatement.setString(25, COMP_CD);
//
//                    int row = preparedStatement.executeUpdate();
//
//                    if (row == 0) {
//                        con.rollback();
//                        jOutPut.put("STATUS_CD", "99");
//                        jOutPut.put("MESSAGE", "You have not rights for update");
//                    } else {
//                        con.commit();
//                        jOutPut.put("STATUS_CD", "0");
//                        jOutPut.put("MESSAGE", "Company Details " + COMP_CD + " Sucessfully Updated.");
//                        jOutPut.put("BOARD_MEM_INSERT_MESSAGE", insertMeg);
//                        jOutPut.put("BOARD_MEM_UPDATE_MESSAGE", updateMsg);
//                        jOutPut.put("BOARD_MEM_DELETE_MESSAGE", deleteMsg);
//                    }
//                    
                    jOutPut.put("STATUS_CD", "999");
                    jOutPut.put("MESSAGE", "Update Not Allow.");  

                    response = jOutPut.toString();
                }
            }
        } catch (Exception e) {
            Utility.PrintMessage("Error in Update Company Details : " + e);
            response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time (Error :"+e.getMessage().replace("\"","")+").\"}";
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


    public static int deleteMem(Connection con, JSONArray memData, String comp_cd) throws JSONException, SQLException {
        Statement stmt = null;
        int row = 0;

        for (int i = 0; i < memData.length(); i++) {
            JSONObject deleteMem = new JSONObject();
            deleteMem = (JSONObject) memData.get(i);

            int id = deleteMem.getInt("ID");
//            String ls_flag = deleteMem.getString("DELETE_FLAG");
            String sql = null;
//            if (ls_flag.equals("Y")) {
                sql = "UPDATE COMP_DTL SET IS_DELETE = 'Y' WHERE COMP_CD = '" + comp_cd + "' AND ID =" + id;
                stmt = con.createStatement();
                row = stmt.executeUpdate(sql);
                row = row + 1;
//            }
        }
        return row;
    }

    public static int insertMem(Connection con, JSONArray memData, String ls_comp_cd) throws SQLException, JSONException {
        int row = 0;
        for (int i = 0; i < memData.length(); i++) {
            JSONObject insertMem = new JSONObject();
            insertMem = (JSONObject) memData.get(i);


//            String INSERT_FLAG = NVL.StringNvl(insertMem.getString("INSERT_FLAG"));
            int ID = 0;
            if (insertMem.getString("ID") == null || insertMem.getString("ID").equals("")) {
                ID = 0;
            } else {
                ID = insertMem.getInt("ID");
            }
            String PARTNE_NM = NVL.StringNvl(insertMem.getString("PARTNE_NM"));
            String ROLE = NVL.StringNvl(insertMem.getString("ROLE"));
            String NATURE_OF_ROLE = NVL.StringNvl(insertMem.getString("NATURE_OF_ROLE"));
            Date JOINING_DT = null;
            if (insertMem.getString("JOINING_DT") == null || insertMem.getString("JOINING_DT").equals("")) {
                JOINING_DT = null;
            } else {
                JOINING_DT = Date.valueOf(insertMem.getString("JOINING_DT"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
            int CONTACT = 0;
            if (insertMem.getString("CONTACT") == null || insertMem.getString("CONTACT").equals("")) {
                CONTACT = 0;
            } else {
                CONTACT = insertMem.getInt("CONTACT");
            }
            String REMARKS = NVL.StringNvl(insertMem.getString("REMARKS"));

            Statement stmt = null;
            ResultSet rs = null;

            String ls_sql = "SELECT COUNT(*) AS CNT FROM COMP_DTL WHERE ID =" + ID + " AND IS_DELETE = 'N'";
            System.out.println(ls_sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(ls_sql);

            int cnt = 0;
            while (rs.next()) {
                cnt = rs.getInt("CNT");
            }

            if (cnt <= 0) {
//                if (INSERT_FLAG.equals("Y")) {
                    JSONObject jOut = new JSONObject();
                    String sql = "INSERT INTO comp_dtl (COMP_CD, ID, PARTNE_NM, ROLE, NATURE_OF_ROLE, JOINING_DT, CONTACT, REMARKS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = null;

                    try {
                        preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setString(1, ls_comp_cd);
                        preparedStatement.setInt(2, ID);
                        preparedStatement.setString(3, PARTNE_NM);
                        preparedStatement.setString(4, ROLE);
                        preparedStatement.setString(5, NATURE_OF_ROLE);
                        preparedStatement.setDate(6, JOINING_DT);
                        preparedStatement.setInt(7, CONTACT);
                        preparedStatement.setString(8, REMARKS);

                        row = preparedStatement.executeUpdate();
                    } catch (Exception e) {
                        row = 0;
                        Utility.PrintMessage("Error in Create Board Member : " + e);
                        //response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                    }
                }
//                    else {
//                    row = 0;
//                }
//            }
        }
        return row;
    }

    public static int updateMem(Connection con, JSONArray memData, String ls_comp_cd) throws JSONException {
        int row = 0;
        for (int i = 0; i < memData.length(); i++) {
            JSONObject updateMem = new JSONObject();
            updateMem = (JSONObject) memData.get(i);
            
            
            
            
            int id = updateMem.getInt("ID");
//            String updateFlag = updateMem.getString("UPDATE_FLAG");
            JSONObject updateData = new JSONObject(updateMem.getString("DATA"));

//            String UPDATE_FLAG = NVL.StringNvl(updateData.getString("UPDATE_FLAG"));
            String PARTNE_NM = NVL.StringNvl(updateData.getString("PARTNE_NM"));
            String ROLE = NVL.StringNvl(updateData.getString("ROLE"));
            String NATURE_OF_ROLE = NVL.StringNvl(updateData.getString("NATURE_OF_ROLE"));
            Date JOINING_DT = null;
            if (updateData.getString("JOINING_DT") == null || updateData.getString("JOINING_DT").equals("")) {
                JOINING_DT = null;
            } else {
                JOINING_DT = Date.valueOf(updateData.getString("JOINING_DT"));
                //(Date) new SimpleDateFormat("YYYY-MM-DD").parse(jReuqest.getString("JOINIG_DT"));
            }
//            int CONTACT = 0;
//            if (updateData.getString("CONTACT") == null || updateData.getString("CONTACT").equals("")) {
//                CONTACT = 0;
//            } else {
//                CONTACT = updateData.getDouble("CONTACT");
//            }
            String CONTACT = NVL.StringNvl(updateData.getString("CONTACT"));
            String REMARKS = NVL.StringNvl(updateData.getString("REMARKS"));

            System.out.println("Testjnkj");
//            if (UPDATE_FLAG.equals("Y")) {
                JSONObject jOut = new JSONObject();
                String sql = "UPDATE comp_dtl SET PARTNE_NM = ?, ROLE = ?, NATURE_OF_ROLE = ?, JOINING_DT = ?, CONTACT = ?, REMARKS = ? WHERE COMP_CD = ? AND ID = ? AND IS_DELETE = ?";
                PreparedStatement preparedStatement = null;

                try {
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, PARTNE_NM);
                    preparedStatement.setString(2, ROLE);
                    preparedStatement.setString(3, NATURE_OF_ROLE);
                    preparedStatement.setDate(4, JOINING_DT);
                    preparedStatement.setString(5, CONTACT);
                    preparedStatement.setString(6, REMARKS);

                    preparedStatement.setString(7, ls_comp_cd);
                    preparedStatement.setInt(8, id);
                    preparedStatement.setString(9, "N");

                    row = preparedStatement.executeUpdate();
                } catch (Exception e) {
                    row = 0;
                    e.printStackTrace();
                    Utility.PrintMessage("Error in Update Board Member : " + e);
                    // response = "{\"STATUS_CD\":\"99\",\"MESSAGE\":\"Something went to wrong,Please try after some time.\"}";
                }
//            } else {
//                row = 0;
//            }
        }
        return 0;
    }

}