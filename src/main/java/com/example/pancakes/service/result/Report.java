package com.example.pancakes.service.result;

import com.example.pancakes.domain.dto.IReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class Report{
    private int id;
    private String name;
    private float price;
    private boolean healthy;
    private int value_Occurrence;

    public Report(int id, String name, float price, boolean healthy, int value_Occurrence) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.healthy = healthy;
        this.value_Occurrence = value_Occurrence;
    }


}
