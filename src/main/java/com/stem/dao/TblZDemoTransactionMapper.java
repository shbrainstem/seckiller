package com.stem.dao;

import com.stem.po.TblZDemoTransaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TblZDemoTransactionMapper {
    /**
     *   `ID` bigint(20) NOT NULL AUTO_INCREMENT,
     *   `USER_Id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户ID',
     *   `USER_NAME` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户产品名称',
     *   `REQUEST_SEQ` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '请求流水',
     *   `REQUEST_TIME` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '请求时间',
     *   `PRODUCT_BATCH_SERIAL_NUM` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '抽奖批次信息',
     *   `PRODUCT_No` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户ID',
     *    `RESULT` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '抽奖结果',
     *    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     *      `RPODUCT_PRO_QUANTITY` int(11) DEFAULT NULL COMMENT '该笔交易扣减前的库存',
     *   `PRODUCT_TRA_QUANTITY` int(11) DEFAULT NULL COMMENT '本次交易获取到的库存数量',
     */
    @Insert("Insert into tbl_z_demo_transaction(USER_Id,USER_NAME,REQUEST_SEQ,REQUEST_TIME,PRODUCT_No,PRODUCT_BATCH_SERIAL_NUM,RESULT,RPODUCT_PRO_QUANTITY,PRODUCT_TRA_QUANTITY) " +
            "VALUES " +
            "(#{userId},#{userName},#{requestSeq},#{requestTime},#{productNo},#{productBatchSerialNum},#{result},#{productProQuantity},#{productTraQuantity})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insertRequest(TblZDemoTransaction tblZDemoRequest);

    @Select(" Select USER_Id,USER_NAME,REQUEST_SEQ,REQUEST_TIME,PRODUCT_No,PRODUCT_BATCH_SERIAL_NUM,RESULT,RPODUCT_PRO_QUANTITY,PRODUCT_TRA_QUANTITY" +
            " from tbl_z_demo_transaction " +
            " where RPODUCT_PRO_QUANTITY = (select MAX(RPODUCT_PRO_QUANTITY) from tbl_z_demo_transaction where  PRODUCT_No = #{productNo} and RESULT='SUCCESS')" +
            " and  PRODUCT_No = #{productNo} " )
    @Results({
            @Result(property = "userId" ,column = "USER_Id"),
            @Result(property = "userName" ,column = "USER_NAME"),
            @Result(property = "requestSeq" ,column = "REQUEST_SEQ"),
            @Result(property = "requestTime" ,column = "REQUEST_TIME"),
            @Result(property = "productNo" ,column = "PRODUCT_No"),
            @Result(property = "productBatchSerialNum" ,column = "PRODUCT_BATCH_SERIAL_NUM"),
            @Result(property = "productProQuantity" ,column = "RPODUCT_PRO_QUANTITY"),
            @Result(property = "productTraQuantity" ,column = "PRODUCT_TRA_QUANTITY"),
            @Result(property = "result" ,column = "RESULT")
    })
    TblZDemoTransaction getMaxTblZDemoTransactionByProductNo(String productNo);
}
