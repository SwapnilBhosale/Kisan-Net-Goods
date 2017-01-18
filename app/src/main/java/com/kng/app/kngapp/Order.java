package com.kng.app.kngapp;

import com.kng.app.kngapp.Fragment.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by GS-0913 on 17-01-2017.
 */

public class Order {

    private String date_purchased;
    private String orders_id;
    private String payment_type_name;

    @Override
    public String toString() {
        return "Order{" +
                "date_purchased='" + date_purchased + '\'' +
                ", orders_id='" + orders_id + '\'' +
                ", payment_type_name='" + payment_type_name + '\'' +
                ", status='" + status + '\'' +
                ", bill=" + bill +
                ", discount=" + discount +
                ", total_bill=" + total_bill +
                ", vat_total=" + vat_total +
                ", orderItems=" + orderItems +
                '}';
    }

    private String status;
    private BigDecimal bill;
    private BigDecimal discount;
    private BigDecimal total_bill;
    private BigDecimal vat_total;
    private List<OrderItem> orderItems;


    public String getDate_purchased() {
        return date_purchased;
    }

    public void setDate_purchased(String date_purchased) {
        this.date_purchased = date_purchased;
    }

    public String getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(String orders_id) {
        this.orders_id = orders_id;
    }

    public String getPayment_type_name() {
        return payment_type_name;
    }

    public void setPayment_type_name(String payment_type_name) {
        this.payment_type_name = payment_type_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(BigDecimal total_bill) {
        this.total_bill = total_bill;
    }

    public BigDecimal getVat_total() {
        return vat_total;
    }

    public void setVat_total(BigDecimal vat_total) {
        this.vat_total = vat_total;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
