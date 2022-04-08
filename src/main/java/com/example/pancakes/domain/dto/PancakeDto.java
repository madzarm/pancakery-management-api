package com.example.pancakes.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PancakeDto {
    private int id;
    private float price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<IngredientDto> ingredientDtos;
}
