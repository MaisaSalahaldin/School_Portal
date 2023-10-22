package com.SchoolPortal.SchoolPortal;

import com.SchoolPortal.SchoolPortal.Controller.AuthenticationController;
import com.SchoolPortal.SchoolPortal.Models.Student;
import com.SchoolPortal.SchoolPortal.Models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {
    @Autowired
    AuthenticationController authenticationController;
    // Allow certain pages and static resources to be seen by the public (not logged in)


    private static final List<String> whitelist = Arrays.asList("/index");

    // Check all pages and static resources against blacklist
    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.equals("/") || path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // Don't require sign-in for whitelisted pages
        if (isWhitelisted(request.getRequestURI())) {
            // Early return to allow request to go through
            return true;
        }

        HttpSession session = request.getSession();
        Teacher teacher = authenticationController.getTeacherFromSession(session);
        Student client=authenticationController.getStudentFromSession(session);

        // The user is logged in
        if (teacher != null || client!=null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("/login");
        return false;
    }
}
