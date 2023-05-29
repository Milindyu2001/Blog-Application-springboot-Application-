package com.blog.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exception.ApiException;
import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.UserDto;
import com.blog.payloads.jwtAuthRespons;
import com.blog.security.JwtTokenHepler;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/")

public class AuthController 
{
	@Autowired
	private JwtTokenHepler jwtTokenHepler;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
     @PostMapping("/login")
	 public ResponseEntity<jwtAuthRespons> createToken(@RequestBody JwtAuthRequest request) throws Exception{
		
		 this.authenticate(request.getUsername(),request.getPassword());
	     UserDetails userDetails=this.userDetailsService.loadUserByUsername(request.getUsername());
	     String token=this.jwtTokenHepler.generateToken(userDetails);
		 jwtAuthRespons respons=new jwtAuthRespons();
	     respons.setToken(token);
	    return new ResponseEntity<jwtAuthRespons>(respons,HttpStatus.OK);
	 }



	private void authenticate(String username, String password) throws Exception {
		// TODO Auto-generated method stub

		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		}catch (BadCredentialsException e) {
			// TODO: handle exception
			System.out.println("invalid Details");
			throw new ApiException("Invalid Username or Password");
			
		}
		
		
	}
	
	//Register New User API
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto)
	{
		
	UserDto registerUser=this.userService.registerNewUser(userDto);
	return new ResponseEntity<UserDto>(registerUser,HttpStatus.CREATED);
	
	}
	
}
