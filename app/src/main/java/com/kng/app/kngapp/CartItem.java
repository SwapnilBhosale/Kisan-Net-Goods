package com.kng.app.kngapp;

import java.math.BigDecimal;

/**
 * Created by hp on 23/12/2016.
 */

public class CartItem {

    private String basketId;
    private String quantity;
    private BigDecimal price;
    private String image;
    private String weight;
    private String name;
    private BigDecimal total;
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "basketId='" + basketId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", weight='" + weight + '\'' +
                ", name='" + name + '\'' +
                ", total=" + total +
                ", productId='" + productId + '\'' +
                '}';
    }
}
