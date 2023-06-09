package com.foodstore.common.entity.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.foodstore.common.entity.Category;
import com.foodstore.common.entity.Cuisine;
import com.foodstore.common.entity.Customer;
import com.foodstore.common.entity.IdBasedEntity;
import com.foodstore.common.entity.Message;

@Entity
@Table(name = "products")
public class Product extends IdBasedEntity {

	@Column(unique = true, length = 255, nullable = false)
	private String name;

	@Column(unique = true, length = 255, nullable = false)
	private String alias;

	@Column(length = 1024, nullable = false, name = "short_description")
	private String shortDescription;

	@Column(length = 4092, nullable = false, name = "full_description")
	private String fullDescription;

	@Column(name = "main_image", nullable = false)
	private String mainImage;

	@Column(name = "created_time", nullable = false, updatable = false)
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;

	private float cost;

	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;

	private float gram;

	@ManyToOne
	@JoinColumn(name = "cuisine_id")
	private Cuisine cuisine;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy="product",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> message = new ArrayList<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductDetail> details = new HashSet<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<>();

	public Set<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<ProductDetail> details) {
		this.details = details;
	}

	public Set<ProductImage> getImages() {
		return images;
	}
	

	public List<Message> getMessage() {
	
		return this.message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}

	public Product(Integer id) {
		this.id = id;
	}

	public Product(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getGram() {
		return gram;
	}

	public void setGram(float gram) {
		this.gram = gram;
	}

	public Cuisine getCuisine() {
		return cuisine;
	}

	public void setCuisine(Cuisine cuisine) {
		this.cuisine = cuisine;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Transient
	public String getMainImagePath() {
		if (id == null || mainImage == null)
			return "/images/image-thumbnail.png";

		return "/product-images/" + this.id + "/" + this.mainImage;
	}

	public Product() {

	}

	public boolean containsImageName(String fileName) {
		Iterator<ProductImage> iterator = images.iterator();
		while (iterator.hasNext()) {
			ProductImage image = iterator.next();
			if (image.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	public void addExtraImage(String fileName) {
		images.add(new ProductImage(fileName, this));
	}

	public void addDetail(Integer id, String name, String value) {

		details.add(new ProductDetail(id, name, value, this));

	}

	public void addDetail(String name, String value) {
		details.add(new ProductDetail(name, value, this));

	}

	@Transient
	public float getDiscountPrice() {
		if (discountPercent > 0) {
			return price * ((100 - discountPercent) / 100);
		}
		return this.price;
	}

	@Transient
	public String getURI() {
		return "/p/" + this.alias + "/";
	}

	public void addMessage(String[] message) {
		
		
	}

	public void addMessageAndCustomer(String[] message, Customer customer) {
		
		
		for (String mess : message) {
			this.message.add(new Message(mess,this,customer));
		}
		
	}

	
}
