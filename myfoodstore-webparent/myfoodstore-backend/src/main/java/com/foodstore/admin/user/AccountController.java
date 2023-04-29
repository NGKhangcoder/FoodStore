package com.foodstore.admin.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foodstore.admin.FileUploadUtil;
import com.foodstore.admin.security.FoodStoreUserDetails;
import com.foodstore.common.entity.User;

@Controller
public class AccountController {
	@Autowired
	private UserService userSevice;
	
	@GetMapping("/account")
	public String ViewDetails(@AuthenticationPrincipal FoodStoreUserDetails userLogger,Model model ) {
		String email = userLogger.getUsername();
		User user = userSevice.findByEmail(email);
		model.addAttribute("user",user);
		return "users/account_form";
	}
	
	@PostMapping("/account/update")
	public String updateUsers(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal FoodStoreUserDetails userLogger,@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			String uploadDir = "/user-photos" + user.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			userLogger.setImage(fileName);
		}else {
			if(user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
		}
		userSevice.savedAccount(user);
		
		userLogger.setFirstName(user.getFirstName());
		userLogger.setLastName(user.getLastName());
		
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
		return "redirect:/account";
	}
}
