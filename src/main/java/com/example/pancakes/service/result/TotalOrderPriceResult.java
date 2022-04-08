package com.example.pancakes.service.result;

import com.example.pancakes.domain.Pancake;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalOrderPriceResult {
    private float totalPrice;
    private String typeOfDiscount;
    private List<Pancake> healthyPancakes;

    public TotalOrderPriceResult(float totalPrice, String typeOfDiscount) {
        this.totalPrice = totalPrice;
        this.typeOfDiscount = typeOfDiscount;
    }
}
