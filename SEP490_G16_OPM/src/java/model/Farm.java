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

    public Farm() {
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
