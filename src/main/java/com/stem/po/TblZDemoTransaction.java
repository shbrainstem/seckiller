package com.stem.po;

import javax.persistence.*;

@Entity
@Table(name = "tbl_z_demo_request")
public class TblZDemoTransaction {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String userName;
    private String requestSeq;
    private String requestTime;
    private String productBatchSerialNum;
    private String productNo;
    private String result;
    private Long productProQuantity;
    private Long productTraQuantity;

    @Override
    public String toString() {
        return "TblZDemoRequest{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", requestSeq='" + requestSeq + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", productBatchSerialNum='" + productBatchSerialNum + '\'' +
                ", productNo='" + productNo + '\'' +
                ", result='" + result + '\'' +
                ", productProQuanitity=" + productProQuantity +
                ", productTraQuanitity=" + productTraQuantity +
                '}';
    }

    /**
     * 该笔交易执行库存扣减前的库存
     * @return
     */
    public Long getProductProQuantity() {
        return productProQuantity;
    }

    public Long getProductProQuantityLong() {
        return new Long(productProQuantity);
    }
    public void setProductProQuantity(Long productProQuantity) {
        this.productProQuantity = productProQuantity;
    }

    /**
     * 该笔交易获取的库存
     * @return
     */
    public Long getProductTraQuantity() {
        return productTraQuantity;
    }

    public void setProductTraQuantity(Long productTraQuantity) {
        this.productTraQuantity = productTraQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestSeq() {
        return requestSeq;
    }

    public void setRequestSeq(String requestSeq) {
        this.requestSeq = requestSeq;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getProductBatchSerialNum() {
        return productBatchSerialNum;
    }

    public void setProductBatchSerialNum(String productBatchSerialNum) {
        this.productBatchSerialNum = productBatchSerialNum;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
