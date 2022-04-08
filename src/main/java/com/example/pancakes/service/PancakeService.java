package com.example.pancakes.service;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.Pancake;
import com.example.pancakes.domain.dto.IngredientDto;
import com.example.pancakes.domain.dto.PancakeDto;
import com.example.pancakes.domain.repository.IngredientRepository;
import com.example.pancakes.domain.repository.PancakeRepository;
import com.example.pancakes.service.request.CreatePancakeRequest;
import com.example.pancakes.service.request.UpdatePancakeRequest;
import com.example.pancakes.service.result.ActionResult;
import com.example.pancakes.service.result.DataResult;
import com.example.pancakes.service.result.SearchPancakeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PancakeService {

    private final IngredientRepository ingredientRepository;
    private final PancakeRepository pancakeRepository;

    @Autowired
    public PancakeService(IngredientRepository ingredientRepository, PancakeRepository pancakeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.pancakeRepository = pancakeRepository;
    }


    public ActionResult createPancake(CreatePancakeRequest request) {

        //Check if there is ingredient with any of requests ids
        List<Ingredient> ingredients = ingredientRepository.findAllById(request.getIngredientsIds());
        if (ingredients.isEmpty())
            return new ActionResult(false, "There are no ingredients with those ids!");

        //Check if pancake meets criteria - exactly 1 base, at least 1 stuffing
        ActionResult result = checkPancakeConstraints(ingredients);
        if (Objects.nonNull(result))
            return result;

        Pancake pancake = new Pancake();
        for (Ingredient ingredient : ingredients) {
            pancake.addIngredient(ingredient);
        }

        pancakeRepository.save(pancake);


        return new ActionResult(true, "Pancake successfully created!");
    }

    public ActionResult deletePancake(int id) {

        //Check if pancake with that id exists
        Optional<Pancake> pancakeOptional = pancakeRepository.findById(id);
        if (!pancakeOptional.isPresent()) {
            return new ActionResult(false, "Pancake with id: '" + id + "' does not exist!");
        }

        //Remove pancake from every ingredient in a list
        List<Ingredient> ingredients = ingredientRepository.findAllByPancakesId(id);
        for (Ingredient ingredient : ingredients) {
            ingredient.removePancake(pancakeOptional.get());
        }
        pancakeRepository.deleteById(id);

        return new ActionResult(true, "Pancake successfully deleted!");
    }


    public ActionResult updatePancake(int id, UpdatePancakeRequest request) {

        //Check if pancake with that id exists
        Optional<Pancake> pancakeOptional = pancakeRepository.findById(id);
        if (pancakeOptional.isEmpty())
            return new ActionResult(false, "Pancake with this id does not exist!");

        List<Ingredient> newIngredients = ingredientRepository.findAllById(request.getIngredientsIds());
        Pancake pancake = pancakeOptional.get();

        //Check if after update meets the criteria - exactly 1 base, at least 1 stuffing
        ActionResult result = checkPancakeConstraints(newIngredients);
        if (Objects.nonNull(result))
            return result;

        //Removing old ingredients
        List<Ingredient> oldIngredients = ingredientRepository.findAllByPancakesId(id);
        for (Ingredient ingredient : oldIngredients) {
            ingredient.removePancake(pancake);
        }
        ingredientRepository.saveAll(oldIngredients);

        //Adding new ingredients
        for (Ingredient ingredient : newIngredients) {
            ingredient.addPancake(pancake);
        }
        ingredientRepository.saveAll(newIngredients);

        return new ActionResult(true, "Pancake ingredients successfully updated!");
    }

    public DataResult<PancakeDto> searchPancakeById(int id) {
        Optional<Pancake> pancakeOptional = pancakeRepository.findById(id);
        if (pancakeOptional.isEmpty())
            return new DataResult<>(false, "Pancake with this id does not exist!", null);

        Pancake pancake = pancakeOptional.get();
        PancakeDto pancakeDto = PancakeDto.builder()
                .id(pancake.getId())
                .price(pancake.calculatePrice())
                .ingredientDtos(convertIngredientsToDtos(pancake.getIngredients()))
                .build();
        return new DataResult<>(true,null,pancakeDto);


    }

    public DataResult<SearchPancakeResult> searchAllPancakes() {

        List<Pancake> pancakes = pancakeRepository.findAll();
        if (pancakes.isEmpty())
            return new DataResult<>(false, "No pancakes in database", null);

        SearchPancakeResult result = new SearchPancakeResult();
        result.setPancakeDtos(convertToPancakeDtos(pancakes));
        return new DataResult<>(true,null,result);
    }

    private List<PancakeDto> convertToPancakeDtos(List<Pancake> pancakes) {
        return pancakes.stream().map(pancake ->
                PancakeDto.builder()
                        .id(pancake.getId())
                        .price(round(pancake.calculatePrice(),2))
                        .ingredientDtos(convertIngredientsToDtos(pancake.getIngredients()))
                        .build()
                ).collect(Collectors.toList());
    }


    private ActionResult checkPancakeConstraints(List<Ingredient> ingredients) {
        int numOfBases = 0;
        int numOfStuffings = 0;
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getType().name().equals("BASE"))
                numOfBases++;
            else if (ingredient.getType().name().equals("STUFFING"))
                numOfStuffings++;
        }
        if (numOfBases != 1)
            return new ActionResult(false, "Number of base ingredients must be exactly 1!");
        if (numOfStuffings < 1)
            return new ActionResult(false, "You must have at least 1 stuffing ingredient!");
        return null;
    }

    private List<IngredientDto> convertIngredientsToDtos(List<Ingredient> ingredients) {
        return ingredients.stream().map(ingredient ->
                IngredientDto.builder()
                        .name(ingredient.getName())
                        .id(ingredient.getId())
                        .healthy(ingredient.isHealthy() ? "YES" : "NO")
                        .price(round(ingredient.getPrice(),2)).build()
        ).collect(Collectors.toList());
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}

