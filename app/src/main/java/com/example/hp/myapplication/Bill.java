package com.example.hp.myapplication;

import java.math.BigDecimal;

/**
 * Created by GS-0913 on 07-01-2017.
 */

public class Bill {
    private BigDecimal total;
    private BigDecimal discountedBill;
    private BigDecimal discount;
    private BigDecimal shippingCharge;
    private String discountPer;


    public Bill(BigDecimal discountedBill, BigDecimal total, BigDecimal discount, BigDecimal shippingCharge) {
        this.discountedBill = discountedBill;
        this.total = total;
        this.discount = discount;
        this.shippingCharge = shippingCharge;
        discountPer = "0%";
    }

    @Override
    public String toString() {
        return "Bill{" +
                "total=" + total +
                ", discountedBill=" + discountedBill +
                ", discount=" + discount +
                ", shippingCharge=" + shippingCharge +
                ", discountPer='" + discountPer + '\'' +
                '}';
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiscountedBill() {
        return discountedBill;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public void setDiscountedBill(BigDecimal discountedBill) {
        this.discountedBill = discountedBill;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(BigDecimal shippingCharge) {
        this.shippingCharge = shippingCharge;
    }
}
