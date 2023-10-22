package com.SchoolPortal.SchoolPortal.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping
    public String displayHomePage(Model model, HttpSession session) {
        model.addAttribute("teacherloggedIn", session.getAttribute("teacher") != null);
        model.addAttribute("studentLoggedIn", session.getAttribute("student") != null);

        return "index";
    }
}
