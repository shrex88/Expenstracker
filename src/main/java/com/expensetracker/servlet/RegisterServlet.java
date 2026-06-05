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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        Optional<User> existing = userDAO.findByEmail(email.trim());
        if (existing.isPresent()) {
            req.setAttribute("error", "Email is already registered.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(PasswordUtil.hash(password));
        
        long id = userDAO.create(user);
        if (id > 0) {
            user.setId(id);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", "Failed to create account. Please try again.");
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
        }
    }
}
