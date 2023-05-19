package com.guidepedia.service;

import com.guidepedia.model.entity.RefreshToken;
import com.guidepedia.model.entity.UserEntity;
import com.guidepedia.model.request.AuthRequest;
import com.guidepedia.model.request.SignUpRequest;
import com.guidepedia.model.response.JwtResponse;
import com.guidepedia.model.response.MessageResponse;
import com.guidepedia.repo.UserRepository;
import com.guidepedia.security.jwt.JwtUtils;
import com.guidepedia.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<?> authenticateUser(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getLogin(), userDetails.getUsername(), userDetails.getRole()));
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Login is already taken!"));
        }
        UserEntity user = new UserEntity();
        user.setLogin(signUpRequest.getLogin());
        user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        user.setUsername(signUpRequest.getUsername());
        user.setUserRole(signUpRequest.getUserRole());
        user.setCardDetails(signUpRequest.getCardDetails());
        user.setProfile(signUpRequest.getProfile());
        user.setStatus(true);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return authenticateUser(new AuthRequest(signUpRequest.getLogin(), signUpRequest.getPassword()));
    }
}