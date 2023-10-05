package com.shoppingmanager.controller;

import com.shoppingmanager.payload.SignInRequest;
import com.shoppingmanager.payload.SignInResponse;
import com.shoppingmanager.payload.SignUpRequest;
import com.shoppingmanager.payload.SignUpResponse;
import com.shoppingmanager.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<SignInResponse> authenticateUser(@Valid @RequestBody final SignInRequest request) {
        return ResponseEntity.ok(this.authService.signIn(request));
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<SignUpResponse> registerUser(@Valid @RequestBody final SignUpRequest request) {
        SignUpResponse signUpResponse = this.authService.signUp(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(request.getUsername()).toUri();

        return ResponseEntity.created(location).body(signUpResponse);
    }
}
