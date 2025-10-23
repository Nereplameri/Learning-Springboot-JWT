package com.alperenavci.controller;

import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;
import com.alperenavci.jwt.AuthResponse;

public interface IRestAuthController {
	public DtoUser register(AuthRequest request);
	public AuthResponse authenticate(AuthRequest request);
}
