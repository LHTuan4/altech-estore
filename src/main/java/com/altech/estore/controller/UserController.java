package com.altech.estore.controller;

import com.altech.estore.dto.BasketDTO;
import com.altech.estore.entities.BasketItem;
import com.altech.estore.params.ParamsAddItemBasket;
import com.altech.estore.response.ResponseApi;
import com.altech.estore.response.ResponseError;
import com.altech.estore.services.BasketService;
import com.altech.estore.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/api/v1")
public class UserController {
    private final DiscountService discountService;
    private final BasketService basketService;

    @Autowired
    public UserController(DiscountService discountService, BasketService basketService) {
        this.discountService = discountService;
        this.basketService = basketService;
    }

    @GetMapping("/basket/get")
    public ResponseEntity<ResponseApi> getBasket(@RequestParam Long basketId) {
        try {
            BasketDTO ret = basketService.get(basketId);
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }
    @PostMapping("/basket/add")
    public ResponseEntity<ResponseApi> createBasket(@RequestBody BasketDTO basketDTO) {
        try {
            BasketDTO ret = basketService.add(basketDTO);
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @PostMapping("/basket/item/add")
    public ResponseEntity<ResponseApi> addItemToBasket(
            @RequestBody ParamsAddItemBasket params) {
        try {
            BasketDTO ret = basketService.addToBasket(params.getBasketId(), params.getRequest().getProductId(), params.getRequest().getQuantity());
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @PostMapping("/basket/item/remove")
    public ResponseEntity<ResponseApi> removeItemFromBasket(
            @RequestBody Long basketId,
            @RequestBody Long productId) {
        try {
            BasketDTO ret = basketService.removeItemFromBasket(basketId, productId);
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }

    @GetMapping("/basket/receipt")
    public ResponseEntity<ResponseApi> generateReceipt(@RequestParam Long basketId) {
        try {
            BasketDTO ret = basketService.get(basketId);
            return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Success, ret));
        } catch (Exception ex) {

        }
        return ResponseEntity.ok(new ResponseApi(ResponseError.Error_Exception));
    }
}
