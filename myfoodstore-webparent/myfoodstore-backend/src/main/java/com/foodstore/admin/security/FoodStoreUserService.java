package com.foodstore.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.foodstore.admin.user.UserRepository;
import com.foodstore.common.entity.User;

public class FoodStoreUserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if(user != null) {
			return new FoodStoreUserDetails(user);
		}
		throw new UsernameNotFoundException("User does not exsit " + username) ;
	}

	
}
