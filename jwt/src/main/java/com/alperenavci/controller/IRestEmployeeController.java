package com.alperenavci.controller;

import com.alperenavci.dto.DtoEmployee;

public interface IRestEmployeeController {
	public DtoEmployee findEmployeeById(Long id);
}
