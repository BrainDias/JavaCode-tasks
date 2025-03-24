package org.objectmapper.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.objectmapper.entities.Order;
import org.objectmapper.entities.Product;
import org.objectmapper.repositories.CustomerRepository;
import org.objectmapper.repositories.OrderRepository;
import org.objectmapper.repositories.ProductRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ShouldSaveOrder() {
        Order order = new Order();
        order.setProducts(List.of(new Product(1L, "Product", "Desc", 100.0, 10)));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder);
        assertEquals(order, savedOrder);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenExists() {
        Order order = new Order();
        order.setOrderId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrder(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getOrderId());
    }
}

