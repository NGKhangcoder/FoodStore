package com.foodstore.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodstore.common.entity.Role;
import com.foodstore.common.entity.User;
import com.foodstore.common.exception.UserNotFoundException;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	private int userPerPage = 5;
	public static final Integer USERS_PER_PAGE = 4;

	public int getUserPerPage() {
		return userPerPage;
	}

	public void setUserPerPage(int userPerPage) {
		this.userPerPage = userPerPage;
	}

	public List<User> findAll() {

		return (List<User>) userRepository.findAll(Sort.by("id").ascending());
	}
	
	public Page<User> listByPage(int pageNum,String sortField,String sortDir,String keyword) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1,userPerPage,sort);
		
		if(keyword != null) {
			return userRepository.findAllByKeyword(pageable,keyword);
		}

		return userRepository.findAll(pageable);
	}
	

	public void deleteUserById(Integer id) throws UserNotFoundException {

		Long countById = userRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		userRepository.deleteById(id);
	}

	public void updateStatus(Integer id, boolean status) throws UserNotFoundException {

		Long countById = userRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		userRepository.updateStatus(id, status);

	}

	public List<Role> findAllRole() {
		return (List<Role>) roleRepository.findAll();
	}

	public void saveUser(User user) {
		boolean isUserExisting = (user.getId() != null) ? true : false;

		if (isUserExisting) {
			User userExisted = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(userExisted.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		userRepository.save(user);

	}

	public User findUserById(Integer id) throws UserNotFoundException {

		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("User's not found");
		}

	}

	public boolean isDuplicatedEmail(Integer id, String email) {
		User userByEmail = userRepository.findByEmail(email);
		if (userByEmail == null) {
			return false;
		}
		boolean isCreatingNewUser = (id == null) ? true : false;
		if (isCreatingNewUser) {
			if (userByEmail != null) {
				return true;
			}
		}

		return false;
	}
	
	private void encodePassword(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}

	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}
	
	public void savedAccount(User userInForm) {
		User userInDB = userRepository.findById(userInForm.getId()).get();
		
		if(!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if(userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		userRepository.save(userInDB);
	}


}
