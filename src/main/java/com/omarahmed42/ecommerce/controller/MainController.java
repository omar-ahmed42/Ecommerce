package com.omarahmed42.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
 
    @GetMapping("")
    public String viewHomePage() {
        return "forward:index";
    }
     
    @GetMapping("/vendor/login")
    public String viewVendorLoginPage() {
        return "vendor/vendor_login";
    }
     
    @GetMapping("/vendor/home")
    public String viewVendorHomePage() {
        return "vendor/vendor_home";
    }
     
    @GetMapping("/customer/login")
    public String viewCustomerLoginPage() {
        return "customer/customer_login";
    }
     
    @GetMapping("/customer/home")
    public String viewCustomerHomePage() {
        return "forward:customer/customer_home";
    }  

    @GetMapping("/checkout")
    public String checkout(){
        return "checkout";
    }
}