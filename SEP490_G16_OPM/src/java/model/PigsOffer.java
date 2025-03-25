/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author dangtuong
 */
public class PigsOffer {

    private int offerID;
    private int sellerID;
    private int farmID;
    private int categoryID;
    private String name;
    private String pigBreed;
    private int quantity;
    private int minQuantity;
    private double minDeposit;
    private double retailPrice;
    private double totalOfferPrice;
    private String description;
    private String imageURL;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private String status;
    private java.sql.Timestamp createdAt;

    public PigsOffer() {
    }

    public PigsOffer(int offerID, int sellerID, int farmID, int categoryID, String name, String pigBreed, int quantity,
            int minQuantity, double minDeposit, double retailPrice, double totalOfferPrice,
            String description, String imageURL, java.sql.Date startDate, java.sql.Date endDate,
            String status, java.sql.Timestamp createdAt) {
        this.offerID = offerID;
        this.sellerID = sellerID;
        this.farmID = farmID;
        this.categoryID = categoryID;
        this.name = name;
        this.pigBreed = pigBreed;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.minDeposit = minDeposit;
        this.retailPrice = retailPrice;
        this.totalOfferPrice = totalOfferPrice;
        this.description = description;
        this.imageURL = imageURL;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getOfferID() {
        return offerID;
    }

    public void setOfferID(int offerID) {
        this.offerID = offerID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getFarmID() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPigBreed() {
        return pigBreed;
    }

    public void setPigBreed(String pigBreed) {
        this.pigBreed = pigBreed;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public double getMinDeposit() {
        return minDeposit;
    }

    public void setMinDeposit(double minDeposit) {
        this.minDeposit = minDeposit;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getTotalOfferPrice() {
        return totalOfferPrice;
    }

    public void setTotalOfferPrice(double totalOfferPrice) {
        this.totalOfferPrice = totalOfferPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
