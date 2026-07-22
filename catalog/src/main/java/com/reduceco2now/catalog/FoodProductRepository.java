package com.reduceco2now.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FoodProductRepository extends JpaRepository<FoodProductEntity, Long> {

    Optional<FoodProductEntity> findByBarcode(String barcode);

    @Query("""
            select f from FoodProductEntity f
            where lower(f.name) like lower(concat('%', :q, '%'))
              and (:category is null or f.categoryCode = :category)
            order by f.name
            """)
    List<FoodProductEntity> search(@Param("q") String q, @Param("category") String category);
}