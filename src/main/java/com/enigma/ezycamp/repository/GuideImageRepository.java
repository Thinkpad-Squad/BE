package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.GuideImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuideImageRepository extends JpaRepository<GuideImage, String> {
    @Query(value = "select * from m_guide_image where name = :name", nativeQuery = true)
    Optional<GuideImage> findByName(@Param("name") String name);
}
