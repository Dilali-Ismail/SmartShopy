package org.usermanagement.smartshopy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanagement.smartshopy.dto.request.CreateCustomerDTO;
import org.usermanagement.smartshopy.dto.request.UpdateCustomerDTO;
import org.usermanagement.smartshopy.dto.response.CustomerDto;
import org.usermanagement.smartshopy.enums.CustomerTier;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.security.RequireRole;
import org.usermanagement.smartshopy.service.Customer.CustomerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<CustomerDto> createCustomer(
            @Valid @RequestBody CreateCustomerDTO dto) {

        CustomerDto customer = customerService.createCustomeer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @Parameter(description = "ID du client", example = "1")
            @PathVariable Long id) {

        CustomerDto customer = customerService.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<CustomerDto> updateCustomer(
            @Parameter(description = "ID du client", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerDTO dto) {

        CustomerDto customer = customerService. updateCustomer(id, dto);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<Map<String, String>> deleteCustomer(
            @Parameter(description = "ID du client", example = "1")
            @PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.ok(Map.of("message", "Client supprimé avec succès"));
    }

    @GetMapping("/tier/{tier}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<List<CustomerDto>> getCustomersByTier(
            @Parameter(description = "Niveau de fidélité", example = "SILVER")
            @PathVariable CustomerTier tier) {

        List<CustomerDto> customers = customerService.getCustomersByTier(tier);
        return ResponseEntity.ok(customers);
    }









}
