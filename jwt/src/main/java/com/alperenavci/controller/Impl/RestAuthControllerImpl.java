package com.alperenavci.controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alperenavci.controller.IRestAuthController;
import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;
import com.alperenavci.jwt.AuthResponse;
import com.alperenavci.jwt.RefreshTokenRequest;
import com.alperenavci.service.IAuthService;
import com.alperenavci.service.IRefreshTokenService;

import jakarta.validation.Valid;

@RestController
public class RestAuthControllerImpl implements IRestAuthController{

	@Autowired
	private IAuthService authService;
	
	@Autowired
	private IRefreshTokenService refreshTokenService;
	
	
	@PostMapping("/register")
	@Override
	public DtoUser register(@Valid @RequestBody AuthRequest request) {
		
		return authService.register(request);
	}
	
	// SecurityConfig 'de filitre dışı URL 'si olmasına dikkat et.
	@PostMapping("/authenticate")
	@Override
	public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
		
		return authService.authenticate(request);
	}
	
	@PostMapping("/refreshToken")
	@Override
	public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
		
		return refreshTokenService.refreshToken(request);
	}

}
