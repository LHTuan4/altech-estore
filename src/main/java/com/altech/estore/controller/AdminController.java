package com.altech.estore.controller;

import com.altech.estore.dto.DiscountDTO;
import com.altech.estore.dto.ProductDTO;
import com.altech.estore.params.ParamsAssignDiscount;
import com.altech.estore.response.ResponseApi;
import com.altech.estore.response.ResponseError;
import com.altech.estore.services.DiscountService;
import com.altech.estore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
    private final ProductService productService;
    private final DiscountService discountService;

    @Autowired
    public AdminController(ProductService productService, DiscountService discountService) {
        this.productService = productService;
        this.discountService = discountService;
    }

    @GetMapping("/product/get")
    public ResponseEntity<ResponseApi> getProductById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, productService.getProduct(id)));
        } catch (Exception ex) {
        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @PostMapping("/product/add")
    public ResponseEntity<ResponseApi> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO ret = productService.addProduct(productDTO);
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @PostMapping("/product/remove")
    public ResponseEntity<ResponseApi> deleteProduct(@RequestBody Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @PostMapping("/discount/add")
    public ResponseEntity<ResponseApi> createDiscount(@RequestBody DiscountDTO discountDTO) {
        try {
            DiscountDTO ret = discountService.addDiscount(discountDTO);
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @PostMapping("/discount/assign")
    public ResponseEntity<ResponseApi> assignDiscountToProduct(@RequestBody ParamsAssignDiscount params) {
        try {
            discountService.assignDiscountToProduct(params.getDiscountId(), params.getProductId());
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }
}
