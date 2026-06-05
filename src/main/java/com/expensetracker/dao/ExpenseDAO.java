package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseDAO {
    long create(Expense expense);
    Optional<Expense> findById(long id);
    List<Expense> findByUserId(long userId);
    List<Expense> search(long userId, String keyword, String category, LocalDate from, LocalDate to);
    boolean update(Expense expense);
    boolean delete(long id);
}
