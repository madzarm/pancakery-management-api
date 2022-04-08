package com.example.pancakes.service;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.IngredientType;
import com.example.pancakes.domain.Pancake;
import com.example.pancakes.domain.PancakeOrder;
import com.example.pancakes.domain.dto.IReport;
import com.example.pancakes.domain.dto.PancakeDto;
import com.example.pancakes.domain.repository.IngredientRepository;
import com.example.pancakes.domain.repository.PancakeOrderRepository;
import com.example.pancakes.domain.repository.PancakeRepository;
import com.example.pancakes.service.request.CreateOrderRequest;
import com.example.pancakes.service.result.ActionResult;
import com.example.pancakes.service.result.DataResult;
import com.example.pancakes.service.result.Report;
import com.example.pancakes.service.result.SearchOrderResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PancakeOrderServiceTest {

    @Mock private PancakeRepository pancakeRepository;
    @Mock private PancakeOrderRepository pancakeOrderRepository;
    private PancakeOrderService underTest;

    @BeforeEach
    void setUp(){
        underTest = new PancakeOrderService(pancakeRepository,pancakeOrderRepository);
    }

    @Test
    void willReturnIfRequestsPancakesAlreadyHaveOrder() {
        int id = 1;
        List<Integer> pancakeIds = List.of(id);
        Pancake pancake = Pancake.builder().id(id).pancakeOrder(new PancakeOrder(1,null,"desc",null)).build();
        CreateOrderRequest request = CreateOrderRequest.builder().pancakesIds(pancakeIds).build();

        given(pancakeRepository.findAllWithOrders(request.getPancakesIds()))
                .willReturn(List.of(pancake));

        ActionResult result = underTest.createOrder(request);

        assertThat(result.getMessage()).startsWith("Can not create order");
    }

    @Test
    void canCreateOrder() {
        int id = 1;
        LocalDate orderTime = LocalDate.now();
        String description = "desc";
        List<Integer> pancakeIds = List.of(id);
        Pancake pancake = Pancake.builder()
                .id(id).build();
        CreateOrderRequest request = CreateOrderRequest.builder().
                pancakesIds(pancakeIds)
                .orderTime(orderTime)
                .description(description).build();
        List<Pancake> pancakes = List.of(pancake);
        PancakeOrder pancakeOrder = PancakeOrder.builder()
                .id(0)
                .orderTime(orderTime)
                .pancakes(pancakes)
                .description(description)
                .build();

        given(pancakeRepository.findAllById(pancakeIds))
                .willReturn(pancakes);

        underTest.createOrder(request);

        ArgumentCaptor<PancakeOrder> argumentCaptor =
                ArgumentCaptor.forClass(PancakeOrder.class);
        verify(pancakeOrderRepository).save(argumentCaptor.capture());

        PancakeOrder capturedPancakeOrder = argumentCaptor.getValue();

        assertThat(capturedPancakeOrder).isEqualTo(pancakeOrder);

    }

    @Test
    void canSearchOrder() {
        int id = 1;
        LocalDate orderTime = LocalDate.now();
        String description = "desc";
        SearchOrderResult result = new SearchOrderResult();
        Pancake pancake = Pancake.builder()
                .id(id)
                .ingredients(List.of(new Ingredient(1,"Nutella",10.5f, IngredientType.BASE,false,
                        null))).build();
        List<Pancake> pancakes = List.of(pancake);
        PancakeOrder pancakeOrder = PancakeOrder.builder()
                .id(id)
                .orderTime(orderTime)
                .pancakes(pancakes)
                .description(description)
                .build();
        PancakeDto pancakeDto = new PancakeDto();
        pancakeDto.setId(id);
        pancakeDto.setPrice(10.5f);

        result.setOrderId(id);
        result.setPancakeDtos(List.of(pancakeDto));//check
        result.setTotalPrice(10.5f);
        result.setDescription(description);

        given(pancakeOrderRepository.findById(id))
                .willReturn(Optional.ofNullable(pancakeOrder));


        DataResult dataResult = underTest.searchOrder(id);
        assertThat(dataResult.getData()).isEqualTo(result);

    }

    @Test
    void canDeleteOrder() {

        int id = 1;
        LocalDate orderTime = LocalDate.now();
        String description = "desc";
        Pancake pancake = Pancake.builder()
                .id(id)
                .ingredients(List.of(new Ingredient(1,"Nutella",10.5f, IngredientType.BASE,false,
                        null))).build();
        List<Pancake> pancakes = List.of(pancake);
        PancakeOrder pancakeOrder = PancakeOrder.builder()
                .id(id)
                .orderTime(orderTime)
                .pancakes(pancakes)
                .description(description)
                .build();
        underTest.deleteOrder(id);
        given(pancakeOrderRepository.findById(id))
                .willReturn(Optional.ofNullable(pancakeOrder));

        ArgumentCaptor<PancakeOrder> argumentCaptor = ArgumentCaptor.forClass(PancakeOrder.class);
        verify(pancakeOrderRepository).delete(argumentCaptor.capture());

        PancakeOrder capturedPancakeOrder = argumentCaptor.getValue();

        assertThat(capturedPancakeOrder).isEqualTo(pancakeOrder);
    }

    @Test
    void getReport() {
    }

    @Test
    void round() {
    }
}