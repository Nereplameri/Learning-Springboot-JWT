package com.alperenavci.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alperenavci.jwt.AuthEntryPoint;
import com.alperenavci.jwt.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	public static final String AUTHENTICATE = "/authenticate";
	public static final String REGISTER = "/register";
	public static final String REFRESHTOKEN = "/refreshToken";
	public static final String[] SWAGGER_PATH = {
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/swagger-ui.html"
	};
	
	// AppConfig 'in metodunun bean 'ını enjekte ettik
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	// JWTAuthenticationFilter class 'ının bean 'ını enjekte ettik
	@Autowired
	private JWTAuthenticationFilter authenticationFilter;
	
	@Autowired
	private AuthEntryPoint authEntryPoint;
	
	// Bütün konfigürasyonlar bu sınıfta yapılıyor.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeHttpRequests(request -> 
		request.requestMatchers(REGISTER ,AUTHENTICATE, REFRESHTOKEN) // authenticate ve register adresine bir istek gelirse
		.permitAll() // filitre katmanını görmezden gelerek al
		.requestMatchers(SWAGGER_PATH).permitAll()
		.anyRequest() //Bu işlemin dışında kalmışları ...
		.authenticated()) //... filitre içerisinden geçir.
		.exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider) // AppConfig sınıfı ile bağlantı
		.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class); // Oluşturduğumuz filter sınıfını kullanır
		
		return http.build();
	}
}
