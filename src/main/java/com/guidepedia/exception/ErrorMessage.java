package com.guidepedia.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private String message;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ErrorMessage (int statusCode, String message, String description) {
        this.statusCode = statusCode;
        this.message = message;
        this.description = description;
    }

}