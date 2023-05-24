package com.example.springreactivecrud.service;

import com.example.springreactivecrud.domain.Cart;
import com.example.springreactivecrud.domain.Item;
import com.example.springreactivecrud.domain.vo.CartItem;
import com.example.springreactivecrud.repository.CartReactiveRepository;
import com.example.springreactivecrud.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final ItemReactiveRepository itemReactiveRepository;
    private final CartReactiveRepository cartReactiveRepository;

    @Override
    public Flux<Item> itemSearchName(String name, String origin, boolean isSuit) {
        Item item = Item.builder()
                .name(name)
                .price(0)
                .origin(origin)
                .build();

        ExampleMatcher matcher = (isSuit
                ? ExampleMatcher.matchingAll().withIgnorePaths("price")
                : ExampleMatcher.matching()
                .withMatcher("name", contains())
                .withMatcher("origin", contains())
                .withIgnorePaths("price"));

        Example<Item> itemExample = Example.of(item, matcher);

        return itemReactiveRepository.findAll(itemExample).log();
    }

    @Override
    public Mono<Cart> delToCartCount(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                        .map(cartItem -> {
                            if (cartItem.isOne()) {
                                cart.removeItem(cartItem);
                            } else {
                                cartItem.decrement();
                            }
                            return Mono.just(cart).log();
                        }).orElseGet(() -> {
                            //삭제하려는 상품이 카트에 없으면 아무것도 하지 않음
                            return Mono.empty();
                        })).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> delToCartAll(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                        .map(cartItem -> {
                            cart.removeItem(cartItem);
                            return Mono.just(cart).log();
                        }).orElseGet(() -> {
                            return Mono.empty();
                        })).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> addToCart(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                        .map(cartItem -> {
                            //카트에 Item이 있는 경우 개수만 +1
                            cartItem.increment();
                            return Mono.just(cart).log();
                        }).orElseGet(() -> itemReactiveRepository.findById(id)
                                //카트에 Item이 없는 경우 CartItem을 새로 생성해서 카트에 담기
                                .map(CartItem::new)
                                .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                                .map(cartItem -> cart))).flatMap(cart -> cartReactiveRepository.save(cart));
    }
}
