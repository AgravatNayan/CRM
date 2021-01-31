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

import com.crm.hrm.salary.getEmpWiseSalary;
import com.crm.hrm.salary.leaveService;
import com.crm.hrm.salary.salaryService;
import com.crm.hrms.branch.branchService;
import com.crm.hrms.company.companyService;
import com.crm.hrms.contract.contractTypeService;
import com.crm.hrms.department.departService;
import com.crm.hrms.designation.designService;
import com.crm.hrms.dropdown.cityService;
import com.crm.hrms.dropdown.contractService;
import com.crm.hrms.dropdown.countryService;
import com.crm.hrms.dropdown.departmentService;
import com.crm.hrms.dropdown.designationService;
import com.crm.hrms.dropdown.gredeService;
import com.crm.hrms.dropdown.leavelService;
import com.crm.hrms.dropdown.pMiscMstService;
import com.crm.hrms.dropdown.religionService;
import com.crm.hrms.dropdown.shiftConfigService;
import com.crm.hrms.dropdown.stateService;
import com.crm.hrms.employee.getEmpList;
import com.crm.hrms.insurance.insuranceService;
import com.crm.hrms.shift.shiftService;
import com.crm.hrms.user.userService;
import com.crm.utility.ConnectionDbB;
import com.crm.utility.loginService;

@RestController @SpringBootApplication
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
            } catch (Exception e) {
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
            if (ls_action.equals("LOGINREQ")) { // Login API
                loginService login = new loginService();
                try {
                    ls_output = login.login(con, input);
                } catch (Exception e) {
                    System.out.println("Login Repo Error :" + e);
                }
            } else if (ls_action.equals("GETCOUNTRYLIST")) { // Get Country List    
                try {
                    ls_output = countryService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Country Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETSTATELIST")) { // Get State List
                try {
                    ls_output = stateService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("State Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETCITYLIST")) { // Get City List
                try {
                    ls_output = cityService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("City Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETRELIGIONLIST")) { // Get Religion List
                try {
                    ls_output = religionService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("religion Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETDEPARTMENTLIST")) { // Get Department List
                try {
                    ls_output = departmentService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Department Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETDESIGNATIONLIST")) { // Get Designation List
                try {
                    ls_output = designationService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Designation Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETBRANCHLIST")) { // Get Branch List
                try {
                    ls_output = branchService.getBranchData(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Branch Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETUSERLEVELLIST")) { // Get User Level List
                try {
                    ls_output = leavelService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("User Level Drop Down Error :" + e);
                }
            } else if (ls_action.equals("GETEMPLOYEELIST")) { // Get Employee List
                try {
                    ls_output = getEmpList.getEMployeeList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Employee Error :" + e);
                }
            } else if (ls_action.equals("GETMAXEMPID")) { // Get Max Employee Id
                try {
                    ls_output = getEmpList.GetMaxEmployeeID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Max Emp ID Error :" + e);
                }
            } else if (ls_action.equals("GETEMPLOYEEDTL")) { // Get Employee Id wise data
                try {
                    ls_output = getEmpList.GetEmployeeID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Emp ID Error :" + e);
                }
            } else if (ls_action.equals("CREATEEMPLOYEE")) { // Create Employee Id
                try {
                    ls_output = getEmpList.CreateEmployee(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Create Emp ID Error :" + e);
                }
            } else if (ls_action.equals("UPDATEEMPLOYEEID")) { // Update Employee Id
                try {
                    ls_output = getEmpList.updateEmployeeID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update Emp ID Error :" + e);
                }
            } else if (ls_action.equals("GETGRADELIST")) { // Get Grade List
                try {
                    ls_output = gredeService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Grade Error :" + e);
                }
            } else if (ls_action.equals("GETCONTRACTTYPE")) { // Get Contact Type Configuration
                try {
                    ls_output = contractService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Contract Type Error :" + e);
                }
            } else if (ls_action.equals("GETSHIFTCONFIG")) { // Get Shift Configuration
                try {
                    ls_output = shiftConfigService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Contract Type Error :" + e);
                }
            } else if (ls_action.equals("DELETEEMP")) { // Delete Employee
                try {
                    ls_output = getEmpList.deleteEmployeeID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Contract Type Error :" + e);
                }
            } else if (ls_action.equals("GET_P_MISC_MST")) { // Get P_MIST_MST Category
                try {
                    ls_output = pMiscMstService.getdropDwon(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get P_MISC_MST Error :" + e);
                }
            } else if (ls_action.equals("GETUSERLIST")) { // Get User List
                try {
                    ls_output = userService.getUserList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get User List Error :" + e);
                }
            } else if (ls_action.equals("GETUSERID")) { // Get User Id wise Data
                try {
                    ls_output = userService.GetUserID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get User Id Error :" + e);
                }
            } else if (ls_action.equals("CREATEUSER")) { // Create User
                try {
                    ls_output = userService.CreateUser(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Create User Error :" + e);
                }
            } else if (ls_action.equals("DELETEUSER")) { // Delete User
                try {
                    ls_output = userService.deleteUserID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Delete User Id Error :" + e);
                }
            } else if (ls_action.equals("UPDATEUSER")) { // Udate User
                try {
                    ls_output = userService.updateUserID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Delete User Id Error :" + e);
                }
            } else if (ls_action.equals("LOGOUTUSER")) { // Logout user
                try {
                    ls_output = loginService.loginAuditrail(con, input, "O").toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("UPDATECONTRACT")) { // Update Contract Detail
                try {
                    ls_output = contractTypeService.updateContractID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("DELETECONTRACTID")) { // Delete Contract Detail Id wise
                try {
                    ls_output = contractTypeService.deleteUserID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("CREATECONTRACT")) { // Create Contract Detail Id wise
                try {
                    ls_output = contractTypeService.CreateContract(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("GETCONTRACTID")) { // Get Contract Detail Id wise
                try {
                    ls_output = contractTypeService.GetContactID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("GETCONTRACTLIST")) { // Get Contract List
                try {
                    ls_output = contractTypeService.getContractList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("GETMAXCONTRATCID")) { // Get Contract List
                try {
                    ls_output = contractTypeService.GetMaxContractID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("DELETEINSU")) { // Delete Insurance Id
                try {
                    ls_output = insuranceService.deleteInsuID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("UPDATEINSU")) { // Update Insurance Id
                try {
                    ls_output = insuranceService.updateInsuID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("CREATEINSU")) { // Create Insurance Id
                try {
                    ls_output = insuranceService.CreateInsu(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("GETINSUID")) { // Get Insurance Id
                try {
                    ls_output = insuranceService.GetInsuID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("GETINSULIST")) { // Get Insurance List
                try {
                    ls_output = insuranceService.getInsuList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }
            } else if (ls_action.equals("GETMAXINSUID")) { // Get Insurance Max Id
                try {
                    ls_output = insuranceService.GetMaxInsuID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETMAXSHIFTID")) { // Get Shift Max Id
                try {
                    ls_output = shiftService.GetMaxShiftID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETSHIFTLIST")) { // Get Shift List
                try {
                    ls_output = shiftService.getShiftList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETSHIFTID")) { // Get Shift Id
                try {
                    ls_output = shiftService.GetShiftID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("CREATESHIFT")) { // Create Shift Id
                try {
                    ls_output = shiftService.CreateShift(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("UPDTAESHIFT")) { // Update Shift
                try {
                    ls_output = shiftService.updateShiftID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("DELETESHIFT")) { // Delete Shift TYpe
                try {
                    ls_output = shiftService.deleteShiftID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETDESIGLIST")) { // Get Designation List
                try {
                    ls_output = designService.getDesigList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETDESIGID")) { // Get Designation Id
                try {
                    ls_output = designService.getDesigId(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETMAXDESIG")) { // Get Max Designation
                try {
                    ls_output = designService.GetMaxShiftID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            }else if (ls_action.equals("CREATEDESIG")) { // Create Designation
                try {
                    ls_output = designService.CreateDesignation(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            }else if (ls_action.equals("DELETEDESIG")) { // Delete Designation
                try {
                    ls_output = designService.deleteDesigID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            }else if (ls_action.equals("UPDATEDESIG")) { // Update Designation
                try {
                    ls_output = designService.updateDesignation(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETMAXDEPART")) { // Get Max Department Id
                try {
                    ls_output = departService.GetMaxDepartID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETDEPARTLIST")) { // Get Department List
                try {
                    ls_output = departService.getDepartList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("GETDEPARTID")) { // Get Department Id
                try {
                    ls_output = departService.getDepartId(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("CREATEDEPART")) { // Create Department
                try {
                    ls_output = departService.CreateDepartment(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("DELETEDEPART")) { // Delete Department
                try {
                    ls_output = departService.deleteDepartID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                         
            } else if (ls_action.equals("UPDATEDEPART")) { // Update Department
                try {
                    ls_output = departService.updateDepartment(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Logout User Error :" + e);
                }                                                     
            } else if (ls_action.equals("UPDATEGRADE")) { // Update Grade
                try {
                    ls_output = gredeService.updateGrade(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            } else if (ls_action.equals("CREATEGRADE")) { // Create Grade
                try {
                    ls_output = gredeService.CreateGrade(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            } else if (ls_action.equals("DELETEGRADE")) { // Delete Grade
                try {
                    ls_output = gredeService.deleteGradeID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("GETGRADELISTS")) { // Get Grade List
                try {
                    ls_output = gredeService.getGradeList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }   else if (ls_action.equals("GETGRADEID")) { // Get Grade Id
                try {
                    ls_output = gredeService.getGradeId(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("GETMAXGRADE")) { // Get Max Grade Id
                try {
                    ls_output = gredeService.GetMaxGradeID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            } else if (ls_action.equals("GETBRANCHDTLIST")) { // Get Branch List
                try {
                    ls_output = branchService.getMaxBranchList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("GETBRANCHID")) { // Get Branch Id
                try {
                    ls_output = branchService.getMBranchId(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("DELETEBRANCH")) { //  Delete Branch
                try {
                    ls_output = branchService.deleteBranchID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("GETMAXBRANCHID")) { // Get Max Branch Id
                try {
                    ls_output = branchService.GetMaxBranchtID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("CREATEBRANCH")) { // Create Branch
                try {
                    ls_output = branchService.CreateBranch(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            }  else if (ls_action.equals("UPDATEBRANCH")) { // Update Branch
                try {
                    ls_output = branchService.updateBranchID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update in grade:" + e);
                }                                                     
            } else if (ls_action.equals("GETCOMPANYDTL")) { // Get Company Details
                try {
                    ls_output = companyService.getCompanyDetails(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Company Detail :" + e);
                }                                                     
            } else if (ls_action.equals("UPDATECOMPANYDETAIL")) { // Update Company Details
                try {
                    ls_output = companyService.updateCompayDetail(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update Comapany Error :" + e);
                }                                                     
            } else if (ls_action.equals("GETMAXBOARDMEM")) { // Get Max Board Member Id
                try {
                    ls_output = companyService.GetMaxBoradMemtID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Max Board Error :" + e);
                }                                     
            } else if (ls_action.equals("SALARYHEADMAXTRANCD")) { // Get Salary Max Tran cd
                try {
                    ls_output = salaryService.GetMaxSalaryTranCD(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Max Salary Tran cd :" + e);
                }                                     
            } else if (ls_action.equals("SALARYMAXHEADCD")) { // Get Salary Max Sr cd
                try {
                    ls_output = salaryService.GetMaxSalaryHeadCD(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Max Salary Sr Cd:" + e);
                }                                     
            } else if (ls_action.equals("SALARYHEADLIST")) { // get Salary Head Detail
                try {
                    ls_output = salaryService.getSalaryHeadList(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Salary Head List :" + e);
                }                                     
            } else if (ls_action.equals("GETHEADID")) { // get Salary Head Detail
                try {
                    ls_output = salaryService.getSalaryHeadDtl(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Salary Head List :" + e);
                }                                     
            }else if (ls_action.equals("DELETESALARYHEAD")) { // Delete Salary Head Detail
                try {
                    ls_output = salaryService.deleteSalaryHead(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Delete Salary Head Detail:" + e);
                }                                     
            } else if (ls_action.equals("CREATESALARYHEAD")) { // Create Salary Head
                try {
                    ls_output = salaryService.CreateSalaryHead(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Create Salary Head :" + e);
                }                                     
            } else if (ls_action.equals("UPDATESALARYHEAD")) { // Update Salary Head
                try {
                    ls_output = salaryService.UpdateSalaryHead(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Update Salary Head :" + e);
                }                                     
            } else if (ls_action.equals("GETHEADID")) { // GET Salary Head ID
                try {
                    ls_output = salaryService.jheadSubId(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Salary Head Id :" + e);
                }                                     
            } else if (ls_action.equals("DELETELEAVECD")) { // Delete Leave 
                try {
                    ls_output = leaveService.deleteLeaveID(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Delete Leave  :" + e);
                }                                     
            } else if (ls_action.equals("GETLEAVECD")) { // Get Leave Cd 
                try {
                    ls_output = leaveService.getLeaveDtl(con, input,"D").toString();
                } catch (Exception e) {
                    System.out.println("Get Leave Cd :" + e);
                }                                     
            } else if (ls_action.equals("GETLEAVELIST")) { // Get Leave List 
                try {
                    ls_output = leaveService.getLeaveDtl(con, input,"L").toString();
                } catch (Exception e) {
                    System.out.println("Get Leave List :" + e);
                }                                     
            } else if (ls_action.equals("GETMAXLEAVECD")) { // Get Max Leave Cd 
                try {
                    ls_output = leaveService.getMaxCd(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Max Leave Cd :" + e);
                }                                     
            } else if (ls_action.equals("CREATELEAVE")) { // Create Leave
                try {
                    ls_output = leaveService.createLeave(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Create Leave Cd :" + e);
                }                                     
            } else if (ls_action.equals("GETEMPLOYEESALARY")) { // Get Employee Salary
                try {
                    ls_output = getEmpWiseSalary.getSalary(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Employee Salary :" + e);
                }                                     
            } else if (ls_action.equals("GETLEAVEEMPDTL")) { // Get Employee Details
                try {
                    ls_output = leaveService.getEmpLeaveDtl(con, input).toString();
                } catch (Exception e) {
                    System.out.println("Get Employee Detail :" + e);
                }                                     
            } else {
                ls_output = "Kindly check your header action";
            }                                                                                                
            return ls_output;
        }
}