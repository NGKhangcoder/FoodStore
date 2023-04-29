package com.foodstore.admin.cuisine;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodstore.common.entity.Cuisine;
import com.foodstore.common.exception.CuisineNotFoundException;


@Service
@Transactional
public class CuisineService {
	@Autowired
	private CuisineRepository cuisineRepository;
	
	public static final int CUISINES_PER_PAGE = 10;

	public Page<Cuisine> listPerPage(int pageNumb, String sortDir, String sortField, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNumb - 1, CUISINES_PER_PAGE,sort);
		
		if(keyword != null) return cuisineRepository.findAll(keyword,pageable);
		
		return cuisineRepository.findAll(pageable);
		
	}

	public void updateCategoryEnabledStatus(Integer id, boolean status) {
		
		
		
	}

	public void deleteById(Integer id) throws CuisineNotFoundException{
		try {
			cuisineRepository.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new CuisineNotFoundException("Id: " + id + " Doesnt exist");
		}
		
	}

	public Cuisine save(Cuisine cuisine) {
		// TODO Auto-generated method stub
		return cuisineRepository.save(cuisine);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Cuisine cuisineName = cuisineRepository.findByName(name);

		if (isCreatingNew) {
			if (cuisineName != null)
				return "Duplicate";
		} else {
			if (cuisineName != null && cuisineName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}

	public Cuisine findById(Integer id) throws CuisineNotFoundException{
		
		return cuisineRepository.findById(id).get();
	}

	public List<Cuisine> listAll() {
		// TODO Auto-generated method stub
		return (List<Cuisine>) cuisineRepository.findAll();
	}
}
