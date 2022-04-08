package com.owen.onlineShop.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cartitem")
public class CartItem implements Serializable {

    private static final long serialVersionUID = -2455760938054036364L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // 主键生成策略： id 在数据库中作为PK，每增加一行，值自动加 1，不需要自己手动设置
    private int id;

    private int quantity;

    private double price;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JsonIgnore
    private Cart cart; //这里在cartItem table中这一列不加说明，默认的column name是“{table}_{PK}”，
    // 也就是cart_id; 如果把cartItems用mappedBy打包到cart table的话，
    // 会出现一个cart_id有很多cartItem rows，这样cart table就没必要多了这么多行

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
