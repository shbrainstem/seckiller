package com.stem.controller;

import com.stem.po.TblZDemoProduct;
import com.stem.service.ProductService;
import com.stem.service.RedisService;
import com.stem.vo.SecondKillRequest;
import com.stem.vo.SecondKillRespond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SecondKillController {

    @Autowired
    ProductService productService;
    @Autowired
    RedisService redisService;

    @PostMapping("/sk")
    @ResponseBody
    public String secondKill(@RequestBody SecondKillRequest request) {
        System.out.println(request.toString());
        SecondKillRespond respond =  redisService.secondKill(request);
        return respond.toString();
    }

    public List<TblZDemoProduct> getTblZDemoInventory(){
        return productService.getTblZDemoProduct();
    }

    public TblZDemoProduct getTblZDemoInventoryById(Long id){
        return productService.getTblZDemoProductById(id);
    }



}
