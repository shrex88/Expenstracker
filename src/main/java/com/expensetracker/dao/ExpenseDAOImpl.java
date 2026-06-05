package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.util.DBConnectionPool;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseDAOImpl implements ExpenseDAO {
    private static final String INSERT_EXPENSE = "INSERT INTO expenses (user_id, title, amount, category, expense_date, description) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT id, user_id, title, amount, category, expense_date, description FROM expenses WHERE id = ?";
    private static final String SELECT_BY_USER = "SELECT id, user_id, title, amount, category, expense_date, description FROM expenses WHERE user_id = ? ORDER BY expense_date DESC, id DESC";
    private static final String UPDATE_EXPENSE = "UPDATE expenses SET title = ?, amount = ?, category = ?, expense_date = ?, description = ? WHERE id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expenses WHERE id = ?";

    @Override
    public long create(Expense expense) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_EXPENSE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, expense.getUserId());
            ps.setString(2, expense.getTitle());
            ps.setDouble(3, expense.getAmount());
            ps.setString(4, expense.getCategory());
            ps.setDate(5, Date.valueOf(expense.getExpenseDate()));
            ps.setString(6, expense.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Optional<Expense> findById(long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Expense> findByUserId(long userId) {
        List<Expense> list = new ArrayList<>();
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Expense> search(long userId, String keyword, String category, LocalDate from, LocalDate to) {
        List<Expense> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, user_id, title, amount, category, expense_date, description FROM expenses WHERE user_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All")) {
            sql.append(" AND category = ?");
            params.add(category.trim());
        }

        if (from != null) {
            sql.append(" AND expense_date >= ?");
            params.add(Date.valueOf(from));
        }

        if (to != null) {
            sql.append(" AND expense_date <= ?");
            params.add(Date.valueOf(to));
        }

        sql.append(" ORDER BY expense_date DESC, id DESC");

        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean update(Expense expense) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_EXPENSE)) {
            ps.setString(1, expense.getTitle());
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setDate(4, Date.valueOf(expense.getExpenseDate()));
            ps.setString(5, expense.getDescription());
            ps.setLong(6, expense.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_EXPENSE)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Expense mapRow(ResultSet rs) throws SQLException {
        Expense e = new Expense();
        e.setId(rs.getLong("id"));
        e.setUserId(rs.getLong("user_id"));
        e.setTitle(rs.getString("title"));
        e.setAmount(rs.getDouble("amount"));
        e.setCategory(rs.getString("category"));
        Date dateVal = rs.getDate("expense_date");
        if (dateVal != null) {
            e.setExpenseDate(dateVal.toLocalDate());
        }
        e.setDescription(rs.getString("description"));
        return e;
    }
}
