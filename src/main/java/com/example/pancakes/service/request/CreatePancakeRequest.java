package com.example.pancakes.service.request;

import com.example.pancakes.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePancakeRequest {

    @NotEmpty
    List<Integer> ingredientsIds;

}
