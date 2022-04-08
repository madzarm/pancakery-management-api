package com.example.pancakes.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIngredientRequest {
    @NotNull
    private List<Integer> pancakesIds;
}
