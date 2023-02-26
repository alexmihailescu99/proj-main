package com.alexm.projmain.controllers;

import com.alexm.projmain.dtos.UserDto;
import com.alexm.projmain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/*
@Controller vs @RestController
@Controller is just a specialisation of @Component
@RestController is @Controller and @ResponseBody
@ResponseBody automatically maps a Java object to the http response(e.g. you can return Java object directly from controller function)
 */

@Controller
@RequestMapping(
        value    ="/api/v1/user",
        produces ="application/json" // So it is treated like JSON and not plaintext
)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        return new ResponseEntity<>(userService.getUser(null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody UserDto user) {
        return new ResponseEntity<>("Hi, " + user.username() + " Your password is:" + user.password(), HttpStatus.OK);
    }

}
