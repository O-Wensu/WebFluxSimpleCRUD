package com.example.springreactivecrud.domain.vo;

import com.example.springreactivecrud.domain.Item;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Item item;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public void increment() {
        this.quantity += 1;
    }

    public void decrement() {
        if (quantity > 0) {
            this.quantity -= 1;
        }
    }

    public boolean isOne() {
        if (this.quantity == 1) {
            return true;
        }
        return false;
    }
}
