package com.alperenavci.service.Impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;
import com.alperenavci.jwt.AuthResponse;
import com.alperenavci.jwt.JwtService;
import com.alperenavci.model.User;
import com.alperenavci.repository.UserRepository;
import com.alperenavci.service.IAuthService;

@Service
public class AuthServiceImpl implements IAuthService{

	@Autowired
	private UserRepository userRepository;
	
	// Doğrudan şifreyi database 'ye kaydetmiyoruz.
	// AppConfig 'te bean 'ı oluşturduğumuz BCryptPasswordEncoder 'yı kullanıyoruz
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtService jwtService;

	@Override
	public AuthResponse authenticate(AuthRequest request) {
		// gelen kullanıcı-şifre 'nin doğruluğunu kontrol et
		try {
			UsernamePasswordAuthenticationToken auth = 
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
			authenticationProvider.authenticate(auth);
			// Eğer kullanıcıadı - şifre doğru ise thread devam, değilse catch bloğunda
			// Kullanıcıya token vermeliyiz.
			
			Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
			jwtService.generateToken(optionalUser.get());
			String token = jwtService.generateToken(optionalUser.get());
			
			return new AuthResponse(token);
			
		} catch (Exception e) {
			// Normalde exception ile ilgilenmen gerekir.
			System.out.println("Kullanıcı adı veya Şifre hatalı: " + e.getMessage());
		}
		return null;
	}
	
	
	@Override
	public DtoUser register(AuthRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		User savedUser = userRepository.save(user);
		
		DtoUser dto = new DtoUser();
		BeanUtils.copyProperties(savedUser, dto);
		
		return dto;
	}


}
