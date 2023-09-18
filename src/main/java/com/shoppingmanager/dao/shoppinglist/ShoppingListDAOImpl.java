package com.shoppingmanager.dao.shoppinglist;

import com.shoppingmanager.model.shopping.ShoppingList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class ShoppingListDAOImpl implements ShoppingListDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ShoppingList createShoppingList(Long userId, ShoppingList shoppingList) {
        return null;
    }

    @Override
    public List<ShoppingList> getShoppingLists(Long userId) {
        final String query = "SELECT * FROM shopping_list WHERE created_by = ?";
        return jdbcTemplate.query(query, ShoppingListDAOHelper::getShoppingLists, userId);
    }

    @Override
    public List<ShoppingList> getShoppingLists(List<Long> ids) {
        return null;
    }

    @Override
    public List<ShoppingList> getActualShoppingLists(Long userId) {
        final String query = "SELECT * FROM shopping_list WHERE created_by = ? AND paid = false";

        return jdbcTemplate.query(query, ShoppingListDAOHelper::getShoppingLists, userId);
    }

    @Override
    public List<ShoppingList> getAlreadyPaidShoppingLists(Long userId) {
        final String query = "SELECT * FROM shopping_list WHERE created_by = ? AND paid = true";

        return jdbcTemplate.query(query, ShoppingListDAOHelper::getShoppingLists, userId);
    }

    @Override
    public ShoppingList setShoppingListToPaid(Long listId) {
        final String updateQuery = "UPDATE shopping_list SET paid = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(updateQuery, true, listId);

        if (rowsUpdated == 1) {
            final String selectQuery = "SELECT * FROM shopping_list WHERE id = ?";
            return jdbcTemplate.query(selectQuery, ShoppingListDAOHelper::getList, listId);
        } else {
            throw new EntityNotFoundException("ShoppingList with ID " + listId + " not found");
        }
    }

    @Override
    public ShoppingList updateShoppingList(ShoppingList shoppingList) {
        final String updateQuery = "UPDATE shopping_list SET name = ?, description = ?, paid = ? WHERE id = ?";

        int rowsUpdated = jdbcTemplate.update(updateQuery,
                shoppingList.getName(),
                shoppingList.getDescription(),
                shoppingList.isPaid(),
                shoppingList.getId());
        if (rowsUpdated == 1) {
            final String selectQuery = "SELECT * FROM shopping_list WHERE id = ?";
            return jdbcTemplate.query(selectQuery, ShoppingListDAOHelper::getList, shoppingList.getId());
        } else {
            throw new EntityNotFoundException("ShoppingList with ID " + shoppingList.getId() + " not found");
        }
    }

    @Override
    public void deleteShoppingList(Long id) {
        final String deleteQuery = "DELETE FROM shopping_list WHERE id = ?";
        int rowsDeleted = jdbcTemplate.update(deleteQuery, id);

        if (rowsDeleted == 0) {
            throw new EntityNotFoundException("ShoppingList with ID " + id + " not found");
        }
    }
}
