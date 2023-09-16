package com.shoppingmanager.dao.shoppinglist;

import com.shoppingmanager.model.shopping.ShoppingList;

import java.util.List;

public interface ShoppingListDAO {
    ShoppingList createShoppingList(final Long userId, final ShoppingList shoppingList);

    List<ShoppingList> getShoppingList(Long userId);

    List<ShoppingList> getShoppingLists(final List<Long> ids);

    List<ShoppingList> getActualShoppingLists(final Long userId);

    List<ShoppingList> getAlreadyPaidShoppingLists(final Long userId);

    ShoppingList setShoppingListToPaid(final Long id);

    ShoppingList updateShoppingList(final ShoppingList shoppingList);

    void deleteShoppingList(final Long id);
}
