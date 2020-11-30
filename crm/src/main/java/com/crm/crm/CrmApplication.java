package com.crm.crm;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.service.loginService;
import com.crm.utility.ConnectionDbB;

@RestController
@SpringBootApplication
public class CrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}
	
	@GetMapping("getConnection")
	public String test() throws ClassNotFoundException, SQLException {
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
	
	@PostMapping("login")
	public String login(@RequestBody String input) {
		String ls_output = null;					
		loginService login = new loginService();
		try {
			ls_output = login.login(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls_output;
	}
}

