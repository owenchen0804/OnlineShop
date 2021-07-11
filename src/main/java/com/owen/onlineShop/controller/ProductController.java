package com.owen.onlineShop.controller;

import com.owen.onlineShop.entity.Product;
import com.owen.onlineShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService; // 定义出来Service object来调用后端DAO里面的method

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public ModelAndView getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ModelAndView("productList", "products", products);
        // 这里的modelName要和productList.j sp Line49的那个变量名完全一致，从而能够把data map过去
    }

    @RequestMapping(value = "/getProductById/{productId}", method = RequestMethod.GET)
    public ModelAndView getProductById(@PathVariable(value = "productId") int productId) {
        // @PathVariable就是把URL上面大括号里的信息extract到value，然后传递给productId的过程
        Product product = productService.getProductById(productId);
        return new ModelAndView("productPage", "product", product);
    }

    @RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.GET)
    public ModelAndView getProductForm() {
        return new ModelAndView("addProduct", "productForm", new Product());
        // 因为是要getProductForm()，所以必须要返回一张新的Product table，里面是空白的，所以要新建出来。
    }

    @RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "addProduct";
        }
        productService.addProduct(product);
        return "redirect:/getAllProducts";
    }

    @RequestMapping(value = "/admin/delete/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") int productId) {
        productService.deleteProduct(productId);
        return "redirect:/getAllProducts";
    }

    @RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.GET)
    public ModelAndView getEditForm(@PathVariable(value = "productId") int productId) {
        Product product = productService.getProductById(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editProduct"); //当click edit button的时候要返回editProduct.jsp
        modelAndView.addObject("editProductObj", product);
        modelAndView.addObject("productId", productId);
        // 根据jsp里面的变量名modelAttribute知道那个product obj被修改，以及主键Id

        return modelAndView;
    }

    @RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.POST)
    public String editProduct(@ModelAttribute Product product,
                              @PathVariable(value = "productId") int productId) {
        product.setId(productId); // 只有加上Id才能知道是修改已经存在的object,否则会认为是添加某个商品
        productService.updateProduct(product);
        return "redirect:/getAllProducts";
    }
}
