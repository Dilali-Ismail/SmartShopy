package org.usermanagement.smartshopy.controller;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Order;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanagement.smartshopy.dto.request.CreateOrderDTO;
import org.usermanagement.smartshopy.dto.response.OrderDTO;
import org.usermanagement.smartshopy.enums.OrderStatus;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.security.RequireRole;
import org.usermanagement.smartshopy.service.Order.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO dto){
       OrderDTO order =  orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<List<OrderDTO>> getAllOrder(){

        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "ID de la commande", example = "1")
            @PathVariable Long id) {
        OrderDTO order = orderService. getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(
            @Parameter(description = "ID du client", example = "1")
            @PathVariable Long customerId) {

        List<OrderDTO> orders = orderService.getOrdersByustomers(customerId);
        return ResponseEntity. ok(orders);
    }

    @GetMapping("/status/{status}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(
            @Parameter(description = "Statut de la commande", example = "PENDING")
            @PathVariable OrderStatus status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/confirm")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<OrderDTO> confirmOrder(
            @Parameter(description = "ID de la commande", example = "1")
            @PathVariable Long id) {
        OrderDTO order = orderService.confirmOrder(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/cancel")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<OrderDTO> cancelOrder(
            @Parameter(description = "ID de la commande", example = "1")
            @PathVariable Long id) {
        OrderDTO order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }



}
