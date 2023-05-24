package com.example.springreactivecrud.controller;

import com.example.springreactivecrud.domain.Cart;
import com.example.springreactivecrud.repository.CartReactiveRepository;
import com.example.springreactivecrud.repository.ItemReactiveRepository;
import com.example.springreactivecrud.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ItemReactiveRepository itemReactiveRepository;
    private final CartReactiveRepository cartReactiveRepository;
    private final CartService cartService;

    @GetMapping("/")
    public Mono<Rendering> home() {
        return Mono.just(Rendering.view("shop")
                .modelAttribute("items", itemReactiveRepository.findAll())
                .modelAttribute("cart", cartReactiveRepository.findById("My Cart")
                        .defaultIfEmpty(new Cart("My Cart"))).build());
    }

    @PostMapping("/cart/item/{id}")
    public Mono<String> addCart(@PathVariable String id) {
        return cartService.addToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/cart/item/deleteAll/{id}")
    public Mono<String> delToCartAll(@PathVariable String id) {
        return cartService.delToCartAll("My Cart", id)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/cart/item/delete/{id}")
    public Mono<String> delToCartItem(@PathVariable String id) {
        return cartService.delToCartCount("My Cart", id)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/item/delete/{id}")
    public Mono<String> deleteItem(@PathVariable String id) {
        return itemReactiveRepository.deleteById(id)
                .thenReturn("redirect:/");
    }
}
