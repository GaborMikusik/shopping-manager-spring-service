package com.shoppingmanager.security.service;

import com.shoppingmanager.dao.user.UserDAO;
import com.shoppingmanager.exception.UserAlreadyExistsException;
import com.shoppingmanager.model.User;
import com.shoppingmanager.payload.SignInRequest;
import com.shoppingmanager.payload.SignInResponse;
import com.shoppingmanager.payload.SignUpRequest;
import com.shoppingmanager.payload.SignUpResponse;
import com.shoppingmanager.security.jwt.JwtTokenProvider;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtTokenProvider tokenProvider;
    @Mock
    UserDAO userDAO;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignIn() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock token generation
        when(tokenProvider.generateToken(authentication)).thenReturn("testJWT");

        // Create a SignInRequest
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsernameOrEmail("testName");
        signInRequest.setPassword("testPassword");

        // Call the method to be tested
        SignInResponse signInResponse = authService.signIn(signInRequest);

        // Assertions
        assertEquals("Bearer", signInResponse.getTokenType());
        assertEquals("testJWT", signInResponse.getAccessToken());
    }

    @Test
    public void testSignUp_UserAlreadyExistsByUsername() {
        when(this.userDAO.getUserByNameOrEmail("testusername")).thenReturn(new User());
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testusername");

        assertThrows(UserAlreadyExistsException.class, () -> {
            this.authService.signUp(signUpRequest);
        });
    }

    @Test
    public void testSignUp_UserAlreadyExistsByEmail() {
        when(this.userDAO.getUserByNameOrEmail("test@username")).thenReturn(new User());
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@username");

        assertThrows(UserAlreadyExistsException.class, () -> {
            this.authService.signUp(signUpRequest);
        });
    }

    @Test
    public void testSignUp() {
        when(this.userDAO.getUserByNameOrEmail(any())).thenReturn(null);
        SignUpRequest signUpRequest = new SignUpRequest();

        SignUpResponse signUpResponse = this.authService.signUp(signUpRequest);

        assertTrue(signUpResponse.getSuccess());
        assertEquals(AuthServiceMessages.SUCCESSFUL_REGISTRATION, signUpResponse.getMessage());
    }
}