package com.example.springreactivecrud;

import com.example.springreactivecrud.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringReactiveCrudApplication {

	@Autowired
	MongoOperations mongoOperations;

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveCrudApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startApp() {
		mongoOperations.dropCollection(Item.class);
		Item item1 = new Item("가죽 가방", "korea", 42000);
		Item item2 = new Item("삼성 모니터 32인치", "korea", 360000);
		Item item3 = new Item("굿즈 키링", "us", 11000);
		Item item4 = new Item("삼성 갤럭시s22", "korea", 230000);
		Item item5 = new Item("애플 이어폰 에어팟", "us", 220000);
		Item item6 = new Item("코딩 신", "유비쿼터스", 1);
		Item item7 = new Item("삼성 이어폰 버즈", "korea", 160000);
		List<Item> itemList = Arrays.asList(item1, item2, item3, item4, item5, item6, item7);
		for (Item item : itemList) {
			mongoOperations.save(item);
		}
	}
}
