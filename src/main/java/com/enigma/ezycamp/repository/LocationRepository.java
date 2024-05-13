package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
    @Query(value = "select * from m_location where name ilike :name", nativeQuery = true)
    Page<Location> findByNameLocation(@Param("name") String name, Pageable pageable);
}
