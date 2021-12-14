package com.example.doan_ltdd.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

    public String OrderNo;
    public String custEmail;
    public String custAddress;
    public String custName;
    public String custPhone;
    public String dateOrder;
    public String status;
    public int numProduct;
    public int totalPrice;
    public ArrayList<ProductCart> listDetailOrder;

    public Order(){}

    public Order(String orderNo, String custEmail, String custAddress, String custName, String custPhone, String dateOrder, String status, int numProduct, int totalPrice) {
        this.OrderNo = orderNo;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.custName = custName;
        this.custPhone = custPhone;
        this.dateOrder = dateOrder;
        this.status = status;
        this.numProduct = numProduct;
        this.totalPrice = totalPrice;
    }

    public Order(String orderNo, String custEmail, String custAddress, String custName, String custPhone, String dateOrder, String status, int numProduct, int totalPrice, ArrayList<ProductCart> listDetailOrder) {
        this.OrderNo = orderNo;
        this.custEmail = custEmail;
        this.custAddress = custAddress;
        this.custName = custName;
        this.custPhone = custPhone;
        this.dateOrder = dateOrder;
        this.status = status;
        this.numProduct = numProduct;
        this.totalPrice = totalPrice;
        this.listDetailOrder = listDetailOrder;
    }
}
