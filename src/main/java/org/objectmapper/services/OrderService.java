package org.objectmapper.services;

import lombok.RequiredArgsConstructor;
import org.objectmapper.entities.Order;
import org.objectmapper.entities.Product;
import org.objectmapper.repositories.CustomerRepository;
import org.objectmapper.repositories.OrderRepository;
import org.objectmapper.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class OrderService {


    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public Order createOrder(Order order) {
        order.setTotalPrice(calculateTotalPrice(order.getProducts()));
        return orderRepository.save(order);
    }

    private double calculateTotalPrice(List<Product> products) {
        return products.stream().mapToDouble(p -> p.getPrice()).sum();
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

