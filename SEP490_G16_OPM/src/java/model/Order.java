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
public class Order {

    private int orderID;
    private int dealerID;
    private int sellerID;
    private int offerID;
    private int quantity;
    private double totalPrice;
    private String status;
    private Timestamp createdAt;

    private User dealer;
    private User seller;
    private PigsOffer pigsOffer;
    private Farm farm;

    public Order() {
    }

    public Order(int orderID, int dealerID, int sellerID, int offerID, int quantity, double totalPrice, String status, Timestamp createdAt, User dealer, User seller, PigsOffer pigsOffer) {
        this.orderID = orderID;
        this.dealerID = dealerID;
        this.sellerID = sellerID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.dealer = dealer;
        this.seller = seller;
        this.pigsOffer = pigsOffer;
    }

    public Order(int orderID, int dealerID, int sellerID, int offerID, int quantity, double totalPrice, String status, Timestamp createdAt, User dealer, User seller, PigsOffer pigsOffer, Farm farm) {
        this.orderID = orderID;
        this.dealerID = dealerID;
        this.sellerID = sellerID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.dealer = dealer;
        this.seller = seller;
        this.pigsOffer = pigsOffer;
        this.farm = farm;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getDealerID() {
        return dealerID;
    }

    public void setDealerID(int dealerID) {
        this.dealerID = dealerID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getOfferID() {
        return offerID;
    }

    public void setOfferID(int offerID) {
        this.offerID = offerID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public User getDealer() {
        return dealer;
    }

    public void setDealer(User dealer) {
        this.dealer = dealer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public PigsOffer getPigsOffer() {
        return pigsOffer;
    }

    public void setPigsOffer(PigsOffer pigsOffer) {
        this.pigsOffer = pigsOffer;
    }

}
