package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "select o.* from t_order as o join m_customer as c on c.id = o.customer_id where c.name like :name and date(o.date) = :date", nativeQuery = true)
    Page<Order> findByCustomerNameAndDate(@Param("name") String customerName, @Param("date") Date date, Pageable pageable);
    @Query(value = "select o.* from t_order as o join m_customer as c on c.id = o.customer_id where c.name like :name", nativeQuery = true)
    Page<Order> findByCustomerName(@Param("name") String customerName, Pageable pageable);
    Page<Order> findAllByDate(Date date, Pageable pageable);
    @Query(value = "select * from t_order where customer_id = :id", nativeQuery = true)
    Page<Order> findAllByCustomerId(@Param("id") String customerId, Pageable pageable);
    @Query(value = "select * from t_order where guide_id = :id", nativeQuery = true)
    Page<Order> findAllByGuideId(@Param("id") String guideId, Pageable pageable);
}
