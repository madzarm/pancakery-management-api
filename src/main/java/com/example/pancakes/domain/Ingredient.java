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
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String name;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IngredientType type;
    @Column(nullable = false)
    private boolean healthy;
    @ManyToMany
    @JoinTable(
            name = "pancake_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "pancake_id"))
    private List<Pancake> pancakes = new ArrayList<>();

    public void addPancake(Pancake pancake) {
        this.pancakes.add(pancake);
        pancake.getIngredients().add(this);
    }

    public void removePancake(Pancake pancake) {
        this.pancakes.remove(pancake);
        pancake.getIngredients().remove(this);
    }
}
