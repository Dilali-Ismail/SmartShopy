package org.usermanagement.smartshopy.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanagement.smartshopy.dto.request.CreateProductDTO;
import org.usermanagement.smartshopy.dto.request.UpdateProductDTO;
import org.usermanagement.smartshopy.dto.response.ProductDTO;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.security.RequireRole;
import org.usermanagement.smartshopy.service.Product.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody CreateProductDTO dto) {

        ProductDTO product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED). body(product);
    }
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        Page<ProductDTO> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/active")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<List<ProductDTO>> getActiveProducts() {
        List<ProductDTO> products = productService.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID du produit", example = "1")
            @PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID du produit", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDTO dto) {

        ProductDTO product = productService.updateProduct(id, dto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<Map<String, String>> deleteProduct(
            @Parameter(description = "ID du produit", example = "1")
            @PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Produit supprimé avec succès"));
    }

    @PatchMapping("/{id}/toggle")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ProductDTO> toggleProductStatus(
            @Parameter(description = "ID du produit", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nouveau statut", example = "false")
            @RequestParam boolean active) {

        ProductDTO product = productService.toggleProductStatus(id, active);
        return ResponseEntity.ok(product);
    }













}
