package org.example.demo.repository;

import org.example.demo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {}