package com.target.ready.library.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.entity.UserProfile;
import com.target.ready.library.system.exceptions.ResourceAlreadyExistsException;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;
import com.target.ready.library.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("library_system/v3")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/student")
    public ResponseEntity<UserProfile> addUser(@RequestBody UserProfile userProfile) throws ResourceAlreadyExistsException {
        try {
            return new ResponseEntity<>(userService.addUser(userProfile), HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("delete/user/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") int userId) throws ResourceAlreadyExistsException,ResourceNotFoundException {
        String message = userService.deleteUser(userId);
        if(message.equals("User has books checked out. Cannot delete user")){
            return new ResponseEntity<>("user cannot be deleted, as user has issued a book",HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>("User deleted successfully!!",HttpStatus.ACCEPTED);
        }

    }

    @GetMapping("users")
    @Operation(
            description = "Getting all the users present in the database",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "page_number", defaultValue = "0", required = false)Integer pageNumber) throws ResourceNotFoundException{
           int pageSize=5;
        List<UserProfile> users = userService.getAllUsers(pageNumber, pageSize);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    @GetMapping("AllUsers")
    public ResponseEntity<?> fetchAllUsers() throws ResourceNotFoundException{
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);

    }
    @GetMapping("users/total_count")
    @Operation(
            description = "Get all the users count",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public ResponseEntity<Mono<Long>> getTotalUsersCount() throws ResourceNotFoundException{

            Mono<Long> totalCount = userService.getTotalUsersCount();
            return new ResponseEntity<>(totalCount, HttpStatus.OK);

    }
    @GetMapping("/books/{user_id}")
    public ResponseEntity<?> findBookNameByUserId(@PathVariable ("user_id") int userId) throws ResourceNotFoundException{
            return new ResponseEntity<>(userService.findBookNameByUserId(userId), HttpStatus.OK);
    }
}