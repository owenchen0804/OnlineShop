package com.owen.onlineShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
//必须加才能被识别
public class HomePageController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String sayIndex() {
        return "index";
    } //String代表viewName = "index"，主页显示index.jsp, 不需要任何data，只需要view

    @RequestMapping("/login") // 加annotation才表示需要框架来处理具体的请求。这里就需要data + view了，所以需要返回ModelAndView
    // -> /login
    // -> /login?error 如果输入信息有误，或者用户名密码对不上，Spring会变成这样的URL格式
    // -> /login?logout 如果用户点击了Logout就会导入到这个URL格式

    // Spring security 框架传的error and logout? 不通过后端的Database吗？？是通过Spring Security实现的！

    // @RequestParam就是得到?后面的参数
    // required = false 指的是/login?(key) 可以不存在"error" or "logout"这样的key，无所谓
    // 也就是说如果不是false，那么前端URL请求如果不含error or logout字样的话method就无法响应请求


    // login这个url生成，是在navbar.jsp里面显示的，当用户点击到login的时候产生了这样一个url
    // ModelAndView 这个对象的object应该就是返回给DispatcherServlet之后再进行后续显示的，
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login"); //viewResolver会找到对应的login.jsp，这是通常情况

        if (error != null) {
            modelAndView.addObject("error", "Invalid username and Password");
        } // .addObject就是data

        if (logout != null) {
            modelAndView.addObject("logout", "You have logged out successfully");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/aboutus", method = RequestMethod.GET)
    public String sayAbout() {
        return "aboutUs";
    }
}

