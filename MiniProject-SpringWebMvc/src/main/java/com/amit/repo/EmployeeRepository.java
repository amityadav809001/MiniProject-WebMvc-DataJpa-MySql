package com.amit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amit.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	@Query("select count(empName) from Employee where empName=:ename")
	public Integer getCountOfEmpName(String ename);
	
	
}
