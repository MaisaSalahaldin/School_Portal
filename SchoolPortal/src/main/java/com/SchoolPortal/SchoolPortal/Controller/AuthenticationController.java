package com.SchoolPortal.SchoolPortal.Controller;

import com.SchoolPortal.SchoolPortal.Data.StudentRepository;
import com.SchoolPortal.SchoolPortal.Data.TeacherRepository;
import com.SchoolPortal.SchoolPortal.Models.DTO.RegisterFormTeachertDTO;
import com.SchoolPortal.SchoolPortal.Models.Student;
import com.SchoolPortal.SchoolPortal.Models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {
@Autowired
    TeacherRepository teacherRepository;
@Autowired
    StudentRepository studentRepository;

    // The key to store user IDs
    private static final String teacherSessionKey = "teacher";
    private static final String studentSessionKey = "student";
    public Teacher getTeacherFromSession(HttpSession session) {
        Integer teacherId = (Integer) session.getAttribute(teacherSessionKey);
        if (teacherId == null) {
            return null;
        }

        Optional<Teacher> teacher = teacherRepository.findById(teacherId);

        if (teacher.isEmpty()) {
            return null;
        }

        return teacher.get();
    }
    // Stores key/value pair with session key and user ID

    private static void setTeacherInSession(HttpSession session, Teacher teacher) {
        session.setAttribute(teacherSessionKey, teacher.getId());
    }

    public boolean studentInSession(HttpSession session){
        Integer studentId = (Integer) session.getAttribute(studentSessionKey);
        if (studentId == null) {
            return false;
        }

        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            return false;
        }

        return true;
    }




    public Student getStudentFromSession(HttpSession session) {
        Integer studentId = (Integer) session.getAttribute(studentSessionKey);
        if (studentId == null) {
            return null;
        }

        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            return null;
        }

        return student.get();
    }
    // Stores key/value pair with session key and user ID

    private static void setStudentInSession(HttpSession session, Student student) {
        session.setAttribute(studentSessionKey, student.getId());
    }
// register a teacher
@GetMapping("/register")
public String displayRegistrationFormTeacher(Model model, HttpSession session) {
    model.addAttribute("registerFormDTO",new RegisterFormTeachertDTO());
    model.addAttribute("title", "Register");
    model.addAttribute("loggedIn", session.getAttribute("teacher") != null);

    return "register";
}

    @PostMapping("/register")
    public String processRegistrationFormTeacher(@ModelAttribute @Valid RegisterFormTeachertDTO registerFormTeachertDTO,

                                                Errors errors, HttpServletRequest request,
                                                Model model) {

        // Send teacher back to form if errors are found
        if (errors.hasErrors()) {
            model.addAttribute("title", "Teacher Register");
            return "register";
        }
// Look up user in database using email they provided in the form
        Teacher existingTeacher = teacherRepository.findByEmail(registerFormTeachertDTO.getEmail());
        // Send user back to form if email already exists
        if (existingTeacher != null) {
            errors.rejectValue("email", "email.alreadyexists", "A user with that email already exists");
            model.addAttribute("title", "Teacher Register");
            return "register";
        }
// Send user back to form if passwords didn't match
        String password = registerFormTeachertDTO.getPassword();
        String verifyPassword = registerFormTeachertDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "teacher Register");
            return "register";
        }

        // OTHERWISE, save new email and hashed password in database, start a new session, and redirect to home page
        Teacher newTeacher = new Teacher(registerFormTeachertDTO.getEmail(), registerFormTeachertDTO.getPassword(), registerFormTeachertDTO.getFirstName(),
                registerFormTeachertDTO.getLastName(), registerFormTeachertDTO.getPhone(),registerFormTeachertDTO.getStatus());
        teacherRepository.save(newTeacher);
        setTeacherInSession(request.getSession(), newTeacher);

        return "redirect:Dashboard";
    }


}
