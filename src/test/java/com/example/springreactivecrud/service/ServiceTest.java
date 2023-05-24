package com.example.springreactivecrud.service;

import com.example.springreactivecrud.domain.Item;
import com.example.springreactivecrud.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    public int itemCount;

    @BeforeEach
    void clean() {
        StepVerifier.create(
                itemReactiveRepository.deleteAll()
        ).verifyComplete();

        Item item1 = new Item("가죽 가방", "korea", 42000);
        Item item2 = new Item("삼성 모니터 32인치", "korea", 360000);
        Item item3 = new Item("굿즈 키링", "us", 11000);
        Item item4 = new Item("삼성 갤럭시s22", "korea", 230000);
        Item item5 = new Item("애플 이어폰 에어팟", "us", 220000);
        Item item6 = new Item("코딩 신", "유비쿼터스", 1);
        Item item7 = new Item("삼성 이어폰 버즈", "korea", 160000);
        List<Item> itemList = Arrays.asList(item1, item2, item3, item4, item5, item6, item7);
        itemCount = itemList.size();

        StepVerifier.create(
                        itemReactiveRepository.saveAll(itemList)
                ).recordWith(ArrayList::new)
                .expectNextCount(itemCount)
                .consumeRecordedWith(items -> {
                    items.forEach(item -> System.out.println(item));
                }).verifyComplete();
    }

    @Test
    public void itemSearchNameT() {
        StepVerifier.create(
                //isSuit가 true: name에 가죽 가방 일치 AND origin이 korea 일치
                cartService.itemSearchName("가죽 가방", "korea", true)
        ).expectNextMatches(item -> {
            assertThat(item.getId()).isNotNull();
            assertThat(item.getName()).isEqualTo("가죽 가방");
            assertThat(item.getOrigin()).isEqualTo("korea");
            assertThat(item.getPrice()).isEqualTo(42000);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemSearchNameF() {
        StepVerifier.create(
                //isSuit가 false: name에 신이 포함 AND origin이 유비쿼터스가 포함
                cartService.itemSearchName("신", "유비쿼터스", false).count()
        ).expectNextMatches(cnt -> {
            assertThat(cnt).isEqualTo(1);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemSearchNameNull() {
        StepVerifier.create(
                //name에 이어폰 포함, origin 무관
                cartService.itemSearchName("이어폰", null, false).count()
        ).expectNextMatches(cnt -> {
            assertThat(cnt).isEqualTo(2);
            return true;
        }).verifyComplete();
    }
}
