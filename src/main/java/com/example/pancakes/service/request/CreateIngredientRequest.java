package com.example.pancakes.service.request;

import com.example.pancakes.domain.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIngredientRequest {

    @Pattern(message ="No digits and special characters allowed!", regexp = "^[a-zA-Z\\s]*$")
    private String name;
    @Positive
    private float price;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Pattern(message ="Must be one of the following: base,dressing,stuffing,fruit!", regexp = "\\b(BASE|FRUIT|DRESSING|STUFFING)\\b")
    private String type;
    @NotNull
    private boolean healthy;
    private List<Integer> pancakesIds;
}
