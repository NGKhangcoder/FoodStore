package com.foodstore.admin.user;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foodstore.admin.FileUploadUtil;
import com.foodstore.admin.user.export.UserCsvExporter;
import com.foodstore.admin.user.export.UserExcelExporter;
import com.foodstore.admin.user.export.UserPdfExporter;
import com.foodstore.common.entity.Role;
import com.foodstore.common.entity.User;
import com.foodstore.common.exception.UserNotFoundException;



@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=id&sortDir=asc&numberOfEntries=5";
	
	@GetMapping("/users")
	public String listFirstPage(Model model) {
	
		return defaultRedirectURL;
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(Model model,
			@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,
			@PathVariable("pageNum")int pageNum,
			@Param("numberOfEntries")Integer numberOfEntries,
			@Param("keyword")String keyword
			) {

		userService.setUserPerPage(numberOfEntries);
		Page<User>  listUsersPerPage = userService.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> listUsers = listUsersPerPage.getContent();
		
		String reversedSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		long startCount = (pageNum - 1) * userService.getUserPerPage() + 1;
		long endCount = startCount + userService.getUserPerPage() - 1;
		if(endCount > listUsersPerPage.getTotalElements()) {
			endCount = listUsersPerPage.getTotalElements();
		}
		
		model.addAttribute("pageHeader", "Users - Manage");
		
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reversedSortDir);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("numberOfEntries",numberOfEntries);
		
		model.addAttribute("listUsers",listUsers);
		model.addAttribute("totalItems", listUsersPerPage.getTotalElements());
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalPages",listUsersPerPage.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		
	
		return "users/users";
	}
	
	
	@GetMapping("/users/delete/{userId}")
	public String deleteUser(Model model, @PathVariable(name = "userId")Integer userId,RedirectAttributes redirectAttributes)  {
		boolean isTextSuccess = false;
		try {
			isTextSuccess = true;
			userService.deleteUserById(userId);
			redirectAttributes.addFlashAttribute("isTextSuccess",isTextSuccess);
			redirectAttributes.addFlashAttribute("message","Deleted user has id = " + userId);
		} catch (UserNotFoundException error) {
			isTextSuccess = false;
			redirectAttributes.addFlashAttribute("isTextSuccess",isTextSuccess);
			redirectAttributes.addFlashAttribute("message", error.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("users/{userId}/enabled/{status}")
	public String updateStatus(@PathVariable(name = "userId")Integer userId,@PathVariable(name = "status")boolean status,
		Model model, RedirectAttributes redirectAttributes) {
		boolean isTextSuccess = false;
		try {
			isTextSuccess = true;
			userService.updateStatus(userId,status);
			String isEnabled = status ? "User " + userId + " is enabled" : "User " + userId + " is disabled";
			redirectAttributes.addFlashAttribute("isTextSuccess",isTextSuccess);
			redirectAttributes.addFlashAttribute("message", isEnabled);
			
		} catch (UserNotFoundException error) {
			isTextSuccess = false;
			redirectAttributes.addFlashAttribute("isTextSuccess",isTextSuccess);
			redirectAttributes.addFlashAttribute("message", error.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("users/new")
	public String createNewUser(Model model) {
		List<Role> listRoles = userService.findAllRole();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageHeader", "Users - Create");
		
		return "users/users_form";
		
	}
	
	@GetMapping("users/edit/{id}")
	public String EditUser(@PathVariable(name="id")Integer id, RedirectAttributes redirectAttributes,Model model){

		try {
			User user = userService.findUserById(id);
			List<Role> listRoles = userService.findAllRole();
			model.addAttribute("user", user);
			model.addAttribute("listRoles",listRoles);
			return "users/users_form";
		} catch (UserNotFoundException e) {
			return defaultRedirectURL;
		}

	}
	
	@PostMapping("users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile multipartFile)throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			userService.saveUser(user);
			
			String uploadDir = "user-photo/" + user.getId(); 
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
		}else {
			if(user.getPhotos().isEmpty()){
				user.setPhotos(null);
			}
			userService.saveUser(user);
		}

		redirectAttributes.addFlashAttribute("message","User had save sucessfully");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException{
		List<User> listUsers = userService.findAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.findAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("users/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> listUsers = userService.findAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
}
