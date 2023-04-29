package com.foodstore.client.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodstore.common.entity.CartItem;
import com.foodstore.common.entity.product.Product;



@Service
public class CheckoutService {
	
	private static final int DIM_DIVISOR = 139;

	public CheckoutInfo prepareCheckout(List<CartItem> cartItems) {//tính toán tất cả chi phí
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems);
		float productTotal = calculateProductTotal(cartItems);
		float shippingCostTotal = calculateShippingCost(cartItems);
		float paymentTotal = productTotal + shippingCostTotal;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		
		return checkoutInfo;
	}

	private float calculateShippingCost(List<CartItem> cartItems) {
		float shippingCostTotal = 0.0f;
		
		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			float dimWeight = product.getGram() / DIM_DIVISOR;
			float finalWeight = product.getGram() > dimWeight ? product.getGram() : dimWeight;
			float shippingCost = finalWeight * item.getQuantity();
			
			item.setShippingCost(shippingCost);
			
			shippingCostTotal += shippingCost;//tổng tiền ship dựa trên chiều dài, chiều rộng, chiều cao
		}
		
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
		float total = 0.0f;
		
		for (CartItem item : cartItems) {
			total += item.getSubtotal();//tổng = (price của mỗi product sau khi đã giảm giá) x số lượng mỗi product
		}
		
		return total;
	}

	private float calculateProductCost(List<CartItem> cartItems) {
		float cost = 0.0f;
		
		for (CartItem item : cartItems) {
			cost += item.getQuantity() * item.getProduct().getCost();//tổng = (cost của mỗi product) x số lượng mỗi product
		}
		
		return cost;
	}
}
