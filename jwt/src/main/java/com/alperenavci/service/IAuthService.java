package com.alperenavci.service;

import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;
import com.alperenavci.jwt.AuthResponse;

public interface IAuthService {
	public DtoUser register(AuthRequest request);
	public AuthResponse authenticate(AuthRequest request);
}
