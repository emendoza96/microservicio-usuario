package com.microservice.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservice.user.domain.Construction;

public interface ConstructionRepository extends JpaRepository<Construction, Integer> {

    @Query(value =
        """
            SELECT
                *
            FROM
                construction co
            JOIN customer cu on co.customer_id = cu.id
            WHERE cu.customerName = :value
        """,
        nativeQuery = true
    )
    public List<Construction> findByCustomerNameOrType(@Param("value") String customerName);

}
