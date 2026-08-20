package com.kvn.schoolinvoices;

import com.kvn.schoolinvoices.service.repository.AppUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.kvn.schoolinvoices.dto.CurrentUserResponse;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AppUserRepository appUserRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid
            @RequestBody
            RegisterRequest request) {

        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    @GetMapping("/me")
    public CurrentUserResponse getCurrentUser(
            Authentication authentication) {
        AppUser user =
                (AppUser) authentication.getPrincipal();

        List<String> roles =
                user.getRoles()
                        .stream()
                        .map(role -> role.getName()+"")
                        .toList();

        return new CurrentUserResponse(
                user.getEmail(),
                user.getCognitoSub(),
                roles
        );

    }

}