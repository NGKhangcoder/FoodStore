package com.foodstore.admin.cuisine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodstore.common.entity.Category;
import com.foodstore.common.entity.Cuisine;
import com.foodstore.common.entity.dto.CategoryDTO;
import com.foodstore.common.exception.CuisineNotFoundException;

@RestController
public class CuisineRestController {

	@Autowired
	private CuisineService cuisineService;
	
	@PostMapping("/cuisines/check_unique")
	public String checkUnique(Integer id, String name) {
		return cuisineService.checkUnique(id, name);
	}
	
	@GetMapping("/cuisines/{id}/categories")
	public List<CategoryDTO> listCategoiresByCuisine(@PathVariable("id")Integer id) throws CuisineNotFoundException{
		List<CategoryDTO> listCategories = new ArrayList<>();
		Cuisine cuisine = cuisineService.findById(id);
		for (Category category : cuisine.getCategories()) {
			CategoryDTO dto = new CategoryDTO(category.getId(),category.getName());
			listCategories.add(dto);
		}
		return listCategories;
	}
}
