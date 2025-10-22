package com.alperenavci.service;

import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;

public interface IAuthService {
	public DtoUser register(AuthRequest request);
}
