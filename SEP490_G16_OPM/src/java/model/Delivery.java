/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author duong
 */
public class Delivery {

    private int deliveryID;
    private int orderID;
    private int sellerID;
    private int dealerID;
    private String deliveryStatus;
    private String recipientName;
    private String phone;
    private int quantity;
    private double totalPrice;
    private Timestamp createdAt;
    private String comments;

    public Delivery() {
    }

    public Delivery(int deliveryID, int orderID, int sellerID, int dealerID, String deliveryStatus, String recipientName, String phone, int quantity, double totalPrice, Timestamp createdAt, String comments) {
        this.deliveryID = deliveryID;
        this.orderID = orderID;
        this.sellerID = sellerID;
        this.dealerID = dealerID;
        this.deliveryStatus = deliveryStatus;
        this.recipientName = recipientName;
        this.phone = phone;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.comments = comments;
    }

    public int getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getDealerID() {
        return dealerID;
    }

    public void setDealerID(int dealerID) {
        this.dealerID = dealerID;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Delivery{" + "deliveryID=" + deliveryID + ", orderID=" + orderID + ", sellerID=" + sellerID + ", dealerID=" + dealerID + ", deliveryStatus=" + deliveryStatus + ", recipientName=" + recipientName + ", quantity=" + quantity + ", totalPrice=" + totalPrice + ", createdAt=" + createdAt + ", comments=" + comments + '}';
    }
}
