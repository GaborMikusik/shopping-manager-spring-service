package com.shoppingmanager.security.service;

import com.shoppingmanager.dao.user.UserDAO;
import com.shoppingmanager.model.User;
import com.shoppingmanager.payload.SignInRequest;
import com.shoppingmanager.payload.SignInResponse;
import com.shoppingmanager.payload.SignUpRequest;
import com.shoppingmanager.payload.SignUpResponse;
import com.shoppingmanager.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    UserDAO userDAO;

    public SignInResponse signIn(final SignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new SignInResponse(jwt);
    }

    public SignUpResponse signUp(final SignUpRequest request) {
        User user = this.userDAO.getUserByNameOrEmail(request.getUsername());
        if (user != null) {
            return null;
        }

        user = userDAO.getUserByNameOrEmail(request.getEmail());
        if (user != null) {
            return null;
        }

        user = new User(request.getName(), request.getUsername(),
                request.getEmail(), request.getPassword());
        user.setRoles(request.getRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.userDAO.save(user);

        return new SignUpResponse(true, "User registered successfully");
    }
}
