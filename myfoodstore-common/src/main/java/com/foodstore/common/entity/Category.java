package com.foodstore.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.foodstore.common.entity.product.Product;

@Table(name = "categories")
@Entity
public class Category extends IdBasedEntity {
	

	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	@Column(length = 128)
	private String alias;
	

	
	@Column(length = 64)
	private String image;

	private boolean enabled;

	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	
	@Transient
	private boolean hasChildren;
	
	public Category() {
		
	}
	public Category(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	

	public Category(String name) {
		this.name = name;
	}
	public String getAllParentIDs() {
		return allParentIDs;
	}
	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
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


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}
	public static Category copyFull(Category rootCategory) {
		Category copyCategory = new Category();
		copyCategory.setId(rootCategory.getId());
		copyCategory.setName(rootCategory.getName());
		copyCategory.setImage(rootCategory.getImage());
		copyCategory.setEnabled(rootCategory.isEnabled());
		copyCategory.setHasChildren(rootCategory.getChildren().size() > 0);
		return copyCategory;
	}

	public static Category copyFull(Category subCategory, String name) {
		Category copyCategory = Category.copyFull(subCategory);
		copyCategory.setName(name);
		return copyCategory;
	}
	
	@Transient
	public String getImagePath() {
		if (this.id == null)return "/images/image-thumbnail.png";
		return "/category-photo/" + this.id + "/" + this.image;
	}

	public String toString() {
		return this.name;
	}

	public static Category copyIdAndName(Category category) {
		return new Category(category.getId(),category.getName());
	}

	public static Category copyIdAndName(Integer id, String name) {
		return  new Category(id,name);
	}
	
}
