package com.alperenavci.controller;

import com.alperenavci.dto.DtoUser;
import com.alperenavci.jwt.AuthRequest;

public interface IRestAuthController {
	public DtoUser register(AuthRequest request);
}
