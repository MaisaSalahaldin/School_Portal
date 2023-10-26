package com.SchoolPortal.SchoolPortal.Models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Teacher extends User{


public Teacher(){

}
    public Teacher(String email, String pwHash, String firstName, String lastName, String phone,Boolean status) {
        super(email, pwHash, firstName, lastName,phone,status);

    }


}
