package com.stem.abs;

import com.stem.po.TblZDemoProduct;

/**
 * 用于构造一个TblZDemoProduct对象，初始化数据库表
 */
public abstract class AbsDemo {

    public static final TblZDemoProduct defaultPO = new TblZDemoProduct();
//    public static  final Long MAX_REDIS_LONG = Long.MAX_VALUE;
    public static  final Long MAX_REDIS_LONG = 200L;

    static {
        defaultPO.setProductName("小商品");
        defaultPO.setProductNo("0003");
        defaultPO.setProductInitQuantity(MAX_REDIS_LONG);
        defaultPO.setProductCurQuantity(0L);
        defaultPO.setProductDesc("I like ");
    }
    public static final Long REDIS_EXPIRE_TIME_MILLISECONDS = 1000*60*30L;
}
