package com.shoppingmanager.security;

import com.shoppingmanager.dao.user.UserDAO;
import com.shoppingmanager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(final String usernameOrEmail) throws UsernameNotFoundException {
        User user = this.userDAO.getUserByNameOrEmail(usernameOrEmail);
        return UserPrincipal.create(user);
    }
}
