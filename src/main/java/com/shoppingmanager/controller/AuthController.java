package com.shoppingmanager.controller;

import com.shoppingmanager.dao.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    UserDAO userDAO;

    @RequestMapping(value = "/createuser", method = RequestMethod.GET)
    public @ResponseBody String createUser() {
//        List<User> users = this.userDAO.getAll();
//        return users.toString();
        return "Create User";
    }
}
