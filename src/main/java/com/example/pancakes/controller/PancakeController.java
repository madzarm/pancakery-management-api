package com.example.pancakes.controller;

import com.example.pancakes.service.PancakeService;
import com.example.pancakes.service.request.CreatePancakeRequest;
import com.example.pancakes.service.request.UpdatePancakeRequest;
import com.example.pancakes.service.result.ActionResult;
import com.example.pancakes.service.result.DataResult;
import com.example.pancakes.service.result.SearchPancakeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class PancakeController {

    private final PancakeService pancakeService;

    @Autowired
    public PancakeController(PancakeService pancakeService) {
        this.pancakeService = pancakeService;
    }

    @PostMapping("/pancake")
    public ResponseEntity<ActionResult> createPancake (@Valid @RequestBody CreatePancakeRequest request) {
        return pancakeService.createPancake(request).intoResponseEntity();
    }

    @DeleteMapping("/pancake")
    public ResponseEntity<ActionResult> deletePancake(@RequestParam int id){
        return pancakeService.deletePancake(id).intoResponseEntity();
    }

    @PostMapping("/pancake/{id}")
    public ResponseEntity<ActionResult> updatePancake(
            @PathVariable int id,
            @Valid @RequestBody UpdatePancakeRequest request
    ){
        return pancakeService.updatePancake(id,request).intoResponseEntity();
    }

    @GetMapping("/pancake")
    public ResponseEntity<DataResult<SearchPancakeResult>> searchPancake(
            @RequestParam(required = false) Integer id
            ){
        if(Objects.nonNull(id))
            return pancakeService.searchPancakeById(id).intoResponseEntity();

        return pancakeService.searchAllPancakes().intoResponseEntity();
    }
}
