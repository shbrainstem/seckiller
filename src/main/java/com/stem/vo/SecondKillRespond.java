package com.stem.vo;

public class SecondKillRespond {

    private String result;

    private String productBatchSerialNum;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProductBatchSerialNum() {
        return productBatchSerialNum;
    }

    public void setProductBatchSerialNum(String productBatchSerialNum) {
        this.productBatchSerialNum = productBatchSerialNum;
    }

    @Override
    public String toString() {
        return "SecondKillRespond{" +
                "result='" + result + '\'' +
                ", productBatchSerialNum='" + productBatchSerialNum + '\'' +
                '}';
    }

}
