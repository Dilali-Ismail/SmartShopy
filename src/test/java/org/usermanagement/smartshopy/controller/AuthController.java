package org.usermanagement.smartshopy.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanagement.smartshopy.dto.request.LoginRequestDTO;
import org.usermanagement.smartshopy.dto.response.Userdto;
import org.usermanagement.smartshopy.service.Auth.AuthService;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class  AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Userdto> login(@Valid @RequestBody LoginRequestDTO requestDTO , HttpSession session){
       Userdto userdto = authService.login(requestDTO,session);
       return ResponseEntity.ok(userdto);
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(HttpSession session){
        authService.logout(session);
        return ResponseEntity.ok(Map.of("message","Deconnected successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<Userdto> currentUser(HttpSession session){

        Userdto me = authService.getCurrentUser(session);
        return ResponseEntity.ok(me);

    }
}
