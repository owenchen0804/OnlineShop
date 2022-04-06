package com.owen.onlineShop.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 8436097833452420298L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // support simple primary key like id
    private int id;

    @OneToOne(mappedBy = "cart") // 凡是有mappedBy自己就不需要加column了，加到对方的table
    @JsonIgnore
    private Customer customer;  // map后到customer这张表上去

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //  这个主要适用于OneToMany,不适用于ManyToOne!
    //  因为如果是ManyToOne的话比如cartItem投射到cart table上，一个cart要对应好多个cartItem
    //  存储起来会觉得很冗余，相当于多了很多rows来存每一个cartItem，但其实只有一个cart
    private List<CartItem> cartItem; // map到cartItem表上去，因为是一对多所以多个cartItem对应的cart id可能一样

    private double totalPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
