package com.SchoolPortal.SchoolPortal.Data;

import com.SchoolPortal.SchoolPortal.Models.Student;
import com.SchoolPortal.SchoolPortal.Models.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
}
