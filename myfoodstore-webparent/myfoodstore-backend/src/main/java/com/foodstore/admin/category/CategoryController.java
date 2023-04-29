
package com.foodstore.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foodstore.admin.FileUploadUtil;
import com.foodstore.common.entity.Category;
import com.foodstore.common.exception.CategoryNotFoundException;




@Controller
public class CategoryController {
	private String defaultRedirectURL = "redirect:/categories";

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(String sortDir, Model model) {
		return listByPage("id","asc",1,null,model);
	}
	@GetMapping("/categories/page/{pageNum}")
	 public String listByPage(@Param("sortField")String sortField,
				@Param("sortDir")String sortDir,
				@PathVariable("pageNum")int pageNumb,
				@Param("keyword")String keyword,Model model) {
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = categoryService.listByPage(pageInfo,pageNumb,sortDir,keyword);
		
		long startCount = (pageNumb - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if(endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNumb);
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortField", "id");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "categories/categories";
	}
	

	@GetMapping("/categories/new")
	public String createNew(Model model) {
		model.addAttribute("category",new Category());
		model.addAttribute("listParents", categoryService.listCategoriesUsedInForm());
		return "categories/categories_form";
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(@RequestParam("fileImage")MultipartFile multipartFile,Category category,RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = categoryService.save(category);
			String dir = "../category-photo/" + savedCategory.getId();
			
			FileUploadUtil.cleanDir(dir);
			FileUploadUtil.saveFile(dir, fileName, multipartFile);
		}else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
		return defaultRedirectURL;
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(RedirectAttributes ra,Model model,@PathVariable("id")Integer id) {
		try {
			model.addAttribute("category", categoryService.get(id));
			model.addAttribute("listParents",categoryService.listCategoriesUsedInForm());
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			
			return "categories/categories_form";
		} catch (CategoryNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(RedirectAttributes ra,@PathVariable("id")Integer id) {
		try {
			categoryService.delete(id);
			String dir = "../category-photo/"  + id;
			FileUploadUtil.removeDir(dir);
		
			ra.addFlashAttribute("message","Deleted Category id: "+ id+ " Successed");
			return defaultRedirectURL;
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
		
	
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
	
}
