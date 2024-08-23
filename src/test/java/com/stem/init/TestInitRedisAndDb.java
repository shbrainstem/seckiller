package com.stem.init;

import com.stem.abs.AbsDemo;
import com.stem.controller.SecondKillController;
import com.stem.dao.TblZDemoTestMapper;
import com.stem.po.TblZDemoProduct;
import com.stem.po.TblZDemoTransaction;
import com.stem.service.InitProductService;
import com.stem.service.ProductService;
import com.stem.vo.SecondKillRequest;
import com.stem.vo.SecondKillRespond;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 *  1.利用defaultPo初始化Product表，清空transaction表
 *  2.模拟服务中断情况下重启服务场景：根据transaction表的Max值，重新初始化数据库Product表和redis数据
 */
@SpringBootTest
@ContextConfiguration
public class TestInitRedisAndDb extends AbsDemo {


    @Autowired
    private SecondKillController demoInventoryController;

    @Autowired
    private ProductService productService;
    @Autowired
    private TblZDemoTestMapper tblZDemoRequestTestMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @MockBean //屏蔽 @PostConstruct
    private InitProductService initInventoryService;

    /**
     * 从最開始头初始化 defaultPo产品
     */
    @Test
    public void initDB(){
        String defaultProductNo = defaultPO.getProductNo();
        initDB(defaultProductNo);
    }

    /**
     * 从最開始头初始化 defaultPo产品
     */
    public void initDB(String productNo){
        delProductAndTransaction(productNo);
        initProductInfo(productNo);
        initDBAfterIntercept();
    }
    /**
     *  redis中斷后初始化
     */
    @Test
    public void initDBAfterIntercept(){
        String defaultProductNo = defaultPO.getProductNo();
        initDBAfterIntercept(defaultProductNo);
    }

    public void initDBAfterIntercept(String productNo){
        String defaultProductNo = productNo;
        List<TblZDemoProduct> resultList =  initDbRecordFromBreak();
        for(TblZDemoProduct dbInventory :resultList) {
            if(defaultProductNo.equals(dbInventory.getProductNo())) {
                initRedisRecord(dbInventory);
                checkRedisAndDb(dbInventory);
            }
        }
    }



    public void initProductInfo(String defaultProductNo){
        defaultPO.setProductNo(defaultProductNo);
        productService.insertProduct(defaultPO);
    }

    /**
     * 清理 defaultPo.ProductNo相关的Inventory，request表的信息
     */
    public void delProductAndTransaction(String productNo){
        String prod1=defaultPO.getProductNo();
        if(StringUtils.isBlank(productNo)){
            prod1 = productNo;
        }
        int result = tblZDemoRequestTestMapper.deleteInvByProductNo(prod1);
        System.out.println(result);
        int result2 = tblZDemoRequestTestMapper.deleteReqByProductNo(prod1);
        System.out.println(result2);
        int result3 = tblZDemoRequestTestMapper.deleteReqByProductNo(prod1+"_ERROR");
        System.out.println(result3);

    }

    @Test
    public void delProductAndTransaction(){
        delProductAndTransaction(defaultPO.getProductNo());
    }


    @AfterEach
    public void afterInventor(){
        //清理redis的key，清理database数据
    }
    /**
     * 利用redis控制库存
     * @param request
     * @return
     */
    public SecondKillRespond secondKill(SecondKillRequest request){
        SecondKillRespond respond = new SecondKillRespond();
        String KEY_INIT = request.getProductNo()+"_INIT";   //初始库存
        String KEY_REMAIN = request.getProductNo()+"_REMAIN";// 表示剩余的库存 ---可以通过修改redis值动态增加库存
        String KEY_CAL = request.getProductNo()+"_CAL";// 本次消耗的库存

        Object initInventry  = redisTemplate.opsForValue().get(KEY_INIT);
        Object resultRemain  = redisTemplate.opsForValue().get(KEY_REMAIN);
        Object result  = redisTemplate.opsForValue().increment(KEY_CAL,request.getObtaindQuantity());

        System.out.println("本次启动截止当前交易消耗库存(CURRENT_Inventory)="+result.toString()+";本次启动之前的剩余库存(REMAIN_Inventory)="+resultRemain.toString());

        TblZDemoTransaction tblZDemoTransaction = new TblZDemoTransaction();
        {
            tblZDemoTransaction.setRequestSeq(request.getRequestSeq());
            tblZDemoTransaction.setProductNo(request.getProductNo());
            tblZDemoTransaction.setUserName(request.getUserName());
            tblZDemoTransaction.setUserId(request.getUserId());
            tblZDemoTransaction.setRequestTime(request.getRequestTime());

            //计算该笔交易获取库存时的原库存; 比如：初始库存100 ；第一次运行扣减了60个库存；重启服务之后， init=100;remain=40;
            Long cur = (Long) initInventry-(Long)resultRemain+(Long)result;
            tblZDemoTransaction.setProductProQuantity(cur);

            //本次得到的库存
            tblZDemoTransaction.setProductTraQuantity(request.getObtaindQuantity());
        }
        if((Integer)resultRemain>=(Long)result){
            tblZDemoTransaction.setProductBatchSerialNum(tblZDemoTransaction.getProductNo()+"_"+tblZDemoTransaction.getProductProQuantityLong());
            tblZDemoTransaction.setResult("SUCCESS");
            try{
                productService.insertReqeust(tblZDemoTransaction);
            }catch (Exception e){
                e.printStackTrace();
                respond.setResult("FALSE");
                return respond;
            }
            return respond;
        }else{
            respond.setResult("FALSE");
            System.out.println("SecondKill fail,respond="+respond.toString());
        }
        return respond;
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
     * 请求对象有两个值
     * productInitQuantity: 商品的总库存
     * productCurQuantity: 商品剩余库存
     * @param dbInventory
     */
    public void initRedisRecord(TblZDemoProduct dbInventory){
        String KEY_REMAIN = dbInventory.getProductNo()+"_REMAIN"; //剩余库存
        String KEY_INIT = dbInventory.getProductNo()+"_INIT"; //记录总库存
        String KEY_CAL = dbInventory.getProductNo()+"_CAL";// 库存计数器，从0开始，持续增加，当大于KEY_INIT时结束

        //
        redisTemplate.opsForValue().set(KEY_REMAIN,dbInventory.getProductInitQuantity() - dbInventory.getProductCurQuantity());
        redisTemplate.opsForValue().set(KEY_INIT,dbInventory.getProductInitQuantity());

        Object obj = redisTemplate.opsForValue().get(KEY_REMAIN);
        Object obj1 = redisTemplate.opsForValue().get(KEY_INIT);
        System.out.println("KEY_REMAIN："+KEY_REMAIN+"value="+obj.toString()+" ; KEY_INIT:"+KEY_INIT+";value="+obj1.toString());
        //删除原来的缓存
        redisTemplate.delete(KEY_CAL);
    }

    /**
     *
     * @param dbInventory
     */
    public void checkRedisAndDb( TblZDemoProduct dbInventory){
        TblZDemoProduct result =  productService.getTblZDemoProductByProductNo(dbInventory.getProductNo());
        //初始库存
        Long initQuantity =  result.getProductInitQuantity();
        //剩余库存;首次启动时剩余库存等于初始库存;重新启动后，初始化阶段会根据交易流水信息更新剩余库存
        Long curQuantity = initQuantity - result.getProductCurQuantity();

        System.out.println("Check DB Result:ProductNo =  "+result.getProductNo() + " initQuantity = "+initQuantity.toString());
        System.out.println("Check DB Result:ProductNo =  "+result.getProductNo() + " curQuantity = "+curQuantity.toString());
        System.out.println("之前活动消耗的库存数量=  "+(initQuantity-curQuantity));

        Object redisResult = redisTemplate.opsForValue().get(dbInventory.getProductNo());

        System.out.println("Check Redis Result = "+redisResult);
    }

}
