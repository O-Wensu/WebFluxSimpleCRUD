package com.example.springreactivecrud.domain;

import com.example.springreactivecrud.domain.vo.CartItem;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    private String id;

    private List<CartItem> cartItems;

    public Cart(String id) {
        this(id, new ArrayList<CartItem>());
    }

    public void removeItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
    }
}
