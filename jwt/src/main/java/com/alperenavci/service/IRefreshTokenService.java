package com.alperenavci.service;

import com.alperenavci.jwt.AuthResponse;
import com.alperenavci.jwt.RefreshTokenRequest;

public interface IRefreshTokenService {
	public AuthResponse refreshToken(RefreshTokenRequest request);
}
