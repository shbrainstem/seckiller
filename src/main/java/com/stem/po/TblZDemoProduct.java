package com.stem.po;

import javax.persistence.*;

@Entity
@Table(name = "tbl_z_demo_product")
public class TblZDemoProduct {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private Long productInitQuantity;

    private Long productCurQuantity;

    private String productNo;

    private String productDesc;

    public Long getId() {
        return id;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductInitQuantity() {
        return productInitQuantity;
    }

    public void setProductInitQuantity(Long productInitQuantity) {
        this.productInitQuantity = productInitQuantity;
    }

    public Long getProductCurQuantity() {
        return productCurQuantity;
    }

    public void setProductCurQuantity(Long productCurQuantity) {
        this.productCurQuantity = productCurQuantity;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @Override
    public String toString() {
        return "TblZDemoInventory{" +
                "productName='" + productName + '\'' +
                ", productInitQuantity=" + productInitQuantity +
                ", productCurQuantity=" + productCurQuantity +
                ", productNo='" + productNo + '\'' +
                ", productDesc='" + productDesc + '\'' +
                '}';
    }
}
