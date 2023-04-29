package com.foodstore.admin.shippingrate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodstore.admin.product.ProductRepository;
import com.foodstore.common.entity.product.Product;



@Service
@Transactional
public class ShippingRateService {
	
	@Autowired private ProductRepository productRepo;
	
	public float calculateShippingCost(Integer productId) {
		Product product = productRepo.findById(productId).get();
		return 45;
	}
}
