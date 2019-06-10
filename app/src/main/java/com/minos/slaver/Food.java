package com.minos.slaver;

public class Food {

    private String foodName;
    private int foodId;

    public Food(String foodName, int foodId) {
        this.foodName = foodName;
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodId() {
        return foodId;
    }
}
