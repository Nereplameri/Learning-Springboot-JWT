package com.alperenavci.service.Impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alperenavci.dto.DtoDepartment;
import com.alperenavci.dto.DtoEmployee;
import com.alperenavci.model.Employee;
import com.alperenavci.repository.EmployeeRepository;
import com.alperenavci.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService{
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public DtoEmployee findEmployeeById(Long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		
		if (optional.isEmpty()) {
			//Normalde exception fırlatılır, ama konumuz olmadığından null dönsün
			return null;
		}
		
		Employee employee = optional.get();
		DtoEmployee dtoEmployee = new DtoEmployee();
		
		BeanUtils.copyProperties(employee, dtoEmployee);
		
		DtoDepartment dtoDepartment = new DtoDepartment();
		BeanUtils.copyProperties(employee.getDepartment(), dtoDepartment);
		
		dtoEmployee.setDepartment(dtoDepartment);
		
		return dtoEmployee;
	}
	
	
}
