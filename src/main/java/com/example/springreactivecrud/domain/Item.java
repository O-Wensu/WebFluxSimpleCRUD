package com.example.springreactivecrud.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    private String id;
    private String name;
    private String origin;
    private int price;

    public Item(String name, String origin, int price) {
        this.name = name;
        this.origin = origin;
        this.price = price;
    }
}
