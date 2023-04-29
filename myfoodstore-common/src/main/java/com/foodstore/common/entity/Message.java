package com.foodstore.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.foodstore.common.entity.product.Product;

@Entity
@Table(name = "messages")
public class Message extends IdBasedEntity {

	@Column(length = 4096)
	private String content;

	@ManyToOne
	@JoinColumn(name = "customer_id",nullable = true)
	private Customer customer;

	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;

	public Message() {

	}
	
	
	

	public Message(String content) {
		this.content = content;
	}

	public Message(String mess, Product product,Customer customer) {
		this.content = mess;
		this.product = product;
		this.customer = customer;
	}




	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	

}
