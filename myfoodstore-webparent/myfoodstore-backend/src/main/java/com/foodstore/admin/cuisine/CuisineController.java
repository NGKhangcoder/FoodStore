package com.foodstore.admin.cuisine;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foodstore.admin.FileUploadUtil;
import com.foodstore.admin.category.CategoryService;
import com.foodstore.common.entity.Cuisine;
import com.foodstore.common.exception.CuisineNotFoundException;

@Controller
public class CuisineController {
	
	private String defaultRedirectURL = "redirect:/cuisines/page/1?sortField=id&sortDir=asc";
	
	@Autowired
	private CuisineService cuisineService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/cuisines")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/cuisines/page/{pageNumb}")
	public String listPerPage(@PathVariable("pageNumb")int pageNumb,@Param("keyword")String keyword,Model model,
			@RequestParam("sortField")String sortField,@RequestParam("sortDir")String sortDir) {
	
		Page<Cuisine> page = cuisineService.listPerPage(pageNumb,sortDir,sortField,keyword);
		List<Cuisine> listCuisines = page.getContent();
		long startCount = (pageNumb - 1) * CuisineService.CUISINES_PER_PAGE + 1;
		long endCount = startCount + CuisineService.CUISINES_PER_PAGE - 1;
		if(page.getTotalPages() < endCount) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNumb);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listCuisines", listCuisines);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "cuisines/cuisines";
	}
	
	@GetMapping("/cuisines/new")
	public String createNew(Model model) {
		
		model.addAttribute("cuisine",new Cuisine());
		model.addAttribute("listCategory",categoryService.listCategoriesUsedInForm());
		
		return "cuisines/cuisines_form";
	}
	
	@GetMapping("cuisines/delete/{id}")
	public String deleteCuisine(RedirectAttributes redirectAttributes, @PathVariable("id")Integer id) {
		try {
			cuisineService.deleteById(id);
			FileUploadUtil.cleanDir("../cuisine-logo/" + id );
			redirectAttributes.addFlashAttribute("message", "The Cuisine ID " + id + " has been deleted successfully");
			
		} catch (CuisineNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}
	@GetMapping("cuisines/edit/{id}")
	public String editCuisine(@PathVariable("id")Integer id,Model model) {
		try {
			Cuisine cuisine = cuisineService.findById(id);
			
			model.addAttribute("cuisine",cuisine );
			model.addAttribute("listCategory",categoryService.listCategoriesUsedInForm());
			return "cuisines/cuisines_form";
		} catch (CuisineNotFoundException e) {
			return defaultRedirectURL;
		}
	
	}
	
	@PostMapping("/cuisines/save")
	public String createNew(Cuisine cuisine,@RequestParam("fileImage")MultipartFile multipartFile,RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = multipartFile.getOriginalFilename();
			cuisine.setLogo(fileName);
			Cuisine newCuisine = cuisineService.save(cuisine);
			String dir = "../cuisine-photo/" + newCuisine.getId();
			FileUploadUtil.cleanDir(dir);
			FileUploadUtil.saveFile(dir, fileName, multipartFile);
		}else {
			if(cuisine.getLogo().isEmpty()) {
				cuisine.setLogo(null);
			}
			cuisineService.save(cuisine);
		}
		redirectAttributes.addFlashAttribute("message", "The Cuisine has been saved successfully.");
		return defaultRedirectURL;
	}

}
