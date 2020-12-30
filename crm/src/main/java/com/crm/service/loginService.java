package com.crm.service;

import java.sql. * ;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.rowset.serial.SerialBlob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.crm.utility.NVL;
import com.crm.utility.Utility;
import com.crm.utility.getMenu;
import com.crm.utility.getParaValue;

public class loginService {
  public static String login(Connection con, String input) throws JSONException,
  ClassNotFoundException,
  SQLException {
    JSONObject jObject = new JSONObject(input);
    String username = jObject.getString("USERNAME");
    String password = jObject.getString("PASSWORD");
    String req_ip = jObject.getString("REQUEST_IP");

    JSONObject jOutput = new JSONObject();
    JSONObject menuGrant = new JSONObject();

    JSONArray jResponse = new JSONArray();

    if (username.length() <= 0) {
      jOutput.put("RESPONSE", jResponse);
      jOutput.put("STATUS_CD", "99");
      jOutput.put("MESSAGE", "Please enter your username.");
    } else if (password.length() <= 0) {
      jOutput.put("RESPONSE", jResponse);
      jOutput.put("STATUS_CD", "99");
      jOutput.put("MESSAGE", "Please enter your password.");
    } else {
      ResultSet rs = null;
      String ls_query = "SELECT COMP_CD,USER_ID,ACTIVE_STATUS,BLOCK_STATUS,USER_PWD,FAILED_CNT,USER_PHOTO FROM USER_MST WHERE USER_ID = ?";
      PreparedStatement ps = null;
      ps = con.prepareStatement(ls_query);
      ps.setString(1, username.toUpperCase());
      rs = ps.executeQuery();

      String ls_comp_cd = null;
      String ls_userid = null;
      String activeStatus = null;
      String blockStatus = null;
      String user_pwd = null;
      String ls_para = null;
      String ls_user_photo = null;
      int failed_cnt = 0;      
      try {
        if (rs.next()) {
          ls_comp_cd = rs.getString("COMP_CD");
          ls_userid = rs.getString("USER_ID");
          activeStatus = rs.getString("ACTIVE_STATUS");
          blockStatus = rs.getString("BLOCK_STATUS");
          user_pwd = rs.getString("USER_PWD");
          failed_cnt = rs.getInt("FAILED_CNT");
          ls_user_photo = NVL.StringNvl(rs.getString("USER_PHOTO"));
          
          

          ls_para = getParaValue.getPara(con, 1);
          int block_cnt = Integer.parseInt(ls_para);
          if (!activeStatus.equals("Y")) {
            jOutput.put("RESPONSE", "INACTIVATED");
            jOutput.put("STATUS_CD", "999");
            jOutput.put("MESSAGE", "UserID Inactived Kindly contact to your admin.");
          } else if (blockStatus.equals("Y")) {
            jOutput.put("RESPONSE", "BLOCK");
            jOutput.put("STATUS_CD", "999");
            jOutput.put("MESSAGE", "UserID is block Kindly contact to your admin");
          } else if (!password.equals(user_pwd) || user_pwd.equals(null)) {
            failed_cnt = failed_cnt + 1;
            try {
              String updateCnt = "UPDATE USER_MST SET BLOCK_STATUS = ? ,FAILED_CNT = ? WHERE USER_ID = ?";
              PreparedStatement update_ps = null;
              update_ps = con.prepareStatement(updateCnt);
              if (block_cnt <= failed_cnt) {
                update_ps.setString(1, "Y");
              } else {
                update_ps.setString(1, "N");
              }
              update_ps.setInt(2, failed_cnt);
              update_ps.setString(3, username.toUpperCase());
              update_ps.executeUpdate();
              con.commit();
            } catch(Exception e) {
              e.printStackTrace();
              System.out.println("Login Failed Count : " + e);
            }
            jOutput.put("RESPONSE", jResponse);
            jOutput.put("STATUS_CD", "999");
            if (block_cnt <= failed_cnt) {
              jOutput.put("MESSAGE", "UserID is block Kindly contact to your admin");
            } else {
              jOutput.put("MESSAGE", "Password Wrong." + (block_cnt - failed_cnt) + " attempt pending..");
            }
          } else {

            try {
              String updateCnt = "UPDATE USER_MST SET BLOCK_STATUS = ? ,FAILED_CNT = ? WHERE USER_ID = ?";
              PreparedStatement update_ps = null;
              update_ps = con.prepareStatement(updateCnt);
              update_ps.setString(1, "N");
              update_ps.setInt(2, 0);
              update_ps.setString(3, username.toUpperCase());
              update_ps.executeUpdate();
              con.commit();
            } catch(Exception e) {
              e.printStackTrace();
              System.out.println("Login Failed Count : " + e);
            }
            
            JSONObject loginJson = new JSONObject();
            loginJson.put("COMP_CD",ls_comp_cd);
            loginJson.put("USER_ID",ls_userid);
            loginJson.put("ACTIVITY_CD","");
            loginJson.put("LOGIN_TYPE","L");
//            loginJson.put("LOG_IN_OUT_DT",new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString());
            String ls_activity_cd = loginAuditrail(con, loginJson.toString(), "L");
            
            menuGrant = getMenu.getMenu(con, username);
            jOutput.put("STATUS_CD", "0");
            jOutput.put("MESSAGE", "SUCESS");
            JSONObject jObj1 = new JSONObject();

            jObj1.put("COMP_CD", ls_comp_cd);
            jObj1.put("USER_ID", ls_userid);            
            jObj1.put("MENU", menuGrant);
            jObj1.put("ACTIVITY_CD", ls_activity_cd);
            jObj1.put("USER_PHOTO", ls_user_photo);
            jResponse.put(jObj1);
            jOutput.put("RESPONSE", jResponse);
            
            
          }

        } else {
          jOutput.put("RESPONSE", jResponse);
          jOutput.put("STATUS_CD", "99");
          jOutput.put("MESSAGE", "Invalid Username/Password.");
        }
      } catch(Exception e) {       
        System.out.println("Login API ERROR : " + e);
      }

    }
    String ls_output = jOutput.toString();
    return ls_output;
  }
  
  public static String loginAuditrail(Connection con, String ls_request,String loginType) {
      String response = "";
      Statement stmt = null;
      int ExistingCnt = 0;
      String ACTIVITY_CD = null; 
      System.out.println("login "+ls_request);
      try {

          JSONObject jin = new JSONObject(ls_request);
                    
          String COMP_CD = jin.getString("COMP_CD");
          String USER_ID = jin.getString("USER_ID");
              
          
          if (loginType.equals("L")) {
        	   ACTIVITY_CD = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()).replace(".","");
          } else {
        	   ACTIVITY_CD = jin.getString("ACTIVITY_CD"); 
          }
                    
          String LOGIN_TYPE = jin.getString("LOGIN_TYPE");
          int tran_cd = getParaValue.getMaxTranCd(con, "LOGIN_AUD_MST", "TRAN_CD");
                                             
          String extingEmp = "SELECT COUNT(*) AS EMP_CNT FROM USER_MST WHERE COMP_CD = '"+COMP_CD+"' AND USER_ID = '"+ USER_ID+"' AND IS_DELETE = 'N'";
          	System.out.println(extingEmp);
          stmt = con.createStatement();
          ResultSet empResultSet = stmt.executeQuery(extingEmp);

          while (empResultSet.next()) {
              ExistingCnt = empResultSet.getInt("EMP_CNT");
          }
          System.out.println(ExistingCnt);
          if (ExistingCnt <= 0) {
          	 JSONObject jOutPut = new JSONObject();

               jOutPut.put("STATUS_CD", "99");
               jOutPut.put("MESSAGE", "User ID " + USER_ID + " Not Exist.");
               response = jOutPut.toString();
               System.out.println(response);
          } else {
              JSONObject jOut = new JSONObject();
              String sql = "INSERT INTO login_aud_mst(COMP_CD,TRAN_CD,USER_ID,ACTIVITY_CD,LOGIN_TYPE)VALUES (?,?,?,?,?)";
              PreparedStatement preparedStatement = con.prepareStatement(sql);
              
              preparedStatement.setString(1,COMP_CD);
              preparedStatement.setInt(2,tran_cd);
              preparedStatement.setString(3,USER_ID);
              preparedStatement.setString(4,ACTIVITY_CD);
              preparedStatement.setString(5,LOGIN_TYPE);              
//              preparedStatement.setDate(6,(java.sql.Date) LOG_IN_OUT_DT);
              
              int row = preparedStatement.executeUpdate();

              if (row == 0) {
                  con.rollback();
              } else {
                  con.commit();
              }

              JSONObject jOutPut = new JSONObject();

              jOutPut.put("STATUS_CD", "0");
              jOutPut.put("MESSAGE", "User ID " + USER_ID + " Login Log Sucessfully Created.");
              response = jOutPut.toString();
          }
      } catch (Exception e) {
    	  e.printStackTrace();
          Utility.PrintMessage("Error in Login Audit : " + e);
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
              
          } catch (Exception e2) {
              // TODO: handle exception
          }
      }
	return ACTIVITY_CD.replace(".","");
  }

  
  
}