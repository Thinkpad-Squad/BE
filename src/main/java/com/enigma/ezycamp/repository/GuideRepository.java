package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, String> {
    @Query("select g.* from m_guide as g join m_user_account as ua where g.id = :id and ua.is_enable = true")
    Optional<Guide> findByIdGuide(@Param("id") String id);
}
