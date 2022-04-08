package com.example.pancakes.service;

import com.example.pancakes.domain.Ingredient;
import com.example.pancakes.domain.Pancake;
import com.example.pancakes.domain.PancakeOrder;
import com.example.pancakes.domain.dto.IReport;
import com.example.pancakes.domain.dto.PancakeDto;
import com.example.pancakes.domain.repository.PancakeOrderRepository;
import com.example.pancakes.domain.repository.PancakeRepository;
import com.example.pancakes.service.request.CreateOrderRequest;
import com.example.pancakes.service.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PancakeOrderService {

    private final PancakeRepository pancakeRepository;
    private final PancakeOrderRepository pancakeOrderRepository;

    @Autowired
    public PancakeOrderService(PancakeRepository pancakeRepository, PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeRepository = pancakeRepository;
        this.pancakeOrderRepository = pancakeOrderRepository;
    }

    public ActionResult createOrder(CreateOrderRequest request){

        List<Integer> pancakeIds = request.getPancakesIds();
        List<Pancake> pancakesWithOrders = pancakeRepository.findAllWithOrders(pancakeIds);
        StringBuilder s = new StringBuilder();
        if(!pancakesWithOrders.isEmpty()) {
            for (Pancake pancake : pancakesWithOrders) {
                if (s.length() == 0)
                    s.append(pancake.getId());
                else
                    s.append(", " + pancake.getId());
            }
            return new ActionResult(true, "Can not create order because pancakes with ids: " +
                    "[" + s + "] are already in others orders");
        }

        List<Pancake> pancakes = pancakeRepository.findAllById(pancakeIds);
        PancakeOrder order = new PancakeOrder();
        order.setOrderTime(request.getOrderTime());
        order.setPancakes(pancakes);
        if(Objects.nonNull(request.getDescription()))
            order.setDescription(request.getDescription());


        pancakeOrderRepository.save(order);
        return new ActionResult(true,"Order successfully created!");
    }

    public DataResult<SearchOrderResult> searchOrder(int id) {

        Optional<PancakeOrder> orderOptional = pancakeOrderRepository.findById(id);
        if(orderOptional.isEmpty())
            return new DataResult<>(false,"Order with this id does not exist",null);

        PancakeOrder order = orderOptional.get();
        TotalOrderPriceResult priceResult = calculateTotalOrderPrice(order);

        SearchOrderResult result = new SearchOrderResult();
        result.setOrderId(order.getId());
        result.setPancakeDtos(convertPancakesToDto(order.getPancakes(),priceResult));
        result.setTotalPrice(round(priceResult.getTotalPrice(),2));
        if(Objects.nonNull(order.getDescription())){
            result.setDescription(order.getDescription());
        }
        if(!priceResult.getTypeOfDiscount().equals("0%"))
            result.setDiscount(priceResult.getTypeOfDiscount());

        return new DataResult<>(true,null,result);
    }

    public ActionResult deleteOrder(int id){

        Optional<PancakeOrder> orderOptional = pancakeOrderRepository.findById(id);
        if(orderOptional.isEmpty())
            return new ActionResult(false,"Order with that id does not exist!");

        PancakeOrder order = orderOptional.get();
        order.removeAllPancakes();
        pancakeOrderRepository.delete(order);

        return new ActionResult(true,"Order successfully deleted!");

    }

    public DataResult<Report> getReport(boolean healthy){
        LocalDate from = LocalDate.now().minusMonths(1);
        IReport iReport;
        if (healthy)
            iReport = pancakeOrderRepository.findMostRepeatedHealthyIngredientInLastMonth(from);
        else
            iReport = pancakeOrderRepository.findMostRepeatedIngredientInLastMonth(from);

        Report report = Report.builder()
                .id(iReport.getId())
                .name(iReport.getName())
                .value_Occurrence(iReport.getValue_Occurrence())
                .price(iReport.getPrice())
                .healthy(iReport.getHealthy()).build();
        return new DataResult<>(true,null,report);

    }

    private List<PancakeDto> convertPancakesToDto(List<Pancake> pancakes, TotalOrderPriceResult priceResult){
            return pancakes.stream().map(pancake -> {
                PancakeDto pancakeDto = new PancakeDto();
                pancakeDto.setId(pancake.getId());
                pancakeDto.setPrice(round(calculatePancakePrice(priceResult,pancake),2));
                return pancakeDto;
            }).collect(Collectors.toList());

    }

    private float calculatePancakePrice(TotalOrderPriceResult result, Pancake pancake){

        float price = 0;
        for(Ingredient ingredient : pancake.getIngredients()){
            price += ingredient.getPrice();
        }

        if(result.getTypeOfDiscount().equals("healthy") && result.getHealthyPancakes().contains(pancake)){
            return (float)(price * 0.85);
        }
        else if(result.getTypeOfDiscount().equals("0%"))
            return price;
        else if(result.getTypeOfDiscount().equals("5%"))
            return (float)(price * 0.95);

        return (float)(price*0.9);
    }

    private TotalOrderPriceResult calculateTotalOrderPrice(PancakeOrder order){
        double sum = 0;
        float healthySum = 0;
        String typeOfDiscount = null;
        List<Pancake> pancakes = new ArrayList<>();

        for(Pancake pancake : order.getPancakes()){
            float pancakeSum = 0;
            float numOfHealthyIngredients = 0;
            for(Ingredient ingredient : pancake.getIngredients()){
                pancakeSum += ingredient.getPrice();
                if(ingredient.isHealthy())
                    numOfHealthyIngredients++;
            }
            if(numOfHealthyIngredients / pancake.getIngredients().size() > 0.75){
                healthySum += pancakeSum * 0.85;
                sum += pancakeSum;
                pancakes.add(pancake);
            } else {
                healthySum += pancakeSum;
                sum += pancakeSum;
            }
        }
        if(sum < 100){
            typeOfDiscount = "0%";
        }
        if(sum>100&&sum<=200) {
            sum = sum * 0.95;
            typeOfDiscount = "5%";
        }
        else if (sum > 200) {
            sum = sum * 0.9;
            typeOfDiscount = "10%";
        }
        if(healthySum < sum)
            return new TotalOrderPriceResult(healthySum,"healthy",pancakes);

        return new TotalOrderPriceResult((float)sum,typeOfDiscount,pancakes);
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
