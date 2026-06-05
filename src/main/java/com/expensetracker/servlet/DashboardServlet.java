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
import java.time.YearMonth;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final ExpenseDAO expenseDAO = new ExpenseDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        List<Expense> expenses = expenseDAO.findByUserId(user.getId());

        // Calculations
        double total = 0;
        double monthly = 0;
        Expense highest = null;
        YearMonth currentMonth = YearMonth.now();

        Map<String, Double> categorySummary = new LinkedHashMap<>();
        String[] categories = {"Food", "Travel", "Shopping", "Bills", "Entertainment", "Education", "Other"};
        for (String cat : categories) {
            categorySummary.put(cat, 0.0);
        }

        Map<String, Double> monthlyTrend = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate targetDate = today.minusMonths(i);
            String monthLabel = targetDate.getMonth().name() + " " + targetDate.getYear();
            monthlyTrend.put(monthLabel, 0.0);
        }

        for (Expense e : expenses) {
            total += e.getAmount();

            YearMonth em = YearMonth.from(e.getExpenseDate());
            if (em.equals(currentMonth)) {
                monthly += e.getAmount();
            }

            if (highest == null || e.getAmount() > highest.getAmount()) {
                highest = e;
            }

            // Category summary
            String cat = e.getCategory();
            if (categorySummary.containsKey(cat)) {
                categorySummary.put(cat, categorySummary.get(cat) + e.getAmount());
            } else {
                categorySummary.put(cat, e.getAmount());
            }

            // Spending trend (last 6 months)
            String trendKey = e.getExpenseDate().getMonth().name() + " " + e.getExpenseDate().getYear();
            if (monthlyTrend.containsKey(trendKey)) {
                monthlyTrend.put(trendKey, monthlyTrend.get(trendKey) + e.getAmount());
            }
        }

        List<Expense> recent = expenses.subList(0, Math.min(5, expenses.size()));

        req.setAttribute("total", total);
        req.setAttribute("monthly", monthly);
        req.setAttribute("highest", highest);
        req.setAttribute("recent", recent);
        req.setAttribute("categorySummary", categorySummary);
        req.setAttribute("monthlyTrend", monthlyTrend);
        req.setAttribute("budget", user.getBudget());
        
        boolean budgetExceeded = false;
        if (user.getBudget() != null && monthly > user.getBudget()) {
            budgetExceeded = true;
        }
        req.setAttribute("budgetExceeded", budgetExceeded);

        req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Handle setting/updating budget
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        String budgetStr = req.getParameter("budget");
        if (budgetStr != null) {
            try {
                double budgetVal = Double.parseDouble(budgetStr);
                if (budgetVal >= 0) {
                    user.setBudget(budgetVal);
                    new com.expensetracker.dao.UserDAOImpl().update(user);
                    session.setAttribute("user", user);
                }
            } catch (NumberFormatException e) {
                // Ignore invalid numbers
            }
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
