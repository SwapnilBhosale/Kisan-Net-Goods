package com.example.hp.myapplication;

/**
 * Created by GS-0913 on 04-01-2017.
 */

public class PaymentType {

    private String paymentTypeId;
    private String payment_type_name;
    private String payment_details;

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPayment_type_name() {
        return payment_type_name;
    }

    public void setPayment_type_name(String payment_type_name) {
        this.payment_type_name = payment_type_name;
    }

    public String getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(String payment_details) {
        this.payment_details = payment_details;
    }

    @Override
    public String toString() {
        return "PaymentType{" +
                "paymentTypeId='" + paymentTypeId + '\'' +
                ", payment_type_name='" + payment_type_name + '\'' +
                ", payment_details='" + payment_details + '\'' +
                '}';
    }
}
