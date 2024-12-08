package com.bsuir.frontendAipos7.dto;

import lombok.Data;

@Data
public class CatDto {
    private Long id;

    private String name;

    private int age;

    private int weight;

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                '}';
    }
}