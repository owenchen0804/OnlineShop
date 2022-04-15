package com.owen.onlineShop.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.owen.onlineShop.entity.Cart;
import com.owen.onlineShop.entity.CartItem;

@Repository
public class CartItemDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void addCartItem(CartItem cartItem) {
        //  我认为这里能够调用addCarItem()这个method，说明已经是登录用户且
        //  知道对应cart的情况下，对这个cart进行添加，所以不需要再找到对应的carId了
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(cartItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void removeCartItem(int cartItemId) {
        //  这里略有不同，因为input是cartItemId而不是cartItem，所以必须还是要找到cart才行
        Session session = null;
        try {
            session = sessionFactory.openSession();
            CartItem cartItem = session.get(CartItem.class, cartItemId);
            Cart cart = cartItem.getCart();
            List<CartItem> cartItems = cart.getCartItem();
            cartItems.remove(cartItem);
            session.beginTransaction();
            session.delete(cartItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void removeAllCartItems(Cart cart) { // 直接对input cart进行清空操作
        List<CartItem> cartItems = cart.getCartItem();
        for (CartItem cartItem : cartItems) {
            removeCartItem(cartItem.getId());
        }
    }
}
