package com.alperenavci.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	public static final String SECRET_KEY = "5N+6yAw9UJlZGIE3ivXxkQlxnb9BauSkvcdSJ447DQE=";
	
	// Token üretir
	public String generateToken(UserDetails userDetails) {
		
		//Token 'e Map eklemeye örnek:
		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("role", "ADMIN");
		
		
		// Jwts: dependency ile gelen bir sınıf
		// builder, sınıfın nesnesini oluşturur. 
		// Diğer metotlar sürekli kendi sınıfını döndüğü için zincirleme set yapılıyor.
		return Jwts.builder()
		.setSubject(userDetails.getUsername()) // Token içindeki Username 'yi set eder.
		.addClaims(claimsMap) // Token 'e map eklemek
		.setIssuedAt(new Date()) // Tokenin oluşturulma zamanına erişip Jwts 'ye atar.
		.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2)) // Token bitiş süresidir. milisaniye cinsinden girilir.
		.signWith(getKey(), SignatureAlgorithm.HS256) //Token oluşurken ve çözerken kullanılan bir key 'e ihtiyaç var.
		.compact();
	}
	
	public void test() {
		//suni bir test sınıfıdır.
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbHBlcmVuIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzYxMjE4NTgyLCJleHAiOjE3NjEyMjU3ODJ9.eDTmHoNF2UykQBvCjca3xFSBC_hfu36SaHHZLMo-fek";
		String key = "role";
		Object value= getClaimsByKey(token, key);
		System.out.println(value);
	}
	
	// Token + token içi istenilen JSON map 'ı verildiğinde key 'i veriyor
	public Object getClaimsByKey(String token, String key) {
		Claims claims = getClaims(token);
		return claims.get(key);
		
	}
	
	// Token 'den body kısmını çeker.
	public Claims getClaims(String token) {
		Claims claims = Jwts.parserBuilder()
		.setSigningKey(getKey()) // Key ile çözme işlemi
		.build() // set zincirini kırıp bir alttaki koda uygun return yapıyor.
		.parseClaimsJws(token).getBody(); // Tokenin body 'sini alır.
		
		return claims;
	}
	
	//Token çözer
	// Function<> sınıfı, bir metodun pointerini verip, kod içinde sonradan kullanmak diye düşün.
	public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
		Claims claims = getClaims(token);
		
		return claimsFunction.apply(claims); // Alınan token body 'i çözer.
	}
	
	
	
	// Java.security 'nin. .signWith 'in tanımlı olduğu 
	// sınıfın metotuna gidip ihtiyacım olan paketi öğrendim.
	public Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes); // Byte dizisini anahtara çevirir
	}
	
	// Token içerisinden username 'yi al.
	public String getUsernameByToken(String token) {
		return exportToken(token, Claims::getSubject);
	}
	
	// Token expired oldu mu?
	public boolean isTokenExpired(String token) {
		Date expiredDate = exportToken(token, Claims::getExpiration);
		
		// Bugünki zaman, expiredDate 'den küçükse hala geçerlidir.
		return new Date().before(expiredDate);
	}
	
}
