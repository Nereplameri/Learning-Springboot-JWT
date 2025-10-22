package com.alperenavci.controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alperenavci.controller.IRestEmployeeController;
import com.alperenavci.dto.DtoEmployee;
import com.alperenavci.service.IEmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/employee")
public class RestEmployeeControllerImpl implements IRestEmployeeController{
	
	@Autowired
	private IEmployeeService employeeService;
	
	@GetMapping("/{id}")
	@Override
	public DtoEmployee findEmployeeById(@Valid @NotEmpty @PathVariable(value = "id") Long id) {
		return employeeService.findEmployeeById(id);
	}
	
}
