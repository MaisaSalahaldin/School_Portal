package com.SchoolPortal.SchoolPortal.Controller;

import com.SchoolPortal.SchoolPortal.Data.StudentRepository;
import com.SchoolPortal.SchoolPortal.Data.TeacherRepository;
import com.SchoolPortal.SchoolPortal.Models.DTO.LoginFormDTO;
import com.SchoolPortal.SchoolPortal.Models.DTO.RegisterFormDTO;
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

    public boolean studentInSession(HttpSession session) {
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
        model.addAttribute("registerFormDTO", new RegisterFormDTO());
        model.addAttribute("title", "Register");
        if (session.getAttribute("teacher") != null) {

            model.addAttribute("loggedIn", session.getAttribute("teacher") != null);
        } else {
            model.addAttribute("studentLoggedIn", session.getAttribute("student") != null);


        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationFormTeacher(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,

                                                 Errors errors, HttpServletRequest request,
                                                 Model model) {

        // Send teacher back to form if errors are found
        if (errors.hasErrors()) {
            model.addAttribute("title", "Teacher Register");
            return "register";
        }
// Look up user in database using email they provided in the form
        Teacher existingTeacher = teacherRepository.findByEmail(registerFormDTO.getEmail());
        Student existingStudent = studentRepository.findByEmail((registerFormDTO.getEmail()));
        // Send user back to form if email already exists
        if (existingTeacher != null || existingStudent != null) {
            errors.rejectValue("email", "email.alreadyexists", "A user with that email already exists");
            model.addAttribute("title", "Register");
            return "register";
        }
// Send user back to form if passwords didn't match
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "teacher Register");
            return "register";
        }
        Boolean isTeacher = registerFormDTO.getStatus();
        // register as A teacher
        if (isTeacher) {
            // OTHERWISE, save new email and hashed password in database, start a new session, and redirect to home page
            Teacher newTeacher = new Teacher(registerFormDTO.getEmail(), registerFormDTO.getPassword(), registerFormDTO.getFirstName(),
                    registerFormDTO.getLastName(), registerFormDTO.getPhone(), registerFormDTO.getStatus());
            teacherRepository.save(newTeacher);
            setTeacherInSession(request.getSession(), newTeacher);
            return "redirect:teacher/dashboard";
        }
        // register as A Student
        else {
            Student newStudent = new Student(registerFormDTO.getEmail(), registerFormDTO.getPassword(), registerFormDTO.getFirstName(),
                    registerFormDTO.getLastName(), registerFormDTO.getPhone(), registerFormDTO.getStatus());
            studentRepository.save(newStudent);
            setStudentInSession(request.getSession(), newStudent);
            return "redirect:task/view";
        }


    }

    @GetMapping("/login")
    public String displayLoginPage(Model model, HttpSession session) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");

        if (session.getAttribute("teacher") != null) {

            model.addAttribute("loggedIn", session.getAttribute("teacher") != null);
        } else {
            model.addAttribute("studentLoggedIn", session.getAttribute("student") != null);


        }
        return "login";

    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {


// Send user back to form if errors are found
        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }
// Look up user in database using email they provided in the form
        Teacher teacher=teacherRepository.findByEmail(loginFormDTO.getEmail());
        Student student=studentRepository.findByEmail(loginFormDTO.getEmail());
        if(teacher==null && student==null){
            errors.rejectValue("email","email.notExist","The given email does not exist");
            model.addAttribute("title","Log in");
            return "login";
        }
     // check if is student
        if(student!=null && teacher==null){
            String password=loginFormDTO.getPassword();
            if(!student.isMatchingPassword(password)){
                errors.rejectValue("password","password.notMatching","invalid password");
                model.addAttribute("title","log in ");
                return "login";
            }
            // OTHERWISE, create a new session for the student and take them to the home page
            setStudentInSession(request.getSession(), student);
            return "redirect:task/view";
        }
        else {
            // check if is Teacher

                String password=loginFormDTO.getPassword();
                if(!teacher.isMatchingPassword(password)){
                    errors.rejectValue("password","password.notMatching","invalid password");
                    model.addAttribute("title","log in ");
                    return "login";
                }
                // OTHERWISE, create a new session for the teacher and take them to the home page
                setTeacherInSession(request.getSession(), teacher);
            return "redirect:/teacher/dashboard";
            }
        }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:..";
    }
}
