package org.usermanagement.smartshopy.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanagement.smartshopy.dto.request.CreatePaymentDTO;
import org.usermanagement.smartshopy.dto.response.PaymentDTO;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.security.RequireRole;
import org.usermanagement.smartshopy.service.Payment.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody CreatePaymentDTO dto){
        PaymentDTO paymentDTO = paymentService.createPayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentDTO);
    }
    @GetMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<PaymentDTO> getPaymentById(
            @Parameter(description = "ID du paiement", example = "1")
            @PathVariable Long id){
        PaymentDTO payment = paymentService.getPayment(id);
        return ResponseEntity.ok(payment);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrderId(
            @Parameter(description = "ID du paiement", example = "1")
            @PathVariable Long id){
        List<PaymentDTO> payments = paymentService.getPaymentsByOrder(id);
        return ResponseEntity.ok(payments);
    }

}
