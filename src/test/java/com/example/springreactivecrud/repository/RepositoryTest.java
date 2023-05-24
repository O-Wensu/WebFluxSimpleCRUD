package com.example.springreactivecrud.repository;

import com.example.springreactivecrud.domain.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
public class RepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    CartReactiveRepository cartReactiveRepository;

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
    public void itemRepositoryCount() {
        StepVerifier.create(
                itemReactiveRepository.findAll().count()
        ).expectNextMatches(cnt -> {
            assertThat(cnt).isEqualTo(itemCount);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemSearchByName() {
        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("삼성")
        ).expectNextMatches(item -> {
            System.out.println(item.toString());
            return true;
        }).expectNextCount(1).verifyComplete(); //이름에 삼성이 들어가는 Item은 2개이므로 기대값은 2-1 = 1
    }

    @Test
    public void itemFindByName() {
        StepVerifier.create(
                itemReactiveRepository.findByName("굿즈 키링")
        ).expectNextMatches(item -> {
            assertThat(item.getId()).isNotNull();
            assertThat(item.getName()).isEqualTo("굿즈 키링");
            assertThat(item.getOrigin()).isEqualTo("us");
            assertThat(item.getPrice()).isEqualTo(11000);
            return true;
        }).verifyComplete();
    }
}
