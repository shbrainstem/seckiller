package com.stem.dao;

import com.stem.po.TblZDemoTransaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 重置两张表的数据，仅用在test场景下
 */
@Mapper
@Repository
public interface TblZDemoTestMapper {

    @Delete("delete from tbl_z_demo_product where PRODUCT_No = #{productNo}")
    int deleteInvByProductNo(String productNo);

    @Delete("delete from tbl_z_demo_transaction where PRODUCT_No = #{productNo} ")
    int deleteReqByProductNo(String productNo);

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
    TblZDemoTransaction getMaxTblZDemoRequestByProductNo(String productNo);
}
