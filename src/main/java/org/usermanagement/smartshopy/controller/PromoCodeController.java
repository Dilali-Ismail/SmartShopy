package org.usermanagement.smartshopy.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanagement.smartshopy.dto.request.CreatePromoCodeDTO;
import org.usermanagement.smartshopy.dto.response.PromoCodeDTO;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.security.RequireRole;
import org.usermanagement.smartshopy.service.PromoCode.PromoCodeService;

import java.util.List;

@RestController
@RequestMapping("/api/promo-codes")
@RequireRole(UserRole.ADMIN)
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping
    public ResponseEntity<PromoCodeDTO> createPromoCode(@Valid @RequestBody CreatePromoCodeDTO dto) {
        PromoCodeDTO promoCode = promoCodeService.createPromocode(dto);
        return ResponseEntity.status(HttpStatus. CREATED). body(promoCode);
    }

    @GetMapping
    public ResponseEntity<List<PromoCodeDTO>> getAllPromoCodes() {
        List<PromoCodeDTO> promoCodes = promoCodeService.getAllPromocodes();
        return ResponseEntity.ok(promoCodes);
    }

}
