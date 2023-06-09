package com.foodstore.client.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.foodstore.client.customer.CustomerService;
import com.foodstore.common.entity.AuthenticationType;
import com.foodstore.common.entity.Customer;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
		Customer customer = userDetails.getCustomer();//đối tượng customer
		
		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);
		
		//khi truy cập đến 1 trang nào đó bắt đăng nhập, sau khi đăng nhập thành công, nó tự động chuyển hướng đến trang tiếp theo
		//khi truy cập đến 1 trang mà ko bắt đăng nhập nhưng bấm đăng nhập, nó tự động chuyển hướng đến trang index
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	
}
