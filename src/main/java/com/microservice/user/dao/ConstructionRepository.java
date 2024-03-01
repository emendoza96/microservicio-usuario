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
                co.*
            FROM
                construction co
            JOIN customer cu on co.customer_id = cu.id
            JOIN construction_type ct on co.type_id = ct.id
            WHERE
                (:businessName is null or cu.business_name = :businessName)
                and (:type is null or ct.type = :type)
        """,
        nativeQuery = true
    )
    public List<Construction> findByBusinessNameAndType(@Param("businessName") String businessName, @Param("type") String type);

}
