package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import com.example.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;
    @Test
    @DisplayName("상품테스트")
    public void createItemTest(){
        for(int i=1; i<10; i ++) {
            Item item = new Item();
            item.setItemName("A" + i);
            item.setItemDetail("AAA" +i);
            item.setPrice(100 + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(10);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품테스트2")
    public void createItemList2(){
        for(int i=1; i<5; i ++) {
            Item item = new Item();
            item.setItemName("테스트상품" + i);
            item.setItemDetail("테스트상품 상세 설명" +i);
            item.setPrice(1000 + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(10);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6; i<10; i ++) {
            Item item = new Item();
            item.setItemName("테스트상품" + i);
            item.setItemDetail("테스트상품 상세 설명" +i);
            item.setPrice(1000 + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemName("A");
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("AAA");
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }



    @Test
    @DisplayName("@Querydsl 조회 테스트")
    public void queryDslTest(){
        this.createItemTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "AAA" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Querydsl2 조회 테스트")
    public void queryDsl2_Test(){
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트상품 상세 설명";
        int price = 1000;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPage = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total :" + itemPage.getTotalElements());

        List<Item> itemList = itemPage.getContent();
        for (Item resultitem: itemList){
            System.out.println(resultitem.toString());
        }
    }
}