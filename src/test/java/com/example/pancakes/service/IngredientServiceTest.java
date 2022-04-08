package com.example.pancakes.service;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.IngredientType;
import com.example.pancakes.domain.Pancake;
import com.example.pancakes.domain.repository.IngredientRepository;
import com.example.pancakes.domain.repository.PancakeRepository;
import com.example.pancakes.service.request.CreateIngredientRequest;
import com.example.pancakes.service.request.UpdateIngredientRequest;
import com.example.pancakes.service.result.ActionResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock private IngredientRepository ingredientRepository;
    @Mock private PancakeRepository pancakeRepository;
    private IngredientService underTest;
    private final String NAME = "name";
    private final float PRICE = 10.5f;

    @BeforeEach
    void setUp(){
        underTest = new IngredientService(ingredientRepository,pancakeRepository);
    }

    @Test
    void willReturnWhenUpdatingIngredientWhenItIsOnlyStuffingToChangedPancakes(){
        int id = 1;
        UpdateIngredientRequest request = new UpdateIngredientRequest(List.of(3,4));
        Ingredient ingredient1 = Ingredient.builder()
                .id(id)
                .name(NAME)
                .healthy(true)
                .pancakes(null)
                .price(PRICE)
                .type(IngredientType.valueOf("STUFFING")).build();
        Ingredient ingredient2 = Ingredient.builder()
                .id(2)
                .name(NAME)
                .healthy(true)
                .pancakes(null)
                .price(PRICE)
                .type(IngredientType.valueOf("BASE")).build();
        List<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(ingredient1);
        ingredients1.add(ingredient2);
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(ingredient2);

        Pancake pancakeOld1 = Pancake.builder().id(1).ingredients(ingredients1).build();
        Pancake pancakeOld2 = Pancake.builder().id(2).ingredients(ingredients2).build();
        Pancake pancakeNew1 = Pancake.builder().id(3).ingredients(ingredients2).build();
        Pancake pancakeNew2 = Pancake.builder().id(4).ingredients(ingredients2).build();

        List<Pancake> pancakes1 = new ArrayList<>();
        pancakes1.add(pancakeNew1); pancakes1.add(pancakeNew2);
        List<Pancake> pancakes2 = new ArrayList<>();
        pancakes2.add(pancakeNew1); pancakes2.add(pancakeNew2);
        pancakes2.add(pancakeOld1); pancakes2.add(pancakeOld2);
        ingredient1.setPancakes(pancakes1);
        ingredient2.setPancakes(pancakes2);

        given(ingredientRepository.findById(id))
                .willReturn(Optional.of(ingredient1));
        given(pancakeRepository.findAllByIngredientsId(id))
                .willReturn(pancakes2);
        given(pancakeRepository.findAllById(request.getPancakesIds()))
                .willReturn(pancakes1);

        ActionResult result = underTest.updateIngredientPancakes(id,request);
        assertThat(result.getMessage()).startsWith("Ingredient can not be modified");
    }

    @Test
    void canUpdateIngredient(){
        int id = 1;
        UpdateIngredientRequest request = new UpdateIngredientRequest(List.of(3,4));
        Ingredient ingredient1 = Ingredient.builder()
                .id(id)
                .name(NAME)
                .healthy(true)
                .pancakes(null)
                .price(PRICE)
                .type(IngredientType.valueOf("STUFFING")).build();
        Ingredient ingredient2 = Ingredient.builder()
                .id(id)
                .name(NAME)
                .healthy(true)
                .pancakes(null)
                .price(PRICE)
                .type(IngredientType.valueOf("STUFFING")).build();
        Pancake pancakeOld1 = Pancake.builder().id(1).ingredients(List.of(ingredient1,ingredient2)).build();
        Pancake pancakeOld2 = Pancake.builder().id(2).ingredients(List.of(ingredient1,ingredient2)).build();
        Pancake pancakeNew3 = Pancake.builder().id(3).ingredients(List.of(ingredient2)).build();
        Pancake pancakeNew4 = Pancake.builder().id(4).ingredients(List.of(ingredient2)).build();

        given(ingredientRepository.findById(id))
                .willReturn(Optional.of(ingredient1));

        ActionResult result = underTest.updateIngredientPancakes(id,request);
        assertThat(result.getMessage()).isEqualTo("Ingredient successfully updated!");
    }

    @Test
    void willReturnWhenIngredientIsDeletingAndIsOnlyStuffingIngredientToPancake() {
        int id = 1;
        Ingredient ingredient = Ingredient.builder()
                .id(id)
                .name(NAME)
                .healthy(true)
                .pancakes(null)
                .price(PRICE)
                .type(IngredientType.valueOf("STUFFING")).build();
        Pancake pancake1 = Pancake.builder().id(1).ingredients(List.of(ingredient)).build();
        Pancake pancake2 = Pancake.builder().id(2).ingredients(List.of(ingredient)).build();
        Pancake pancake3 = Pancake.builder().id(3).ingredients(List.of(ingredient)).build();
        Pancake pancake4 = Pancake.builder().id(4).ingredients(List.of(ingredient)).build();
        List<Pancake> pancakes = List.of(pancake1, pancake2, pancake3, pancake4);
        ingredient.setPancakes(pancakes);

        given(ingredientRepository.findById(id))
                .willReturn(Optional.of(ingredient));

        ActionResult result = underTest.deleteIngredient(id);

        assertThat(result.getMessage()).startsWith("Ingredient can not be modified");
    }

    @Test
    void willReturnWhenIngredientIsDeletingAndIsBaseAndHasPancakes(){
        int id = 1;
        Pancake pancake1 = Pancake.builder().id(1).build();
        Pancake pancake2 = Pancake.builder().id(2).build();
        Pancake pancake3 = Pancake.builder().id(3).build();
        Pancake pancake4 = Pancake.builder().id(4).build();
        List<Pancake> pancakes = List.of(pancake1,pancake2,pancake3,pancake4);
        Ingredient ingredient = Ingredient.builder()
                .id(id)
                .name(NAME)
                .healthy(true)
                .pancakes(pancakes)
                .price(PRICE)
                .type(IngredientType.valueOf("BASE")).build();

        given(ingredientRepository.findById(id))
                .willReturn(Optional.ofNullable(ingredient));

        ActionResult result = underTest.deleteIngredient(id);

        assertThat(result.getMessage()).startsWith("This ingredient can not be deleted " +
                "because it is a base ingredient in pancakes with ids");
    }

    @Test
    void willReturnWhenIngredientWithIdDoesNotExist(){
        int id = 1;
        given(ingredientRepository.findById(id))
                .willReturn(Optional.empty());

        ActionResult result = underTest.deleteIngredient(id);
        assertThat(result.getMessage()).endsWith("does not exist!");
    }

    @Test
    void willReturnWhenPancakesWithRequestsPancakesIdsDoNotExist(){
        List<Integer> pancakeIds = List.of(1,2,3,4,5);
        CreateIngredientRequest request = new CreateIngredientRequest(
                NAME,
                PRICE,
                "BASE",
                true,
                pancakeIds
        );

        given(pancakeRepository.findAllById(pancakeIds))
                .willReturn(List.of());

        ActionResult result = underTest.createIngredient(request);
        assertThat(result.getMessage()).isEqualTo("No pancakes with those Ids!");
    }

    @Test
    void willReturnExistsWhenIngredientAlreadyExists() {
        CreateIngredientRequest request = new CreateIngredientRequest(
                NAME,
                PRICE,
                "BASE",
                true,
                null
        );
        Ingredient ingredient = Ingredient.builder()
                .id(1)
                .name(request.getName())
                .healthy(request.isHealthy())
                .pancakes(null)
                .price(request.getPrice())
                .type(IngredientType.valueOf(request.getType())).build();

        given(ingredientRepository.findByName(request.getName()))
                .willReturn(Optional.ofNullable(ingredient));

        ActionResult actionResult = underTest.createIngredient(request);
        assertThat(actionResult.getMessage()).endsWith("already exists");
    }

    @Test
    void canCreateIngredient() {
        //given
        CreateIngredientRequest request = new CreateIngredientRequest(
                NAME,
                PRICE,
                "BASE",
                true,
                null
        );
        Ingredient ingredient = new Ingredient(
                0,
                request.getName(),
                request.getPrice(),
                IngredientType.valueOf(request.getType()),
                request.isHealthy(),
                null
        );
        //when
        underTest.createIngredient(request);
        //then
        ArgumentCaptor<Ingredient> argumentCaptor =
                ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(argumentCaptor.capture());

        Ingredient capturedIngredient = argumentCaptor.getValue();
        assertThat(capturedIngredient).isEqualTo(ingredient);

    }


    @Test
    void canGetAllIngredients() {
        underTest.searchAllIngredients();
        verify(ingredientRepository).findAll();
    }
}