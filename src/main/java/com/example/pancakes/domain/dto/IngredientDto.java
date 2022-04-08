package com.example.pancakes.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IngredientDto {
    private int id;
    private String name;
    private float price;
    private String healthy;
}
