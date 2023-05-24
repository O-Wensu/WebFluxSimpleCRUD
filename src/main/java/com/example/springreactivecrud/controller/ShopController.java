package com.example.springreactivecrud.controller;

import com.example.springreactivecrud.domain.Cart;
import com.example.springreactivecrud.domain.vo.ItemRequestDto;
import com.example.springreactivecrud.repository.CartReactiveRepository;
import com.example.springreactivecrud.repository.ItemReactiveRepository;
import com.example.springreactivecrud.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/item")
    public Mono<Rendering> itemRegisterPage() {
        return Mono.just(Rendering.view("register").build());
    }

    @PostMapping("/item")
    public Mono<String> registerItem(@RequestBody ItemRequestDto itemRequestDto) {
        return cartService.registerItem(itemRequestDto)
                .thenReturn("redirect:/");
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
