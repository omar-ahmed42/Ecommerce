package com.omarahmed42.ecommerce.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.enums.Status;
import com.omarahmed42.ecommerce.exception.MoreThanStockCapacityException;
import com.omarahmed42.ecommerce.exception.OrderNotFoundException;
import com.omarahmed42.ecommerce.exception.ProductNotFoundException;
import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.CustomerOrders;
import com.omarahmed42.ecommerce.model.Orders;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.model.ProductItem;
import com.omarahmed42.ecommerce.repository.BillingAddressRepository;
import com.omarahmed42.ecommerce.repository.CustomerOrdersRepository;
import com.omarahmed42.ecommerce.repository.OrderRepository;
import com.omarahmed42.ecommerce.repository.ProductItemRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrderRepository orderRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final CustomerOrdersRepository customerOrdersRepository;
    private final BillingAddressRepository billingAddressRepository;

    @Autowired
    public OrdersServiceImpl(OrderRepository orderRepository, ProductItemRepository productItemRepository,
            ProductRepository productRepository, CustomerOrdersRepository customerOrdersRepository, BillingAddressRepository billingAddressRepository) {
        this.orderRepository = orderRepository;
        this.productItemRepository = productItemRepository;
        this.productRepository = productRepository;
        this.customerOrdersRepository = customerOrdersRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    @Transactional
    @Override
    public void addOrder(Orders order) {
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void deleteOrder(byte[] id) {
        orderRepository
                .findById(id)
                .ifPresentOrElse(orderRepository::delete,
                        () -> {
                            throw new OrderNotFoundException("Order not found");
                        });
    }

    @Transactional
    @Override
    public void updateOrder(Orders order) {
        orderRepository
                .findById(order.getId())
                .ifPresentOrElse(presentOrder -> orderRepository.save(order),
                        () -> {
                            throw new OrderNotFoundException("Order not found");
                        });
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Orders addNewOrders(byte[] customerId, ProductItem[] productItems, BillingAddress billingAddress) {
        BigDecimal totalPrice = new BigDecimal(0);
        Product[] products = new Product[productItems.length];
        for (int i = 0; i < productItems.length; i++) {
            products[i] = productRepository
                    .findById(productItems[i].getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            productItems[i].setProductId(products[i].getId());
            int remainingStock = (products[i].getStock() - productItems[i].getQuantity());
            if (remainingStock < 0) {
                throw new MoreThanStockCapacityException("More than stock capacity");
            }
            products[i].setStock(remainingStock);
            BigDecimal subtotal = products[i].getPrice().multiply(BigDecimal.valueOf(productItems[i].getQuantity()));
            productItems[i].setTotalPrice(subtotal);
            totalPrice = totalPrice.add(subtotal);
        }

        new Thread(() -> productRepository.saveAll(Arrays.asList(products))).start();

        billingAddress = billingAddressRepository.save(billingAddress);
        Orders order = new Orders();
        order.setBillingAddressId(billingAddress.getId());
        order.setTotalPrice(totalPrice);
        order.setStatus(Status.PENDING);
        order.setPurchaseDate(Instant.now());
        order = orderRepository.save(order);
        customerOrdersRepository.save(new CustomerOrders(customerId, order.getId()));

        List<ProductItem> productItemsToBeSaved = new ArrayList<>(productItems.length);
        for (ProductItem productItem : productItems){
            productItem.setOrderId(order.getId());
            productItemsToBeSaved.add(productItem);
        }
        productItemRepository.saveAll(productItemsToBeSaved);
       
        return order;
    }

    @Transactional
    public Orders getOrder(byte[] orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orders not found"));
    }
}
