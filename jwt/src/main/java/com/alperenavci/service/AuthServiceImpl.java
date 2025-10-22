package com.alperenavci.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;
import com.alperenavci.model.User;
import com.alperenavci.repository.UserRepository;

@Service
public class AuthServiceImpl implements IAuthService{

	@Autowired
	private UserRepository userRepository;
	
	// Doğrudan şifreyi database 'ye kaydetmiyoruz.
	// AppConfig 'te bean 'ı oluşturduğumuz BCryptPasswordEncoder 'yı kullanıyoruz
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
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
