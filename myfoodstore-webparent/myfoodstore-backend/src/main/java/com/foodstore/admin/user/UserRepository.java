package com.foodstore.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import com.foodstore.common.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

//	@Query("DELETE FROM User u WHERE u.id = ?1")
//	@Modifying
//	void deleteById(Integer userId);

	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2  WHERE u.id = ?1")
	@Modifying
	void updateStatus(Integer userId, boolean status);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User findByEmail(@Param("email") String  email);

	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' '," + " u.lastName) LIKE %?1%")
	public Page<User> findAllByKeyword(Pageable pageable, String keyword);
	

}
