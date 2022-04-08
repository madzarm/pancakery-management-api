package com.example.pancakes.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pancake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany(mappedBy = "pancakes")
    private List<Ingredient> ingredients = new ArrayList<>();
    @ManyToOne
    private PancakeOrder pancakeOrder;

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
        ingredient.getPancakes().add(this);
    }
    public void removeIngredient(Ingredient ingredient){
        this.ingredients.remove(ingredient);
        ingredient.getPancakes().remove(this);
    }


    public float calculatePrice(){
        float price = 0;
        for(Ingredient ingredient : this.ingredients){
            price+=ingredient.getPrice();
        }
        return price;
    }

}
