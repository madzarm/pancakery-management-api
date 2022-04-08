package com.example.pancakes.domain.repository;

import com.example.pancakes.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Integer> {
    Optional<Ingredient> findByName(String name);

    List<Ingredient> findAllByPancakesId(int id);

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE ingredient " +
                    "SET price = :price " +
                    "WHERE id = :id",
            nativeQuery = true
    )
    void updatePrice(@Param("id") int id,@Param("price") float price);
}
