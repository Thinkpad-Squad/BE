package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query(value = "select c.* from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where c.id = :id and ua.is_enable = true", nativeQuery = true)
    Optional<Customer> findByIdCustomer(@Param("id") String id);
    @Query(value = "select c.* from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where ua.is_enable = true", nativeQuery = true)
    Page<Customer> findAllCustomer(Pageable pageable);
    @Query(value = "select c.* from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where c.name ilike :name and ua.is_enable = true", nativeQuery = true)
    Page<Customer> findByNameCustomer(@Param("name") String name, Pageable pageable);
    @Query(value = "select c.* from m_customer as c join m_user_account as ua on ua.id = c.user_account_id where ua.username = :username and ua.is_enable = true", nativeQuery = true)
    Optional<Customer> findByUsernameCustomer(@Param("username") String username);
}
