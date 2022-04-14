package com.owen.onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.owen.onlineShop.entity.Cart;
import com.owen.onlineShop.entity.Customer;
import com.owen.onlineShop.service.CartService;
import com.owen.onlineShop.service.CustomerService;

@Controller
public class CartController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/cart/getCartById", method = RequestMethod.GET)
    public ModelAndView getCartId(){
        // cartDao能返回的只是cart本身，而不是对应的cartId
        // 要找一个用户的cart Id，由于customer和cart是OneToOne的
        // 所以可以从customerDao那边拿到emailId，从而拿到具体的某个customer, 再可以getCart().getId()
        ModelAndView modelAndView = new ModelAndView("cart");
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        Customer customer = customerService.getCustomerByUserName(username);
        modelAndView.addObject("cartId", customer.getCart().getId());
        return modelAndView;
    }

    @RequestMapping(value = "/cart/getCart/{cartId}", method = RequestMethod.GET)
    @ResponseBody
    public Cart getCartItems(@PathVariable(value="cartId")int cartId){
        return cartService.getCartById(cartId);
    }
}
