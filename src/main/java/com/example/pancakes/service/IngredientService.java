package com.example.pancakes.service;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.IngredientType;
import com.example.pancakes.domain.Pancake;
import com.example.pancakes.domain.dto.IngredientDto;
import com.example.pancakes.domain.repository.IngredientRepository;
import com.example.pancakes.domain.repository.PancakeRepository;
import com.example.pancakes.service.request.CreateIngredientRequest;
import com.example.pancakes.service.request.UpdateIngredientRequest;
import com.example.pancakes.service.result.ActionResult;
import com.example.pancakes.service.result.DataResult;
import com.example.pancakes.service.result.SearchIngredientResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final PancakeRepository pancakeRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, PancakeRepository pancakeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.pancakeRepository = pancakeRepository;
    }

    public ActionResult createIngredient(CreateIngredientRequest request){

        //Check if ingredient with that name already exists
        Optional<Ingredient> ingredientOptional = ingredientRepository.findByName(request.getName());
        if(ingredientOptional.isPresent()){
            return new ActionResult(false,"Ingredient with name: '" + request.getName() + "'" +
                    " already exists");
        }

        //Check if any of request ids match pancake ids in database
        List<Pancake> pancakes = null;
        if(Objects.nonNull(request.getPancakesIds()) && !request.getPancakesIds().isEmpty()){
            pancakes = pancakeRepository.findAllById(request.getPancakesIds());
            if(pancakes.isEmpty()){
                return new ActionResult(false, "No pancakes with those Ids!");
            }
        }


        Ingredient ingredient = Ingredient.builder()
                .pancakes(pancakes)
                .name(request.getName())
                .price(request.getPrice())
                .type(IngredientType.valueOf(request.getType()))
                .healthy(request.isHealthy())
                .build();

        ingredientRepository.save(ingredient);

        return new ActionResult(true,"Ingredient successfully created!");
    }

    public ActionResult deleteIngredient(int id) {

        //Check if exists
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if(!ingredientOptional.isPresent()){
            return new ActionResult(false,"Ingredient with id: '"+id+"' does not exist!");
        }

        //Check if is base and has pancakes
        Ingredient ingredient = ingredientOptional.get();
        if(ingredient.getType().name().equals("BASE") && !ingredient.getPancakes().isEmpty()){
            StringBuilder s = new StringBuilder();
            for(Pancake pancake : ingredient.getPancakes()){
                if(s.length() == 0){
                    s.append(pancake.getId());
                } else
                    s.append(", "+pancake.getId());
            }
            return new ActionResult(false,"This ingredient can not be deleted because" +
                    " it is a base ingredient in pancakes with ids: ["+s+"]!");
        }

        //Check if is stuffing and if pancake would be left with no stuffing ingredient
        ActionResult result = checkIngredientStuffingConstraint(ingredient,ingredient.getPancakes());
        if(Objects.nonNull(result))
            return result;

        ingredientRepository.deleteById(id);
        return new ActionResult(true, "Ingredient successfully deleted!");
    }

    public ActionResult updateIngredientPancakes(int id, UpdateIngredientRequest request){

        //check if ingredient with that id exists
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if(ingredientOptional.isEmpty())
            return new ActionResult(false,"Ingredient with that id does not exist!");

        //Check if is base ingredient - can't be updated because every pancake already has base
        Ingredient ingredient = ingredientOptional.get();
        if(ingredientOptional.get().getType().name().equals("BASE"))
            return new ActionResult(false, "Base ingredients can not be added to or removed from" +
                    " pancakes because they have exactly 1 base ingredient");

        List<Pancake> oldPancakes = pancakeRepository.findAllByIngredientsId(id);
        List<Pancake> newPancakes = pancakeRepository.findAllById(request.getPancakesIds());
        oldPancakes.removeAll(newPancakes);

        //Check if is stuffing and if pancake would be left with no stuffing ingredient
        ActionResult result = checkIngredientStuffingConstraint(ingredient,oldPancakes);
        if(Objects.nonNull(result))
            return result;

        ingredient.setPancakes(newPancakes);
        ingredientRepository.save(ingredient);

        return new ActionResult(true,"Ingredient successfully updated!");
    }

    public DataResult<SearchIngredientResult> searchAllIngredients(){

        List<Ingredient> ingredients = ingredientRepository.findAll();
        if(ingredients.isEmpty())
            return new DataResult<>(false,"There are not ingredients in database",null);

        SearchIngredientResult result = new SearchIngredientResult();
        result.setIngredientDtos(convertIngredientsToDtos(ingredients));

        return new DataResult<>(true,null,result);
    }


    public DataResult<SearchIngredientResult> searchIngredientById(Integer id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if(ingredientOptional.isEmpty())
            return new DataResult<SearchIngredientResult>(false,"Ingredient with this id does not exist!",null);

        SearchIngredientResult result = new SearchIngredientResult();
        result.setIngredientDtos(convertIngredientsToDtos(List.of(ingredientOptional.get())));

        return new DataResult<>(true, null,result);
    }

    public DataResult<SearchIngredientResult> searchIngredientByName(String name) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findByName(name);
        if(ingredientOptional.isEmpty())
            return new DataResult<>(false,"Ingredient with this name does not exist!",null);

        SearchIngredientResult result = new SearchIngredientResult();
        result.setIngredientDtos(convertIngredientsToDtos(List.of(ingredientOptional.get())));

        return new DataResult<>(true, null,result);
    }

    public DataResult<SearchIngredientResult> searchIngredientsByPancakeId(int pancakeId) {

        Optional<Pancake> pancakeOptional = pancakeRepository.findById(pancakeId);
        if(pancakeOptional.isEmpty())
            return new DataResult<>(false,"Pancake with that id does not exist!", null);

        List<Ingredient> ingredients = ingredientRepository.findAllByPancakesId(pancakeId);

        SearchIngredientResult result = new SearchIngredientResult();
        result.setIngredientDtos(convertIngredientsToDtos(ingredients));

        return new DataResult<>(true, null, result);
    }

    private List<IngredientDto> convertIngredientsToDtos(List<Ingredient> ingredients){
        return ingredients.stream().map(ingredient ->
                IngredientDto.builder()
                        .name(ingredient.getName())
                        .id(ingredient.getId())
                        .healthy(ingredient.isHealthy() ? "YES" : "NO")
                        .price(ingredient.getPrice()).build()
                ).collect(Collectors.toList());
    }


    public ActionResult updateIngredientPrice(int id, float price){

        //Check if ingredient with id exists
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
        if(ingredientOptional.isEmpty())
            return new ActionResult(false,"Ingredient with that id does not exist");

        if (price<=0)
            return new ActionResult(false,"Price must be above 0!");

        ingredientRepository.updatePrice(id, price);
        return new ActionResult(true,"Ingredients price successfully updated!");
    }
    private ActionResult checkIngredientStuffingConstraint(Ingredient ingredient, List<Pancake> pancakes){
        if(ingredient.getType().name().equals("STUFFING")){
            StringBuilder s = new StringBuilder();

            for(Pancake pancake : pancakes){
                int counter = 0;
                for(Ingredient ing : pancake.getIngredients()){
                    if (ing.getType().name().equals("STUFFING")){
                        counter++;
                    }
                }
                if(counter==1){

                    if(s.length()==0){
                        s.append(pancake.getId());
                    } else
                        s.append(", "+pancake.getId());
                }
            }
            if(s.length()!=0)
                return new ActionResult(false,"Ingredient can not be modified because pancakes" +
                        "with ids: [" + s + "] will have less than one stuffing!");
        }
        return null;
    }

}
