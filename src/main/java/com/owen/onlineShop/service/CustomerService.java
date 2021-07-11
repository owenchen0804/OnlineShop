package com.owen.onlineShop.service;

import com.owen.onlineShop.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.owen.onlineShop.dao.CustomerDao;
import com.owen.onlineShop.entity.Customer;

@Service // 和Repository类似，都是本质为component，细分为处理business logic的
public class CustomerService { // 不像DAO只是update数据库，这是处理业务逻辑

    @Autowired
    private CustomerDao customerDao;

    // 由 RegistrationController 这个 controller 来 call 这个service，从而调用这个method，里面再call DAO
    public void addCustomer(Customer customer) {
        customer.getUser().setEnabled(true); // 如果以后要注销就把true变为false即可

        Cart cart = new Cart();
        customer.setCart(cart); // 所以这里是业务逻辑，所以是对Java object customer的update，此刻并没有对DB进行更新，而是要等到DAO来。

        customerDao.addCustomer(customer); // 包括cart信息以及其它所有之前填表的信息
    }

    public Customer getCustomerByUserName(String userName) {
        return customerDao.getCustomerByUserName(userName);
    }
}