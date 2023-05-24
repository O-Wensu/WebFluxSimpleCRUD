package com.example.springreactivecrud.service;

import com.example.springreactivecrud.domain.Cart;
import com.example.springreactivecrud.domain.Item;
import com.example.springreactivecrud.domain.vo.ItemRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {
    public Mono<Item> registerItem(ItemRequestDto itemRequestDto);
    public Flux<Item> itemSearchName(String name, String origin, boolean isSuit);
    public Mono<Cart> delToCartCount(String cartId, String id);
    public Mono<Cart> delToCartAll(String cartId, String id);
    public Mono<Cart> addToCart(String cartId, String id);
}
