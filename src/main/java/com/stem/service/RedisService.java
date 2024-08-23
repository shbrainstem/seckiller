package com.stem.service;

import com.stem.po.TblZDemoTransaction;
import com.stem.vo.SecondKillRequest;
import com.stem.vo.SecondKillRespond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private ProductService productService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 利用redis控制库存
     * @param request
     * @return
     */
    public SecondKillRespond secondKill(SecondKillRequest request){
        SecondKillRespond respond = new SecondKillRespond();
        if(productNotExist(request.getProductNo())){
            respond.setResult("FALSE");
            return respond;
        }

        String KEY_INIT = request.getProductNo()+"_INIT";
        String KEY_REMAIN = request.getProductNo()+"_REMAIN";// 表示剩余的库存
        String KEY_CAL = request.getProductNo()+"_CAL";

        Long initInventry  = new Long(redisTemplate.opsForValue().get(KEY_INIT)+"");
        Long resultRemain  =  new Long(redisTemplate.opsForValue().get(KEY_REMAIN)+"");

        Object resultValue = redisTemplate.opsForValue().get(KEY_CAL);
        if(null == resultValue){
            resultValue = 0L;
        }

        if((new Long(resultValue+"") + request.getObtaindQuantity() )<= resultRemain){
            //通过库存判断，减少进入正常处理逻辑的请求
            Long result  =  new Long(redisTemplate.opsForValue().increment(KEY_CAL,request.getObtaindQuantity())+"");
            //待补充逻辑，如果总库存 200 ，已用库存198， obtain库存是4。在执行increment后， 198+4=202>200，这时有必要把202还原到198，供后面小于等于2的交易继续使用。
            System.out.println("本次启动截止当前交易消耗库存(CURRENT_Inventory)="+result.toString()+";本次启动之前的剩余库存(REMAIN_Inventory)="+resultRemain.toString());

            TblZDemoTransaction tblZDemoTransaction = new TblZDemoTransaction();
            {
                tblZDemoTransaction.setRequestSeq(request.getRequestSeq());
                tblZDemoTransaction.setProductNo(request.getProductNo());
                tblZDemoTransaction.setUserName(request.getUserName());
                tblZDemoTransaction.setUserId(request.getUserId());
                tblZDemoTransaction.setRequestTime(request.getRequestTime());

                //计算该笔交易获取库存时的原库存; 比如：初始库存100 ；第一次运行扣减了60个库存；重启服务之后， init=100;remain=40;
                Long cur = (Long) initInventry-(Long)resultRemain+(Long)result-request.getObtaindQuantity();
                tblZDemoTransaction.setProductProQuantity(cur);
                //本次得到的库存数量
                tblZDemoTransaction.setProductTraQuantity(request.getObtaindQuantity());
            }
            //正常扣减库存
            if(resultRemain >= result){
                tblZDemoTransaction.setProductBatchSerialNum(tblZDemoTransaction.getProductNo()+"_"+tblZDemoTransaction.getProductProQuantityLong());
                tblZDemoTransaction.setResult("SUCCESS");
                try{
                    productService.insertReqeust(tblZDemoTransaction);
                    respond.setResult("SUCCESS");
                    respond.setProductBatchSerialNum(tblZDemoTransaction.getProductBatchSerialNum());
                }catch (Exception e){
                    e.printStackTrace();
                    respond.setResult("FALSE");
                    return respond;
                }
                return respond;
            }else if(resultRemain < result  ) { //退回： 查询result时库存足够，但是扣减不足，让库存值回退；以方便后续符合库存条件的请求正常执行
                redisTemplate.opsForValue().increment(KEY_CAL, -request.getObtaindQuantity());
                respond.setResult("FALSE");
                System.out.println("########################################库存回退,resultRemain=" +resultRemain+" result="+result+" request.getObtaindQuantity()="+request.getObtaindQuantity());
                System.out.println("########################################库存回退,after value KEY_CAL="+ redisTemplate.opsForValue().get(KEY_CAL));
            }
        }else{
            respond.setResult("FALSE");
        }

        return respond;
    }

    /**
     * 检查--产品不存在
     * @param productNo
     * @return
     */
    private Boolean productNotExist(String productNo){
        String KEY_INIT = productNo+"_INIT";
        String KEY_REMAIN = productNo+"_REMAIN";// 表示剩余的库存
        String KEY_CAL = productNo+"_CAL";

        Object initInventry  = redisTemplate.opsForValue().get(KEY_INIT);
        Object resultRemain  = redisTemplate.opsForValue().get(KEY_REMAIN);

        return (null==initInventry || null ==resultRemain );
    }

}
