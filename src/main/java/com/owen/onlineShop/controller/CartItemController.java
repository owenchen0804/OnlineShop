package com.owen.onlineShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.owen.onlineShop.entity.Cart;
import com.owen.onlineShop.entity.CartItem;
import com.owen.onlineShop.entity.Customer;
import com.owen.onlineShop.entity.Product;
import com.owen.onlineShop.service.CartItemService;
import com.owen.onlineShop.service.CartService;
import com.owen.onlineShop.service.CustomerService;
import com.owen.onlineShop.service.ProductService;

@Controller
public class CartItemController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/cart/add/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCartItem(@PathVariable(value = "productId") int productId) {
        //  这里需要在controller里就要验证一下是不是loggedInUser，是的话才可能有下一步
        //  用到了别的比如验证用户，customerService, productService的情况都需要在controller里面写
        //  其它属于cartItemService自己的才在Service and DAO里面写
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        Customer customer = customerService.getCustomerByUserName(username);
        //  验证完了user之后由于和customer一一对应，所以可从customerService调用拿到customer信息

        Cart cart = customer.getCart();
        //  cart本身是Mapped By到cartItem的，所以拿到customer才拿得到cart，通过cart
        //  自己的API可以拿到所有的List<CartItem>
        List<CartItem> cartItems = cart.getCartItem();
        Product product = productService.getProductById(productId);

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            if (product.getId() == (cartItem.getProduct().getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getProductPrice());
                cartItemService.addCartItem(cartItem);
                return;
            }
        }

        //  如果购物车为空，也就是cartItems没有值，或者该carItem是新放入cart的new item的情况下：
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setProduct(product);
        cartItem.setPrice(product.getProductPrice());
        cartItem.setCart(cart);
        cartItemService.addCartItem(cartItem);
    }

    @RequestMapping(value = "/cart/removeCartItem/{cartItemId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCartItem(@PathVariable(value = "cartItemId") int cartItemId) {
        cartItemService.removeCartItem(cartItemId);
    }

    @RequestMapping(value = "/cart/removeAllItems/{cartId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeAllCartItems(@PathVariable(value = "cartId") int cartId) {
        Cart cart = cartService.getCartById(cartId);
        cartItemService.removeAllCartItems(cart);
    }

}
