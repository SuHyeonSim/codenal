package com.codenal.employee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void registerEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeDto.toEntity();
        employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long empId) {
        return employeeRepository.findByEmpId(empId);
    }

    // USER 권한이면서 본인을 제외한 재직중인 직원 모두 조회
    public List<EmployeeDto> getActiveEmployeeList(String username) {
        Long empId = Long.parseLong(username);
        List<Employee> empList = employeeRepository.findAllActiveEmployees(empId);
        List<EmployeeDto> dtoList = new ArrayList<>();
        for (Employee e : empList) {
            EmployeeDto dto = EmployeeDto.fromEntity(e);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public EmployeeDto getEmployeeDtoById(Long empId) {
        Employee employee = employeeRepository.findByEmpId(empId);
        if (employee != null) {
            return EmployeeDto.fromEntity(employee);
        }
        return null;
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}

