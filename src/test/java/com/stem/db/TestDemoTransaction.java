package com.stem.db;

import com.stem.po.TblZDemoTransaction;
import com.stem.service.InitProductService;
import com.stem.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

/**
 * 数据库表Transaction的test
 */
@SpringBootTest
@ContextConfiguration
public class TestDemoTransaction {

    @Autowired
    private ProductService productService;

    @MockBean
    private InitProductService initProductService;

    @Test
    void insertRequet(){
        //delete inventory
        TblZDemoTransaction tblZDemoRequest = new TblZDemoTransaction();
        {
            String sysCur = System.currentTimeMillis()+"";
            tblZDemoRequest.setUserId("USR_ID_"+sysCur);
            tblZDemoRequest.setRequestSeq("REQ_SEQ_"+sysCur);
            tblZDemoRequest.setRequestTime("REQ_TIME_"+sysCur);
            tblZDemoRequest.setUserName("USER_NAME_"+sysCur);
            tblZDemoRequest.setProductNo("0003");
            tblZDemoRequest.setProductProQuantity(50L);
            tblZDemoRequest.setProductTraQuantity(49L);
        }
        //init product_inventory
        productService.insertReqeust(tblZDemoRequest);
    }

    @Test
    void getMaxRecord(){
        TblZDemoTransaction tblZDemoRequest = new TblZDemoTransaction();
        tblZDemoRequest.setProductNo("0003");
        TblZDemoTransaction res = productService.getMaxRequest(tblZDemoRequest.getProductNo());
        System.out.println(res);
    }

}
