package com.expensetracker.servlet;

import com.expensetracker.dao.UserDAO;
import com.expensetracker.dao.UserDAOImpl;
import com.expensetracker.model.User;
import com.expensetracker.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Email and Password are required.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        Optional<User> userOpt = userDAO.findByEmail(email.trim());
        if (userOpt.isPresent() && PasswordUtil.verify(password, userOpt.get().getPassword())) {
            HttpSession session = req.getSession();
            session.setAttribute("user", userOpt.get());
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }
}
