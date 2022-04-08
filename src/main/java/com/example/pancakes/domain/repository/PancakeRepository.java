package com.example.pancakes.domain.repository;

import com.example.pancakes.domain.Pancake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PancakeRepository extends JpaRepository<Pancake,Integer> {
    List<Pancake> findAllByIngredientsId( int id);

    @Query(
            value = "SELECT pancake.id, pancake.pancake_order_id " +
                    "FROM (pancake INNER JOIN pancake_order " +
                    "ON pancake.pancake_order_id = pancake_order.id) " +
                    "WHERE pancake.id IN :pancakeIds",
            nativeQuery = true
    )
    List<Pancake> findAllWithOrders(@Param("pancakeIds") List<Integer> pancakeIds);

}
