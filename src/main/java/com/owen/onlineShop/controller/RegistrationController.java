package com.owen.onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.owen.onlineShop.entity.Customer;
import com.owen.onlineShop.service.CustomerService;

@Controller
public class RegistrationController {

    @Autowired
    private CustomerService customerService; // 因为Service也是depend on DAO的，所以需要用Injection方式在controller这边拿到。

    // 前端用户触发navbar.jsp里面跳转到以下这个URL，于是map过来开始由registration controller接管
    @RequestMapping(value = "/customer/registration", method = RequestMethod.GET)
    public ModelAndView getRegistrationForm() {
        Customer customer = new Customer(); // 要把生成的customer对象传进去是为了validate,
        // 也就是说填表的那些信息必须符合一个customer的所有的field，否则的话会报错

        // 返回一个register的form给前端来填写
        return new ModelAndView("register", "customer", customer);
        // viewName = register 表示返回给前端长成的样子就是register.jsp呈现出来的
        // 填好的表单需要对应到customer这个未来的table上，所以需要框架自动做validation
        // 去验证jsp里面的比如firstname, lastname这些path是否和这里modelName: customer class的那些成员变量相一致。
        // 所以这里viewName对应要去validate的jsp名字（register）
        // modalName就是customer，也要和register.jsp里面的modelAttribute的customer相对应
    }

    @RequestMapping(value = "/customer/registration", method = RequestMethod.POST)
    public ModelAndView registerCustomer(@ModelAttribute Customer customer,
                                         BindingResult result) {
        //@ModalAttribute可以把用户填表的那些数据也就是这里的result绑定到customer对象上
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }
        customerService.addCustomer(customer);
        modelAndView.setViewName("login");
        modelAndView.addObject("registrationSuccess", "Registered Successfully. Login using username and password");
        return modelAndView;
    }
}
