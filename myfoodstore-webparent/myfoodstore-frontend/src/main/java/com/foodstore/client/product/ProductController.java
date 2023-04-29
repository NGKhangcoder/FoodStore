package com.foodstore.client.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodstore.client.category.CategoryService;
import com.foodstore.client.customer.CustomerRepository;
import com.foodstore.client.security.CustomerUserDetails;
import com.foodstore.common.entity.Category;
import com.foodstore.common.entity.Customer;
import com.foodstore.common.entity.Message;
import com.foodstore.common.entity.product.Product;
import com.foodstore.common.exception.CategoryNotFoundException;
import com.foodstore.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
	@Autowired private ProductService productService;
	@Autowired private CategoryService categoryService;
	@Autowired private CustomerRepository customerRepository;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
			Model model) {
		return viewCategoryByPage(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") int pageNum,
			Model model) {
		try {
			Category category = categoryService.getCategory(alias);		
			List<Category> listCategoryParents = categoryService.getCategoryParents(category);//lấy ra tất cả categories cha,ông,...của category hiện tại
			
			Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());//lấy ra tất cả products thuộc về category hiện tại và tất cả products thuộc về categories con,cháu,... của category hiện tại
			List<Product> listProducts = pageProducts.getContent();
			
			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}
			
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("category", category);
		
			
			return "product/products_by_category";
		} catch (CategoryNotFoundException ex) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model,
			HttpServletRequest request,@AuthenticationPrincipal CustomerUserDetails loggedUser) {
		try {
			Product product = productService.getProduct(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());//lấy ra tất cả categories cha,ông,...của category hiện tại
			List<Message> listMessages = product.getMessage();
			
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("listMessages", listMessages);
		
			
			
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String searchFirstPage(String keyword, Model model) {
		return searchByPage(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(String keyword, @PathVariable("pageNum") int pageNum, Model model) {
		Page<Product> pageProducts = productService.search(keyword, pageNum);
		List<Product> listResult = pageProducts.getContent();
		
		long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", keyword + " - Search Result");
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("searchKeyword", keyword);
		model.addAttribute("listResult", listResult);
		
		return "product/search_result";
	}	
	
	


	@PostMapping("/product/message")
	public String message(Product product,@RequestParam("Newmessage")String[] message,Model model, @AuthenticationPrincipal CustomerUserDetails loggedUser) {
		product =  productService.findById(product.getId());
		Customer customer;
		customer = customerRepository.findByEmail(loggedUser.getUsername());
		product.addMessageAndCustomer(message,customer);
		productService.save(product);
	
		Product newProduct =  product;
		List<Category> listCategoryParents = categoryService.getCategoryParents(newProduct.getCategory());//lấy ra tất cả categories cha,ông,...của category hiện tại
		List<Message> listMessages = newProduct.getMessage();
		
		model.addAttribute("listCategoryParents", listCategoryParents);
		model.addAttribute("product",newProduct);
		model.addAttribute("listMessages", listMessages);
	
		return "product/product_detail";
	}	
	
}
