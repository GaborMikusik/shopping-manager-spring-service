package com.shoppingmanager.dao.shoppinglist;

import com.shoppingmanager.model.shopping.ShoppingList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

public class ShoppingListDAOImpl implements ShoppingListDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ShoppingList createShoppingList(Long userId, ShoppingList shoppingList) {
//        String query = "insert into ShoppingList (name, description, paid, items, created_by, updated_by, created_at, updated_at) values (?,?,?,?,?,?,NOW(),NOW())";
//        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
//            ps.setString(1, shoppingList.getName());
//            ps.setString(2, shoppingList.getDescription());
//            ps.setBoolean(3, shoppingList.isPaid());
//            ps.setObject(4, shoppingList.getItems());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public List<ShoppingList> getShoppingList(Long userId) {
        final String query = "SELECT * FROM shopping_list WHERE created_by = ?";

        return getShoppingLists(userId, query);
    }

    @Override
    public List<ShoppingList> getShoppingLists(List<Long> ids) {
        return null;
    }

    @Override
    public List<ShoppingList> getActualShoppingLists(Long userId) {
        final String query = "SELECT * FROM shopping_list WHERE created_by = ? AND paid = false";

        return getShoppingLists(userId, query);
    }

    @Override
    public List<ShoppingList> getAlreadyPaidShoppingLists(Long userId) {
        final String query = "SELECT * FROM shopping_list WHERE created_by = ? AND paid = true";

        return getShoppingLists(userId, query);
    }

    @Override
    public ShoppingList setShoppingListToPaid(Long listId) {
        final String updateQuery = "UPDATE shopping_list SET paid = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(updateQuery, true, listId);

        if (rowsUpdated == 1) {
            return getShoppingListById(listId);
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
            return getShoppingListById(shoppingList.getId());
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

    private List<ShoppingList> getShoppingLists(Long userId, String query) {
        return jdbcTemplate.query(query, rs -> {
            List<ShoppingList> resultList = new LinkedList<>();
            while (rs.next()) {
                ShoppingList shoppingList = getShoppingList(rs);
                resultList.add(shoppingList);
            }
            return resultList;
        }, userId);
    }

    private ShoppingList getShoppingListById(Long id) {
        final String selectQuery = "SELECT * FROM shopping_list WHERE id = ?";
        return jdbcTemplate.query(selectQuery, rs -> {
            if (rs.next()) {
                return getShoppingList(rs);
            } else {
                return null;
            }
        }, id);
    }

    private ShoppingList getShoppingList(ResultSet rs) throws SQLException {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(rs.getLong("id"));
        shoppingList.setName(rs.getString("name"));
        shoppingList.setDescription(rs.getString("description"));
        shoppingList.setPaid(rs.getBoolean("paid"));
        shoppingList.setCreatedAt(((LocalDateTime) rs.getObject("created_at")).toInstant(ZoneOffset.UTC));
        shoppingList.setUpdatedAt(((LocalDateTime) rs.getObject("updated_at")).toInstant(ZoneOffset.UTC));
        shoppingList.setCreatedBy(rs.getLong("created_by"));
        shoppingList.setUpdatedBy(rs.getLong("updated_by"));
        return shoppingList;
    }
}
