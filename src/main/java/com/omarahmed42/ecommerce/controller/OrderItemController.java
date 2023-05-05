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

import com.omarahmed42.ecommerce.DTO.OrderItemDTO;
import com.omarahmed42.ecommerce.model.OrderItem;
import com.omarahmed42.ecommerce.service.OrderItemService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@Hidden
public class OrderItemController {
    private final OrderItemService productItemService;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrderItemController(OrderItemService productItemService) {
        this.productItemService = productItemService;
    }

    @PostMapping(value = "/orders/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addNewOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        if (orderItemDTO.getQuantity() <= 0) {
            return ResponseEntity.ok().build();
        } else if (orderItemDTO.getProductId() == null || orderItemDTO.getOrderId() == null) {
            return ResponseEntity.unprocessableEntity().build();
        }

        OrderItem productItem = modelMapper.map(orderItemDTO, OrderItem.class);
        // TODO: Fix this
        // productItem.setProduct(orderItemDTO.getProductId());
        // productItem.setOrderId(orderItemDTO.getOrderId());
        productItemService.addOrderItem(productItem);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(value = "/orders/items/{order-item-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateOrderItem(@PathVariable(name = "order-item-id") UUID orderItemId,
            @RequestBody OrderItemDTO orderItemDTO) {
        OrderItem productItem = modelMapper.map(orderItemDTO, OrderItem.class);
        productItem.setId(orderItemId);
        productItemService.updateOrderItem(productItem);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/orders/items/{order-item-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable(name = "order-item-id") UUID orderItemId) {
        productItemService.deleteOrderItem(new OrderItem(orderItemId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/orders/items/{order-item-id}")
    public ResponseEntity<OrderItemDTO> getOrderItem(@PathVariable(name = "order-item-id") UUID orderItemId) {
        OrderItemDTO orderItemDTO = modelMapper
                .map(productItemService.getOrderItem(orderItemId), OrderItemDTO.class);
        return ResponseEntity.ok(orderItemDTO);
    }
}
