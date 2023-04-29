package com.foodstore.admin.cuisine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.foodstore.common.entity.Cuisine;

public interface CuisineRepository extends PagingAndSortingRepository<Cuisine, Integer> {

	@Query("SELECT c FROM Cuisine c WHERE c.name LIKE %?1%")
	Page<Cuisine> findAll(String keyword, Pageable pageable);

	Cuisine findByName(String name);

}
