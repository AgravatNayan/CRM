package com.crm.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getMenu {
  public static JSONObject getMenu(Connection conMain, String user_id) throws JSONException,
  ClassNotFoundException,
  SQLException {

    String menuName = null;
    String menuVisible = null;
    String MENU_ID = null;
    String userVisible = null;
    String ls_view = null;
    String ls_create = null;
    String ls_insert = null;
    String ls_delete = null;

    JSONArray jArray = new JSONArray();
    int cnt = 0;
    Statement stmt = null;
    ResultSet rs = null;

    JSONObject Menu = null;
    JSONObject SubMenu = null;
    JSONObject MainMenu = new JSONObject();
    JSONArray jMenu = new JSONArray();
    try {
      String mainMenu = "SELECT m.MENU_ID,m.MENU_NM,m.VISIBLE AS MENU_VISIBLE,u.VISIBLE AS USER_VISIBLE,u.VIEW,u.CREATE,u.UPDATE,u.DELETE FROM user_menu_mst u, menu_mst m WHERE u.USER_ID = '" + user_id + "'AND u.MENU_ID = m.MENU_ID";

      stmt = conMain.createStatement();
      rs = stmt.executeQuery(mainMenu);

      while (rs.next()) {
        Menu = new JSONObject();
        SubMenu = new JSONObject();

        menuName = rs.getString("MENU_NM");
        MENU_ID = rs.getString("MENU_ID");
        menuVisible = rs.getString("MENU_VISIBLE");
        userVisible = rs.getString("USER_VISIBLE");
        ls_view = rs.getString("VIEW");;
        ls_create = rs.getString("CREATE");;
        ls_insert = rs.getString("UPDATE");;
        ls_delete = rs.getString("DELETE");;

        Menu.put("NAME", menuName);

        if (menuVisible.equals("Y")) {
          Menu.put("VISIBLE", userVisible);
        } else {
          Menu.put("VISIBLE", "N");
        }

        SubMenu = getSubMenu(conMain, user_id, MENU_ID);
        Menu.put("SUBMENU", SubMenu);
        Menu.put("VIEW", ls_view);
        Menu.put("CREATE", ls_create);
        Menu.put("UPDATE", ls_insert);
        Menu.put("DELETE", ls_delete);
        MainMenu.put(MENU_ID, Menu);

      }
      System.out.println(MainMenu);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      try {
        rs.close();
      } catch(Exception e2) {
        // TODO: handle exception
      }
      try {
        stmt.close();
      } catch(Exception e) {
        // TODO: handle exception
      }

    }

    return MainMenu;
  }

  public static JSONObject getSubMenu(Connection conMain, String user_id, String MENU_ID) throws JSONException,
  ClassNotFoundException,
  SQLException {

    String menuSubName = null;
    String menuSubId = null;
    String menuVisible = null;
    String userVisible = null;
    String ls_view = null;
    String ls_create = null;
    String ls_insert = null;
    String ls_delete = null;

    JSONArray jArray = new JSONArray();
    int cnt = 0;
    Statement stmt = null;
    ResultSet rs = null;

    JSONObject Menu = null;
    JSONObject SubMenu = null;
    JSONArray jMenu = new JSONArray();

    JSONObject MainObject = new JSONObject();
    try {
      String mainMenu = "SELECT u.MENU_ID,u.MENU_SUB_ID,m.MENU_NM,u.VISIBLE AS USER_VISIBLE,m.VISIBLE AS MENU_VISIBLE,u.VIEW,u.UPDATE,u.CREATE,u.DELETE FROM user_menu_dtl u, menu_dtl m WHERE u.USER_ID = '" + user_id + "' AND u.MENU_ID = '" + MENU_ID + "' AND u.MENU_ID = m.MENU_ID AND u.MENU_SUB_ID = m.MENU_SUB_ID";

      stmt = conMain.createStatement();
      rs = stmt.executeQuery(mainMenu);

      while (rs.next()) {
        Menu = new JSONObject();
        SubMenu = new JSONObject();

        menuSubName = rs.getString("MENU_NM");
        menuSubId = rs.getString("MENU_SUB_ID");
        menuVisible = rs.getString("MENU_VISIBLE");
        userVisible = rs.getString("USER_VISIBLE");
        ls_view = rs.getString("VIEW");
        ls_create = rs.getString("CREATE");
        ls_insert = rs.getString("UPDATE");
        ls_delete = rs.getString("DELETE");

        Menu.put("NAME", menuSubName);
        if (menuVisible.equals("Y")) {
          Menu.put("VISIBLE", userVisible);
        } else {
          Menu.put("VISIBLE", "N");
        }

        SubMenu = getsSubMenu(conMain, user_id, MENU_ID, menuSubId);
        Menu.put("SUBMENU", SubMenu);
        Menu.put("VIEW", ls_view);
        Menu.put("CREATE", ls_create);
        Menu.put("UPDATE", ls_insert);
        Menu.put("DELETE", ls_delete);
        MainObject.put(menuSubId, Menu);
      }
      System.out.println(jMenu);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      try {
        rs.close();
      } catch(Exception e2) {
        // TODO: handle exception
      }
      try {
        stmt.close();
      } catch(Exception e) {
        // TODO: handle exception
      }

    }

    return MainObject;
  }

  public static JSONObject getsSubMenu(Connection conMain, String user_id, String MENU_ID, String MENU_SUB_ID) throws JSONException,
  ClassNotFoundException,
  SQLException {

    String ls_view = null;
    String ls_create = null;
    String ls_insert = null;
    String ls_delete = null;
    String menuName = null;
    String menuVisible = null;
    String userVisible = null;
    JSONArray jArray = new JSONArray();
    int cnt = 0;
    Statement stmt = null;
    ResultSet rs = null;

    JSONObject Menu = null;
    JSONObject SubMenu = null;
    JSONArray jMenu = new JSONArray();
    JSONObject MeinObject = new JSONObject();
    try {
      String mainMenu = "SELECT m.MENU_ID,m.MENU_SUB_ID,m.MENU_SUB_SUB_ID,m.MENU_NM,u.VISIBLE AS USER_VISIBLE,m.VISIBLE AS MENU_VISIBLE,u.VIEW,u.UPDATE,u.CREATE,u.DELETE FROM user_menu_sdt u, menu_sdt m WHERE u.USER_ID = '" + user_id + "' AND u.MENU_ID = '" + MENU_ID + "' AND u.MENU_SUB_ID = '" + MENU_SUB_ID + "' AND u.MENU_ID = m.MENU_ID AND u.MENU_SUB_ID = m.MENU_SUB_ID AND u.MENU_SUB_SUB_ID = m.MENU_SUB_SUB_ID";
      stmt = conMain.createStatement();
      rs = stmt.executeQuery(mainMenu);

      while (rs.next()) {
        Menu = new JSONObject();
        menuName = rs.getString("MENU_NM");
        MENU_ID = rs.getString("MENU_SUB_SUB_ID");
        menuVisible = rs.getString("MENU_VISIBLE");
        userVisible = rs.getString("USER_VISIBLE");
        ls_view = rs.getString("VIEW");
        ls_create = rs.getString("CREATE");
        ls_insert = rs.getString("UPDATE");
        ls_delete = rs.getString("DELETE");
        Menu.put("NAME", menuName);
        if (menuVisible.equals("Y")) {
          Menu.put("VISIBLE", userVisible);
        } else {
          Menu.put("VISIBLE", "N");
        }
        Menu.put("VIEW", ls_view);
        Menu.put("CREATE", ls_create);
        Menu.put("UPDATE", ls_insert);
        Menu.put("DELETE", ls_delete);
        MeinObject.put(MENU_ID, Menu);

      }
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      try {
        rs.close();
      } catch(Exception e2) {
        // TODO: handle exception
      }
      try {
        stmt.close();
      } catch(Exception e) {
        // TODO: handle exception
      }

    }

    return MeinObject;
  }
}