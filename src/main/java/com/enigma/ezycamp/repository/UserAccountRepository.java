package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    @Query(value = "select * from m_user_account where username = :username and is_enable = true")
    Optional<UserAccount> findByUsername(@Param("username") String username);
}
