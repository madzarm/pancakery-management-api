package com.example.pancakes.domain.repository;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.PancakeOrder;
import com.example.pancakes.domain.dto.IReport;
import com.example.pancakes.service.result.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface PancakeOrderRepository extends JpaRepository<PancakeOrder,Integer> {

    @Query(
            value = "SELECT ingredient.id AS id, ingredient.healthy AS healthy, ingredient.name AS name, ingredient.price AS price,  \n" +
                    "COUNT(ingredient.id) AS value_occurrence FROM (\n" +
                    "pancake INNER JOIN pancake_order ON pancake.pancake_order_id = pancake_order.id \n" +
                    "INNER JOIN pancake_ingredient ON pancake.id = pancake_ingredient.pancake_id\n" +
                    "INNER JOIN ingredient ON pancake_ingredient.ingredient_id = ingredient.id\n" +
                    ") WHERE pancake_order.order_time > :from\n" +
                    "GROUP BY ingredient.id\n" +
                    "ORDER BY value_occurrence DESC, healthy DESC, price DESC, id ASC\n" +
                    "LIMIT 1;",
            nativeQuery = true
    )
    IReport findMostRepeatedIngredientInLastMonth(@Param("from") LocalDate from);

    @Query(
            value = "SELECT ingredient.id AS id, ingredient.healthy AS healthy, ingredient.name AS name, ingredient.price AS price,  \n" +
                    "COUNT(ingredient.id) AS value_occurrence FROM (\n" +
                    "pancake INNER JOIN pancake_order ON pancake.pancake_order_id = pancake_order.id \n" +
                    "INNER JOIN pancake_ingredient ON pancake.id = pancake_ingredient.pancake_id\n" +
                    "INNER JOIN ingredient ON pancake_ingredient.ingredient_id = ingredient.id\n" +
                    ") WHERE pancake_order.order_time > :from AND ingredient.healthy = 1\n" +
                    "GROUP BY ingredient.id\n" +
                    "ORDER BY value_occurrence DESC, price DESC, id ASC\n" +
                    "LIMIT 1;",
            nativeQuery = true
    )
    IReport findMostRepeatedHealthyIngredientInLastMonth(@Param("from") LocalDate from);
}
