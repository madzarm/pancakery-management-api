package com.example.pancakes.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PancakeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "pancakeOrder")
    private List<Pancake> pancakes;
    private String description;
    private LocalDate orderTime;

    public void setPancakes(List<Pancake> pancakes) {
        this.pancakes = pancakes;
        for(Pancake pancake : pancakes){
            pancake.setPancakeOrder(this);
        }
    }
    public void removeAllPancakes() {
        for(Pancake pancake : this.pancakes){
            pancake.setPancakeOrder(null);
        }
    }
}
