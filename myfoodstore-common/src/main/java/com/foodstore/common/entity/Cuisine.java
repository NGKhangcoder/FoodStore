package com.foodstore.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.foodstore.common.entity.product.Product;

@Entity
@Table(name = "cuisines")
public class Cuisine extends IdBasedEntity {
	@Column(nullable = false, length = 45, unique = true)
	private String name;

	@Column(nullable = false, length = 128)
	private String logo;

	@OneToMany(mappedBy="cuisine")
	private Set<Product> products = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "cuisines_categories", joinColumns = @JoinColumn(name = "cuisine_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	public Cuisine() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Cuisine [id=" + id + ", name=" + name + ", categories=" + categories + "]";
	}

	public String getLogoPath() {
		if (this.id == null)
			return "/images/image-thumbnail.png";
		return "/cuisine-photo/" + this.id + "/" + this.logo;
	}
}
