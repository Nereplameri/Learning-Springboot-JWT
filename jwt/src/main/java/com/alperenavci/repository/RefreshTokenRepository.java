package com.alperenavci.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alperenavci.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	
	@Query(value = "from RefreshToken r WHERE r.refreshToken = :refreshToken")
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
