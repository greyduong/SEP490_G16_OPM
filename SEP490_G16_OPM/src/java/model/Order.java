/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
import java.util.List;

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
    private Timestamp processedDate;
    private String note;

    private User dealer;
    private User seller;
    private PigsOffer pigsOffer;
    private Farm farm;
    private List<Delivery> deliveries;

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

    public Order(int orderID, int dealerID, int sellerID, int offerID, int quantity, double totalPrice, String status, Timestamp createdAt, Timestamp processedDate, User dealer, User seller, PigsOffer pigsOffer, Farm farm) {
        this.orderID = orderID;
        this.dealerID = dealerID;
        this.sellerID = sellerID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.processedDate = processedDate;
        this.dealer = dealer;
        this.seller = seller;
        this.pigsOffer = pigsOffer;
        this.farm = farm;
    }

    public Order(int orderID, int dealerID, int sellerID, int offerID, int quantity, double totalPrice, String status, Timestamp createdAt, Timestamp processedDate, String note) {
        this.orderID = orderID;
        this.dealerID = dealerID;
        this.sellerID = sellerID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.processedDate = processedDate;
        this.note = note;
    }

    public Order(int orderID, int dealerID, int sellerID, int offerID, int quantity, double totalPrice, String status, Timestamp createdAt, Timestamp processedDate, String note, User dealer, User seller, PigsOffer pigsOffer, Farm farm) {
        this.orderID = orderID;
        this.dealerID = dealerID;
        this.sellerID = sellerID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.processedDate = processedDate;
        this.note = note;
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

    public Timestamp getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Timestamp processedDate) {
        this.processedDate = processedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

}
