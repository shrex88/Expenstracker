package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.ExpenseDAOImpl;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional; 

@WebServlet("/expenses")
public class ExpenseServlet extends HttpServlet {
    private final ExpenseDAO expenseDAO = new ExpenseDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");

        if ("edit".equalsIgnoreCase(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                Optional<Expense> expOpt = expenseDAO.findById(id);
                if (expOpt.isPresent() && expOpt.get().getUserId() == user.getId()) {
                    req.setAttribute("expense", expOpt.get());
                    req.getRequestDispatcher("/jsp/editExpense.jsp").forward(req, resp);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid ID
            }
            resp.sendRedirect(req.getContextPath() + "/expenses");
        } else if ("delete".equalsIgnoreCase(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                Optional<Expense> expOpt = expenseDAO.findById(id);
                if (expOpt.isPresent() && expOpt.get().getUserId() == user.getId()) {
                    expenseDAO.delete(id);
                }
            } catch (NumberFormatException e) {
                // Invalid ID
            }
            resp.sendRedirect(req.getContextPath() + "/expenses");
        } else {
            // Search / Filter and List
            String q = req.getParameter("q");
            String category = req.getParameter("category");
            String fromStr = req.getParameter("fromDate");
            String toStr = req.getParameter("toDate");

            LocalDate from = null;
            LocalDate to = null;
            try {
                if (fromStr != null && !fromStr.trim().isEmpty()) {
                    from = LocalDate.parse(fromStr.trim());
                }
                if (toStr != null && !toStr.trim().isEmpty()) {
                    to = LocalDate.parse(toStr.trim());
                }
            } catch (Exception e) {
                // Parsing error, keep null
            }

            // Defaults if not specified
            if (from == null) from = LocalDate.of(2000, 1, 1);
            if (to == null) to = LocalDate.of(2099, 12, 31);

            List<Expense> list = expenseDAO.search(user.getId(), q, category, from, to);
            req.setAttribute("expenses", list);
            req.setAttribute("q", q);
            req.setAttribute("category", category);
            req.setAttribute("fromDate", fromStr);
            req.setAttribute("toDate", toStr);

            req.getRequestDispatcher("/jsp/expenses.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");

        String title = req.getParameter("title");
        String amountStr = req.getParameter("amount");
        String category = req.getParameter("category");
        String dateStr = req.getParameter("expenseDate");
        String description = req.getParameter("description");

        double amount = 0;
        LocalDate date = LocalDate.now();
        try {
            if (amountStr != null) amount = Double.parseDouble(amountStr);
            if (dateStr != null) date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            // keep default values
        }

        if ("add".equalsIgnoreCase(action)) {
            Expense e = new Expense(user.getId(), title, amount, category, date, description);
            expenseDAO.create(e);
        } else if ("edit".equalsIgnoreCase(action)) {
            try {
                long id = Long.parseLong(req.getParameter("id"));
                Optional<Expense> expOpt = expenseDAO.findById(id);
                if (expOpt.isPresent() && expOpt.get().getUserId() == user.getId()) {
                    Expense e = expOpt.get();
                    e.setTitle(title);
                    e.setAmount(amount);
                    e.setCategory(category);
                    e.setExpenseDate(date);
                    e.setDescription(description);
                    expenseDAO.update(e);
                }
            } catch (NumberFormatException e) {
                // Invalid ID
            }
        }
        resp.sendRedirect(req.getContextPath() + "/expenses");
    }
}
