package com.foodstore.admin.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

	@Autowired
	UserService service;
	
	@PostMapping("/users/check_email")
	public boolean IsDuplicatedEmail(Integer id, String email) {
		return service.isDuplicatedEmail(id,email);
	}
}
