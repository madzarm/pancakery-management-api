package com.example.pancakes.service.result;

import com.example.pancakes.domain.dto.IngredientDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class SearchIngredientResult {
    List<IngredientDto> ingredientDtos;
}
