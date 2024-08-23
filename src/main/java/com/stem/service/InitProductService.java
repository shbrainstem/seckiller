package com.stem.service;

import com.stem.po.TblZDemoProduct;
import com.stem.po.TblZDemoTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 依赖数据库记录信息，初始胡Redis库存信息。
 */
@Service
public class InitProductService {

    @Autowired
    private ProductService productService;

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 1.查询产品列表
     * 2.查询产品对应的交易流水，获取交易流水中最大的库存消耗，更新到产品表产品序号中
     * 3.将产品库存消耗更新到redis； 剩余库存= 产品初始库存-库存消耗 ；
     */
    @PostConstruct
    public void initInventory(){
        List<TblZDemoProduct> resultList =  initDbRecordFromBreak();
        for(TblZDemoProduct dbProduct :resultList) {
            initRedisRecord(dbProduct);
            checkRedisAndDb(dbProduct);
        }
    }

    public void afterInventor(){
        //清理redis的key，清理database数据
    }
    /**
     * 系统启动时，根据Demo_Request中最晚交易记录评估当前库存剩余
     */
    public List<TblZDemoProduct> initDbRecordFromBreak(){
        //select max RPODUCT_PRO_QUANTITY from tbal_z_demo_request;
        List<TblZDemoProduct> list = productService.getTblZDemoProduct();
        List<TblZDemoProduct>  resultList = new ArrayList<TblZDemoProduct>();
        for(TblZDemoProduct demoIn:list){
            TblZDemoTransaction req =  productService.getMaxRequest(demoIn.getProductNo());
            if(null!= req){
                //利用流水表里最大的序号值，更新curQuantity
                TblZDemoProduct tblZDemoProduct = new TblZDemoProduct();
                tblZDemoProduct.setProductNo(req.getProductNo());
                //tProductCurQuantity当前使用库存= ProductProQuantity 最后一笔成功交易的执行前消耗库存+ 该笔交易本身消耗的库存
                tblZDemoProduct.setProductCurQuantity(req.getProductProQuantity()+req.getProductTraQuantity());
                productService.updateTblZDemoInventory(tblZDemoProduct);

                TblZDemoProduct resultInv =  productService.getTblZDemoProductByProductNo(demoIn.getProductNo());
                resultList.add(resultInv);
            }else {
                //使用Inventory表的的curQuantity
                TblZDemoProduct resultInv =  productService.getTblZDemoProductByProductNo(demoIn.getProductNo());
                resultList.add(resultInv);
            }
        }
        return resultList;
    }



    /**
     * Redis初始化两个库存
     * productInitQuantity: 商品的总库存
     * productCurQuantity: 商品剩余库存
     * @param dbProduct
     */
    public void initRedisRecord(TblZDemoProduct dbProduct){
        String KEY_REMAIN = dbProduct.getProductNo()+"_REMAIN"; //剩余库存
        String KEY_INIT = dbProduct.getProductNo()+"_INIT"; //记录总库存
        String KEY_CAL = dbProduct.getProductNo()+"_CAL";// 库存计数器，从0开始，持续增加，当大于KEY_INIT时结束

        //
        redisTemplate.opsForValue().set(KEY_REMAIN,dbProduct.getProductInitQuantity() - dbProduct.getProductCurQuantity());
        redisTemplate.opsForValue().set(KEY_INIT,dbProduct.getProductInitQuantity());

        Object obj = redisTemplate.opsForValue().get(KEY_REMAIN);
        Object obj1 = redisTemplate.opsForValue().get(KEY_INIT);
        System.out.println("KEY_REMAIN："+KEY_REMAIN+"value="+obj.toString()+" ; KEY_INIT:"+KEY_INIT+";value="+obj1.toString());
        //删除原来的缓存
        redisTemplate.delete(KEY_CAL);
    }

    /**
     *
     * @param dbProduct
     */
    public void checkRedisAndDb( TblZDemoProduct dbProduct){
        TblZDemoProduct result =  productService.getTblZDemoProductByProductNo(dbProduct.getProductNo());
        //初始库存
        Long initQuantity =  result.getProductInitQuantity();
        //剩余库存;首次启动时剩余库存等于初始库存;重新启动后，初始化阶段会根据交易流水信息更新剩余库存
        Long curQuantity = initQuantity - result.getProductCurQuantity();

        System.out.println("初始库存：产品编号 =  "+result.getProductNo() + ";数量 = "+initQuantity.toString());
        System.out.println("剩余库存：产品编号 =  "+result.getProductNo() + ";数量 = "+curQuantity.toString());
        System.out.println("已用库存： "+ (initQuantity-curQuantity));
        try {
            Object redisResult = redisTemplate.opsForValue().get(dbProduct.getProductNo());
            System.out.println("Check Redis Result = " + redisResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
