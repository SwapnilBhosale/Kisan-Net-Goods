package com.kng.app.kngapp.Fragment;

import java.math.BigDecimal;

/**
 * Created by GS-0913 on 17-01-2017.
 */

public class OrderItem {
    private String productName;
    private String quantity;
    private BigDecimal final_price;


    @Override
    public String toString() {
        return "OrderItem{" +
                "productName='" + productName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", final_price=" + final_price +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFinal_price() {
        return final_price;
    }

    public void setFinal_price(BigDecimal final_price) {
        this.final_price = final_price;
    }
}
