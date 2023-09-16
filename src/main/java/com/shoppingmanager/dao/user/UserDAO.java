package com.shoppingmanager.dao.user;

import com.shoppingmanager.model.User;

import java.util.List;

public interface UserDAO {
    //Create
    public void save(User user);
    //Read
    public User getById(Long id);
    //Update
    public void update(User user);
    //Delete
    public void deleteById(Long id);
    //Get All
    public List<User> getAll();
}
