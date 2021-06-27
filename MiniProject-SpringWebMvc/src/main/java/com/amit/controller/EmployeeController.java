package com.amit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amit.exception.EmployeeNotFoundException;
import com.amit.model.Employee;
import com.amit.service.IEmployeeService;


@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	private IEmployeeService service;

	@RequestMapping("/register")
	public String showReg() {
		log.info("entered into the showReg()");
		return "EmployeeRegister";
	}

	@RequestMapping("/save")
	public String saveEmp(@ModelAttribute Employee employee, Model model)

	{
		log.info("entered into the saveEmp()..");
		try {
			Integer id = service.saveEmployee(employee);
			String message = "Employee" + " " + id + " " + "created!!";
			model.addAttribute("message", message);
			log.debug(message);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Problem in saveEmp()"+e.getMessage());
		}
		
		log.info("About to leave saveEmp()");
		return "EmployeeRegister";

	}

	@GetMapping("/all")
	public String getAllEmps(@PageableDefault(page = 0, size = 5) Pageable pageable, Model model) {
		commonDataFetch(model, pageable);
		return "EmployeeData";
	}

	private void commonDataFetch(Model model, Pageable pageable) {

		Page<Employee> page = service.getAllEmployees(pageable);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getContent());
	}

	@GetMapping("/delete")
	public String deleteById(@RequestParam Integer id, Model model) {
		String message = null;
		try {
			service.deleteEmployee(id);
			message = "Employee" + " " + id + " " + "Deleted!!";
		} catch (EmployeeNotFoundException e) {
			e.printStackTrace();
			message = e.getMessage();
		}

		model.addAttribute("message", message);
		commonDataFetch(model, PageRequest.of(0, 5));

		return "EmployeeData";

	}

	@GetMapping("/edit")
	public String showEdit(@RequestParam Integer id, Model model) {

		String page = null;

		try {
			Employee emp = service.getOneEmployee(id);
			model.addAttribute("employee", emp);
			page = "EmployeeEdit";
		} catch (EmployeeNotFoundException e) {
			e.printStackTrace();
			page = "EmployeeData";
			commonDataFetch(model, PageRequest.of(0, 5));
			model.addAttribute("message", e.getMessage());
		}
		return page;

	}

	@PostMapping("/update")
	public String doUpdate(@ModelAttribute Employee employee, Model model) {
		service.updateEmployee(employee);
		String message = "Employee" + " " + employee.getEmpId() + " " + "Updated!!";

		model.addAttribute("message", message);
		commonDataFetch(model, PageRequest.of(0, 5));

		return "EmployeeData";

	}

	@GetMapping("/validate")
	public @ResponseBody String validateEname(@RequestParam String ename) {
		String message = "";
		if (service.isEmployeeExistByName(ename)) {
			message = ename + " " + ",Already Exist!!";
		}
		return message;

	}

}
