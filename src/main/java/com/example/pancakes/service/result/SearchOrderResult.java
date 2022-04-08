package com.example.pancakes.service.result;

import com.example.pancakes.domain.dto.PancakeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderResult {
    private int orderId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String discount;
    private float totalPrice;
    private List<PancakeDto> pancakeDtos;
}
