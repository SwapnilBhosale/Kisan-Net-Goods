package com.example.hp.myapplication;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hp on 22/12/2016.
 */

public class ProductData {

    private String productId;
    private String productImage;
    private BigDecimal product_Price;
    private String product_weight;
    private String manufacturer_name;
    private String manufacturer_image;
    private List<ProductInfo> productInfo;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getProduct_Price() {
        return product_Price;
    }

    public void setProduct_Price(BigDecimal product_Price) {
        this.product_Price = product_Price;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getManufacturer_image() {
        return manufacturer_image;
    }

    public void setManufacturer_image(String manufacturer_image) {
        this.manufacturer_image = manufacturer_image;
    }

    public List<ProductInfo> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(List<ProductInfo> productInfo) {
        this.productInfo = productInfo;
    }
}
