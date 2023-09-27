package com.shoppingmanager.dao.user;

import com.shoppingmanager.model.User;

public interface UserDAO {
    User save(User user);

    User getById(Long id);

    User getUserByNameOrEmail(String usernameOrEmail);

    User update(User user);

    void deleteById(Long id);
}
