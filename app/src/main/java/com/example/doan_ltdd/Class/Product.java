package com.example.doan_ltdd.Class;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {

    public String productID;
    public String productImage;
    public String productName;
    public String productDesc;
    public int productPrice;
    public int productQuantity = 1;
    public int numProduct=1;
    public String categoryName;

    public Product() {
    }

    public Product(String productID, String productImage, String productName,
                   String productDesc, int productPrice, int productQuantity, String categoryName) {
        this.productID = productID;
        this.productImage = productImage;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productPrice == product.productPrice && productQuantity == product.productQuantity && Objects.equals(productID, product.productID) && Objects.equals(productImage, product.productImage) && Objects.equals(productName, product.productName) && Objects.equals(productDesc, product.productDesc) && Objects.equals(categoryName, product.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, productImage, productName, productDesc, productPrice, productQuantity, categoryName);
    }
}
