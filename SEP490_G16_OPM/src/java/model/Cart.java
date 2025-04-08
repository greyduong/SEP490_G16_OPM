/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author duong
 */
public class Cart {

    private int cartID;
    private int userID;
    private int offerID;
    private int quantity;

    private User user;
    private PigsOffer pigsOffer;

    public Cart() {
    }

    public Cart(int cartID, int userID, int offerID, int quantity, PigsOffer pigsOffer) {
        this.cartID = cartID;
        this.userID = userID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.pigsOffer = pigsOffer;
    }

    public Cart(int cartID, int userID, int offerID, int quantity, User user, PigsOffer pigsOffer) {
        this.cartID = cartID;
        this.userID = userID;
        this.offerID = offerID;
        this.quantity = quantity;
        this.user = user;
        this.pigsOffer = pigsOffer;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PigsOffer getPigsOffer() {
        return pigsOffer;
    }

    public void setPigsOffer(PigsOffer pigsOffer) {
        this.pigsOffer = pigsOffer;
    }

}
