package com.foodstore.client.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.foodstore.common.entity.Country;
import com.foodstore.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
	
}
