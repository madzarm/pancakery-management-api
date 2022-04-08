package com.example.pancakes.domain.repository;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.IngredientType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByName() {
        //given
        String name = "name";
        Ingredient ingredient = new Ingredient(
                1,
                name,
                1.5f,
                IngredientType.BASE,
                true,
                null
        );
        underTest.save(ingredient);
        //when
        boolean exists = underTest.findByName(name).isPresent();
        //then
        assertThat(exists).isTrue();
    }
}