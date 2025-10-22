package com.alperenavci.service;

import com.alperenavci.dto.DtoEmployee;

public interface IEmployeeService {
	public DtoEmployee findEmployeeById(Long id);
}
