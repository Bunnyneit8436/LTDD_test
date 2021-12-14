package com.example.doan_ltdd.Class;

import java.io.Serializable;

public class ProductCart implements Serializable {

    public String productID;
    public String productImage;
    public String productName;
    public String productDesc;
    public int productPrice;
    public int numProduct=1;
    public String categoryName;
    public int totalPrice;

    public ProductCart() {
    }

    public ProductCart(String productID, String productImage, String productName, String productDesc, int productPrice, int numProduct, String categoryName, int totalPrice) {
        this.productID = productID;
        this.productImage = productImage;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.numProduct = numProduct;
        this.categoryName = categoryName;
        this.totalPrice = totalPrice;
    }
}
