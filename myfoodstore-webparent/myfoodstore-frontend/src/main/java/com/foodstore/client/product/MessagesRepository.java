package com.foodstore.client.product;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.foodstore.common.entity.Message;


interface MessagesRepository  extends CrudRepository<Message, Integer> {




	
	@Query("Select m from Message m where m.product.id = ?1 ")
	List<Message> findByProductId(Integer id);

}
