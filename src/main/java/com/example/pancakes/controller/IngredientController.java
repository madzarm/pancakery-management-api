package com.example.pancakes.controller;

import com.example.pancakes.service.IngredientService;
import com.example.pancakes.service.request.CreateIngredientRequest;
import com.example.pancakes.service.request.UpdateIngredientRequest;
import com.example.pancakes.service.result.ActionResult;
import com.example.pancakes.service.result.DataResult;
import com.example.pancakes.service.result.SearchIngredientResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredient")
    public ResponseEntity<DataResult<SearchIngredientResult>> searchIngredient(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer pancakeId,
            @RequestParam(required = false) String name
    ){
        boolean hasIdSearch = Objects.nonNull(id);
        boolean hasPancakeIdSearch = Objects.nonNull(pancakeId);
        boolean hasNameSearch = Objects.nonNull(name);

        if(!hasIdSearch && !hasPancakeIdSearch && !hasNameSearch)
            return ingredientService.searchAllIngredients().intoResponseEntity();
        else if(hasIdSearch && !(hasNameSearch||hasPancakeIdSearch))
            return ingredientService.searchIngredientById(id).intoResponseEntity();
        else if(hasNameSearch && !(hasIdSearch||hasPancakeIdSearch))
            return ingredientService.searchIngredientByName(name).intoResponseEntity();
        else if(hasPancakeIdSearch && !(hasNameSearch||hasIdSearch))
            return ingredientService.searchIngredientsByPancakeId(pancakeId).intoResponseEntity();

        return new DataResult<SearchIngredientResult>(false,"You can not combine searches!",null).intoResponseEntity();
    }

    @PostMapping("/ingredient")
    public ResponseEntity<ActionResult> createIngredient(@Valid @RequestBody CreateIngredientRequest request){
        return ingredientService.createIngredient(request).intoResponseEntity();
    }

    @DeleteMapping("/ingredient")
    public ResponseEntity<ActionResult> deleteIngredient(@RequestParam int id){
        return ingredientService.deleteIngredient(id).intoResponseEntity();
    }

    @PostMapping("/ingredient/{id}")
    public ResponseEntity<ActionResult> updateIngredient(
            @PathVariable int id,
            @RequestBody(required = false) UpdateIngredientRequest request,
            @RequestParam(required = false) Float price
    ){

        boolean hasPancakeUpdate = Objects.nonNull(request);
        boolean hasPriceUpdate = Objects.nonNull(price);

        if(!hasPancakeUpdate && !hasPriceUpdate)
            return new ActionResult(false,"You can update name or a list of pancakes!").intoResponseEntity();
        if(hasPriceUpdate && hasPancakeUpdate)
            return new ActionResult(false,"You can either update name or a list of pancakes!").intoResponseEntity();
        if(hasPancakeUpdate)
            return ingredientService.updateIngredientPancakes(id,request).intoResponseEntity();

        return ingredientService.updateIngredientPrice(id,price).intoResponseEntity();
    }
}
