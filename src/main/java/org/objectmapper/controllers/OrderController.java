package org.objectmapper.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.objectmapper.entities.Order;
import org.objectmapper.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @SneakyThrows
    @PostMapping("/asstring")
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody String order){
        Order order1 = objectMapper.readValue(order, Order.class);
        Order order2 = orderService.createOrder(order1);
        return objectMapper.writeValueAsString(order2);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}/filtered")
    public String getFilteredOrder(@PathVariable Long id, @RequestParam List<String> fields) throws JsonProcessingException {
        Order order = orderService.getOrder(id);
        ObjectNode filteredNode = objectMapper.valueToTree(order);
        ObjectNode result = objectMapper.createObjectNode();
        for (String field : fields) {
            if (filteredNode.has(field)) {
                result.set(field, filteredNode.get(field));
            }
        }
        return objectMapper.writeValueAsString(result);
    }
}

