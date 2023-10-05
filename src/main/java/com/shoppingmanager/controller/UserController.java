package com.shoppingmanager.controller;

import com.shoppingmanager.dao.user.UserDAO;
import com.shoppingmanager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    UserDAO userDAO;

    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userDAO.save(user));
    }
}
