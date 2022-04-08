package com.example.pancakes.controller;

import com.example.pancakes.domain.dto.IngredientDto;
import com.example.pancakes.service.PancakeOrderService;
import com.example.pancakes.service.request.CreateOrderRequest;
import com.example.pancakes.service.result.ActionResult;
import com.example.pancakes.service.result.DataResult;
import com.example.pancakes.service.result.Report;
import com.example.pancakes.service.result.SearchOrderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class PancakeOrderController {

    private final PancakeOrderService pancakeOrderService;

    @Autowired
    public PancakeOrderController(PancakeOrderService pancakeOrderService) {
        this.pancakeOrderService = pancakeOrderService;
    }

    @PostMapping("/order")
    public ResponseEntity<ActionResult> createOrder(@Valid @RequestBody CreateOrderRequest request){
        return pancakeOrderService.createOrder(request).intoResponseEntity();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<DataResult<SearchOrderResult>> searchOrder(@PathVariable int id){
        return pancakeOrderService.searchOrder(id).intoResponseEntity();
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<ActionResult> deleteOrder(@PathVariable int id){
        return pancakeOrderService.deleteOrder(id).intoResponseEntity();
    }
    @GetMapping("/report")
    public ResponseEntity<DataResult<Report>> getReport(@RequestParam(required = false) boolean healthy){
        if(healthy)
         return pancakeOrderService.getReport(true).intoResponseEntity();
        return pancakeOrderService.getReport(false).intoResponseEntity();
    }


}
