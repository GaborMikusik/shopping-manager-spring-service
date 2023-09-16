package com.shoppingmanager.controller;

import com.shoppingmanager.dao.shoppinglist.ShoppingListDAO;
import com.shoppingmanager.model.shopping.ShoppingList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShoppingListController {
    @Autowired
    ShoppingListDAO shoppingListDAO;

    @RequestMapping(value = "/getlists", method = RequestMethod.GET)
    public @ResponseBody List<ShoppingList> getLists(@RequestParam(name = "userId") Long userId) {
        return this.shoppingListDAO.getShoppingList(userId);
    }

    @RequestMapping(value = "/actuallists", method = RequestMethod.GET)
    public @ResponseBody List<ShoppingList> getActualShoppingLists(@RequestParam(name = "userId") Long userId) {
        return this.shoppingListDAO.getActualShoppingLists(userId);
    }

    @RequestMapping(value = "/paidlists", method = RequestMethod.GET)
    public @ResponseBody List<ShoppingList> getPaidShoppingLists(@RequestParam(name = "userId") Long userId) {
        return this.shoppingListDAO.getAlreadyPaidShoppingLists(userId);
    }

    @RequestMapping(value = "/setlistaspaid", method = RequestMethod.PATCH)
    public @ResponseBody ShoppingList setListAsPaid(@RequestParam(name = "userId") Long userId) {
        return this.shoppingListDAO.setShoppingListToPaid(userId);
    }

    @RequestMapping(value = "/updatelist", method = RequestMethod.PUT)
    public @ResponseBody ShoppingList udpateShoppingList(@RequestBody ShoppingList list) {
        return this.shoppingListDAO.updateShoppingList(list);
    }
}
