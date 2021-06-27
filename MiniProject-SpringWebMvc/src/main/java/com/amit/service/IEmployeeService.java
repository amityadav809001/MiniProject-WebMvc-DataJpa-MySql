package com.amit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.amit.model.Employee;

public interface IEmployeeService {

	Integer saveEmployee(Employee e);

	void deleteEmployee(Integer id);
	
	void updateEmployee(Employee e);

	Employee getOneEmployee(Integer id);

	Page<Employee> getAllEmployees(Pageable page);
	
	boolean isEmployeeExistByName(String ename);

}
