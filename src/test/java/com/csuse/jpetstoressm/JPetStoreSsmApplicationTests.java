package com.csuse.jpetstoressm;

import com.csuse.jpetstoressm.domain.Account;
import com.csuse.jpetstoressm.domain.CartItem;
import com.csuse.jpetstoressm.domain.Item;
import com.csuse.jpetstoressm.domain.Product;
import com.csuse.jpetstoressm.service.CatalogService;
import com.csuse.jpetstoressm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("com.csuse.jpetstoressm.persistence")
class JPetStoreSsmApplicationTests {

    @Autowired
    private OrderServiceImpl orderService ;

    @Autowired
    private CatalogService catalogService;
    @Test
    void contextLoads() {
        Item item = catalogService.getItem("EST-6");
        System.out.println("ItemId"+":"+item.getProduct().getProductId());
    }
    @Test
    void Test()
    {
        Account account = new Account();
        account.setUsername("j2ee");

    }

}
