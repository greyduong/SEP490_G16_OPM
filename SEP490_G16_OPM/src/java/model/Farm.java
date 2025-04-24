package model;

import java.sql.Timestamp;

public class Farm {

    private Integer farmID;
    private Integer sellerID;
    private User seller;
    private String farmName;
    private String location;
    private String description;
    private String status;
    private Timestamp createdAt;

    //Dem so luong offer
    private int offerCount;
    private int orderCount;

    public Farm() {
    }

    public Farm(Integer farmID, Integer sellerID, User seller, String farmName, String location, String description, String status, Timestamp createdAt) {
        this.farmID = farmID;
        this.sellerID = sellerID;
        this.seller = seller;
        this.farmName = farmName;
        this.location = location;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Farm(Integer farmID, Integer sellerID, User seller, String farmName, String location, String description, String status, Timestamp createdAt, int offerCount, int orderCount) {
        this.farmID = farmID;
        this.sellerID = sellerID;
        this.seller = seller;
        this.farmName = farmName;
        this.location = location;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.offerCount = offerCount;
        this.orderCount = orderCount;
    }

    public int getFarmID() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }   

    @Override
    public String toString() {
        return """
               Farm {
               \tFarmID = %s
               \tSellerID = %s
               \tFarmName = %s
               \tLocation = %s
               \tDescription = %s
               \tStatus = %s
               \tCreatedAt = %s
               }
               """.formatted(farmID, sellerID, farmName, location, description, status, createdAt == null ? "null" : createdAt.toString());
    }
}
