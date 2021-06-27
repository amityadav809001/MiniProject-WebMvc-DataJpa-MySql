package com.amit.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amit.exception.EmployeeNotFoundException;
import com.amit.model.Employee;
import com.amit.repo.EmployeeRepository;
import com.amit.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private EmployeeRepository erepo;

	public Integer saveEmployee(Employee e) {

		calculateEmp(e);

		var id = erepo.save(e).getEmpId();

		return id;
	}

	private void calculateEmp(Employee e) {
		var sal = e.getEmpSal();
		var hra = sal * 20 / 100;
		var ta = sal * 10 / 100;

		e.setEmpHra(hra);
		e.setEmpTa(ta);

	}

	public Page<Employee> getAllEmployees(Pageable pageable) {

		Page<Employee> page = erepo.findAll(pageable);

		return page;
	}

	public void deleteEmployee(Integer id) {
		erepo.delete(getOneEmployee(id));

	}

	public Employee getOneEmployee(Integer id) {
		Optional<Employee> opt = erepo.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new EmployeeNotFoundException("EMPLOYEE" + " " + id + " " + "NOT EXIST!!");
		}

	}

	public void updateEmployee(Employee e) {

		if (erepo.existsById(e.getEmpId())) {
			calculateEmp(e);
			erepo.save(e);
		}

	}

	@Override
	public boolean isEmployeeExistByName(String ename) {

		return erepo.getCountOfEmpName(ename) > 0;
	}

}
