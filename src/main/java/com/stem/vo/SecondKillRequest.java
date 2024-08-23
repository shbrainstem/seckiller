package com.stem.vo;

public class SecondKillRequest {

    /**
     * 请求流水
     */
    private String requestSeq;

    private String userName;

    private String requestTime;

    private String userId;

    private String productNo;

    /**
     * 本次秒杀产品的顺序号
     */
    private String productBatchSerialNum;

    /**
     * 客户单次获取的库存数 默认为1
     */
    private Long obtaindQuantity = 1L;

    public Long getObtaindQuantity() {
        return obtaindQuantity;
    }

    public void setObtaindQuantity(Long obtaindQuantity) {
        this.obtaindQuantity = obtaindQuantity;
    }

    public String getRequestSeq() {
        return requestSeq;
    }

    public void setRequestSeq(String requestSeq) {
        this.requestSeq = requestSeq;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductBatchSerialNum() {
        return productBatchSerialNum;
    }

    public void setProductBatchSerialNum(String productBatchSerialNum) {
        this.productBatchSerialNum = productBatchSerialNum;
    }

    @Override
    public String toString() {
        return "SecondKillRequest{" +
                "requestSeq='" + requestSeq + '\'' +
                ", userName='" + userName + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", userId='" + userId + '\'' +
                ", productNo='" + productNo + '\'' +
                ", productBatchSerialNum='" + productBatchSerialNum + '\'' +
                ", obtaindQuantity=" + obtaindQuantity +
                '}';
    }
}
