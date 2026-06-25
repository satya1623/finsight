package com.finsight.finsight.repository;

import com.finsight.finsight.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}