package com.expensetracker.dao;

import com.expensetracker.model.User;
import com.expensetracker.util.DBConnectionPool;
import java.sql.*;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final String INSERT_USER = "INSERT INTO users (name, email, password, budget) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_EMAIL = "SELECT id, name, email, password, budget FROM users WHERE email = ?";
    private static final String SELECT_BY_ID = "SELECT id, name, email, password, budget FROM users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, email = ?, password = ?, budget = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";

    @Override
    public long create(User user) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            if (user.getBudget() != null) {
                ps.setDouble(4, user.getBudget());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
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
    public Optional<User> findByEmail(String email) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL)) {
            ps.setString(1, email);
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
    public Optional<User> findById(long id) {
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
    public boolean update(User user) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            if (user.getBudget() != null) {
                ps.setDouble(4, user.getBudget());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
            ps.setLong(5, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        double budgetVal = rs.getDouble("budget");
        if (rs.wasNull()) {
            user.setBudget(null);
        } else {
            user.setBudget(budgetVal);
        }
        return user;
    }
}
