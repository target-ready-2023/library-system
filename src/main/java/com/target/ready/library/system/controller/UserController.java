package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("library_system/v3")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/student")
    public ResponseEntity<UserProfile> addUser(@RequestBody UserProfile userProfile)  {
        try {
            return new ResponseEntity<>(userService.addUser(userProfile), HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("users")
    public ResponseEntity<?> getAllUsers(){
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Currently no users!");
        }
    }
}