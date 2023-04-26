package com.omarahmed42.ecommerce.controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.omarahmed42.ecommerce.DTO.OrderItemDTO;
import com.omarahmed42.ecommerce.exception.ProductItemNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.ProductItem;
import com.omarahmed42.ecommerce.service.ProductItemService;

@RestController
@RequestMapping("/v1")
public class OrderItemController {
    private final ProductItemService productItemService;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrderItemController(ProductItemService productItemService) {
        this.productItemService = productItemService;
    }

    @PostMapping(value = "/orders/items", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> addNewOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        try {
            if (orderItemDTO.getQuantity() <= 0){
                return ResponseEntity.ok().build();
            } else if (orderItemDTO.getProductId() == null || orderItemDTO.getOrderId() == null){
                return ResponseEntity.unprocessableEntity().build();
            }
            
            ProductItem productItem = modelMapper.map(orderItemDTO, ProductItem.class);
            productItem.setProductId(orderItemDTO.getProductId());
            productItem.setOrderId(orderItemDTO.getOrderId());
            productItemService.addProductItem(productItem);
            return ResponseEntity.status(201).build();
        } catch (ProductNotFoundException productNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/orders/items/{orderItemId}", consumes = "application/json")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> updateOrderItem(@PathVariable(name = "orderItemId") UUID orderItemId,
            @RequestBody OrderItemDTO orderItemDTO) {
        try {
            ProductItem productItem = modelMapper.map(orderItemDTO, ProductItem.class);
            productItem.setId(orderItemId);
            productItemService.updateProductItem(productItem);
            return ResponseEntity.noContent().build();
        } catch (ProductItemNotFoundException | ProductNotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/orders/items/{orderItemId}")
    @PreAuthorize("hasRole(Role.ADMIN.toString())")
    public ResponseEntity<String> deleteOrderItem(@PathVariable(name = "orderItemId") UUID orderItemId) {
        try {
            productItemService.deleteProductItem(new ProductItem(orderItemId));
            return ResponseEntity.noContent().build();
        } catch (ProductItemNotFoundException productItemNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/orders/items/{orderItemId}", produces = "application/json")
    public ResponseEntity<String> getOrderItem(@PathVariable(name = "orderItemId") UUID orderItemId) {
        try {
            OrderItemDTO orderItemDTO = modelMapper
                    .map(productItemService.getProductItem(orderItemId), OrderItemDTO.class);
            return ResponseEntity.ok(new Gson().toJson(orderItemDTO));
        } catch (ProductItemNotFoundException productItemNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
