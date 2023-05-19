package com.guidepedia.controller;

import com.guidepedia.exception.TokenRefreshException;
import com.guidepedia.model.entity.RefreshToken;
import com.guidepedia.model.request.AuthRequest;
import com.guidepedia.model.request.SignUpRequest;
import com.guidepedia.model.request.TokenRefreshRequest;
import com.guidepedia.model.response.TokenRefreshResponse;
import com.guidepedia.security.jwt.JwtUtils;
import com.guidepedia.service.AuthService;
import com.guidepedia.service.RefreshTokenService;
import com.guidepedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage authorization",
        name = "User Resource")
@CrossOrigin
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @Operation(summary = "User Authentification")
    @PostMapping(value = "/auth/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Operation(summary = "User Registration")
    @PostMapping(value = "/auth/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @Operation(summary = "Update access token")
    @PostMapping(value = "/auth/refreshtoken",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromLogin(user.getLogin());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}