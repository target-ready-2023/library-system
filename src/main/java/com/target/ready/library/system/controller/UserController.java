package com.target.ready.library.system.controller;

import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("library_system/v3")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/student")
    public ResponseEntity<String> addUser(@RequestBody UserProfile userProfile){
        return new ResponseEntity<>(userService.addUser(userProfile), HttpStatus.CREATED);
    }
}
