package com.example.doan_ltdd.Class;

public class DetailOrder {

    public String OrderNo;
    public String productName;
    public String urlImg;
    public String status;
    public int productPrice;
    public int numProduct;

    public DetailOrder(){
    }

    public DetailOrder(String orderNo, String productName, String urlImg, String status, int productPrice, int numProduct) {
        OrderNo = orderNo;
        this.productName = productName;
        this.urlImg = urlImg;
        this.status = status;
        this.productPrice = productPrice;
        this.numProduct = numProduct;
    }
}
