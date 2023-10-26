package com.SchoolPortal.SchoolPortal.Models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Student extends User{


    public Student( String email, String pwHash, String firstName, String lastName, String phone,Boolean status) {
        super( email, pwHash, firstName, lastName,phone,status);

    }
    public Student(){}

}
