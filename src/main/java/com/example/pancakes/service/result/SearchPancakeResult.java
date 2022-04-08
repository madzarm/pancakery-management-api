package com.example.pancakes.service.result;

import com.example.pancakes.domain.dto.PancakeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchPancakeResult {
    List<PancakeDto> pancakeDtos;
}
