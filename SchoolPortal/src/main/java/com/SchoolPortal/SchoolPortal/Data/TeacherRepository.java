package com.SchoolPortal.SchoolPortal.Data;

import com.SchoolPortal.SchoolPortal.Models.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    Teacher findByEmail(String email);
}
