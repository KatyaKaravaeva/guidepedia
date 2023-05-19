package com.guidepedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guidepedia.model.UserEntity;
import com.guidepedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;


@RestController
@SecurityRequirement(name = "Authorization")
@Tag(description = "Api to manage users",
        name = "User Resource")
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Get user for course",
            description = "Provides user for course by id")
    @RequestMapping(value = "user/{userID}/application/approved", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('TEACHER', 'STUDENT', 'ASSIST')")
    public UserEntity getUser(@PathVariable("userID") Long userID){
        return userService.findUser(userID);
    }

    @Operation(summary = "Authorization",
            description = "Provides user entity")
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ResponseBody
    public UserEntity auth(){
        return userService.registerUser();
    }

    }
}
