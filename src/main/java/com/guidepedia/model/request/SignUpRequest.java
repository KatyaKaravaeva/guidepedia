package com.guidepedia.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String login;
    private String password;
    private String username;
    private String userRole;
    private String cardDetails;
    private String profile;
}