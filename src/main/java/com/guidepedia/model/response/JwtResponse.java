package com.guidepedia.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String login;
    private String username;
    private String role;
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;

    public JwtResponse(String accessToken, String refreshToken, Long id, String login, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.login = login;
        this.username = username;
        this.role = role;
    }
}