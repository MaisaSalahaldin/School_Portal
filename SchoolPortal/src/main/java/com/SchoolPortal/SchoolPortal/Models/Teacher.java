package com.SchoolPortal.SchoolPortal.Models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Teacher extends User{
    @NotBlank(message = "Phone number is required")
    private String phone;
    @NotNull(message="Status is required.")
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
public Teacher(){

}
    public Teacher(String email, String pwHash, String firstName, String lastName, String phone,Boolean status) {
        super(email, pwHash, firstName, lastName);
        this.phone=phone;
        this.status=status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
