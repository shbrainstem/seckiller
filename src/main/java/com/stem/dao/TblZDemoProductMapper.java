package com.stem.dao;

import com.stem.po.TblZDemoProduct;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TblZDemoProductMapper {

    @Delete("delete from tbl_z_demo_product where PRODUCT_No = #{productNo}")
    @Results({
            @Result(property = "productNo" ,column = "PRODUCT_No")
    })
    void deleteProduct(String productNo);

    @Insert("Insert into tbl_z_demo_product(PRODUCT_NAME,PRODUCT_No,PRODUCT_INIT_QUANTITY,PRODUCT_CUR_QUANITYT,PRODUCT_DESC) VALUES (#{productName},#{productNo},#{productInitQuantity},#{productCurQuantity},#{productDesc})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertProduct(TblZDemoProduct tblZDemoProduct);

    @Select(" Select * from tbl_z_demo_product" )
    @Results({
            @Result(property = "productName" ,column = "PRODUCT_NAME"),
            @Result(property = "productNo" ,column = "PRODUCT_No"),
            @Result(property = "productDesc" ,column = "PRODUCT_DESC"),
            @Result(property = "productCurQuantity" ,column = "PRODUCT_CUR_QUANITYT"),
            @Result(property = "productInitQuantity" ,column = "PRODUCT_INIT_QUANTITY")
    })
    List<TblZDemoProduct> getAllTblZDemoIn();

    @Select(" Select * from tbl_z_demo_product where PRODUCT_No = #{productNo}" )
    @Results({
            @Result(property = "productName" ,column = "PRODUCT_NAME"),
            @Result(property = "productNo" ,column = "PRODUCT_No"),
            @Result(property = "productDesc" ,column = "PRODUCT_DESC"),
            @Result(property = "productCurQuantity" ,column = "PRODUCT_CUR_QUANITYT"),
            @Result(property = "productInitQuantity" ,column = "PRODUCT_INIT_QUANTITY")
    })
    TblZDemoProduct getTblZDemoInventoryByProductNo(String productNo);

    @Select(" Select * from tbl_z_demo_product where id = #{id}" )
    @Results({
            @Result(property = "productName" ,column = "PRODUCT_NAME"),
            @Result(property = "productNo" ,column = "PRODUCT_No"),
            @Result(property = "productDesc" ,column = "PRODUCT_DESC"),
            @Result(property = "productCurQuantity" ,column = "PRODUCT_CUR_QUANITYT"),
            @Result(property = "productInitQuantity" ,column = "PRODUCT_INIT_QUANTITY")
    })
    TblZDemoProduct getTblZDemoProductByID(Long id);

    @Update("Update tbl_z_demo_product set PRODUCT_CUR_QUANITYT=#{productCurQuantity} where PRODUCT_No = #{productNo}")
    int updateTblZDemoProductCurQuantity(TblZDemoProduct tblZDemoProduct);

}

