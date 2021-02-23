package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    //Optional<Schedule> findByActivitiesContaining_activitiesAndPetsContaining_petsAndEmployeesContaining_employeesAndDateEquals_date(Set<EmployeeSkill> activities, Set<Pet> pets, Set<Employee> employees, LocalDate date);
    List<Schedule> findAllByEmployeesContaining(Employee employee);
    List<Schedule> findAllByPetsContaining(Pet pet);
    List<Schedule> findAllByPetsIn(List<Pet> pets);
}
