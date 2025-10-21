package com.alperenavci.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	// Açtığımız "User implements UserDetails" sınıfını yönetebilmek için bu servisi kullanıyoruz.
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header;
		String token;
		String username;
		
		// Yapılan request 'in header bölümündeki "Authorization" kısmını tutar.
		header = request.getHeader("Authorization"); 
		// Header: "Bearer ??(token)??"
		
		// "Authorization" boş ise, tokeni yok demektir.
		if (header == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// Header str 'deki gereksiz "Bearer" yazısından kurtulduk
		token = header.substring(7);
		
		// Token içerisinden username değerini almak.
		try {
			username = jwtService.getUsernameByToken(token);
			// Username boş değilse ve Context 'in Authentication 'da Token değeri yoksa
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// loadUserByUsername metodu, veritabanında bu user var mı yok mu diye kontrol eder.
				// Varsa döner yoksa null döner.
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				// Database 'de token 'deki username var mı yok mu kontrolü
				// Token 'in son kullanım tarihi geçmiş mi kontrolü
				if (userDetailsService!=null && jwtService.isTokenExpired(token)) {
					// Kişi SecurityContext 'e alınır. Yani kişi Controller katmanına erişebilir.
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
					
					authentication.setDetails(userDetails);
					
					// Controller katmanına erişim sağlanır!
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					
					
				}
				
			}
		} catch (ExpiredJwtException e) { // token süresi biterse
			System.out.println("Token süresi dolmuştur: "+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Genel bir hata oluştu: " + e.getMessage());
		}
		
		// Süreci devam ettir. Thread 'ı kontroller katmanına yönlendirmek gibi düşün.
		filterChain.doFilter(request, response);
		
		
		
	}
	
	
}
