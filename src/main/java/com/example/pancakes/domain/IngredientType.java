package com.example.pancakes.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum IngredientType {
    BASE("BASE"),
    STUFFING("STUFFING"),
    DRESSING("DRESSING"),
    FRUIT("FRUIT");

    String value;

    IngredientType(String value) {
        this.value =value;
    }

}
