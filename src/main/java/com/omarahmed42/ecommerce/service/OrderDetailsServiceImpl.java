package com.omarahmed42.ecommerce.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.DTO.BillingAddressDTO;
import com.omarahmed42.ecommerce.DTO.CartItemDTO;
import com.omarahmed42.ecommerce.DTO.OrderDetailsDTO;
import com.omarahmed42.ecommerce.enums.Status;
import com.omarahmed42.ecommerce.exception.InvalidInputException;
import com.omarahmed42.ecommerce.exception.MissingFieldException;
import com.omarahmed42.ecommerce.exception.MoreThanStockCapacityException;
import com.omarahmed42.ecommerce.exception.OrderNotFoundException;
import com.omarahmed42.ecommerce.model.BillingAddress;
import com.omarahmed42.ecommerce.model.CustomerOrders;
import com.omarahmed42.ecommerce.model.OrderDetails;
import com.omarahmed42.ecommerce.model.OrderItem;
import com.omarahmed42.ecommerce.model.Product;
import com.omarahmed42.ecommerce.repository.BillingAddressRepository;
import com.omarahmed42.ecommerce.repository.CustomerOrdersRepository;
import com.omarahmed42.ecommerce.repository.OrderDetailsRepository;
import com.omarahmed42.ecommerce.repository.ProductRepository;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerOrdersRepository customerOrdersRepository;
    private final BillingAddressRepository billingAddressRepository;
    private ModelMapper modelMapper;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderRepository,
            ProductRepository productRepository, CustomerOrdersRepository customerOrdersRepository,
            BillingAddressRepository billingAddressRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerOrdersRepository = customerOrdersRepository;
        this.billingAddressRepository = billingAddressRepository;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    public void deleteOrderDetails(UUID id) {
        orderRepository
                .findById(id)
                .ifPresentOrElse(orderRepository::delete,
                        () -> {
                            throw new OrderNotFoundException("Order not found");
                        });
    }

    @Override
    @Transactional
    public void updateOrderDetailsPartially(UUID id, OrderDetailsDTO orderDetailsDTO) {
        if (orderDetailsDTO.getTotalPrice() != null && orderDetailsDTO.getTotalPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidInputException("Total price cannot be less than 0");

        OrderDetails order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        modelMapper.map(orderDetailsDTO, order);
        order.setId(id);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateOrderDetails(OrderDetails order) {
        orderRepository
                .findById(order.getId())
                .ifPresentOrElse(presentOrder -> orderRepository.save(order),
                        () -> {
                            throw new OrderNotFoundException("Order not found");
                        });
    }

    @Transactional(readOnly = true)
    public OrderDetails getOrderDetails(UUID orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @PreAuthorize("principal.user.id == #userId")
    public OrderDetails addOrderDetails(UUID userId, CartItemDTO[] cartItems, BillingAddressDTO billingAddressDTO) {
        Map<UUID, Integer> productIdToQuantity = mapProductIdsToQuantity(cartItems);
        Set<UUID> productIds = productIdToQuantity.keySet();
        if (productIds.isEmpty())
            throw new MissingFieldException("Products ids are missing");

        List<Product> products = productRepository.findAllById(productIds);
        List<OrderItem> orderItems = new ArrayList<>(products.size());

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : products) {
            Integer quantity = productIdToQuantity.get(product.getId());
            updateStock(product, quantity);
            OrderItem orderItem = new OrderItem(product, calculateSubTotal(product, quantity), quantity);
            orderItems.add(orderItem);
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
        }

        productRepository.saveAll(products);

        BillingAddress billingAddress = modelMapper.map(billingAddressDTO, BillingAddress.class);
        billingAddress = billingAddressRepository.save(billingAddress);

        OrderDetails order = new OrderDetails();
        order.setBillingAddress(billingAddress);
        order.setTotalPrice(totalPrice);
        order.setStatus(Status.PENDING);
        order.setPurchaseDate(Instant.now());
        order.addAllOrderItems(orderItems);
        order = orderRepository.save(order);

        customerOrdersRepository.save(new CustomerOrders(userId, order.getId()));
        return order;
    }

    private Map<UUID, Integer> mapProductIdsToQuantity(CartItemDTO[] cartItems) {
        if (cartItems == null || cartItems.length == 0)
            return Collections.emptyMap();
        Map<UUID, Integer> productIdToQuantity = new HashMap<>(cartItems.length);
        for (CartItemDTO cartItem : cartItems) {
            if (cartItem == null || cartItem.getProductId() == null)
                continue;
            productIdToQuantity.put(cartItem.getProductId(), cartItem.getQuantity());
        }
        return productIdToQuantity;
    }

    private void updateStock(Product product, Integer quantity) {
        int remainingStock = product.getStock() - quantity;
        if (remainingStock < 0)
            throw new MoreThanStockCapacityException("More than stock capacity");

        product.setStock(remainingStock);
    }

    private BigDecimal calculateSubTotal(Product product, Integer quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

}
