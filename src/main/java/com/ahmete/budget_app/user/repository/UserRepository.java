package com.ahmete.budget_app.user.repository;

import com.ahmete.budget_app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}