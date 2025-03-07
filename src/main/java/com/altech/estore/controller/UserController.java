package com.altech.estore.controller;

import com.altech.estore.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1")
public class UserController {
    private final DiscountService discountService;

    @Autowired
    public UserController(DiscountService discountService) {
        this.discountService = discountService;
    }



}
