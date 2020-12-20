package com.crm.service;

import java.sql. * ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.crm.utility.getMenu;
import com.crm.utility.getParaValue;

public class loginService {
  public String login(Connection con, String input) throws JSONException,
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
      String ls_query = "SELECT COMP_CD,USER_ID,MENU_RIGHRS,ACTIVE_STATUS,BLOCK_STATUS,USER_PWD,FAILED_CNT FROM USER_MST WHERE USER_ID = ?";
      PreparedStatement ps = null;
      ps = con.prepareStatement(ls_query);
      ps.setString(1, username.toUpperCase());
      rs = ps.executeQuery();

      String ls_comp_cd = null;
      String ls_userid = null;
      String ls_menu = null;
      String activeStatus = null;
      String blockStatus = null;
      String user_pwd = null;
      String ls_para = null;
      int failed_cnt = 0;      
      try {
        if (rs.next()) {
          ls_comp_cd = rs.getString("COMP_CD");
          ls_userid = rs.getString("USER_ID");
          ls_menu = rs.getString("MENU_RIGHRS");
          activeStatus = rs.getString("ACTIVE_STATUS");
          blockStatus = rs.getString("BLOCK_STATUS");
          user_pwd = rs.getString("USER_PWD");
          failed_cnt = rs.getInt("FAILED_CNT");

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
            menuGrant = getMenu.getMenu(con, username);
            jOutput.put("STATUS_CD", "0");
            jOutput.put("MESSAGE", "SUCESS");
            JSONObject jObj1 = new JSONObject();

            jObj1.put("COMP_CD", ls_comp_cd);
            jObj1.put("USER_ID", ls_userid);
            jObj1.put("MENU", menuGrant);
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
}