package com.crm.crm;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.crm.service.branchService;
import com.crm.service.cityService;
import com.crm.service.contractService;
import com.crm.service.countryService;
import com.crm.service.departmentService;
import com.crm.service.designationService;
import com.crm.service.getEmpList;
import com.crm.service.gredeService;
import com.crm.service.leavelService;
import com.crm.service.loginService;
import com.crm.service.religionService;
import com.crm.service.stateService;
import com.crm.utility.ConnectionDbB;

@RestController@SpringBootApplication
public class CrmApplication {

  public static void main(String[] args) {
    SpringApplication.run(CrmApplication.class, args);
  }

  @GetMapping("getConnection")
  public String test() throws ClassNotFoundException,
  SQLException {
    Connection con;
    String ls_return = null;
    try {
      con = ConnectionDbB.getCon();
      if (con != null) {
        ls_return = "Connection Done";
      } else {
        ls_return = "Some thing going to wrong";
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return ls_return;
  }

  @PostMapping("request")
  public String requestData(@RequestBody String input, @RequestHeader("Action") String ls_action) throws ClassNotFoundException,
  SQLException {

    Connection con = null;
    con = ConnectionDbB.getCon();
    String ls_output = null;
    if (ls_action.equals("LOGINREQ")) {			// Login API
      loginService login = new loginService();
      try {
        ls_output = login.login(con, input);
      } catch(Exception e) {
        System.out.println("Login Repo Error :" + e);
      }
    }
    else if (ls_action.equals("GETCOUNTRYLIST")) {  // Get Country List    
      try {
        ls_output = countryService.getdropDwon(con, input).toString();
      } catch(Exception e) {
        System.out.println("Country Drop Down Error :" + e);
      }
    } else if (ls_action.equals("GETSTATELIST")) {      // Get State List
      try {
        ls_output = stateService.getdropDwon(con, input).toString();
      } catch(Exception e) {
        System.out.println("State Drop Down Error :" + e);
      }
    } else if (ls_action.equals("GETCITYLIST")) {      // Get City List
        try {
            ls_output = cityService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("City Drop Down Error :" + e);
          }
    } else if (ls_action.equals("GETRELIGIONLIST")) {      // Get Religion List
        try {
            ls_output = religionService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("religion Drop Down Error :" + e);
          }
    } else if (ls_action.equals("GETDEPARTMENTLIST")) {      // Get Department List
        try {
            ls_output = departmentService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("Department Drop Down Error :" + e);
          }
    } else if (ls_action.equals("GETDESIGNATIONLIST")) {       // Get Designation List
        try {
            ls_output = designationService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("Designation Drop Down Error :" + e);
          }
    } else if (ls_action.equals("GETBRANCHLIST")) {      // Get Branch List
        try {
            ls_output = branchService.getBranchData(con, input).toString();
          } catch(Exception e) {
            System.out.println("Branch Drop Down Error :" + e);
          }
    } else if (ls_action.equals("GETUSERLEVELLIST")) {      // Get User Level List
        try {
            ls_output = leavelService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("User Level Drop Down Error :" + e);
          }
    } else if (ls_action.equals("GETEMPLOYEELIST")) {      // Get Employee List
        try {
            ls_output = getEmpList.getEMployeeList(con, input).toString();
          } catch(Exception e) {
            System.out.println("Get Employee Error :" + e);
          }
    } else if (ls_action.equals("GETMAXEMPID")) {      // Get Max Employee Id
        try {
            ls_output = getEmpList.GetMaxEmployeeID(con, input).toString();
          } catch(Exception e) {
            System.out.println("Get Max Emp ID Error :" + e);
          }
    } else if (ls_action.equals("GETEMPLOYEEDTL")) {      // Get Employee Id wise data
        try {
            ls_output = getEmpList.GetEmployeeID(con, input).toString();
          } catch(Exception e) {
            System.out.println("Get Emp ID Error :" + e);
          }
    } else if (ls_action.equals("CREATEEMPLOYEE")) {      // Create Employee Id
        try {
            ls_output = getEmpList.CreateEmployee(con, input).toString();
          } catch(Exception e) {
            System.out.println("Create Emp ID Error :" + e);
          }
    } else if (ls_action.equals("UPDATEEMPLOYEEID")) {      // Update Employee Id
        try {
            ls_output = getEmpList.updateEmployeeID(con, input).toString();
          } catch(Exception e) {
            System.out.println("Update Emp ID Error :" + e);
          }
    } else if (ls_action.equals("GETGRADELIST")) {      // Get Grade List
        try {
            ls_output = gredeService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("Get Grade Error :" + e);
          }
    } else if (ls_action.equals("GETCONTRACTTYPE")) {      // Get Contact Type Configuration
        try {
            ls_output = contractService.getdropDwon(con, input).toString();
          } catch(Exception e) {
            System.out.println("Get Contract Type Error :" + e);
          }
    }
    else {
      ls_output = "Kindly check your header action";
    }
    return ls_output;
  }
}