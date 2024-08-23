package com.stem.db;

import com.stem.abs.AbsDemo;
import com.stem.controller.SecondKillController;
import com.stem.po.TblZDemoProduct;
import com.stem.po.TblZDemoTransaction;
import com.stem.service.InitProductService;
import com.stem.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * 数据库表Product,操作的Test
 */
@SpringBootTest
@ContextConfiguration
public class TestDemoProduct extends AbsDemo {

    @Autowired
    private SecondKillController secondKillController;

    @Autowired
    private ProductService productService;

    @MockBean
    private InitProductService initProductService;

    @Test
    void insertRequest(){
        //delete inventory
        TblZDemoTransaction tblZDemoRequest = new TblZDemoTransaction();
        {
            String sysCur = System.currentTimeMillis()+"";
            tblZDemoRequest.setUserId("USR_ID_"+sysCur);
            tblZDemoRequest.setRequestSeq("REQ_SEQ_"+sysCur);
            tblZDemoRequest.setRequestTime("REQ_TIME_"+sysCur);
            tblZDemoRequest.setUserName("USER_NAME_"+sysCur);
            tblZDemoRequest.setProductNo("0002");
        }
        //init product_inventory
        productService.insertReqeust(tblZDemoRequest);
    }

    @Test
    void initDbRecord(){
        List<TblZDemoProduct> list = productService.getTblZDemoProduct();
        //delete inventory
        if(!list.isEmpty()){
            System.out.println("ListSize = "+ list.size());
            productService.deleteProduct(defaultPO.getProductNo());
        }
        //init product_inventory
        productService.insertProduct(defaultPO);
    }

    @Test
    void testDbDefault(){
        List<TblZDemoProduct> result = secondKillController.getTblZDemoInventory();
        for(TblZDemoProduct it:result){
            System.out.println(it.toString());
        }
    }

    @Test
    void testDbById(){
        TblZDemoProduct result = secondKillController.getTblZDemoInventoryById(1L);
        System.out.println(result);
    }

    @Test
    void testQueryListInventory(){
        List<TblZDemoProduct>  result = secondKillController.getTblZDemoInventory();
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        //2147483647 2147万
        System.out.println(Long.MAX_VALUE);
        //9223372036854775807

    }
}
