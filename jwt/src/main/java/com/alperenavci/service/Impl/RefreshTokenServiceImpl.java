package com.alperenavci.service.Impl;

import java.security.PublicKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alperenavci.jwt.AuthResponse;
import com.alperenavci.jwt.JwtService;
import com.alperenavci.jwt.RefreshTokenRequest;
import com.alperenavci.model.RefreshToken;
import com.alperenavci.model.User;
import com.alperenavci.repository.RefreshTokenRepository;
import com.alperenavci.service.IRefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService{

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireDate(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 4));
		refreshToken.setUser(user);
		
		return refreshToken;
	}
	
	public boolean isRefreshTokenExpired(Date expiredDate) {
		return new Date().before(expiredDate);
	}
	
	
	
	@Override
	public AuthResponse refreshToken(RefreshTokenRequest request) {
		
		Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
		
		// gelen refreshtoken, database 'deki ile eşleşiyor mu diye kontrol
		if (optional.isEmpty()) {
			// Exception attığımızı varsay
			System.out.println("RefreshToken Geçersizdir.");
		}
		
		RefreshToken refreshToken = optional.get();
		
		// Token 'in süresini kontrol ediyoruz.
		if(!isRefreshTokenExpired(refreshToken.getExpireDate())) {
			System.out.println("RefreshToken expaired olmuş" + request.getRefreshToken());
		}
		
		// Problem yok ise accessToken + refreshToken 'i yenile
		// Aynı zamanda refreshToken 'i database ye kaydetmeliyiz.
		
		String token = jwtService.generateToken(refreshToken.getUser());
		
		RefreshToken newRefreshToken = createRefreshToken(refreshToken.getUser());
		
		refreshTokenRepository.save(newRefreshToken);
		
		return new AuthResponse(token, newRefreshToken.getRefreshToken());
	}
	
}
