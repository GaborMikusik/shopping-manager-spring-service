import com.shoppingmanager.dao.shoppinglist.ShoppingListDAO;
import com.shoppingmanager.dao.shoppinglist.ShoppingListDAOImpl;
import com.shoppingmanager.model.shopping.ShoppingList;
import configuration.TestDataSourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Import({ShoppingListDAOImpl.class})
@Transactional
public class ShoppingListDAOIntegrationTest {
    @Autowired
    private ShoppingListDAO shoppingListDAO;

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 2', 3, 'Note for Item 2', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetShoppingList() {
        List<ShoppingList> shoppingLists = this.shoppingListDAO.getShoppingLists(1L);

        assertEquals(1, shoppingLists.size());
        assertEquals("Test Shopping List", shoppingLists.get(0).getName());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 2', 3, 'Note for Item 2', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetShoppingList_FakeUser() {
        List<ShoppingList> shoppingLists = this.shoppingListDAO.getShoppingLists(2L);

        assertNotNull(shoppingLists);
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Unpaid Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (2, 'Test Paid Shopping List', 'Test description', true, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 3, 'Note for Item 1', false, 2, 1, 1)",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetActualShoppingLists() {
        List<ShoppingList> actualShoppingLists = this.shoppingListDAO.getActualShoppingLists(1L);

        assertEquals(1, actualShoppingLists.size());
        assertEquals("Test Unpaid Shopping List", actualShoppingLists.get(0).getName());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Unpaid Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (2, 'Test Paid Shopping List', 'Test description', true, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 3, 'Note for Item 1', false, 2, 1, 1)",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetActualShoppingLists_FakeUser() {
        List<ShoppingList> actualShoppingLists = this.shoppingListDAO.getActualShoppingLists(1L);

        assertNotNull(actualShoppingLists);
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Unpaid Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (2, 'Test Paid Shopping List', 'Test description', true, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 3, 'Note for Item 1', false, 2, 1, 1)",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAlreadyPaidShoppingLists() {
        List<ShoppingList> actualShoppingLists = this.shoppingListDAO.getAlreadyPaidShoppingLists(1L);

        assertEquals(1, actualShoppingLists.size());
        assertEquals("Test Paid Shopping List", actualShoppingLists.get(0).getName());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Unpaid Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (2, 'Test Paid Shopping List', 'Test description', true, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 3, 'Note for Item 1', false, 2, 1, 1)",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAlreadyPaidShoppingLists_FakeUser() {
        List<ShoppingList> actualShoppingLists = this.shoppingListDAO.getAlreadyPaidShoppingLists(1L);

        assertNotNull(actualShoppingLists);
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 2', 3, 'Note for Item 2', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testSetShoppingListToPaid() {
        List<ShoppingList> shoppingLists = this.shoppingListDAO.getShoppingLists(1L);
        assertFalse(shoppingLists.get(0).isPaid());

        ShoppingList shoppingList = this.shoppingListDAO.setShoppingListToPaid(1L);

        assertTrue(shoppingList.isPaid());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 2', 3, 'Note for Item 2', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testSetShoppingListToPaid_FakeUserId() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.shoppingListDAO.setShoppingListToPaid(2L);
        });
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateShoppingList() {
        final String TEST_UPDATED_SHOPPING_LIST = "Test Updated Shopping List";
        final String TEST_UPDATED_DESCRIPTION = "Test updated description";
        final ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(1L);
        shoppingList.setName(TEST_UPDATED_SHOPPING_LIST);
        shoppingList.setDescription(TEST_UPDATED_DESCRIPTION);
        shoppingList.setPaid(true);

        ShoppingList updated = this.shoppingListDAO.updateShoppingList(shoppingList);
        assertEquals(TEST_UPDATED_SHOPPING_LIST, updated.getName());
        assertEquals(TEST_UPDATED_DESCRIPTION, updated.getDescription());
        assertTrue(updated.isPaid());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateShoppingList_FakeUser() {
        final String TEST_UPDATED_SHOPPING_LIST = "Test Updated Shopping List";
        final String TEST_UPDATED_DESCRIPTION = "Test updated description";
        final ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(2L);
        shoppingList.setName(TEST_UPDATED_SHOPPING_LIST);
        shoppingList.setDescription(TEST_UPDATED_DESCRIPTION);
        shoppingList.setPaid(true);

        assertThrows(EntityNotFoundException.class, () -> {
            this.shoppingListDAO.updateShoppingList(shoppingList);
        });
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteShoppingList() {
        this.shoppingListDAO.deleteShoppingList(1L);

        List<ShoppingList> shoppingLists = this.shoppingListDAO.getShoppingLists(1L);
        assertEquals(0, shoppingLists.size());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO shopping_list (id, name, description, paid, created_at, updated_at, created_by, updated_by) VALUES (1, 'Test Shopping List', 'Test description', false, current_timestamp, current_timestamp, 1, 1)",
            "INSERT INTO item (name, quantity, note, purchased, shopping_list_id, created_by, updated_by) VALUES ('Item 1', 2, 'Note for Item 1', false, 1, 1, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM item",
            "DELETE FROM shopping_list"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteShoppingList_FakeUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            this.shoppingListDAO.deleteShoppingList(2L);
        });
    }
}
