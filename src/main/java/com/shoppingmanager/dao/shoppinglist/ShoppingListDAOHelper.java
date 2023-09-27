package com.shoppingmanager.dao.shoppinglist;

import com.shoppingmanager.dao.converter.InstantConverter;
import com.shoppingmanager.model.shopping.ShoppingList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public final class ShoppingListDAOHelper {
    public static List<ShoppingList> getShoppingLists(ResultSet rs) throws SQLException {
        List<ShoppingList> resultList = new LinkedList<>();
        while (rs.next()) {
            ShoppingList shoppingList = getShoppingList(rs);
            resultList.add(shoppingList);
        }
        return resultList;
    }

    public static ShoppingList getList(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return getShoppingList(rs);
        } else {
            return null;
        }
    }

    public static ShoppingList getShoppingList(ResultSet rs) throws SQLException {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(rs.getLong("id"));
        shoppingList.setName(rs.getString("name"));
        shoppingList.setDescription(rs.getString("description"));
        shoppingList.setPaid(rs.getBoolean("paid"));
        shoppingList.setCreatedAt(InstantConverter.convertToInstant(rs, "created_at"));
        shoppingList.setUpdatedAt(InstantConverter.convertToInstant(rs, "updated_at"));
        shoppingList.setCreatedBy(rs.getLong("created_by"));
        shoppingList.setUpdatedBy(rs.getLong("updated_by"));
        return shoppingList;
    }

}
