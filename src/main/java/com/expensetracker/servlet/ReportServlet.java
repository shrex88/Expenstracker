package com.expensetracker.servlet;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.ExpenseDAOImpl;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.util.PDFGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private final ExpenseDAO expenseDAO = new ExpenseDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

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
            // keep null
        }

        if (from == null) from = LocalDate.of(2000, 1, 1);
        if (to == null) to = LocalDate.of(2099, 12, 31);

        List<Expense> list = expenseDAO.search(user.getId(), null, category, from, to);

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=\"Expense_Report_" + LocalDate.now() + ".pdf\"");

        try {
            PDFGenerator.generateReport(user, list, resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating report: " + e.getMessage());
        }
    }
}
