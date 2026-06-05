package com.expensetracker.dao;

import com.expensetracker.model.User;
import java.util.Optional;

public interface UserDAO {
    long create(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    boolean update(User user);
    boolean delete(long id);
}
