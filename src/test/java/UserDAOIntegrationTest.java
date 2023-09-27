import com.shoppingmanager.dao.user.UserDAO;
import com.shoppingmanager.dao.user.UserDAOImpl;
import com.shoppingmanager.model.Role;
import com.shoppingmanager.model.RoleName;
import com.shoppingmanager.model.User;
import configuration.TestDataSourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Import({UserDAOImpl.class})
@Transactional
public class UserDAOIntegrationTest {
    @Autowired
    private UserDAO userDAO;

    @Test
    @Sql(statements = {
            "DELETE FROM user_roles",
            "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateUser() {
        User user = new User("testuser", "testusername", "test@email", "password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setRoles(Set.of(new Role(RoleName.ROLE_ADMIN), new Role(RoleName.ROLE_USER)));

        User savedUser = this.userDAO.save(user);

        assertNotNull(savedUser.getId());
        assertUser(savedUser, user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRoles());

    }

    @Test
    @Sql(statements = {
            "INSERT INTO users (id, name, username, email, password, created_at, updated_at) VALUES (2, 'testname', 'testusername', 'test@email', 'testpassword', current_timestamp, current_timestamp)",
            "INSERT INTO user_roles (user_id, role_id) VALUES (2, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM user_roles",
            "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetById() {
        User user = this.userDAO.getById(2L);
        assertUser(user, "testname", "testusername", "test@email", "testpassword", Set.of(new Role(RoleName.ROLE_USER)));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO users (id, name, username, email, password, created_at, updated_at) VALUES (2, 'testname', 'testusername', 'test@email', 'testpassword', current_timestamp, current_timestamp)",
            "INSERT INTO user_roles (user_id, role_id) VALUES (2, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM user_roles",
            "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteById() {
        this.userDAO.deleteById(2L);
        User user = this.userDAO.getById(2L);
        assertNull(user);
    }

    @Test
    @Sql(statements = {
            "INSERT INTO users (id, name, username, email, password, created_at, updated_at) VALUES (2L, 'testname', 'testusername', 'test@email', 'testpassword', current_timestamp, current_timestamp)",
            "INSERT INTO user_roles (user_id, role_id) VALUES (2, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM user_roles",
            "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdate() {
        User user = new User("newname", "newusername", "new@email", "newpassword");
        user.setId(2L);
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleName.ROLE_ADMIN);
        roles.add(role);
        user.setRoles(roles);

        this.userDAO.update(user);
        User updatedUser = this.userDAO.getById(2L);

        assertNotNull(updatedUser);
        assertUser(updatedUser, user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), Set.of(new Role(RoleName.ROLE_ADMIN)));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO users (id, name, username, email, password, created_at, updated_at) VALUES (2, 'testname', 'testusername', 'test@email', 'testpassword', current_timestamp, current_timestamp)",
            "INSERT INTO user_roles (user_id, role_id) VALUES (2, 1)"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM user_roles",
            "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetUserByUsernameOREmail() {
        User user = this.userDAO.getUserByNameOrEmail("testusername");
        assertUser(user, "testname", "testusername", "test@email", "testpassword", Set.of(new Role(RoleName.ROLE_USER)));

        user = this.userDAO.getUserByNameOrEmail("test@email");
        assertUser(user, "testname", "testusername", "test@email", "testpassword", Set.of(new Role(RoleName.ROLE_USER)));
    }

    private void assertUser(final User user, final String name, final String username, final String email, final String password, Set<Role> roles) {
        assertEquals(name, user.getName());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertTrue(isRolesEqual(user.getRoles(), roles));
    }

    private boolean isRolesEqual(Set<Role> set1, Set<Role> set2) {
        // Check if both sets have the same size
        if (set1.size() != set2.size()) {
            return false;
        }
        // Check if all items in set1 exist in set2 based on the 'name' field
        for (Role item1 : set1) {
            boolean found = false;
            for (Role item2 : set2) {
                if (item1.getName().equals(item2.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }
}
