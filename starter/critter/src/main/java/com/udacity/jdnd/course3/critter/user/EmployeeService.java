package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeDTO save(EmployeeDTO employeeDTO){
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        employee = employeeRepository.save(employee);
        return convertEntityToEmployeeDTO(employee);
    }

    public EmployeeDTO getEmployeeById(Long id){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            return convertEntityToEmployeeDTO(optionalEmployee.get());
        }
        else throw new NullPointerException();
    }

    public void setEmployeeAvailability(Long id, Set<DayOfWeek> daysAvailable){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            Employee employee = optionalEmployee.get();
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        }
        else throw new NullPointerException();
    }

    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO){
        Set<EmployeeSkill> employeeSkills = employeeRequestDTO.getSkills();
        DayOfWeek availability = employeeRequestDTO.getDate().getDayOfWeek();
        List<Employee> employees = employeeRepository.findAllByDaysAvailableContaining(availability);
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for(Employee employee : employees){
            if(employee.getSkills().containsAll(employeeSkills)){
                employeeDTOs.add(convertEntityToEmployeeDTO(employee));
            }
        }
        return employeeDTOs;
    }

    private static EmployeeDTO convertEntityToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private static Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
