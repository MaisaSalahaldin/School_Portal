package com.SchoolPortal.SchoolPortal.Models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Student extends User{
    @NotBlank(message = "Phone number is required")
    private String ParentPhone;

    public String getParentPhone() {
        return ParentPhone;
    }

    public void setParentPhone(String parentPhone) {
        ParentPhone = parentPhone;
    }

    public Student(int id, String email, String pwHash, String firstName, String lastName, String parentPhone) {
        super( email, pwHash, firstName, lastName);
        ParentPhone = parentPhone;
    }
    public Student(){}

}
